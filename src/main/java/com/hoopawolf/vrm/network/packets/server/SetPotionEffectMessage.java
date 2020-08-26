package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

public class SetPotionEffectMessage extends MessageToServer
{
    private int entity_ID,
            potion_type,
            duration,
            amplifier;

    public SetPotionEffectMessage(int entityID, int potionType, int durationIn, int amplifierIn)
    {
        messageIsValid = true;
        messageType = 0;

        entity_ID = entityID;
        potion_type = potionType;
        duration = durationIn;
        amplifier = amplifierIn;
    }

    public SetPotionEffectMessage()
    {
        messageIsValid = false;
    }

    public static SetPotionEffectMessage decode(PacketByteBuf buf)
    {
        int entityID, potionType, _duration, _amplifier;

        try
        {
            entityID = buf.readInt();
            potionType = buf.readInt();
            _duration = buf.readInt();
            _amplifier = buf.readInt();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SetPotionEffectMessageToServer: " + e);
            return new SetPotionEffectMessage();
        }

        return new SetPotionEffectMessage(entityID, potionType, _duration, _amplifier);
    }

    public int getEntityID()
    {
        return entity_ID;
    }

    public int getPotionType()
    {
        return potion_type;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getAmplifier()
    {
        return amplifier;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(entity_ID);
        buf.writeInt(potion_type);
        buf.writeInt(duration);
        buf.writeInt(amplifier);
    }

    @Override
    public String toString()
    {
        return "SetPotionEffectMessage[targetID=" + entity_ID + "]";
    }
}
