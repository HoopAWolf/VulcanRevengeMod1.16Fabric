package com.hoopawolf.vrm.potion.sin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class EnvyTrialEffect extends StatusEffect
{
    private final EntityType[] spawnList = {
            EntityType.VEX,
            EntityType.SILVERFISH
    };

    public EnvyTrialEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 480 >> amplifier;
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
            int rand = entityLivingBaseIn.world.random.nextInt(3) + 2;

            while (rand != 0)
            {
                double x = entityLivingBaseIn.getX() + (entityLivingBaseIn.world.random.nextDouble() - 0.5D) * 10.0D;
                double y = entityLivingBaseIn.getY() + (double) (entityLivingBaseIn.world.random.nextInt(3) - 1);
                double z = entityLivingBaseIn.getZ() + (entityLivingBaseIn.world.random.nextDouble() - 0.5D) * 10.0D;

                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

                while (blockpos$mutable.getY() > 0 && !entityLivingBaseIn.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement())
                {
                    blockpos$mutable.move(Direction.DOWN);
                }

                BlockState blockstate = entityLivingBaseIn.world.getBlockState(blockpos$mutable);
                boolean flag = blockstate.getMaterial().blocksMovement();
                boolean flag1 = blockstate.getFluidState().isIn(FluidTags.WATER);
                if (flag && !flag1)
                {
                    MobEntity entity = (MobEntity) spawnList[entityLivingBaseIn.world.random.nextInt(spawnList.length)].create(entityLivingBaseIn.world);
                    entity.updatePositionAndAngles(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ(), 0.0F, 0.0F);
                    entity.setTarget(entityLivingBaseIn);
                    entityLivingBaseIn.world.spawnEntity(entity);
                    --rand;
                }
            }
        }
    }
}
