package com.hoopawolf.vrm.network.packets.client;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class SpawnParticleMessage extends MessageToClient
{
    private Vec3d targetCoordinates, targetSpeed;
    private int iteration, particle_type;
    private double spread;

    public SpawnParticleMessage(Vec3d i_targetCoordinates, Vec3d i_targetSpeed, int _iteration, int _particle_type, double _spread)
    {
        messageIsValid = true;
        messageType = 1;
        targetCoordinates = i_targetCoordinates;
        targetSpeed = i_targetSpeed;
        iteration = _iteration;
        particle_type = _particle_type;
        spread = _spread;
    }

    public SpawnParticleMessage()
    {
        messageIsValid = false;
    }

    public static SpawnParticleMessage decode(PacketByteBuf buf)
    {
        int iterationAmount, particletype;
        double x;
        double y;
        double z;

        double speedx;
        double speedy;
        double speedz;

        double spreadDist;
        try
        {
            particletype = buf.readInt();
            iterationAmount = buf.readInt();
            x = buf.readDouble();
            y = buf.readDouble();
            z = buf.readDouble();
            speedx = buf.readDouble();
            speedy = buf.readDouble();
            speedz = buf.readDouble();
            spreadDist = buf.readDouble();

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SpawnParticleMessageToClient: " + e);
            return new SpawnParticleMessage();
        }

        return new SpawnParticleMessage(new Vec3d(x, y, z), new Vec3d(speedx, speedy, speedz), iterationAmount, particletype, spreadDist);
    }

    public Vec3d getTargetCoordinates()
    {
        return targetCoordinates;
    }

    public Vec3d getTargetSpeed()
    {
        return targetSpeed;
    }

    public int getIteration()
    {
        return iteration;
    }

    public int getPartcleType()
    {
        return particle_type;
    }

    public double getParticleSpread()
    {
        return spread;
    }

    @Override
    public void encode(PacketByteBuf buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(particle_type);
        buf.writeInt(iteration);
        buf.writeDouble(targetCoordinates.x);
        buf.writeDouble(targetCoordinates.y);
        buf.writeDouble(targetCoordinates.z);
        buf.writeDouble(targetSpeed.x);
        buf.writeDouble(targetSpeed.y);
        buf.writeDouble(targetSpeed.z);
        buf.writeDouble(spread);
    }

    @Override
    public String toString()
    {
        return "SpawnParticleMessageToClient[targetCoordinates=" + targetCoordinates + "]";
    }
}
