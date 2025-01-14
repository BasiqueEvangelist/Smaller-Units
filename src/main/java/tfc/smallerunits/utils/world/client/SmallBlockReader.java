package tfc.smallerunits.utils.world.client;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;

import javax.annotation.Nullable;

public class SmallBlockReader implements IBlockDisplayReader {
	private final World wld;
	
	public SmallBlockReader(World wld) {
		this.wld = wld;
	}
	
	@Nullable
	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return wld.getTileEntity(pos);
	}
	
	@Override
	public BlockState getBlockState(BlockPos pos) {
		return wld.getBlockState(pos);
	}
	
	@Override
	public FluidState getFluidState(BlockPos pos) {
		return wld.getFluidState(pos);
	}
	
	@Override
	public int getLightValue(BlockPos pos) {
		return wld.getLightValue(pos);
	}
	
	@Override
	public int getMaxLightLevel() {
		return wld.getMaxLightLevel();
	}
	
	@Override
	public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
		return wld.func_230487_a_(p_230487_1_, p_230487_2_);
	}
	
	@Override
	public WorldLightManager getLightManager() {
		return wld.getLightManager();
	}
	
	@Override
	public int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn) {
		return wld.getBlockColor(blockPosIn, colorResolverIn);
	}
	
	@Override
	public int getLightFor(LightType lightTypeIn, BlockPos blockPosIn) {
		return wld.getLightFor(lightTypeIn, blockPosIn);
	}
	
	@Override
	public int getLightSubtracted(BlockPos blockPosIn, int amount) {
		return wld.getLightSubtracted(blockPosIn, amount);
	}
	
	@Override
	public boolean canSeeSky(BlockPos blockPosIn) {
		return wld.canBlockSeeSky(blockPosIn);
	}
}
