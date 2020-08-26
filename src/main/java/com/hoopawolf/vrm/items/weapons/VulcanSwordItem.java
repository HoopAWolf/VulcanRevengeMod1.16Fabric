package com.hoopawolf.vrm.items.weapons;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class VulcanSwordItem extends SwordItem
{

    public VulcanSwordItem(ToolMaterial tier, int attackDamageIn, float attackSpeedIn, Settings builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    public static int getType(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("type", 0);

        return stack.getTag().getInt("type");
    }

    private static void setType(ItemStack stack, int type)
    {
        stack.getOrCreateTag().putInt("type", type);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isClient)
        {
            ItemStack itemStack = playerIn.getStackInHand(handIn);

            setType(itemStack, getType(itemStack) + 1);

            if (getType(itemStack) > 3)
                setType(itemStack, 0);

            return TypedActionResult.pass(playerIn.getStackInHand(handIn));
        }

        return TypedActionResult.fail(playerIn.getStackInHand(handIn));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.postHit(stack, target, attacker);

        switch (getType(stack))
        {
            case 0:
                target.setOnFireFor(10);
                break;
            case 1:
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 140, 10));
                stack.damage(2, target, (p_220009_1_) -> p_220009_1_.sendToolBreakStatus(target.getActiveHand()));
                break;

            case 2:
                stack.damage(5, target, (p_220009_1_) -> p_220009_1_.sendToolBreakStatus(target.getActiveHand()));
                break;

            case 3:
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 140, 0));
                stack.damage(2, target, (p_220009_1_) -> p_220009_1_.sendToolBreakStatus(target.getActiveHand()));
                break;
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isClient)
        {
            if (isSelected && getType(stack) == 2)
            {
                if (entityIn instanceof LivingEntity)
                {
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1, 5));
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1, 10));
                }
            }
        }
    }

    @Override
    public Text getName(ItemStack stack)
    {
        return new TranslatableText(this.getTranslationKey(stack) + getType(stack));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:vulcansrevenge") + getType(stack)).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:vulcansrevengeinfo")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
    }
}
