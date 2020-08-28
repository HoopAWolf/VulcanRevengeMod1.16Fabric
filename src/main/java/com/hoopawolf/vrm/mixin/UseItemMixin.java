package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class UseItemMixin
{
    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract Hand getActiveHand();

    @Shadow
    public abstract void stopUsingItem();

    @Shadow
    public abstract boolean isUsingItem();

    @Inject(method = "consumeItem",
            at = @At(value = "HEAD"))
    private void onUseItemEvent(CallbackInfo callbackInfo)
    {
        LivingEntity livingEntity = ((LivingEntity) ((Object) this));

        if (this.activeItemStack.equals(this.getStackInHand(this.getActiveHand())))
        {
            if (!this.activeItemStack.isEmpty() && this.isUsingItem())
            {
                if (!livingEntity.world.isClient)
                {
                    if (this.activeItemStack.isFood() && livingEntity instanceof PlayerEntity)
                    {
                        PlayerEntity player = (PlayerEntity) livingEntity;

                        if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                        {
                            SinsArmorItem.increaseFulfilment(player.inventory.armor.get(3), this.activeItemStack.getItem().getFoodComponent().getHunger(), SinsArmorItem.getSin(player.inventory.armor.get(3)).getMaxUse());
                        }
                    }
                }
            }
        }
    }
}
