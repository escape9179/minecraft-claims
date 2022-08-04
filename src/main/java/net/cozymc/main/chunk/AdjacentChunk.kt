package net.cozymc.main.chunk

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

class AdjacentChunk(val chunk: Chunk, val adjacency: Adjacency) {

    fun getBorderBlocksAtHeightRange(range: IntRange): List<Block> {
        val borderBlocks = mutableListOf<Block>()
        when (adjacency) {
            Adjacency.POS_X, Adjacency.NEG_X ->
                for (z in 0..15)
                    for (y in range)
                        borderBlocks.add(chunk.getBlock(if (adjacency == Adjacency.POS_X) 0 else 15, y, z))
            Adjacency.POS_Z, Adjacency.NEG_Z ->
                for (x in 0..15)
                    for (y in range)
                        borderBlocks.add(chunk.getBlock(x, y, if (adjacency == Adjacency.POS_Z) 0 else 15))
        }
        return borderBlocks
    }
}

enum class Adjacency {
    POS_X, NEG_X, POS_Z, NEG_Z
}