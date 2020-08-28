package com.hoopawolf.vrm.entities;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.stream.Stream;

public class SlothPetEntity extends PathAwareEntity
{
    protected static final TrackedData<Byte> CHARGE_FLAG;

    static
    {
        CHARGE_FLAG = DataTracker.registerData(SlothPetEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    private PlayerEntity owner;

    private BlockPos boundOrigin;

    public SlothPetEntity(EntityType<? extends SlothPetEntity> p_i50190_1_, World p_i50190_2_)
    {
        super(p_i50190_1_, p_i50190_2_);
        this.moveControl = new SlothPetEntity.MoveHelperController(this);
    }

    public static DefaultAttributeContainer.Builder createSlothPetAttributes()
    {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void move(MovementType type, Vec3d movement)
    {
        super.move(type, movement);
        this.checkBlockCollision();
    }

    @Override
    public void tick()
    {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);

        if (!world.isClient)
        {
            if (boundOrigin == null || owner == null || (int) owner.getX() != boundOrigin.getX() || (int) owner.getZ() != boundOrigin.getZ() ||
                    !(owner.inventory.armor.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR) && SinsArmorItem.isActivated(owner.inventory.armor.get(3))))
            {
                this.remove();
            }

            if (getTarget() != null)
            {
                SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(this.getX(), this.getY(), this.getZ()), new Vec3d(0.0D, 0.0D, 0.0D), 3, 2, 0.0F);
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeInt(spawnParticleMessage.getMessageType());
                spawnParticleMessage.encode(passedData);

                Stream<PlayerEntity> playerInDimension = PlayerStream.world(this.world);
                playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));

                if (!getTarget().isAlive())
                {
                    setTarget(null);
                }
            }

            for (Entity entity : EntityHelper.getEntitiesNearby(this, ItemEntity.class, 1))
            {
                entity.refreshPositionAndAngles(getOwner().getBlockPos(), entity.yaw, entity.pitch);
            }
        }
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SlothPetEntity.ChargeAttackGoal());
        this.goalSelector.add(8, new SlothPetEntity.MoveRandomGoal());
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGE_FLAG, (byte) 0);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound)
    {
        super.readCustomDataFromTag(compound);
        if (compound.contains("BoundX"))
        {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound)
    {
        super.writeCustomDataToTag(compound);
        if (this.boundOrigin != null)
        {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }
    }

    public PlayerEntity getOwner()
    {
        return this.owner;
    }

    public void setOwner(PlayerEntity ownerIn)
    {
        this.owner = ownerIn;
    }

    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }

    public void setBoundOrigin(BlockPos boundOriginIn)
    {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getChargeFlag(int mask)
    {
        int i = this.dataTracker.get(CHARGE_FLAG);
        return (i & mask) != 0;
    }

    private void setChargeFlag(int mask, boolean value)
    {
        int i = this.dataTracker.get(CHARGE_FLAG);
        if (value)
        {
            i = i | mask;
        } else
        {
            i = i & ~mask;
        }

        this.dataTracker.set(CHARGE_FLAG, (byte) (i & 255));
    }

    public boolean isCharging()
    {
        return this.getChargeFlag(1);
    }

    public void setCharging(boolean charging)
    {
        this.setChargeFlag(1, charging);
    }

    @Override
    public float getBrightnessAtEyes()
    {
        return 1.0F;
    }

    @Override
    public boolean damage(DamageSource source, float amount)
    {
        if (!world.isClient)
        {
            if (source.getAttacker() != null)
            {
                if (!owner.isCreative())
                {
                    owner.damage(source, amount * 0.2F);
                }
                return super.damage(source, amount);
            }
        }

        return false;
    }

    class ChargeAttackGoal extends Goal
    {
        public ChargeAttackGoal()
        {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart()
        {
            return SlothPetEntity.this.getTarget() != null && SlothPetEntity.this.random.nextInt(3) == 0;
        }

        @Override
        public boolean shouldContinue()
        {
            return SlothPetEntity.this.getMoveControl().isMoving() && SlothPetEntity.this.isCharging() && SlothPetEntity.this.getTarget() != null && SlothPetEntity.this.getTarget().isAlive();
        }

        @Override
        public void start()
        {
            LivingEntity livingentity = SlothPetEntity.this.getTarget();
            Vec3d vector3d = livingentity.getCameraPosVec(1.0F);
            SlothPetEntity.this.moveControl.moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            SlothPetEntity.this.setCharging(true);
            SlothPetEntity.this.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 1.0F, 0.5F);
        }

        @Override
        public void stop()
        {
            SlothPetEntity.this.setCharging(false);
        }

        @Override
        public void tick()
        {
            LivingEntity livingentity = SlothPetEntity.this.getTarget();
            if (SlothPetEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox()))
            {
                SlothPetEntity.this.tryAttack(livingentity);
                SlothPetEntity.this.setCharging(false);
            } else
            {
                Vec3d vector3d = livingentity.getCameraPosVec(1.0F);
                SlothPetEntity.this.moveControl.moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }

        }
    }

    class MoveHelperController extends MoveControl
    {
        public MoveHelperController(SlothPetEntity vex)
        {
            super(vex);
        }

        @Override
        public void tick()
        {
            if (this.state == State.MOVE_TO)
            {
                Vec3d vector3d = new Vec3d(this.targetX - SlothPetEntity.this.getX(), this.targetY - SlothPetEntity.this.getY(), this.targetZ - SlothPetEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < SlothPetEntity.this.getBoundingBox().getAverageSideLength())
                {
                    this.state = State.WAIT;
                    SlothPetEntity.this.setVelocity(SlothPetEntity.this.getVelocity().multiply(0.5D));
                } else
                {
                    SlothPetEntity.this.setVelocity(SlothPetEntity.this.getVelocity().add(vector3d.multiply(this.speed * 0.05D / d0)));
                    if (SlothPetEntity.this.getTarget() == null)
                    {
                        Vec3d vector3d1 = SlothPetEntity.this.getVelocity();
                        SlothPetEntity.this.yaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    } else
                    {
                        double d2 = SlothPetEntity.this.getTarget().getX() - SlothPetEntity.this.getX();
                        double d1 = SlothPetEntity.this.getTarget().getZ() - SlothPetEntity.this.getZ();
                        SlothPetEntity.this.yaw = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                    }
                    SlothPetEntity.this.bodyYaw = SlothPetEntity.this.yaw;
                }

            }
        }
    }

    class MoveRandomGoal extends Goal
    {
        public MoveRandomGoal()
        {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart()
        {
            return !SlothPetEntity.this.getMoveControl().isMoving() && SlothPetEntity.this.random.nextInt(7) == 0;
        }

        @Override
        public boolean shouldContinue()
        {
            return false;
        }

        @Override
        public void tick()
        {
            BlockPos blockpos = SlothPetEntity.this.getBoundOrigin();
            if (blockpos == null)
            {
                blockpos = SlothPetEntity.this.getBlockPos();
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(SlothPetEntity.this.random.nextInt(15) - 7, SlothPetEntity.this.random.nextInt(11) - 5, SlothPetEntity.this.random.nextInt(15) - 7);
                if (SlothPetEntity.this.world.isAir(blockpos1))
                {
                    SlothPetEntity.this.moveControl.moveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (SlothPetEntity.this.getTarget() == null)
                    {
                        SlothPetEntity.this.getLookControl().lookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}