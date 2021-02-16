package com.tfc.smallerunits.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.concurrent.DelegatedTaskExecutor;
import net.minecraft.util.concurrent.ITaskExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkTaskPriorityQueueSorter;
import net.minecraft.world.chunk.IChunkLightProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.lighting.IWorldLightListener;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorldLightManager;

import javax.annotation.Nullable;

public class FakeLightingManager extends ServerWorldLightManager {
	private WorldLightManager lightManager;
	
	private ChunkManager manager;
	private int lastSize = 0;
	private int[] lighting = new int[0];
	private int lastX = 0;
	private int lastY = 0;
	private int lastZ = 0;
	
	public FakeLightingManager(IChunkLightProvider provider, ChunkManager chunkManagerIn, boolean hasSkyLight, DelegatedTaskExecutor<Runnable> p_i50701_4_, ITaskExecutor<ChunkTaskPriorityQueueSorter.FunctionEntry<Runnable>> p_i50701_5_, FakeServerWorld world) {
		super(provider, chunkManagerIn, hasSkyLight, p_i50701_4_, p_i50701_5_);
		this.lightManager = new WorldLightManager(provider, true, hasSkyLight);
		this.manager = chunkManagerIn;
	}
	
	@Override
	public void checkBlock(BlockPos blockPosIn) {
		lightManager.checkBlock(blockPosIn);
	}
	
	@Override
	public int tick(int toUpdateCount, boolean updateSkyLight, boolean updateBlockLight) {
		FakeServerWorld world = (FakeServerWorld) manager.world;
		if (lastSize != world.owner.unitsPerBlock) {
			lighting = new int[world.owner.unitsPerBlock * world.owner.unitsPerBlock * world.owner.unitsPerBlock];
			lastSize = world.owner.unitsPerBlock;
		}
		int i;
		for (i = 0; i < toUpdateCount; i++) {
			lastX++;
			
			if (lastX > world.owner.unitsPerBlock - 1) {
				lastZ++;
				lastX = 0;
				
				if (lastZ > world.owner.unitsPerBlock - 1) {
					lastY++;
					lastZ = 0;
					
					if (lastY > world.owner.unitsPerBlock - 1) {
						lastY = 0;
						break;
					}
				}
			}
			
			BlockPos pos = new BlockPos(lastX, lastY, lastZ);
			if (isInbounds(pos, world.owner.unitsPerBlock)) {
				BlockState state = world.getBlockState(pos.add(0, 64, 0));
				int stateLight = state.getLightValue(world, pos.add(0, 64, 0));
				int max = stateLight;
				for (Direction dir : Direction.values()) {
					if (isInbounds(pos.offset(dir), world.owner.unitsPerBlock)) {
						int amt = lighting[toIndex(pos.offset(dir))];
						max = Math.max(max, amt - 1);
					}
				}
				if (stateLight == 0) {
					max -= state.getOpacity(manager.world, pos.add(0, 64, 0));
				}
				lighting[toIndex(pos)] = max;
			} else {
				lastX = 0;
				lastY = 0;
				lastZ = 0;
				break;
			}
		}
		return i;
//		return lightManager.tick(toUpdateCount, updateSkyLight, updateBlockLight);
	}
	
	public boolean isInbounds(BlockPos pos, int upb) {
		return
				!(
						pos.getX() < 0 ||
								pos.getX() > upb - 1 ||
								pos.getZ() < 0 ||
								pos.getZ() > upb - 1 ||
								pos.getY() < 0 ||
								pos.getY() > upb - 1
				) && toIndex(pos) >= 0 && toIndex(pos) < lighting.length;
	}
	
	public int getBlockLight(BlockPos pos) {
		if (isInbounds(pos, ((FakeServerWorld) manager.world).owner.unitsPerBlock))
			return lighting[toIndex(pos)];
		return 0;
	}
	
	public int toIndex(BlockPos pos) {
		return
				pos.getX() +
						(pos.getY() * ((FakeServerWorld) manager.world).owner.unitsPerBlock) +
						(pos.getZ() * ((FakeServerWorld) manager.world).owner.unitsPerBlock * ((FakeServerWorld) manager.world).owner.unitsPerBlock);
	}
	
	@Override
	public boolean hasLightWork() {
		return lightManager.hasLightWork();
	}
	
	@Override
	public IWorldLightListener getLightEngine(LightType type) {
		return lightManager.getLightEngine(type);
	}
	
	@Override
	public String getDebugInfo(LightType p_215572_1_, SectionPos p_215572_2_) {
		return lightManager.getDebugInfo(p_215572_1_, p_215572_2_);
	}
	
	@Override
	public int getLightSubtracted(BlockPos blockPosIn, int amount) {
		return lightManager.getLightSubtracted(blockPosIn, amount);
	}
	
	@Override
	public void onBlockEmissionIncrease(BlockPos blockPosIn, int p_215573_2_) {
		lightManager.onBlockEmissionIncrease(blockPosIn, p_215573_2_);
	}
	
	@Override
	protected void updateChunkStatus(ChunkPos p_215581_1_) {
		for (int i = 64; i < 64 + 16; i++) {
			lightManager.updateSectionStatus(SectionPos.from(p_215581_1_, i), false);
		}
	}
	
	@Override
	public void updateSectionStatus(SectionPos pos, boolean isEmpty) {
		lightManager.updateSectionStatus(pos, isEmpty);
	}
	
	@Override
	public void enableLightSources(ChunkPos p_215571_1_, boolean p_215571_2_) {
		lightManager.enableLightSources(p_215571_1_, p_215571_2_);
	}
	
	@Override
	public void setData(LightType type, SectionPos pos, @Nullable NibbleArray array, boolean p_215574_4_) {
		lightManager.setData(type, pos, array, p_215574_4_);
	}
	
	@Override
	public void retainData(ChunkPos pos, boolean retain) {
		lightManager.retainData(pos, retain);
	}
	
	@Override
	public void func_215567_a(BlockPos p_215567_1_, boolean p_215567_2_) {
		lightManager.func_215567_a(p_215567_1_, p_215567_2_);
	}
}
