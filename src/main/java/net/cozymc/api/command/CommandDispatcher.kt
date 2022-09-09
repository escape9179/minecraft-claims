package net.cozymc.api.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

class CommandDispatcher private constructor() {

    enum class Result {
        NO_RESULT, NO_PERMISSION, WRONG_TARGET, INVALID_NUMBER_OF_ARGUMENTS, INVALID_ARGUMENT_TYPE, SUCCESS
    }

    companion object : CommandExecutor {

        private val registeredCommands = mutableSetOf<BasicCommand<*>>()
        val NO_PERMISSION_MESSAGE  = "${ChatColor.RED}No permission."
        var lastResult: Result = Result.NO_RESULT

        fun registerCommand(command: BasicCommand<*>) = registeredCommands.add(command)

        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {

            val foundCommand = searchForSubCommandRecursively(label, args) ?: return true

            if (!sender.isOp && !sender.hasPermission(foundCommand.first.permissionNode)) {
                sender.sendMessage(NO_PERMISSION_MESSAGE)
                lastResult = Result.NO_PERMISSION
                return true
            }

            val commandTarget = if (sender is Player) SenderTarget.PLAYER else SenderTarget.CONSOLE

            if (!isValidTarget(commandTarget, foundCommand.first.target)) {
                sender.sendMessage("${ChatColor.RED}You cannot perform this command as ${sender::class.simpleName}")
                lastResult = Result.WRONG_TARGET
                return true
            }

            /* Subtract 1 from the arg size to ignore sub-command name arg. */
            if (!isValidArgLength(foundCommand.second.size, foundCommand.first.argRange)) {
                foundCommand.first.usage?.let { sender.sendMessage(it) }
                lastResult = Result.INVALID_NUMBER_OF_ARGUMENTS
                return true
            }

            if (foundCommand.first.argTypes.isNotEmpty() && !isCorrectArgTypeList(foundCommand.second, foundCommand.first.argTypes)) {
                foundCommand.first.usage?.let { sender.sendMessage(it) }
                lastResult = Result.INVALID_ARGUMENT_TYPE
                return true
            }

            return when (foundCommand.first.target) {
                SenderTarget.PLAYER -> (foundCommand.first as BasicCommand<Player>).run(
                    sender as Player,
                    foundCommand.second
                )
                else -> (foundCommand.first as BasicCommand<CommandSender>).run(sender, foundCommand.second)
            }
        }

        private fun searchForSubCommandRecursively(
            label: String,
            args: Array<out String>
        ): Pair<BasicCommand<*>, Array<out String>>? {

            val foundCommand = registeredCommands.find { it.name == label || label in it.aliases } ?: return null

            /* If there are no arguments provided to the main command, then the user is probably
             expecting help with the command, but this can be left up to the developer.
             */
            if (args.isEmpty()) return foundCommand to args

            return searchForSubCommandRecursively(args[0], args.sliceArray(1..args.lastIndex)) ?: (foundCommand to args)
        }

        private fun isValidTarget(receivedCommandTarget: SenderTarget, foundCommandTarget: SenderTarget) =
            !((receivedCommandTarget != foundCommandTarget) && (foundCommandTarget != SenderTarget.BOTH))

        private fun isValidArgLength(receivedArgNum: Int, requiredArgRange: IntRange) =
            (receivedArgNum >= requiredArgRange.first) && (receivedArgNum <= requiredArgRange.last)

        private fun isCorrectArgTypeList(receivedArgs: Array<out String>, requiredArgTypes: List<KClass<*>>): Boolean {
            for (i in receivedArgs.indices) {
                when (requiredArgTypes[i]) {
                    Int::class -> receivedArgs[i].toIntOrNull() ?: return false
                    Double::class -> receivedArgs[i].toDoubleOrNull() ?: return false
                    String::class -> receivedArgs[i]
                    else -> return false
                }
            }
            return true
        }
    }
}