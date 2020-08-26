package com.hoopawolf.vrm.network.packets.client;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

public class PlaySoundEffectMessage extends MessageToClient
{
    private int entityID, sound;
    private float pitch, volume;

    public PlaySoundEffectMessage(int entityIDIn, int _sound_type, float volumeIn, float pitchIn)
    {
        messageIsValid = true;
        messageType = 2;
        entityID = entityIDIn;
        sound = _sound_type;
        pitch = pitchIn;
        volume = volumeIn;
    }

    public PlaySoundEffectMessage()
    {
        messageIsValid = false;
    }

    public static PlaySoundEffectMessage decode(PacketByteBuf buf)
    {
        int soundtype, entityID;
        float pitch, volume;

        try
        {
            soundtype = buf.readInt();
            entityID = buf.readInt();
            pitch = buf.readFloat();
            volume = buf.readFloat();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading PlaySoundEffectMessageToClient: " + e);
            return new PlaySoundEffectMessage();
        }

        return new PlaySoundEffectMessage(entityID, soundtype, volume, pitch);
    }

    public int getEntityID()
    {
        return entityID;
    }

    public int getSoundType()
    {
        return sound;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getVolume()
    {
        return volume;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(sound);
        buf.writeInt(entityID);
        buf.writeFloat(pitch);
        buf.writeFloat(volume);
    }

    @Override
    public String toString()
    {
        return "PlaySoundEffectMessageToClient[EntityID=" + entityID + "]";
    }
}
