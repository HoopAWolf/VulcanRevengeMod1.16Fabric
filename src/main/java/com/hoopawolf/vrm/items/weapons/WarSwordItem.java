package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public class WarSwordItem extends SwordItem
{
    public WarSwordItem(ToolMaterial tier, int attackDamageIn, float attackSpeedIn, Settings builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.rarity(Rarity.UNCOMMON));
    }

    public static int getWarCryCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("warcry", 0);

        return stack.getTag().getInt("warcry");
    }

    public static int getRageCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("rage", 0);

        return stack.getTag().getInt("rage");
    }

    public static void setWarCryCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("warcry", amount);
    }

    public static void setRageCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("rage", amount);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!playerIn.isSneaking() && handIn.equals(Hand.MAIN_HAND))
        {
            if (getWarCryCoolDown(playerIn.getStackInHand(handIn)) <= 0)
            {
                if (!worldIn.isClient)
                {
                    setWarCryCoolDown(playerIn.getStackInHand(handIn), 200);
                    playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 150, 3));
                    playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 150, 3));
                    if (playerIn.getHealth() < playerIn.getMaxHealth() * 0.3F)
                    {
                        playerIn.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 150, 3));
                    }

                    playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            } else
            {
                if (!worldIn.isClient)
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    EntityHelper.sendCoolDownMessage(playerIn, getWarCryCoolDown(playerIn.getStackInHand(handIn)));
                }
            }
        } else
        {
            if (handIn.equals(Hand.MAIN_HAND))
            {
                if (getRageCoolDown(playerIn.getStackInHand(handIn)) <= 0)
                {
                    if (!worldIn.isClient)
                    {
                        setRageCoolDown(playerIn.getStackInHand(handIn), 200);
                        MobEntity temp = null;
                        for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 10, 2, 10, 15))
                        {
                            if (entity instanceof MobEntity)
                            {
                                if (temp == null)
                                {
                                    temp = (MobEntity) entity;
                                } else
                                {
                                    ((MobEntity) entity).setTarget(temp);
                                    temp.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.RAGE_EFFECT, 300, 0));
                                    entity.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.RAGE_EFFECT, 300, 0));
                                    temp.setTarget(entity);

                                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);

                                    SpawnParticleMessage mob1 = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getY() + 0.5F, entity.getZ()), new Vec3d(0.0D, 0.0D, 0.0D), 3, 8, temp.getWidth());
                                    PacketByteBuf passedData1 = new PacketByteBuf(Unpooled.buffer());
                                    passedData1.writeInt(mob1.getMessageType());
                                    mob1.encode(passedData1);

                                    SpawnParticleMessage mob2 = new SpawnParticleMessage(new Vec3d(temp.getX(), temp.getY() + 0.5F, temp.getZ()), new Vec3d(0.0D, 0.0D, 0.0D), 3, 0, temp.getWidth());
                                    PacketByteBuf passedData2 = new PacketByteBuf(Unpooled.buffer());
                                    passedData2.writeInt(mob1.getMessageType());
                                    mob2.encode(passedData2);

                                    playerInDimension.forEach(player ->
                                    {
                                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData1);
                                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData2);
                                    });

                                    temp = null;
                                }
                            }
                        }

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = (double) i * 360.D / 180.D;
                            double speed = 1.5;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getX(), playerIn.getY() + 0.5F, playerIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedData);
                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);
                            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                        }

                        playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    }
                } else
                {
                    if (!worldIn.isClient)
                    {
                        playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else
                    {
                        EntityHelper.sendCoolDownMessage(playerIn, getRageCoolDown(playerIn.getStackInHand(handIn)));
                    }
                }
            }
        }

        return TypedActionResult.success(playerIn.getStackInHand(handIn));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.postHit(stack, target, attacker);

        if (attacker.world.random.nextInt(100) < 30 || attacker.getHealth() < attacker.getMaxHealth() * 0.3F)
        {
            target.setFireTicks(10);
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isClient)
        {
            if (entityIn.age % 2 == 0)
            {
                if (getRageCoolDown(stack) > 0)
                {
                    setRageCoolDown(stack, getRageCoolDown(stack) - 1);
                }

                if (getWarCryCoolDown(stack) > 0)
                {
                    setWarCryCoolDown(stack, getWarCryCoolDown(stack) - 1);
                }
            }

            if (entityIn instanceof LivingEntity && isSelected)
            {
                if (((LivingEntity) entityIn).getHealth() < ((LivingEntity) entityIn).getMaxHealth() * 0.3F)
                {
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1, 3));
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1, 3));
                }

                if (entityIn.getFireTicks() > 0)
                {
                    entityIn.extinguish();
                }
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war1")).setStyle(Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war2")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getWarCryCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war2.1") + ((getWarCryCoolDown(stack) > 0) ? " [" + (getWarCryCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getWarCryCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war3")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getRageCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war3.1") + ((getRageCoolDown(stack) > 0) ? " [" + (getRageCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getRageCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:war4")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
    }
}
