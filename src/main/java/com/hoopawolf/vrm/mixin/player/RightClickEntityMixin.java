package com.hoopawolf.vrm.mixin.player;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.data.EnvyEntityData;
import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.helper.VRMEnvyEntityDataHandler;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.stream.Stream;

@Mixin(PlayerEntity.class)
public abstract class RightClickEntityMixin
{
    @Shadow
    public abstract boolean isSpectator();

    @Inject(method = "interact",
            at = @At(value = "INVOKE"), cancellable = true)
    private void onRightClickEntityEvent(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo)
    {
        PlayerEntity playerEntity = ((PlayerEntity) (Object) this);
        if (!this.isSpectator())
        {
            if (!playerEntity.world.isClient)
            {
                ItemStack itemStack = playerEntity.getStackInHand(hand);

                if (itemStack.isEmpty())
                {
                    if (playerEntity.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR) && SinsArmorItem.isActivated(playerEntity.inventory.armor.get(3)))
                    {
                        if (SinsArmorItem.getSlothPetID(playerEntity.inventory.armor.get(3)) != 0)
                        {
                            if (entity instanceof LivingEntity && !(entity instanceof SlothPetEntity))
                            {
                                if (playerEntity.world.getEntityById(SinsArmorItem.getSlothPetID(playerEntity.inventory.armor.get(3))) instanceof SlothPetEntity)
                                {
                                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerEntity.world);

                                    PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(playerEntity.getEntityId(), 7, 2.0F, 1.0F);
                                    PacketByteBuf passedDingSoundData = new PacketByteBuf(Unpooled.buffer());
                                    passedDingSoundData.writeInt(playDingSoundMessage.getMessageType());
                                    playDingSoundMessage.encode(passedDingSoundData);

                                    playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedDingSoundData));

                                    ((SlothPetEntity) playerEntity.world.getEntityById(SinsArmorItem.getSlothPetID(playerEntity.inventory.armor.get(3)))).setTarget((LivingEntity) entity);
                                    callbackInfo.setReturnValue(ActionResult.PASS);
                                }
                            }
                        }
                    } else if (playerEntity.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.LUST_MASK_ARMOR) && SinsArmorItem.isActivated(playerEntity.inventory.armor.get(3)))
                    {
                        SinsArmorItem.increaseFulfilment(playerEntity.inventory.armor.get(3), 5, SinsArmorItem.getSin(playerEntity.inventory.armor.get(3)).getMaxUse());

                        if (entity instanceof AnimalEntity)
                        {
                            if (!((AnimalEntity) entity).isBaby() && !((AnimalEntity) entity).isInLove())
                            {
                                ((AnimalEntity) entity).setLoveTicks(600);
                                ((AnimalEntity) entity).setBreedingAge(0);

                                Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerEntity.world);
                                SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getY() + 0.85F, entity.getZ()), new Vec3d(0.0F, 0.0D, 0.0F), 3, 7, entity.getWidth());
                                PacketByteBuf passedParticleData = new PacketByteBuf(Unpooled.buffer());
                                passedParticleData.writeInt(spawnParticleMessage.getMessageType());
                                spawnParticleMessage.encode(passedParticleData);

                                playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedParticleData));
                                callbackInfo.setReturnValue(ActionResult.PASS);
                            }
                        }
                    } else if (playerEntity.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.ENVY_MASK_ARMOR) && SinsArmorItem.isActivated(playerEntity.inventory.armor.get(3)))
                    {
                        if (entity instanceof LivingEntity && ((SinsArmorItem) playerEntity.inventory.armor.get(3).getItem()).getFulfilmentAmount(playerEntity.inventory.armor.get(3)) > 0.85D)
                        {
                            if (VRMEnvyEntityDataHandler.INSTANCE.data.get(entity.getSavedEntityId()) != null)
                            {
                                int milkRemainingDuration = 0;

                                SinsArmorItem.increaseFulfilment(playerEntity.inventory.armor.get(3), SinsArmorItem.getSin(playerEntity.inventory.armor.get(3)).getMaxUse(), SinsArmorItem.getSin(playerEntity.inventory.armor.get(3)).getMaxUse());
                                EnvyEntityData data = VRMEnvyEntityDataHandler.INSTANCE.data.get(entity.getEntityName());

                                Stream<PlayerEntity> playerInDimension = PlayerStream.world(playerEntity.world);
                                PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(playerEntity.getEntityId(), 8, 1.0F, 1.0F);
                                PacketByteBuf passedDingSoundData = new PacketByteBuf(Unpooled.buffer());
                                passedDingSoundData.writeInt(playDingSoundMessage.getMessageType());
                                playDingSoundMessage.encode(passedDingSoundData);

                                playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedDingSoundData));

                                if (playerEntity.hasStatusEffect(PotionRegistryHandler.MILK_EFFECT))
                                {
                                    milkRemainingDuration = playerEntity.getStatusEffect(PotionRegistryHandler.MILK_EFFECT).getDuration();
                                    playerEntity.removeStatusEffect(PotionRegistryHandler.MILK_EFFECT);
                                }

                                for (String effects : data.getListOfEffects())
                                {
                                    for (Identifier list : Registry.STATUS_EFFECT.getIds())
                                    {
                                        if (list.toString().equals(effects))
                                        {
                                            playerEntity.addStatusEffect(new StatusEffectInstance(Registry.STATUS_EFFECT.get(list), (Objects.equals(Registry.STATUS_EFFECT.get(list), StatusEffects.INSTANT_DAMAGE) || Objects.equals(Registry.STATUS_EFFECT.get(list), StatusEffects.INSTANT_HEALTH)) ? 1 : 500, data.getAmplifier()));
                                            break;
                                        }
                                    }
                                }

                                if (milkRemainingDuration > 0)
                                {
                                    playerEntity.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.MILK_EFFECT, milkRemainingDuration, 0));
                                }
                                callbackInfo.setReturnValue(ActionResult.PASS);
                            }
                        }
                    }
                }
            }
        }
    }
}
