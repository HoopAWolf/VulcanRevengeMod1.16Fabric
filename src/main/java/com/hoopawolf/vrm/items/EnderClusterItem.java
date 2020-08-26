package com.hoopawolf.vrm.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class EnderClusterItem extends Item
{
    public EnderClusterItem(Settings builder)
    {
        super(builder);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getStackInHand(handIn);

        for (int i = 0; i < 8; ++i)
        {
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
            playerIn.getItemCooldownManager().set(this, 20);
            if (!worldIn.isClient)
            {
                EnderPearlEntity enderpearlentity = new EnderPearlEntity(worldIn, playerIn);
                enderpearlentity.setItem(new ItemStack(Items.ENDER_PEARL));
                enderpearlentity.setProperties(playerIn, playerIn.pitch, playerIn.yaw, 0.0F, 1.5F, 3.0F);
                worldIn.spawnEntity(enderpearlentity);
            }

            playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!playerIn.abilities.creativeMode)
        {
            itemstack.decrement(1);
        }

        return TypedActionResult.method_29237(itemstack, worldIn.isClient());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:endercluster")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.DARK_GRAY)));
    }
}