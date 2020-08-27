package com.hoopawolf.vrm.mixin;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.items.weapons.DeathSwordItem;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(LivingEntity.class)
public abstract class DamageMixin
{
    @Shadow
    public abstract boolean isDead();

    @Shadow
    @Nullable
    public abstract LivingEntity getAttacker();

    @Inject(method = "damage",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onDamageEvent(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo)
    {
        LivingEntity livingEntity = ((LivingEntity) ((Object) this));

        if (!livingEntity.world.isClient)
        {
            if (source.isExplosive())
            {
                if (livingEntity.hasStatusEffect(PotionRegistryHandler.EXPLOSIVE_RESISTANCE_EFFECT))
                {
                    callbackInfo.cancel();
                }
            }

            if (livingEntity.hasStatusEffect(PotionRegistryHandler.DAZED_EFFECT))
            {
                livingEntity.removeStatusEffect(PotionRegistryHandler.DAZED_EFFECT);
            }

            if (source.getAttacker() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) source.getAttacker();

                if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.WRATH_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                {
                    SinsArmorItem.increaseFulfilment(player.inventory.armor.get(3), 2, SinsArmorItem.getSin(player.inventory.armor.get(3)).getMaxUse());

                    if (((SinsArmorItem) player.inventory.armor.get(3).getItem()).getFulfilmentAmount(player.inventory.armor.get(3)) < 0.2D)
                    {
                        livingEntity.setFireTicks(100);
                    }
                }

                if (player.hasStatusEffect(PotionRegistryHandler.POISON_ATTACK_EFFECT))
                {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 0));
                }

                if (player.hasStatusEffect(PotionRegistryHandler.WITHER_ATTACK_EFFECT))
                {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 0));
                }

                if (player.hasStatusEffect(PotionRegistryHandler.PRIDE_TRIAL_EFFECT))
                {
                    if (livingEntity.getY() + 1 > player.getY())
                    {
                        callbackInfo.cancel();
                    }
                }

                if (player.hasStatusEffect(PotionRegistryHandler.ENVY_TRIAL_EFFECT))
                {
                    amount = 1;
                }
            }

            if (livingEntity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) livingEntity;

                if (player.getMainHandStack().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD))
                {
                    if (DeathSwordItem.getVoodooID(player.getMainHandStack()) != 0 &&
                            player.world.getEntityById(DeathSwordItem.getVoodooID(player.getMainHandStack())) != null &&
                            player.world.getEntityById(DeathSwordItem.getVoodooID(player.getMainHandStack())).isAlive())
                    {
                        player.world.getEntityById(DeathSwordItem.getVoodooID(player.getMainHandStack())).damage(DamageSource.MAGIC, amount);
                        callbackInfo.cancel();

                        PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 4, 1.0F, 0.2F);
                        PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                        passedSoundData.writeInt(playVexSoundMessage.getMessageType());
                        playVexSoundMessage.encode(passedSoundData);

                        Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                        playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                    } else if (DeathSwordItem.getDeathCoolDown(player.getMainHandStack()) <= 0)
                    {
                        if (source.getAttacker() instanceof LivingEntity)
                        {
                            LivingEntity attacker = (LivingEntity) source.getAttacker();

                            if (player.isDead())
                            {
                                attacker.damage(DamageSource.MAGIC, attacker.getMaxHealth() * 0.5F);
                                player.setHealth(player.getMaxHealth() * 0.5F);
                                player.getHungerManager().setFoodLevel(20);
                                DeathSwordItem.setDeathCoolDown(player.getMainHandStack(), 600);

                                PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);

                                PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                                passedSoundData.writeInt(playVexSoundMessage.getMessageType());
                                playVexSoundMessage.encode(passedSoundData);

                                Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                                playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));

                                for (int i = 1; i <= 180; ++i)
                                {
                                    double yaw = (double) i * 360.D / 180.D;
                                    double speed = 0.3;
                                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(player.getX(), player.getY() + 0.5F, player.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);

                                    PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                                    passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                                    spawnParticleMessage.encode(passedParticleData);

                                    Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                                    playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                                }
                            }
                        }
                    }
                } else if ((player.getMainHandStack().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE) || player.getOffHandStack().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE)))
                {
                    if (player.world.random.nextInt(100) < 40)
                    {
                        if (source.getAttacker() instanceof LivingEntity)
                        {
                            LivingEntity attacker = (LivingEntity) source.getAttacker();

                            attacker.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.DAZED_EFFECT, 100));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(attacker.getX(), attacker.getY() + 0.5F, attacker.getZ()), new Vec3d(0.0F, 0.0D, 0.0F), 3, 9, source.getAttacker().getWidth());
                            PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                            passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                            spawnParticleMessage.encode(passedParticleData);

                            Stream<PlayerEntity> playerInDimension1 = PlayerStream.world(player.world);
                            playerInDimension1.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                        }

                        PlaySoundEffectMessage playBanjoSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 3, 1.0F, 1.0F);
                        PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                        passedSoundData.writeInt(playBanjoSoundMessage.getMessageType());
                        playBanjoSoundMessage.encode(passedSoundData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));
                    }
                } else if (player.getMainHandStack().getItem().equals(ItemBlockRegistryHandler.WAR_SWORD))
                {
                    if (player.getHealth() < player.getMaxHealth() * 0.3F && source.getAttacker() instanceof LivingEntity)
                    {
                        source.getAttacker().setFireTicks(10);
                        PlaySoundEffectMessage playCrackleSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 2, 1.0F, 1.0F);
                        PacketByteBuf passedSoundData = new PacketByteBuf(Unpooled.buffer());
                        passedSoundData.writeInt(playCrackleSoundMessage.getMessageType());
                        playCrackleSoundMessage.encode(passedSoundData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedSoundData));
                    }
                }

                if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                {
                    if (player.world.random.nextInt(100) < 40)
                    {
                        SinsArmorItem.increaseFulfilment(player.inventory.armor.get(3), (int) amount * 2, SinsArmorItem.getSin(player.inventory.armor.get(3)).getMaxUse());
                        player.getHungerManager().setFoodLevel(MathHelper.clamp(player.getHungerManager().getFoodLevel() + (int) amount, 0, 20));
                        PlaySoundEffectMessage playEatSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 0, 1.0F, 1.0F);
                        PlaySoundEffectMessage playBurpSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 1, 1.0F, 1.0F);

                        PacketByteBuf passedEatSoundData = new PacketByteBuf(Unpooled.buffer());
                        passedEatSoundData.writeInt(playEatSoundMessage.getMessageType());
                        playEatSoundMessage.encode(passedEatSoundData);

                        PacketByteBuf passedBurpSoundData = new PacketByteBuf(Unpooled.buffer());
                        passedBurpSoundData.writeInt(playBurpSoundMessage.getMessageType());
                        playEatSoundMessage.encode(passedBurpSoundData);

                        Stream<PlayerEntity> playerInDimension = PlayerStream.world(player.world);
                        playerInDimension.forEach(playerIn ->
                        {
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedEatSoundData);
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerIn, VulcanRevengeMod.CLIENT_PACKET_ID, passedBurpSoundData);
                        });

                        callbackInfo.cancel();
                    }
                } else if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.WRATH_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                {
                    SinsArmorItem.increaseFulfilment(player.inventory.armor.get(3), 2, SinsArmorItem.getSin(player.inventory.armor.get(3)).getMaxUse());
                } else if (player.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.PRIDE_MASK_ARMOR) && SinsArmorItem.isActivated(player.inventory.armor.get(3)))
                {
                    SinsArmorItem.decreaseFulfilment(player.inventory.armor.get(3), 5, SinsArmorItem.getSin(player.inventory.armor.get(3)).getMaxUse());
                }
            }
        }
    }
}
