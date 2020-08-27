package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class JumpMixin
{
    @Inject(method = "jump",
            at = @At(value = "HEAD"), cancellable = true)
    private void onJumpEvent(CallbackInfo callbackInfo)
    {
        LivingEntity target = ((LivingEntity) ((Object) this));

        if (target.hasStatusEffect(PotionRegistryHandler.DAZED_EFFECT))
        {
            callbackInfo.cancel();
        }
    }
}
