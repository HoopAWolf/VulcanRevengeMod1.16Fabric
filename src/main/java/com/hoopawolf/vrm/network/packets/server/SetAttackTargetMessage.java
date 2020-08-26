package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;

public class SetAttackTargetMessage extends MessageToServer
{
    private int attackerID, targetID;

    public SetAttackTargetMessage(int attackerIDIn, int targetIDIn)
    {
        messageIsValid = true;
        messageType = 4;

        attackerID = attackerIDIn;
        targetID = targetIDIn;
    }

    public SetAttackTargetMessage()
    {
        messageIsValid = false;
    }

    public static SetAttackTargetMessage decode(PacketByteBuf buf)
    {
        int _attackerID, _targetID;

        try
        {
            _attackerID = buf.readInt();
            _targetID = buf.readInt();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SetAttackTargetMessageToServer: " + e);
            return new SetAttackTargetMessage();
        }

        return new SetAttackTargetMessage(_attackerID, _targetID);
    }

    public int getAttackerID()
    {
        return attackerID;
    }

    public int getTargetID()
    {
        return targetID;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(attackerID);
        buf.writeInt(targetID);
    }

    @Override
    public String toString()
    {
        return "SetAttackTargetMessage[targetID=" + attackerID + "]";
    }
}

