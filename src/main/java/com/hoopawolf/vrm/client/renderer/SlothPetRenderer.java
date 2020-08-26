package com.hoopawolf.vrm.client.renderer;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerBulletEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SlothPetRenderer extends EntityRenderer<SlothPetEntity>
{
    private static final Identifier SHULKER_SPARK_TEXTURE = new Identifier(Reference.MOD_ID, "textures/entity/slothpet.png");
    private static final RenderLayer field_229123_e_ = RenderLayer.getEntityTranslucent(SHULKER_SPARK_TEXTURE);
    private final ShulkerBulletEntityModel<SlothPetEntity> model = new ShulkerBulletEntityModel<>();

    public SlothPetRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    protected int getBlockLight(SlothPetEntity entityIn, BlockPos partialTicks)
    {
        return 15;
    }

    @Override
    public void render(SlothPetEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn)
    {
        matrixStackIn.push();
        float f = MathHelper.lerpAngle(entityIn.prevYaw, entityIn.yaw, partialTicks);
        float f1 = MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.pitch);
        float f2 = (float) entityIn.age + partialTicks;
        matrixStackIn.translate(0.0D, 0.15F, 0.0D);
        matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.sin(f2 * 0.1F) * 180.0F));
        matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.cos(f2 * 0.1F) * 180.0F));
        matrixStackIn.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(f2 * 0.15F) * 360.0F));
        matrixStackIn.scale(-0.5F, -0.5F, 0.5F);
        this.model.setAngles(entityIn, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.getLayer(SHULKER_SPARK_TEXTURE));
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(field_229123_e_);
        this.model.render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public Identifier getTexture(SlothPetEntity entity)
    {
        return SHULKER_SPARK_TEXTURE;
    }
}