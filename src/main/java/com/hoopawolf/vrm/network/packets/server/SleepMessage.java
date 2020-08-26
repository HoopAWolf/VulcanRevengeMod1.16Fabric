package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class SleepMessage extends MessageToServer
{
    private UUID player_ID;
    private boolean affectedByNight;

    public SleepMessage(UUID playerIDIn, boolean affectedByNightIn)
    {
        messageIsValid = true;
        messageType = 3;

        player_ID = playerIDIn;
        affectedByNight = affectedByNightIn;
    }

    public SleepMessage()
    {
        messageIsValid = false;
    }

    public static SleepMessage decode(PacketByteBuf buf)
    {
        UUID _playerID;
        boolean _affectedByNight;

        try
        {
            _playerID = buf.readUuid();
            _affectedByNight = buf.readBoolean();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SleepMessageToServer: " + e);
            return new SleepMessage();
        }

        return new SleepMessage(_playerID, _affectedByNight);
    }

    public UUID getPlayerID()
    {
        return player_ID;
    }

    public boolean isAffectedByNight()
    {
        return affectedByNight;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeUuid(player_ID);
        buf.writeBoolean(affectedByNight);
    }

    @Override
    public String toString()
    {
        return "SleepMessage[targetID=" + player_ID + "]";
    }
}

