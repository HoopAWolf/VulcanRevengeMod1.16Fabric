package com.hoopawolf.vrm.network.packets.client;

import net.minecraft.network.PacketByteBuf;

public abstract class MessageToClient
{
    protected boolean messageIsValid;
    protected int messageType;

    public boolean isMessageValid()
    {
        return messageIsValid;
    }

    public int getMessageType()
    {
        return messageType;
    }

    public abstract void encode(PacketByteBuf buf);

    public abstract String toString();
}
