package com.hoopawolf.vrm.items.armors;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class SinsArmorItem extends ArmorItem
{
    private final SINS sinHolder;
    private final int maxUse;

    public SinsArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Settings p_i48534_3_, SINS sinIn)
    {
        super(materialIn, slot, p_i48534_3_);
        sinHolder = sinIn;
        maxUse = sinHolder.getMaxUse();
    }

    public static SINS getSin(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("sin", 0);

        return SINS.values()[stack.getTag().getInt("sin")];
    }

    public static int getFulfilment(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("fulfil", 0);

        return stack.getTag().getInt("fulfil");
    }

    public static int getSlothPetID(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("slothpet", 0);

        return stack.getTag().getInt("slothpet");
    }

    public static BlockPos getLastPos(ItemStack stack)
    {
        if (!stack.hasTag())
        {
            stack.getOrCreateTag().putInt("lastPosX", 0);
            stack.getOrCreateTag().putInt("lastPosY", 0);
            stack.getOrCreateTag().putInt("lastPosZ", 0);
        }

        return new BlockPos(stack.getTag().getInt("lastPosX"), stack.getTag().getInt("lastPosY"), stack.getTag().getInt("lastPosZ"));
    }

    public static void setFulfilment(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("fulfil", amount);
    }

    public static void setSlothPetID(ItemStack stack, int id)
    {
        stack.getOrCreateTag().putInt("slothpet", id);
    }

    public static boolean isActivated(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putBoolean("activate", true);

        return stack.getTag().getBoolean("activate");
    }

    public static void setActivated(ItemStack stack, boolean isActiveIn)
    {
        stack.getOrCreateTag().putBoolean("activate", isActiveIn);
    }

    public static void setLastPos(ItemStack stack, BlockPos lastPos)
    {
        stack.getOrCreateTag().putInt("lastPosX", lastPos.getX());
        stack.getOrCreateTag().putInt("lastPosY", lastPos.getY());
        stack.getOrCreateTag().putInt("lastPosZ", lastPos.getZ());
    }

    public static void increaseFulfilment(ItemStack stack, int amount, int maxAmount)
    {
        setFulfilment(stack, MathHelper.clamp(getFulfilment(stack) - amount, 0, maxAmount));
    }

    public static void decreaseFulfilment(ItemStack stack, int amount, int maxAmount)
    {
        setFulfilment(stack, MathHelper.clamp(getFulfilment(stack) + amount, 0, maxAmount));
    }

    public static void setSin(ItemStack stack, SINS sinIn)
    {
        stack.getOrCreateTag().putInt("sin", sinIn.getValue());
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int slot, boolean selected)
    {
        if (!worldIn.isClient)
        {
            if (getSin(stack) != sinHolder)
            {
                setSin(stack, sinHolder);

                if (sinHolder.equals(SINS.ENVY))
                {
                    setFulfilment(stack, maxUse);
                }
            }
        }

        if (isActivated(stack))
        {
            if (entityIn instanceof PlayerEntity)
            {
                PlayerEntity entityPlayer = (PlayerEntity) entityIn;

                if (entityPlayer.inventory.armor.contains(stack))
                {
                    switch (getSin(stack))
                    {
                        case ENVY:
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    if (getFulfilment(stack) < maxUse)
                                    {
                                        decreaseFulfilment(stack, 1, maxUse);
                                    }
                                }

                                if (getFulfilmentAmount(stack) > 0.99F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 50, 0));
                                }
                            }
                            break;
                        case LUST:
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    if (getFulfilment(stack) < maxUse)
                                    {
                                        decreaseFulfilment(stack, 1, maxUse);
                                    }
                                }

                                if (getFulfilmentAmount(stack) > 0.8F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0));
                                }
                            }
                            break;
                        case GREED:
                        {
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    int totalItems = 0;
                                    for (ItemStack itemstack : entityPlayer.inventory.main)
                                    {
                                        totalItems += itemstack.getCount();
                                    }

                                    setFulfilment(stack, (int) (100.0F - (((float) totalItems / (float) (entityPlayer.inventory.main.size() * 64)) * 100.0F)));
                                }

                                if (getFulfilmentAmount(stack) > 0.9F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 2));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 2));
                                } else if (getFulfilmentAmount(stack) > 0.7F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 1));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 1));
                                } else if (getFulfilmentAmount(stack) > 0.5F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 0));
                                } else if (getFulfilmentAmount(stack) < 0.1F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 2));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 10, 2));
                                } else if (getFulfilmentAmount(stack) < 0.3F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 1));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 10, 1));
                                } else if (getFulfilmentAmount(stack) < 0.5F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 0));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 10, 0));
                                }
                            }
                        }
                        break;
                        case PRIDE:
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    if (getFulfilment(stack) > 0)
                                    {
                                        increaseFulfilment(stack, 1, maxUse);
                                    }
                                }

                                if (entityIn.age % 5 == 0)
                                {
                                    for (Entity entity : EntityHelper.getEntitiesNearby(entityIn, Entity.class, 5, 5, 5, 20))
                                    {
                                        if (entity instanceof PathAwareEntity)
                                        {
                                            if (!((PathAwareEntity) entity).hasStatusEffect(StatusEffects.WEAKNESS))
                                            {
                                                ((PathAwareEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 400, 0));

                                                if (entity.world.random.nextInt(100) < 50)
                                                {
//                                            ((PathAwareEntity) entity).addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.FEAR_EFFECT, 200, 0));
//                                            PlaySoundEffectMessage playFearSoundMessage = new PlaySoundEffectMessage(entity.getEntityId(), 9, 1.0F, 1.0F);
//                                            VRMPacketHandler.packetHandler.sendToDimension(entity.world.func_234923_W_(), playFearSoundMessage);
                                                }
                                            }
                                        } else if (entity instanceof ProjectileEntity)
                                        {
                                            if (!entity.velocityDirty)
                                            {
                                                if (entity.world.random.nextInt(100) < 50)
                                                {
                                                    if (((ProjectileEntity) entity).getOwner() != null)
                                                    {
                                                        if (!(((ProjectileEntity) entity).getOwner() instanceof PlayerEntity && ((ProjectileEntity) entity).getOwner().getUuid().equals(entityIn.getUuid())))
                                                        {
                                                            entity.setVelocity(entity.getVelocity().multiply(-1).multiply(1.2D));
                                                            ((ProjectileEntity) entity).setOwner(entityIn);
                                                            entity.velocityDirty = true;
                                                        }
                                                    } else
                                                    {
                                                        entity.setVelocity(entity.getVelocity().multiply(-1).multiply(1.2D));
                                                        ((ProjectileEntity) entity).setOwner(entityIn);
                                                        entity.velocityDirty = true;
                                                    }
                                                } else
                                                {
                                                    entity.velocityDirty = true;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (getFulfilmentAmount(stack) > 0.90F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.FEAR_EFFECT, 10, 0));
                                }
                            }
                            break;
                        case SLOTH:
                        {
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 12 == 0)
                                {
                                    setLastPos(stack, new BlockPos((int) entityIn.getX(), (int) entityIn.getY(), (int) entityIn.getZ()));
                                }

                                if (getLastPos(stack).getX() == (int) entityIn.getX() && getLastPos(stack).getY() == (int) entityIn.getY() &&
                                        getLastPos(stack).getZ() == (int) entityIn.getZ())
                                {
                                    if (entityIn.age % 10 == 0)
                                    {
                                        if (getFulfilment(stack) > 0)
                                        {
                                            increaseFulfilment(stack, 1, maxUse);
                                        }
                                    }

                                    if (getSlothPetID(stack) == 0)
                                    {
                                        SlothPetEntity slothpetentity = EntityRegistryHandler.SLOTH_PET_ENTITY.create(entityIn.world);
                                        slothpetentity.refreshPositionAndAngles(entityIn.getX(), entityIn.getY() + 2, entityIn.getZ(), entityIn.yaw, entityIn.pitch);
                                        slothpetentity.setOwner(entityPlayer);
                                        slothpetentity.setBoundOrigin(new BlockPos((int) entityIn.getX(), (int) entityIn.getY(), (int) entityIn.getZ()));
                                        slothpetentity.setGlowing(true);
                                        entityIn.world.spawnEntity(slothpetentity);

                                        setSlothPetID(stack, slothpetentity.getEntityId());
                                    } else
                                    {
                                        if (entityIn.world.getEntityById(getSlothPetID(stack)) == null ||
                                                !(entityIn.world.getEntityById(getSlothPetID(stack)) instanceof SlothPetEntity))
                                        {
                                            setSlothPetID(stack, 0);
                                        }
                                    }

                                } else
                                {
                                    if (entityIn.age % 10 == 0)
                                    {
                                        if (getFulfilment(stack) < maxUse)
                                        {
                                            decreaseFulfilment(stack, 2, maxUse);
                                        }
                                    }
                                }

                                if (getFulfilmentAmount(stack) > 0.90F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.DAZED_EFFECT, 10, 0));
                                    entityPlayer.sleep(new BlockPos(entityIn.getPos()));
                                }
                            }
                        }
                        break;
                        case WRATH:
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    if (getFulfilment(stack) < maxUse)
                                    {
                                        decreaseFulfilment(stack, 1, maxUse);
                                    }
                                }

                                if (getFulfilmentAmount(stack) < 0.2F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 4));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 4));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 10, 4));
                                } else if (getFulfilmentAmount(stack) < 0.3F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 3));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 3));
                                } else if (getFulfilmentAmount(stack) < 0.5F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 2));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 2));

                                } else if (getFulfilmentAmount(stack) < 0.7F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 1));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 1));
                                } else if (getFulfilmentAmount(stack) < 0.85F)
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 0));
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 0));
                                } else
                                {
                                    entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 10, 0));
                                }
                            }
                            break;
                        case GLUTTONY:
                        {
                            if (!worldIn.isClient)
                            {
                                if (entityIn.age % 10 == 0)
                                {
                                    if (getFulfilment(stack) < maxUse)
                                    {
                                        decreaseFulfilment(stack, 1, maxUse);
                                    }
                                }
                            }

                            if (!entityPlayer.getHungerManager().isNotFull())
                            {
                                entityPlayer.getHungerManager().setFoodLevel(19);
                            }

                            if (getFulfilmentAmount(stack) > 0.5F)
                            {
                                entityPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1, 3));

                                if (getFulfilmentAmount(stack) > 0.90F)
                                {
                                    if (entityPlayer.getHungerManager().getFoodLevel() > 1)
                                    {
                                        entityPlayer.getHungerManager().setFoodLevel(1);
                                        entityPlayer.getHungerManager().addExhaustion(40);
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public double getFulfilmentAmount(ItemStack stack)
    {
        return (double) getFulfilment(stack) / (double) maxUse;
    }

//
//    @Override
//    public double getDurabilityForDisplay(ItemStack stack)
//    {
//        return (double) getFulfilment(stack) / (double) maxUse;
//    }
//
//    @Override
//    public boolean showDurabilityBar(ItemStack stack)
//    {
//        return true;
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:sinmask") + getSin(stack).getValue()).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.DARK_GRAY)));
    }

    public enum SINS
    {
        TEMP(0, 0),
        GLUTTONY(1, 40),
        ENVY(2, 40),
        LUST(3, 40),
        GREED(4, 100),
        SLOTH(5, 40),
        WRATH(6, 50),
        PRIDE(7, 50);

        private final int value, maxUse;

        SINS(int valueIn, int maxUseIn)
        {
            this.value = valueIn;
            this.maxUse = maxUseIn;
        }

        public int getValue()
        {
            return value;
        }

        public int getMaxUse()
        {
            return maxUse;
        }
    }
}
