package com.klnon.quickstore.gui.render;

import com.klnon.quickstore.utils.Region;
import com.klnon.quickstore.utils.Utils;
import com.klnon.quickstore.model.BlockData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import java.util.ArrayList;
import java.util.List;

public class RenderEnqueue implements Runnable
{
	private final Region box;

	public RenderEnqueue(Region region )
	{
		box = region;
	}

	@Override
	public void run() // Our thread code for finding syncRenderList near the player.
	{
		blockFinder();
	}


	private void blockFinder() {

		final World world = Minecraft.getInstance().world;
        final PlayerEntity player = Minecraft.getInstance().player;
        if( world == null || player == null )
        	return;

		final List<RenderBlockProps> renderQueue = new ArrayList<>();
		int lowBoundX, highBoundX, lowBoundY, highBoundY, lowBoundZ, highBoundZ;

		// Used for cleaning up the searching process
		BlockState currentState;

		ResourceLocation block;
		BlockData data;

		// Loop on chunks (x, z)
		for ( int chunkX = box.minChunkX; chunkX <= box.maxChunkX; chunkX++ )
		{
			// Pre-compute the extend bounds on X
			int x = chunkX << 4; // lowest x coord of the chunk in block/world coordinates
			lowBoundX = (x < box.minX) ? box.minX - x : 0; // lower bound for x within the extend
			highBoundX = (x + 15 > box.maxX) ? box.maxX - x : 15;// and higher bound. Basically, we clamp it to fit the radius.

			for ( int chunkZ = box.minChunkZ; chunkZ <= box.maxChunkZ; chunkZ++ )
			{
				// Time to getStore the chunk (16x256x16) and split it into 16 vertical extends (16x16x16)
				if (!world.chunkExists(chunkX, chunkZ)) {
					continue; // We won't find anything interesting in unloaded chunks
				}

				Chunk chunk = world.getChunk( chunkX, chunkZ );
				ChunkSection[] extendsList = chunk.getSections();

				// Pre-compute the extend bounds on Z
				int z = chunkZ << 4;
				lowBoundZ = (z < box.minZ) ? box.minZ - z : 0;
				highBoundZ = (z + 15 > box.maxZ) ? box.maxZ - z : 15;

				// Loop on the extends around the player's layer (6 down, 2 up)
				for ( int curExtend = box.minChunkY; curExtend <= box.maxChunkY; curExtend++ )
				{
					ChunkSection ebs = extendsList[curExtend];
					if (ebs == null) // happens quite often!
						continue;

					// Pre-compute the extend bounds on Y
					int y = curExtend << 4;
					lowBoundY = (y < box.minY) ? box.minY - y : 0;
					highBoundY = (y + 15 > box.maxY) ? box.maxY - y : 15;

					// Now that we have an extend, let's check all its blocks
					for ( int i = lowBoundX; i <= highBoundX; i++ ) {
						for ( int j = lowBoundY; j <= highBoundY; j++ ) {
							for ( int k = lowBoundZ; k <= highBoundZ; k++ ) {
								currentState = ebs.getBlockState(i, j, k);

								block = currentState.getBlock().getRegistryName();
								if( block == null )
									continue;

								data = Utils.getBlockData();
								// Reject blacklisted blocks
								if( Utils.blackList.contains(currentState.getBlock().asItem()) || !(currentState.getBlock().getRegistryName().toString().equals(data.getBlockName())) )
									continue;

								if(!data.isDrawing()) // fail safe
									continue;

								// Push the block to the render queue
								renderQueue.add(new RenderBlockProps(x + i, y + j, z + k, data.getColor()));
							}
						}
					}
				}
			}
		}
		final BlockPos playerPos = player.getPosition();
		renderQueue.sort((t, t1) -> Double.compare(t1.getPos().distanceSq(playerPos), t.getPos().distanceSq((playerPos))));
		Render.syncRenderList.clear();
		Render.syncRenderList.addAll( renderQueue ); // Add all our found blocks to the Render.syncRenderList list. To be use by Render when drawing.
	}

	public static void checkBlock(BlockPos pos, BlockState state, boolean add )
	{
		if ( !Utils.isQuickSeeActive() || Utils.getBlockData()==null)
		    return; // just pass

		// If we're removing then remove :D
		if( !add ) {
			Render.syncRenderList.remove( new RenderBlockProps(pos,0) );
			return;
		}

		ResourceLocation block = state.getBlock().getRegistryName();
		if( block == null )
			return;

		BlockData data = Utils.getBlockData();
		if( data == null ||  !data.isDrawing() )
			return;

		// the block was added to the world, let's add it to the drawing buffer
		Render.syncRenderList.add(new RenderBlockProps(pos, data.getColor()) );
	}
}
