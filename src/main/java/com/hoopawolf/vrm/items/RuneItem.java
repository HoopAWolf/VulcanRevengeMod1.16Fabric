package com.hoopawolf.vrm.items;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class RuneItem extends Item
{
    public RuneItem(Settings properties)
    {
        super(properties);
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
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (!playerIn.world.isClient)
        {
            if (getType(stack) == 0)
            {
                if (target instanceof HorseBaseEntity)
                {
                    if (target.hasStatusEffect(StatusEffects.WITHER))
                    {
                        setType(stack, 1);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (target.getFireTicks() > 0)
                    {
                        setType(stack, 2);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (((HorseBaseEntity) target).getTemper() == ((HorseBaseEntity) target).getMaxTemper())
                    {
                        setType(stack, 3);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else if (target.hasStatusEffect(StatusEffects.POISON))
                    {
                        setType(stack, 4);
                        target.setHealth(0);
                        playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    }

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isClient)
        {
            switch (getType(playerIn.getStackInHand(handIn)))
            {
                case 1:
                {
                    if (playerIn.getOffHandStack().getTranslationKey().equals(VulcanRevengeMod.VRM_CONFIG.itemconfig.deathScytheItem))
                    {
                        playerIn.getOffHandStack().decrement(1);
                        playerIn.getStackInHand(handIn).decrement(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.DEATH_SWORD), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = (double) i * 360.D / 180.D;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getX(), playerIn.getY() + 0.5F, playerIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);
                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedData);
                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);
                            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                        }
                    }
                }
                break;

                case 2:
                {
                    if (playerIn.getOffHandStack().getTranslationKey().equals(VulcanRevengeMod.VRM_CONFIG.itemconfig.warSwordItem))
                    {
                        playerIn.getOffHandStack().decrement(1);
                        playerIn.getStackInHand(handIn).decrement(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.WAR_SWORD), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = (double) i * 360.D / 180.D;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getX(), playerIn.getY() + 0.5F, playerIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedData);
                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);
                            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                        }
                    }
                }
                break;
                case 3:
                {
                    if (playerIn.getOffHandStack().getTranslationKey().equals(VulcanRevengeMod.VRM_CONFIG.itemconfig.famineScaleItem))
                    {
                        playerIn.getOffHandStack().decrement(1);
                        playerIn.getStackInHand(handIn).decrement(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.FAM_SCALE), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = (double) i * 360.D / 180.D;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getX(), playerIn.getY() + 0.5F, playerIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 6, 0.0F);
                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedData);
                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);
                            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                        }
                    }
                }
                break;
                case 4:
                {
                    if (playerIn.getOffHandStack().getTranslationKey().equals(VulcanRevengeMod.VRM_CONFIG.itemconfig.pesBowItem))
                    {
                        playerIn.getOffHandStack().decrement(1);
                        playerIn.getStackInHand(handIn).decrement(1);
                        playerIn.dropItem(new ItemStack(ItemBlockRegistryHandler.PES_BOW), true);
                        playerIn.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = (double) i * 360.D / 180.D;
                            double speed = 0.3;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(playerIn.getX(), playerIn.getY() + 0.5F, playerIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 5, 0.0F);
                            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                            passedData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedData);
                            Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerIn.world);
                            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                        }
                    }
                }
                break;
            }
        }

        return TypedActionResult.pass(playerIn.getStackInHand(handIn));
    }

//    @Override
//    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
//    {
//        return false;
//    }

    @Override
    public Text getName(ItemStack stack)
    {
        return new TranslatableText(this.getTranslationKey(stack) + getType(stack));
    }
}
