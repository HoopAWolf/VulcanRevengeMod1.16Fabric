package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class BowAttackEffect extends StatusEffect
{
    public BowAttackEffect(StatusEffectType typeIn, int liquidColorIn)
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
                    PersistentProjectileEntity abstractarrowentity = ProjectileUtil.createArrowProjectile(entityLivingBaseIn, new ItemStack(Items.ARROW), BowItem.getPullProgress(20));

                    double d0 = entity.getX() - entityLivingBaseIn.getX();
                    double d1 = entity.getBodyY(0.3333333333333333D) - abstractarrowentity.getY();
                    double d2 = entity.getZ() - entityLivingBaseIn.getZ();
                    double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                    abstractarrowentity.setVelocity(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - entityLivingBaseIn.world.getDifficulty().getId() * 4));
                    entityLivingBaseIn.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (entityLivingBaseIn.getRandom().nextFloat() * 0.4F + 0.8F));
                    entityLivingBaseIn.world.spawnEntity(abstractarrowentity);

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
