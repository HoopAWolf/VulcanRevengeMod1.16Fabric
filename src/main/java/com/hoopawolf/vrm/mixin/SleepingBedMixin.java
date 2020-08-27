package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SleepingBedMixin
{
    @Inject(method = "isSleepingInBed",
            at = @At(value = "INVOKE"), cancellable = true)
    private void checkSleepingPlaceEvent(CallbackInfoReturnable<Boolean> callbackInfo)
    {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.world.isClient)
        {
            if (livingEntity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) livingEntity;

                if (player.getSleepTimer() < 100)
                {
                    if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)) ||
                            player.hasStatusEffect(PotionRegistryHandler.SLEEP_EFFECT))
                    {
                        callbackInfo.setReturnValue(true);
                    }
                }
            }
        }
    }
}
