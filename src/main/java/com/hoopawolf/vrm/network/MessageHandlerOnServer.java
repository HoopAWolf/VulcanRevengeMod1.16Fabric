package com.hoopawolf.vrm.network;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.server.*;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MessageHandlerOnServer
{
    static final StatusEffect[] types =
            {
                    StatusEffects.POISON, //0
                    StatusEffects.SLOWNESS, //1
                    StatusEffects.WEAKNESS, //2
                    StatusEffects.NAUSEA, //3
            };

    public static void onMessageReceived(final PacketByteBuf message, PacketContext ctx)
    {
        boolean isServer = !ctx.getPlayer().world.isClient;

        if (!isServer)
        {
            Reference.LOGGER.warn("MessageToServer received on wrong side:" + ctx.getTaskQueue().getName());
            return;
        }

        final ServerPlayerEntity sendingPlayer = (ServerPlayerEntity) ctx.getPlayer();
        if (sendingPlayer == null)
        {
            Reference.LOGGER.warn("EntityPlayerMP was null when MessageToServer was received");
        }

        processMessage(message, sendingPlayer, ctx);
    }


    static void processMessage(PacketByteBuf message, ServerPlayerEntity sendingPlayer, PacketContext ctx)
    {
        int id = message.readInt();
        switch (id)
        {
            case 0:
            {
                SetPotionEffectMessage _message = SetPotionEffectMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    Entity _entity = sendingPlayer.world.getEntityById(_message.getEntityID());

                    if (_entity instanceof LivingEntity)
                    {
                        if (_entity.isAlive())
                        {
                            ((LivingEntity) _entity).addStatusEffect(new StatusEffectInstance(types[_message.getPotionType()], _message.getDuration(), _message.getAmplifier()));
                        }
                    }
                });
            }
            break;
            case 1:
            {
                SetPotionEffectMultipleMessage _message = SetPotionEffectMultipleMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    Entity _entity = sendingPlayer.world.getEntityById(_message.getEntityID());

                    if (_entity instanceof LivingEntity)
                    {
                        if (_entity.isAlive())
                        {
                            int max = MathHelper.clamp(_message.getEnding(), 0, types.length - 1);

                            for (int i = _message.getStarting(); i <= max; ++i)
                            {
                                ((LivingEntity) _entity).addStatusEffect(new StatusEffectInstance(types[i], _message.getDuration(), _message.getAmplifier()));
                            }
                        }
                    }
                });
            }
            break;
            case 2:
            {
                SinMaskActivateMessage _message = SinMaskActivateMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    PlayerEntity _player = sendingPlayer.world.getPlayerByUuid(_message.getPlayerID());

                    if (_player != null)
                    {
                        if (_player.isAlive())
                        {
                            SinsArmorItem.setActivated(_player.inventory.armor.get(3), _message.isActivated());
                        }
                    }
                });
            }
            break;
            case 3:
            {
                SleepMessage _message = SleepMessage.decode(message);
                ctx.getTaskQueue().submit(() ->
                {
                    PlayerEntity _player = sendingPlayer.world.getPlayerByUuid(_message.getPlayerID());

                    if (_player != null)
                    {
                        if (_player.isAlive())
                        {
                            if (!_message.isAffectedByNight() || _player.world.isNight())
                            {
                                _player.sleep(new BlockPos(_player.getPos()));
                            }
                        }
                    }
                });
            }
            break;
            case 4:
            {
                SetAttackTargetMessage _message = SetAttackTargetMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    if (sendingPlayer.world.getEntityById(_message.getAttackerID()) instanceof PathAwareEntity &&
                            sendingPlayer.world.getEntityById(_message.getTargetID()) instanceof PathAwareEntity)
                    {
                        PathAwareEntity attacker = (PathAwareEntity) sendingPlayer.world.getEntityById(_message.getAttackerID());
                        PathAwareEntity target = (PathAwareEntity) sendingPlayer.world.getEntityById(_message.getTargetID());

                        if (attacker.isAlive() && target.isAlive())
                        {
                            attacker.setTarget(target);
                        }
                    }
                });
            }
            break;
            case 5:
            {
                TeleportMessage _message = TeleportMessage.decode(message);

                ctx.getTaskQueue().submit(() ->
                {
                    if (sendingPlayer.world.getEntityById(_message.getHostID()) instanceof LivingEntity)
                    {
                        LivingEntity host = (LivingEntity) sendingPlayer.world.getEntityById(_message.getHostID());

                        if (host.isAlive())
                        {
                            host.refreshPositionAfterTeleport(_message.getTeleportPos().getX(), _message.getTeleportPos().getY(), _message.getTeleportPos().getZ());
                        }
                    }
                });
            }
            break;
            default:
                break;
        }
    }
}
