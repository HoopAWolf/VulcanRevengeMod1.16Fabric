package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.entities.ai.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class JoinWorldMixin
{
    @Shadow
    protected abstract boolean checkUuid(Entity entity);

    @Inject(method = "addEntity",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onJoinEvent(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        if (entity instanceof PathAwareEntity && !(entity instanceof SlothPetEntity))
        {
            ((PathAwareEntity) entity).goalSelector.add(0, new DazedGoal(((PathAwareEntity) entity)));
            ((PathAwareEntity) entity).goalSelector.add(0, new LustAvoidEntityGoal(((PathAwareEntity) entity), PlayerEntity.class, 12.0F, 1.2D, 2.2D));
            ((PathAwareEntity) entity).goalSelector.add(1, new FearPanicGoal((PathAwareEntity) entity, 1.5D));
        }

        if (entity instanceof AnimalEntity)
        {
            ((AnimalEntity) entity).goalSelector.add(1, new AnimalAttackGoal(((AnimalEntity) entity), 1.0D, true, 2, 1));
            ((AnimalEntity) entity).goalSelector.add(1, new LustSinTemptGoal(((AnimalEntity) entity), 1.0D));
        }
    }

    @Inject(method = "loadEntity",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onLoadEvent(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        if (!this.checkUuid(entity))
        {
            if (entity instanceof PathAwareEntity && !(entity instanceof SlothPetEntity))
            {
                ((PathAwareEntity) entity).goalSelector.add(0, new DazedGoal(((PathAwareEntity) entity)));
                ((PathAwareEntity) entity).goalSelector.add(0, new LustAvoidEntityGoal(((PathAwareEntity) entity), PlayerEntity.class, 12.0F, 1.2D, 2.2D));
                ((PathAwareEntity) entity).goalSelector.add(1, new FearPanicGoal((PathAwareEntity) entity, 1.5D));
            }

            if (entity instanceof AnimalEntity)
            {
                ((AnimalEntity) entity).goalSelector.add(1, new AnimalAttackGoal(((AnimalEntity) entity), 1.0D, true, 2, 1));
                ((AnimalEntity) entity).goalSelector.add(1, new LustSinTemptGoal(((AnimalEntity) entity), 1.0D));
            }
        }
    }
}
