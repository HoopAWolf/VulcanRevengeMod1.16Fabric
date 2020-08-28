package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.PedestalTileEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;

public class PedestalRenderer<T extends PedestalTileEntity> extends BlockEntityRenderer<T>
{
    public PedestalRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.35D, 0.5D);
        float currentTime = tileEntityIn.getWorld().getTime() + partialTicks;
        matrixStackIn.translate(0D, Math.sin(Math.PI * (currentTime * 0.0125F)) * 0.2F, 0D);
        matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(tileEntityIn.getDegree()));
        renderItem(tileEntityIn.getStoredItem(), partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        matrixStackIn.pop();
    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn,
                            int combinedLightIn)
    {
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, combinedLightIn,
                OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn);
    }

}
