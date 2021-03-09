package com.tfc.smallerunits.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.smallerunits.SmallerUnitsConfig;
import com.tfc.smallerunits.SmallerUnitsTESR;
import com.tfc.smallerunits.block.SmallerUnitBlock;
import com.tfc.smallerunits.block.UnitTileEntity;
import com.tfc.smallerunits.utils.rendering.BufferCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.LightType;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class RenderingHandler {
	//	public static void onRenderWorldLast(RenderWorldLastEvent event) {
	public static void onRenderWorldLast(MatrixStack matrixStack, Matrix4f projectionIn) {
//		float partialTicks = event.getPartialTicks();
//		ActiveRenderInfo activerenderinfo = Minecraft.getInstance().getRenderManager().info;
//		MatrixStack matrixStackIn = new MatrixStack();
//
//		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(Minecraft.getInstance().gameRenderer, activerenderinfo, partialTicks);
//		activerenderinfo.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
//		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(cameraSetup.getRoll()));
//
//		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(activerenderinfo.getPitch()));
//		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(activerenderinfo.getYaw() + 180.0F));
		
		MatrixStack.Entry entry = matrixStack.getLast();
		matrixStack.stack.removeLast();
		MatrixStack stack = new MatrixStack();
//		stack.getLast().getMatrix().mul(event.getProjectionMatrix());
		stack.stack.add(entry);
//		stack.rotate(Minecraft.getInstance().getRenderManager().info.getRotation());
		stack.translate(
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getX(),
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getY(),
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ()
		);
		ClippingHelper clippinghelper = new ClippingHelper(stack.getLast().getMatrix(), projectionIn);
		clippinghelper.setCameraPosition(Minecraft.getInstance().renderViewEntity.getPosX(), Minecraft.getInstance().renderViewEntity.getPosY(), Minecraft.getInstance().renderViewEntity.getPosZ());
		for (TileEntity tileEntity : Minecraft.getInstance().world.loadedTileEntityList) {
			if (tileEntity instanceof UnitTileEntity) {
//				if (clippinghelper.isBoundingBoxInFrustum(tileEntity.getRenderBoundingBox())) {
				stack.push();
				MatrixStack stack1 = new MatrixStack();
				stack.translate(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
				BufferCache cache = new BufferCache(Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), stack1);
				stack1.stack.add(stack.getLast());
				SmallerUnitsTESR.render(
						(UnitTileEntity) tileEntity, Minecraft.getInstance().getRenderPartialTicks(),
						stack1, cache,
						LightTexture.packLight(
								Minecraft.getInstance().world.getLightFor(LightType.BLOCK, tileEntity.getPos()),
								Minecraft.getInstance().world.getLightFor(LightType.SKY, tileEntity.getPos())
						),
						OverlayTexture.NO_OVERLAY
				);
				stack.pop();
//				}
			}
		}
		matrixStack.stack.add(entry);
	}
	
	public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
		SmallerUnitsTESR.bufferCache.forEach((pos, pair) -> {
			pair.getSecond().dispose();
		});
		SmallerUnitsTESR.bufferCache.clear();
	}
	
	public static void onLeaveWorld(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		SmallerUnitsTESR.bufferCache.forEach((pos, pair) -> {
			pair.getSecond().dispose();
		});
		SmallerUnitsTESR.bufferCache.clear();
	}
	
	public static void onDrawSelectionBox(DrawHighlightEvent event) {
		if (!SmallerUnitsConfig.CLIENT.useExperimentalSelection.get()) return;
		if (!(event.getTarget() instanceof BlockRayTraceResult)) return;
		BlockState state = Minecraft.getInstance().world.getBlockState(((BlockRayTraceResult) event.getTarget()).getPos());
		if (!(state.getBlock() instanceof SmallerUnitBlock)) return;
		event.getMatrix().push();

//		UnitTileEntity tileEntity = (UnitTileEntity) Minecraft.getInstance().player.getEntityWorld().getTileEntity(((BlockRayTraceResult) event.getTarget()).getPos());
//		UnitRaytraceContext raytraceContext = UnitRaytraceHelper.raytraceBlock(tileEntity, Minecraft.getInstance().player, true, ((BlockRayTraceResult) event.getTarget()).getPos(), Optional.empty());
//		raytraceContext.posHit = raytraceContext.posHit.offset(raytraceContext.hitFace.orElse(Direction.UP));
//		BlockPos worldPos = ((BlockRayTraceResult) event.getTarget()).getPos();
////		if (raytraceContext.vecHit.equals(new Vector3d(-100,-100,-100))) {
////			raytraceContext.vecHit = hit.getHitVec()
////					.subtract(worldPos.getX(), worldPos.getY(), worldPos.getZ())
////					.add(worldPos.getX(), worldPos.getY(), worldPos.getZ())
////			;
////			if (raytraceContext.hitFace.orElse(hit.getFace()).equals(Direction.UP)) {
////				raytraceContext.vecHit = raytraceContext.vecHit.subtract(0,-(1f/16)/4,0);
////			}
////			raytraceContext.posHit = raytraceContext.posHit.up();
////		}
//		raytraceContext.vecHit = raytraceContext.vecHit
//				.subtract(worldPos.getX(),worldPos.getY(),worldPos.getZ())
//				.subtract(raytraceContext.posHit.getX()/((float)tileEntity.unitsPerBlock),(raytraceContext.posHit.getY()-64)/((float)tileEntity.unitsPerBlock),raytraceContext.posHit.getZ()/((float)tileEntity.unitsPerBlock))
//		;
//		raytraceContext.vecHit = raytraceContext.vecHit.scale(tileEntity.unitsPerBlock).add(raytraceContext.posHit.getX(),raytraceContext.posHit.getY(),raytraceContext.posHit.getZ());
//		Direction face = raytraceContext.hitFace.orElse(Direction.UP);
//		raytraceContext.posHit = raytraceContext.posHit.offset(face.getOpposite());
//		tileEntity.world.result = new BlockRayTraceResult(
//				raytraceContext.vecHit,
//				face,
//				raytraceContext.posHit,
//				true
//		);
//
//		MinecraftForge.EVENT_BUS.post(new DrawHighlightEvent.HighlightBlock(
//				event.getContext(),
//				event.getInfo(),
//				tileEntity.world.result,
//				event.getPartialTicks(),
//				event.getMatrix(),
//				event.getBuffers()
//		));
		
		event.getMatrix().translate(
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getX(),
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getY(),
				-Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ()
		);
		VoxelShape shape = SmallerUnitBlock.getShapeOld(state, Minecraft.getInstance().world, ((BlockRayTraceResult) event.getTarget()).getPos(),
				new ISelectionContext() {
					@Override
					public boolean getPosY() {
						return false;
					}
					
					@Override
					public boolean func_216378_a(VoxelShape shape, BlockPos pos, boolean p_216378_3_) {
						return false;
					}
					
					@Override
					public boolean hasItem(Item itemIn) {
						return false;
					}
					
					@Nullable
					@Override
					public Entity getEntity() {
						return Minecraft.getInstance().player;
					}
					
					@Override
					public boolean func_230426_a_(FluidState p_230426_1_, FlowingFluid p_230426_2_) {
						return false;
					}
				});
		IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.getLines());
		event.getMatrix().translate(
				((BlockRayTraceResult) event.getTarget()).getPos().getX(),
				((BlockRayTraceResult) event.getTarget()).getPos().getY(),
				((BlockRayTraceResult) event.getTarget()).getPos().getZ()
		);
		Matrix4f matrix4f = event.getMatrix().getLast().getMatrix();
		float red = 0;
		float green = 0;
		float blue = 0;
		float alpha = 0.5f;
		shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
			builder.pos(matrix4f, (float) (x1), (float) (y1), (float) (z1)).color(red, green, blue, alpha).endVertex();
			builder.pos(matrix4f, (float) (x2), (float) (y2), (float) (z2)).color(red, green, blue, alpha).endVertex();
		});
		if (event.isCancelable()) {
			event.setCanceled(true);
		}
		event.getMatrix().pop();
	}
}