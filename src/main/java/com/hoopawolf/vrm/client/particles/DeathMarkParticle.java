package com.hoopawolf.vrm.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class DeathMarkParticle extends SpriteBillboardParticle
{
    private DeathMarkParticle(ClientWorld p_i51030_1_, double p_i51030_2_, double p_i51030_4_, double p_i51030_6_)
    {
        super(p_i51030_1_, p_i51030_2_, p_i51030_4_, p_i51030_6_, 0.0D, 0.0D, 0.0D);
        this.gravityStrength = 0.0F;
        this.maxAge = 1;
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
        return 0.5F;
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
            DeathMarkParticle flameparticle = new DeathMarkParticle(worldIn, x, y, z);
            flameparticle.setSprite(this.spriteSet);
            return flameparticle;
        }
    }
}
