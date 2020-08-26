package com.hoopawolf.vrm.network.packets.server;

import net.minecraft.network.PacketByteBuf;

public abstract class MessageToServer
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
