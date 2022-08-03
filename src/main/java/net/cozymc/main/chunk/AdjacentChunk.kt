package net.cozymc.main.chunk

import org.bukkit.Chunk
import org.bukkit.block.Block

class AdjacentChunk(val chunk: Chunk, val adjacency: Adjacency) {
    val borderBlocks = mutableListOf<Block>()

    init {
        when (adjacency) {
            Adjacency.POS_X -> {
                for (z in 0..15)
                    for (y in 0..255)
                        borderBlocks.add(chunk.getBlock(0, y, z))
            }

            Adjacency.NEG_X -> {
                for (z in 0..15)
                    for (y in 0..255)
                        borderBlocks.add(chunk.getBlock(15, y, z))
            }

            Adjacency.POS_Z -> {
                for (x in 0..15)
                    for (y in 0..255)
                        borderBlocks.add(chunk.getBlock(x, y, 0))
            }

            Adjacency.NEG_Z -> {
                for (x in 0..15)
                    for (y in 0..255)
                        borderBlocks.add(chunk.getBlock(x, y, 15))
            }
        }
    }
}

enum class Adjacency {
    POS_X, NEG_X, POS_Z, NEG_Z
}