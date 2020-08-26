package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class EggAttackEffect extends StatusEffect
{
    public EggAttackEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 30 >> amplifier;
        if (k > 0)
        {
            return duration % k == 0;
        } else
        {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.world.isClient)
        {
            int charge = 1;
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 10, 5, 10, 10))
            {
                if (entityLivingBaseIn.canSee(entity))
                {
                    EggEntity snowballentity = new EggEntity(entityLivingBaseIn.world, entityLivingBaseIn);
                    double d0 = entity.getEyeY() - (double) 1.1F;
                    double d1 = entity.getX() - entityLivingBaseIn.getX();
                    double d2 = d0 - snowballentity.getY();
                    double d3 = entity.getZ() - entityLivingBaseIn.getZ();
                    float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                    snowballentity.setVelocity(d1, d2 + (double) f, d3, 1.6F, 12.0F);
                    entityLivingBaseIn.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (entityLivingBaseIn.getRandom().nextFloat() * 0.4F + 0.8F));
                    entityLivingBaseIn.world.spawnEntity(snowballentity);
                    --charge;
                }

                if (charge <= 0)
                {
                    break;
                }
            }
        }
    }
}