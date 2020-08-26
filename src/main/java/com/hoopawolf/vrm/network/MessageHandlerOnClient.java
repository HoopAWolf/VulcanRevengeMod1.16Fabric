package com.hoopawolf.vrm.network;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SendPlayerMessageMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.ParticleRegistryHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


public class MessageHandlerOnClient
{
    private static final ParticleEffect[] types =
            {
                    ParticleTypes.FLAME, //0
                    ParticleTypes.FIREWORK, //1
                    ParticleTypes.END_ROD, //2
                    ParticleRegistryHandler.DEATH_MARK_PARTICLE, //3
                    ParticleTypes.SMOKE, //4
                    ParticleRegistryHandler.PLAGUE_PARTICLE, //5
                    ParticleTypes.ITEM_SLIME, //6
                    ParticleTypes.HEART, //7
                    ParticleTypes.ANGRY_VILLAGER, //8
                    ParticleTypes.WITCH, //9
                    ParticleTypes.SQUID_INK, //10
                    ParticleTypes.HAPPY_VILLAGER, //11
                    ParticleTypes.DRAGON_BREATH, //12
                    ParticleTypes.SNEEZE, //13
            };

    private static final SoundEvent[] sound_type =
            {
                    SoundEvents.ENTITY_GENERIC_EAT, //0
                    SoundEvents.ENTITY_PLAYER_BURP, //1
                    SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, //2
                    SoundEvents.BLOCK_NOTE_BLOCK_BANJO, //3
                    SoundEvents.ENTITY_VEX_CHARGE, //4
                    SoundEvents.BLOCK_END_PORTAL_SPAWN, //5
                    SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, //6
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, //7
                    SoundEvents.BLOCK_NOTE_BLOCK_CHIME, //8
                    SoundEvents.ENTITY_FOX_SCREECH, //9
            };

    public static void onMessageReceived(final PacketByteBuf message, PacketContext ctx)
    {
        boolean isClient = ctx.getPlayer().world.isClient;

        if (!isClient)
        {
            Reference.LOGGER.warn("MessageToClient received on wrong side:" + ctx.getTaskQueue().getName());
            return;
        }

        if (ctx.getPlayer().world == null)
        {
            Reference.LOGGER.warn("MessageToClient context could not provide a ClientWorld.");
            return;
        }

        processMessage((ClientWorld) ctx.getPlayer().world, message, ctx);
    }

    private static void processMessage(ClientWorld worldClient, PacketByteBuf message, PacketContext ctx)
    {
        int id = message.readInt();

        switch (id)
        {
            case 1:
            {
                SpawnParticleMessage _message = SpawnParticleMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    for (int i = 0; i < _message.getIteration(); ++i)
                    {
                        Vec3d targetCoordinates = _message.getTargetCoordinates();
                        Vec3d targetSpeed = _message.getTargetSpeed();
                        double spread = _message.getParticleSpread();
                        double spawnXpos = targetCoordinates.x;
                        double spawnYpos = targetCoordinates.y;
                        double spawnZpos = targetCoordinates.z;
                        double speedX = targetSpeed.x;
                        double speedY = targetSpeed.y;
                        double speedZ = targetSpeed.z;

                        worldClient.addParticle(/*_message.getPartcleType() == 2 ? new BlockParticleData(ParticleTypes.BLOCK, worldClient.getBlockState(new BlockPos(spawnXpos, spawnYpos - 1.0F, spawnZpos))) : */types[_message.getPartcleType()],
                                true,
                                MathHelper.lerp(worldClient.random.nextDouble(), spawnXpos + spread, spawnXpos - spread),
                                spawnYpos,
                                MathHelper.lerp(worldClient.random.nextDouble(), spawnZpos + spread, spawnZpos - spread),
                                speedX, speedY, speedZ);
                    }
                });
            }
            break;
            case 2:
            {
                PlaySoundEffectMessage _message = PlaySoundEffectMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    Entity entity = worldClient.getEntityById(_message.getEntityID());
                    float pitch = _message.getPitch();
                    float volume = _message.getVolume();

                    assert entity != null;
                    entity.playSound(sound_type[_message.getSoundType()], volume, pitch);
                });
            }
            break;
            case 3:
            {
                SendPlayerMessageMessage _message = SendPlayerMessageMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    PlayerEntity entity = worldClient.getPlayerByUuid(_message.getPlayerUUID());
                    String messageID = _message.getMessageID();
                    int color = _message.getColor();

                    EntityHelper.sendMessage(entity, messageID, Formatting.byColorIndex(color));
                });
            }
            break;
            default:
                break;
        }

    }
}
