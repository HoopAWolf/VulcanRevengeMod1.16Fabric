package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;

public class ExplosiveFireChargeEffect extends StatusEffect
{
    public ExplosiveFireChargeEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 50 >> amplifier;
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
                    Vec3d vector3d = entityLivingBaseIn.getRotationVec(1.0F);
                    double d2 = entity.getX() - (entityLivingBaseIn.getX() + vector3d.x * 4.0D);
                    double d3 = entity.getBodyY(0.5D) - (0.5D + entityLivingBaseIn.getBodyY(0.5D));
                    double d4 = entity.getZ() - (entityLivingBaseIn.getZ() + vector3d.z * 4.0D);

                    entityLivingBaseIn.world.syncWorldEvent(null, 1018, entityLivingBaseIn.getBlockPos(), 0);

                    FireballEntity fireballentity = new FireballEntity(entityLivingBaseIn.world, entityLivingBaseIn, d2, d3, d4);
                    fireballentity.explosionPower = 1;
                    fireballentity.updatePosition(entityLivingBaseIn.getX() + vector3d.x * 4.0D, entityLivingBaseIn.getBodyY(0.5D) + 1.5D, entityLivingBaseIn.getZ() + vector3d.z * 4.0D);
                    entityLivingBaseIn.world.spawnEntity(fireballentity);
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
