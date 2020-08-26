package com.hoopawolf.vrm.client.particles;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.server.SetPotionEffectMultipleMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PlagueParticle extends SpriteBillboardParticle
{
    private PlagueParticle(ClientWorld p_i51010_1_, double p_i51010_2_, double p_i51010_4_, double p_i51010_6_, double p_i51010_8_, double p_i51010_10_, double p_i51010_12_, float p_i51010_14_)
    {
        super(p_i51010_1_, p_i51010_2_, p_i51010_4_, p_i51010_6_, 0.0D, 0.0D, 0.0D);
        this.velocityX += p_i51010_8_;
        this.velocityZ += p_i51010_12_;
        this.velocityX *= 0.9F;
        this.velocityY *= -0.1F;
        this.velocityZ *= 0.9F;
        this.colorRed = 0.5F;
        this.colorGreen = 0.7F;
        this.colorBlue = 0.0F;
        this.scale *= 0.75F * p_i51010_14_;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int) ((float) this.maxAge * p_i51010_14_);
        this.maxAge = Math.max(this.maxAge, 1);
        this.collidesWithWorld = false;
    }

    @Override
    public ParticleTextureSheet getType()
    {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float scaleFactor)
    {
        return this.scale * MathHelper.clamp(((float) this.age + scaleFactor) / (float) this.maxAge * 32.0F, 0.0F, 0.5F);
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge)
        {
            this.markDead();
        } else
        {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.y == this.prevPosY)
            {
                this.velocityX *= 1.1D;
                this.velocityZ *= 1.1D;
            }

            this.velocityX *= 0.96F;
            this.velocityZ *= 0.96F;
            if (this.onGround)
            {
                this.velocityX *= 0.7F;
                this.velocityZ *= 0.7F;
            }

        }

        for (LivingEntity entity : EntityHelper.getEntitiesNearbyWithPos(world, getBoundingBox(), new BlockPos(this.x, this.y, this.z), LivingEntity.class, 2, 2, 2, 3))
        {
            SetPotionEffectMultipleMessage _messagePoison = new SetPotionEffectMultipleMessage(entity.getEntityId(), 300, 1, 0, 3);
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeInt(_messagePoison.getMessageType());
            _messagePoison.encode(passedData);

            ClientSidePacketRegistry.INSTANCE.sendToServer(VulcanRevengeMod.SERVER_PACKET_ID, passedData);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType>
    {
        private final SpriteProvider spriteSet;

        public Factory(SpriteProvider p_i50823_1_)
        {
            this.spriteSet = p_i50823_1_;
        }

        @Override
        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            PlagueParticle flameparticle = new PlagueParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 5);
            flameparticle.setSprite(this.spriteSet);
            return flameparticle;
        }
    }
}
