package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

public class SetPotionEffectMultipleMessage extends MessageToServer
{
    private int entity_ID,
            duration,
            amplifier,
            start,
            end;

    public SetPotionEffectMultipleMessage(int entityID, int durationIn, int amplifierIn, int startIn, int endIn)
    {
        messageIsValid = true;
        messageType = 1;

        entity_ID = entityID;
        duration = durationIn;
        amplifier = amplifierIn;
        start = startIn;
        end = endIn;
    }

    public SetPotionEffectMultipleMessage()
    {
        messageIsValid = false;
    }

    public static SetPotionEffectMultipleMessage decode(PacketByteBuf buf)
    {
        int entityID, _duration, _amplifier, _start, _end;

        try
        {
            entityID = buf.readInt();
            _duration = buf.readInt();
            _amplifier = buf.readInt();
            _start = buf.readInt();
            _end = buf.readInt();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SetPotionEffectMessageToServer: " + e);
            return new SetPotionEffectMultipleMessage();
        }

        return new SetPotionEffectMultipleMessage(entityID, _duration, _amplifier, _start, _end);
    }

    public int getEntityID()
    {
        return entity_ID;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getAmplifier()
    {
        return amplifier;
    }

    public int getStarting()
    {
        return start;
    }

    public int getEnding()
    {
        return end;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(entity_ID);
        buf.writeInt(duration);
        buf.writeInt(amplifier);
        buf.writeInt(start);
        buf.writeInt(end);
    }

    @Override
    public String toString()
    {
        return "SetPotionEffectMultipleMessage[targetID=" + entity_ID + "]";
    }
}

