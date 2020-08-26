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

public class SlothTrialEffect extends StatusEffect
{
    private final EntityType[] spawnList = {
            EntityType.ENDERMAN
    };

    public SlothTrialEffect(StatusEffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        int k = 380 >> amplifier;
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
            teleportRandomly(entityLivingBaseIn);

            if (entityLivingBaseIn.world.random.nextInt(100) < 20)
            {
                int rand = entityLivingBaseIn.world.random.nextInt(1) + 1;

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

    protected boolean teleportRandomly(LivingEntity entityLivingBaseIn)
    {
        if (entityLivingBaseIn.isAlive())
        {
            double d0 = entityLivingBaseIn.getX() + (entityLivingBaseIn.world.random.nextDouble() - 0.5D) * 10.0D;
            double d1 = entityLivingBaseIn.getY() + (double) (entityLivingBaseIn.world.random.nextInt(3) - 1);
            double d2 = entityLivingBaseIn.getZ() + (entityLivingBaseIn.world.random.nextDouble() - 0.5D) * 10.0D;
            return this.teleportTo(entityLivingBaseIn, d0, d1, d2);
        } else
        {
            return false;
        }
    }

    private boolean teleportTo(LivingEntity entityLivingBaseIn, double x, double y, double z)
    {
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
            return entityLivingBaseIn.teleport(x, y, z, true);
        } else
        {
            return false;
        }
    }
}
