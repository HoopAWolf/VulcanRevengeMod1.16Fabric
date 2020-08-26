package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class FamScaleItem extends Item
{
    private final int range = 10;

    public FamScaleItem(Settings properties)
    {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    public static int getSacrificeCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("sacrifice", 0);

        return stack.getTag().getInt("sacrifice");
    }

    public static void setSacrificeCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("sacrifice", amount);
    }

//    @Override
//    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
//    {
//        return false;
//    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (handIn.equals(Hand.MAIN_HAND) && playerIn.isSneaking())
        {
            if (playerIn.getHungerManager().getFoodLevel() > playerIn.getHealth())
            {
                if (!worldIn.isClient)
                {
                    setSacrificeCoolDown(playerIn.getStackInHand(handIn), 300);
                    playerIn.setHealth(playerIn.getHungerManager().getFoodLevel());
                    playerIn.getHungerManager().setFoodLevel(0);
                    playerIn.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            } else
            {
                if (!worldIn.isClient)
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    EntityHelper.sendCoolDownMessage(playerIn, getSacrificeCoolDown(playerIn.getStackInHand(handIn)));
                }
            }
        }

        return TypedActionResult.pass(playerIn.getStackInHand(handIn));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isClient)
        {
            if (entityIn.age % 2 == 0)
            {
                if (getSacrificeCoolDown(stack) > 0)
                {
                    setSacrificeCoolDown(stack, getSacrificeCoolDown(stack) - 1);
                }
            }

            if (entityIn.age % 20 == 0)
            {
                if (entityIn instanceof PlayerEntity)
                {
                    PlayerEntity playerIn = (PlayerEntity) entityIn;

                    if ((isSelected || playerIn.getOffHandStack().equals(stack)) && worldIn.random.nextInt(100) < 50)
                    {
                        for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 5, 2, 5, 10))
                        {
                            if (entity instanceof AnimalEntity)
                            {
                                if (!entity.isBaby() && !((AnimalEntity) entity).isInLove())
                                {
                                    ((AnimalEntity) entity).setLoveTicks(600);
                                    ((AnimalEntity) entity).setBreedingAge(0);
//                                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getY() + 0.85F, entity.getZ()), new Vec3d(0.0F, 0.0D, 0.0F), 3, 7, entity.getWidth());
//                                    VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), spawnParticleMessage);
                                }
                            }
                        }
                    }

                    if (playerIn.getHungerManager().isNotFull())
                    {
                        if (worldIn.random.nextInt(100) < 50)
                        {
                            boolean _flag = false;
                            for (int x = -range; x < range; ++x)
                            {
                                for (int z = -range; z < range; ++z)
                                {
                                    BlockPos pos = new BlockPos(entityIn.getX() + x, entityIn.getY(), entityIn.getZ() + z);
                                    if (worldIn.getBlockState(pos).getBlock() instanceof PlantBlock)
                                    {
                                        worldIn.breakBlock(pos, false);
                                        increaseFood(playerIn);
                                        _flag = true;
                                        break;
                                    }
                                }

                                if (_flag)
                                {
                                    break;
                                }
                            }
                        } else
                        {
                            if ((isSelected || playerIn.getOffHandStack().equals(stack)))
                            {
                                for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityIn, range, 1, range, 15))
                                {
                                    if (!entity.hasStatusEffect(PotionRegistryHandler.DAZED_EFFECT))
                                    {
                                        entity.damage(DamageSource.STARVE, 2);
                                        increaseFood(playerIn);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void increaseFood(PlayerEntity entityIn)
    {
        entityIn.getHungerManager().setFoodLevel(entityIn.getHungerManager().getFoodLevel() + 1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:fam1")).setStyle(Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:fam2")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:fam3")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:fam4") + ((getSacrificeCoolDown(stack) > 0) ? " [" + (getSacrificeCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getSacrificeCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
    }
}
