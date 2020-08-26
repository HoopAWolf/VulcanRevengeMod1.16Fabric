package com.hoopawolf.vrm.entities.projectiles;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class PesArrowEntity extends PersistentProjectileEntity
{
    public PesArrowEntity(EntityType<? extends PersistentProjectileEntity> p_i50172_1_, World p_i50172_2_)
    {
        super(p_i50172_1_, p_i50172_2_);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public PesArrowEntity(World worldIn, double x, double y, double z)
    {
        super(EntityRegistryHandler.PES_ARROW_ENTITY, x, y, z, worldIn);
    }

    public PesArrowEntity(World worldIn, LivingEntity shooter)
    {
        super(EntityRegistryHandler.PES_ARROW_ENTITY, shooter, worldIn);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!this.world.isClient)
        {
            if (this.inGround && this.inGroundTime != 0)
            {
                this.world.sendEntityStatus(this, (byte) 0);
                SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(this.getX(), this.getY(), this.getZ()), new Vec3d(0.0D, 0.0D, 0.0D), 3, 5, 0.0F);
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeInt(spawnParticleMessage.getMessageType());
                spawnParticleMessage.encode(passedData);

                Stream<PlayerEntity> playerInDimension = PlayerStream.world(this.world);
                playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
            }

            Vec3d vec3d = this.getVelocity();
            double d3 = vec3d.x;
            double d4 = vec3d.y;
            double d0 = vec3d.z;

            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(this.getX() + d3, this.getY() + d4, this.getZ() + d0), new Vec3d(0.0D, 0.0D, 0.0D), 3, 5, 0.0F);
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeInt(spawnParticleMessage.getMessageType());
            spawnParticleMessage.encode(passedData);
            Stream<PlayerEntity> playerInDimension = PlayerStream.world(this.world);
            playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
        }
    }

    @Override
    protected void age()
    {
        if (this.inGroundTime >= 1)
        {
            this.remove();
        }
    }

    @Override
    public float getBrightnessAtEyes()
    {
        return 15;
    }

    @Override
    protected void onHit(LivingEntity living)
    {
        super.onHit(living);
        if (!this.world.isClient)
        {
            living.addStatusEffect(new StatusEffectInstance(PotionRegistryHandler.PLAGUE_EFFECT, 1000, 0));
        }
    }

    @Override
    protected ItemStack asItemStack()
    {
        return Items.AIR.getStackForRender();
    }

//    @Override
//    public Packet<?> createSpawnPacket() {
//        return Fabric;
//    }
}

