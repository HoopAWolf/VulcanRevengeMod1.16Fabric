package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class SinMaskActivateMessage extends MessageToServer
{
    private UUID player_ID;
    private boolean isActivated;

    public SinMaskActivateMessage(UUID playerIDIn, boolean isActivatedIn)
    {
        messageIsValid = true;
        messageType = 2;

        player_ID = playerIDIn;
        isActivated = isActivatedIn;
    }

    public SinMaskActivateMessage()
    {
        messageIsValid = false;
    }

    public static SinMaskActivateMessage decode(PacketByteBuf buf)
    {
        UUID _playerID;
        boolean _isActivated;

        try
        {
            _playerID = buf.readUuid();
            _isActivated = buf.readBoolean();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SinMaskActivateMessageToServer: " + e);
            return new SinMaskActivateMessage();
        }

        return new SinMaskActivateMessage(_playerID, _isActivated);
    }

    public UUID getPlayerID()
    {
        return player_ID;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeUuid(player_ID);
        buf.writeBoolean(isActivated);
    }

    @Override
    public String toString()
    {
        return "SinMaskActivateMessage[targetID=" + player_ID + "]";
    }
}
