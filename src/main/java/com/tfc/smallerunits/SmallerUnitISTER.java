package com.tfc.smallerunits;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.smallerunits.utils.SmallUnit;
import com.tfc.smallerunits.utils.UnitPallet;
import com.tfc.smallerunits.utils.rendering.RenderTypeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SmallerUnitISTER extends ItemStackTileEntityRenderer {
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		super.func_239207_a_(stack, p_239207_2_, matrixStack, buffer, combinedLight, combinedOverlay);
		
		CompoundNBT nbt;
		//Carry on compat
		if (stack.getOrCreateTag().contains("tileData"))
			nbt = stack.getOrCreateTag().getCompound("tileData");
		else
			nbt = stack.getOrCreateTag().getCompound("BlockEntityTag");
		
		//More Carryon Compat
		if (nbt.isEmpty()) {
			if (p_239207_2_.equals(ItemCameraTransforms.TransformType.NONE)) {
				ItemStack stack1 = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
				if (stack1.getItem().getRegistryName().equals(new ResourceLocation("carryon:tile_item"))) {
					nbt = stack1.getOrCreateTag().getCompound("tileData");
				}
			}
		}
		
		int unitsPerBlock = nbt.getInt("upb");
		
		UnitPallet pallet = new UnitPallet(nbt.getCompound("containedUnits"), null);
		
		matrixStack.push();
		matrixStack.scale(1f / unitsPerBlock, 1f / unitsPerBlock, 1f / unitsPerBlock);
		for (SmallUnit value : pallet.posUnitMap.values()) {
			matrixStack.push();
			matrixStack.translate(value.pos.getX(), value.pos.getY() - 64, value.pos.getZ());
			Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(
					value.state, matrixStack,
					buffer, combinedLight, combinedOverlay,
					EmptyModelData.INSTANCE
			);
			if (!value.state.getFluidState().isEmpty()) {
				ResourceLocation texture = value.state.getFluidState().getFluid().getAttributes().getFlowingTexture();
				TextureAtlasSprite texture1 = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(texture);
				RenderType type = RenderTypeLookup.getRenderType(value.state.getFluidState());
				IVertexBuilder builder = buffer.getBuffer(RenderTypeHelper.getType(type));
				float flHeight = value.state.getFluidState().getHeight();
				
				int color = value.state.getFluidState().getFluid().getAttributes().getColor();
				
				RenderSystem.enableRescaleNormal();
				
				vert(0, 0, 1, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, 0, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, flHeight, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, flHeight, 1, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				
				vert(0, flHeight, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, flHeight, 0, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, 0, 0, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, 0, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				
				vert(0, 0, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, 0, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, flHeight, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, flHeight, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				
				vert(1, flHeight, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, flHeight, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, 0, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, 0, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				
				vert(0, flHeight, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(0, flHeight, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMaxV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, flHeight, 1, combinedLight, combinedOverlay, texture1.getMaxU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				vert(1, flHeight, 0, combinedLight, combinedOverlay, texture1.getMinU(), texture1.getMinV(), 0, 0, 0, builder, matrixStack, color);
				
				RenderSystem.disableRescaleNormal();
			}
			if (value.tileEntity != null) {
				matrixStack.push();
				TileEntity tileEntity = value.tileEntity;
				TileEntityRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.getRenderer(tileEntity);
				int matrixSize = matrixStack.stack.size();
				if (renderer != null) {
					try {
						renderer.render(tileEntity, 0, matrixStack, buffer, combinedLight, combinedOverlay);
					} catch (Throwable ignored) {
					}
				}
				while (matrixStack.stack.size() != matrixSize) {
					matrixStack.pop();
				}
				matrixStack.pop();
			}
//			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(value.state);
//			IVertexBuilder builder = buffer.getBuffer(RenderTypeLookup.getChunkRenderType(value.state));
//			for (Direction direction : Direction.values()) {
//				List<BakedQuad> quadList = model.getQuads(value.state, direction, new Random(value.pos.toLong()));
//				for (BakedQuad bakedQuad : quadList) {
//					builder.addQuad(matrixStack.getLast(),bakedQuad,1,1,1,combinedLight,combinedOverlay);
//				}
//			}
//			List<BakedQuad> quadList = model.getQuads(value.state, null, new Random(value.pos.toLong()));
//			for (BakedQuad bakedQuad : quadList) {
//				builder.addQuad(matrixStack.getLast(),bakedQuad,1,1,1,combinedLight,combinedOverlay);
//			}
			matrixStack.pop();
		}
		matrixStack.pop();

//		if (pallet.posUnitMap.isEmpty()) {
//			matrixStack.push();
//			IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
//			ItemCameraTransforms transforms = model.getItemCameraTransforms();
//			TransformationMatrix transformationMatrix = TransformationHelper.toTransformation(transforms.getTransform(p_239207_2_));
//			{
//				Vector3f trans = transformationMatrix.getTransformaion().getTranslation();
//				matrixStack.translate(-trans.getX(), -trans.getY(), -trans.getZ());
//
//				Quaternion quaternion = transformationMatrix.getTransformaion().getRotationLeft().copy();
//				quaternion.multiply(-1);
//				matrixStack.rotate(quaternion);
//
//				Vector3f scale = transformationMatrix.getTransformaion().getScale();
//				matrixStack.scale(1f / scale.getX(), 1f / scale.getY(), 1f / scale.getZ());
//
////					matrixStack.rotate(transformationMatrix.getTransformaion().getRightRot());
//			}
//
//			IBakedModel swordModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
//			ItemCameraTransforms vec3f = swordModel.getItemCameraTransforms();
//			TransformationMatrix matrix = TransformationHelper.toTransformation(vec3f.getTransform(p_239207_2_));
//
//			matrix.push(matrixStack);
//			RenderSystem.disableTexture();
//			ResourceLocation texture = new ResourceLocation("minecraft:textures/block/white_concrete.png");
//			for (int i = 1; i < 16; i++) {
//				matrixStack.push();
//				matrixStack.translate(-1.5f,-0.75f,0);
//				matrixStack.scale(1f / 2, 1f / 2, 1f / 2);
//				{
//					int amt = i >= 14 || i == 1 ? 1 : 0;
//					matrixStack.translate(i / 4f, i / 4f, 1);
//					SmallerUnitsTESR.renderCube(amt, amt, amt, 0, 0, 0, buffer.getBuffer(RenderType.getEntitySolid(texture)), combinedOverlay, combinedLight, matrixStack, !p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI));
//					matrixStack.push();
//					matrixStack.translate(0, -0.25f, 0);
//					SmallerUnitsTESR.renderCube(amt, amt, amt, 0, 0, 0, buffer.getBuffer(RenderType.getEntitySolid(texture)), combinedOverlay, combinedLight, matrixStack, !p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI));
//					matrixStack.pop();
//					matrixStack.push();
//					matrixStack.translate(0, 0, -0.25f);
//					SmallerUnitsTESR.renderCube(amt, amt, amt, 0, 0, 0, buffer.getBuffer(RenderType.getEntitySolid(texture)), combinedOverlay, combinedLight, matrixStack, !p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI));
//					matrixStack.pop();
//				}
//				matrixStack.pop();
//			}
//			matrixStack.translate(-1.5f,-0.75f,0);
//			matrixStack.scale(1f / 2, 1f / 2, 1f / 2);
//			matrixStack.translate(0,0,1);
//			SmallerUnitsTESR.renderCube(1, 1, 1, 0, 0, 0, buffer.getBuffer(RenderType.getEntitySolid(texture)), combinedOverlay, combinedLight, matrixStack, !p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI));
//			RenderSystem.enableTexture();
//			matrixStack.pop();
//
//			matrixStack.pop();
//		}
		if (pallet.posUnitMap.isEmpty()) {
			matrixStack.push();
			matrixStack.scale(4, 4, 4);
			RenderSystem.disableTexture();
			matrixStack.scale(1f / unitsPerBlock, 1f / unitsPerBlock, 1f / unitsPerBlock);
			renderHalf(matrixStack, buffer, combinedOverlay, combinedLight, unitsPerBlock);
			matrixStack.push();
			matrixStack.translate(unitsPerBlock / 4f, 0, unitsPerBlock / 4f);
			matrixStack.rotate(new Quaternion(0, 180, 0, true));
			renderHalf(matrixStack, buffer, combinedOverlay, combinedLight, unitsPerBlock);
			matrixStack.pop();
			RenderSystem.enableTexture();
			matrixStack.pop();
			
			matrixStack.push();
			int scale = 16;
			matrixStack.scale(1f / scale, 1f / scale, 1f / scale);
			matrixStack.rotate(new Quaternion(0, 0, 180, true));
			matrixStack.translate(-8, -8, -1);
			matrixStack.translate(-Minecraft.getInstance().fontRenderer.getStringWidth("1/" + unitsPerBlock) / 2f, 0, 0);
			Minecraft.getInstance().fontRenderer.renderString("1/" + unitsPerBlock, 0, 0, 16777215, true, matrixStack.getLast().getMatrix(), buffer, false, 0, combinedLight);
			matrixStack.pop();
		}
	}
	
	public void renderHalf(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedOverlayIn, int combinedLightIn, int unitsPerBlock) {
		renderCorner(matrixStackIn, bufferIn, combinedOverlayIn, combinedLightIn);
		matrixStackIn.push();
		matrixStackIn.translate(0, unitsPerBlock / 4f, 0);
		matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
		renderCorner(matrixStackIn, bufferIn, combinedOverlayIn, combinedLightIn);
		matrixStackIn.pop();
		matrixStackIn.push();
		matrixStackIn.translate(0, 0, unitsPerBlock / 4f);
		matrixStackIn.rotate(new Quaternion(0, 90, 0, true));
		renderCorner(matrixStackIn, bufferIn, combinedOverlayIn, combinedLightIn);
		matrixStackIn.pop();
		matrixStackIn.push();
		matrixStackIn.translate(0, unitsPerBlock / 4f, unitsPerBlock / 4f);
		matrixStackIn.rotate(new Quaternion(180, 0, 0, true));
		renderCorner(matrixStackIn, bufferIn, combinedOverlayIn, combinedLightIn);
		matrixStackIn.pop();
	}
	
	public void renderCorner(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedOverlayIn, int combinedLightIn) {
		matrixStackIn.push();
		matrixStackIn.scale(0.001f, 1, 1);
		SmallerUnitsTESR.renderCube(1, 1, 0, 0, 0, 0, bufferIn.getBuffer(RenderType.getEntitySolid(new ResourceLocation("textures/block/white_concrete.png"))), combinedOverlayIn, combinedLightIn, matrixStackIn, true);
		matrixStackIn.pop();
		matrixStackIn.push();
		matrixStackIn.scale(1, 0.001f, 1);
		SmallerUnitsTESR.renderCube(1, 1, 0, 0, 0, 0, bufferIn.getBuffer(RenderType.getEntitySolid(new ResourceLocation("textures/block/white_concrete.png"))), combinedOverlayIn, combinedLightIn, matrixStackIn, true);
		matrixStackIn.pop();
		matrixStackIn.push();
		matrixStackIn.scale(1, 1, 0.001f);
		SmallerUnitsTESR.renderCube(1, 1, 0, 0, 0, 0, bufferIn.getBuffer(RenderType.getEntitySolid(new ResourceLocation("textures/block/white_concrete.png"))), combinedOverlayIn, combinedLightIn, matrixStackIn, true);
		matrixStackIn.pop();
	}
	
	private void vert(float x, float y, float z, int light, int overlay, float u, float v, float nx, float ny, float nz, IVertexBuilder builder1, MatrixStack matrixStack, int color) {
		Vector3f vec = SmallerUnitsTESR.translate(matrixStack, x, y, z);
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = ((color >> 0) & 0xFF) / 255f;
		builder1.addVertex(
				vec.getX(), vec.getY(), vec.getZ(),
				r, g, b, ((color >> 24) & 0xff) / 255f,
				u, v,
				light, overlay,
				nx, ny, nz
		);
	}
}
