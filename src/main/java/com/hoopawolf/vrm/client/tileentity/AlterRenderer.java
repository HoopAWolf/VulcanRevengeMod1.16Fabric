package com.hoopawolf.vrm.client.tileentity;

import com.hoopawolf.vrm.blocks.tileentity.AlterTileEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class AlterRenderer extends BlockEntityRenderer<AlterTileEntity>
{
    private static final float field_229057_l_ = (float) (Math.sqrt(3.0D) / 2.0D);

    public AlterRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    private static void func_229061_a_(VertexConsumer p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_)
    {
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).next();
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).next();
    }

    private static void func_229060_a_(VertexConsumer p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_, Vec3i color)
    {
        p_229060_0_.vertex(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(color.getX(), color.getY(), color.getZ(), 0).next();
    }

    private static void func_229062_b_(VertexConsumer p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_, Vec3i color)
    {
        p_229062_0_.vertex(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(color.getX(), color.getY(), color.getZ(), 0).next();
    }

    private static void func_229063_c_(VertexConsumer p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_, Vec3i color)
    {
        p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(color.getX(), color.getY(), color.getZ(), 0).next();
    }

    @Override
    public void render(AlterTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        Random random = new Random();

        if (tileEntityIn.isActivated())
        {
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, 1.35D + (tileEntityIn.getTimer() * 0.0125F) * 0.2F, 0.5D);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(tileEntityIn.getDegree()));
            renderItem(tileEntityIn.getActivationItem(), partialTicks, matrixStackIn, bufferIn, combinedLightIn);

            if (tileEntityIn.getDegree() <= 0 && tileEntityIn.isActivated())
            {
                matrixStackIn.pop();
            }

            if (tileEntityIn.isDone())
            {
                float f5 = 0.08F;
                float f7 = 0.0F;

                VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RenderLayer.getLightning());
                matrixStackIn.push();
                matrixStackIn.scale(0.02F, 0.02F, 0.02F);

                for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i)
                {
                    matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(360.0F));
                    matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
                    matrixStackIn.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0F + f5 * 90.0F));
                    float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                    float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                    Matrix4f matrix4f = matrixStackIn.peek().getModel();
                    int j = (int) (255.0F * (1.0F - f7));
                    func_229061_a_(ivertexbuilder2, matrix4f, j);
                    func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(255, 255, 127));
                    func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(0, 0, 255));
                    func_229061_a_(ivertexbuilder2, matrix4f, j);
                    func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(255, 255, 255));
                    func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(127, 127, 255));
                    func_229061_a_(ivertexbuilder2, matrix4f, j);
                    func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, new Vec3i(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                }

                matrixStackIn.pop();
            }

            matrixStackIn.pop();
        }
    }

    private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn,
                            int combinedLightIn)
    {
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, combinedLightIn,
                OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn);
    }
}
