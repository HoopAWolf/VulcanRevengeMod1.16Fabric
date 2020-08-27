package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public class DeathSwordItem extends SwordItem
{
    public DeathSwordItem(ToolMaterial tier, int attackDamageIn, float attackSpeedIn, Settings builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.rarity(Rarity.UNCOMMON));
    }

    public static int getVoodooCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("voodoo", 0);

        return stack.getTag().getInt("voodoo");
    }

    public static int getDeathCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("death", 0);

        return stack.getTag().getInt("death");
    }

    public static int getMarkCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("mark", 0);

        return stack.getTag().getInt("mark");
    }

    public static int getVoodooID(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("voodooid", 0);

        return stack.getTag().getInt("voodooid");
    }

    public static void setVoodooCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("voodoo", amount);
    }

    public static void setDeathCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("death", amount);
    }

    public static void setMarkCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("mark", amount);
    }

    public static void setVoodooID(ItemStack stack, int id)
    {
        stack.getOrCreateTag().putInt("voodooid", id);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!playerIn.isSneaking() && handIn.equals(Hand.MAIN_HAND))
        {
            if (getMarkCoolDown(playerIn.getStackInHand(handIn)) <= 0)
            {
                if (!worldIn.isClient)
                {
                    setMarkCoolDown(playerIn.getStackInHand(handIn), 200);
                    for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 5, 2, 5, 10))
                    {
                        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 180, 0));
                    }
                    playerIn.playSound(SoundEvents.ENTITY_SQUID_SQUIRT, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            } else
            {
                if (!worldIn.isClient)
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    EntityHelper.sendCoolDownMessage(playerIn, getMarkCoolDown(playerIn.getStackInHand(handIn)));
                }
            }
        }

        return TypedActionResult.pass(playerIn.getStackInHand(handIn));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {

        if (playerIn.isSneaking() && hand.equals(Hand.MAIN_HAND))
        {
            if (getVoodooCoolDown(playerIn.getStackInHand(hand)) <= 0)
            {
                if (!playerIn.world.isClient)
                {
                    setVoodooCoolDown(playerIn.getStackInHand(hand), 200);
                    setVoodooID(playerIn.getStackInHand(hand), target.getEntityId());
                    playerIn.playSound(SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    return ActionResult.SUCCESS;
                }
            } else
            {
                if (!playerIn.world.isClient)
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    EntityHelper.sendCoolDownMessage(playerIn, getVoodooCoolDown(playerIn.getStackInHand(hand)));
                }
            }
        }

        return ActionResult.FAIL;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.postHit(stack, target, attacker);

        if (attacker.world.random.nextInt(100) < 30 || target.hasStatusEffect(StatusEffects.GLOWING))
        {
            attacker.heal(this.getAttackDamage() * 0.5F);
        }

        if (target.hasStatusEffect(StatusEffects.GLOWING))
        {
            target.damage(DamageSource.MAGIC, this.getAttackDamage() * 2.0F);
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isClient)
        {
            if (entityIn.age % 2 == 0)
            {
                if (getDeathCoolDown(stack) > 0)
                {
                    setDeathCoolDown(stack, getDeathCoolDown(stack) - 1);
                }

                if (getMarkCoolDown(stack) > 0)
                {
                    setMarkCoolDown(stack, getMarkCoolDown(stack) - 1);
                }

                if (getVoodooCoolDown(stack) > 0)
                {
                    setVoodooCoolDown(stack, getVoodooCoolDown(stack) - 1);
                } else if (getVoodooCoolDown(stack) <= 0 && getVoodooID(stack) != 0)
                {
                    setVoodooID(stack, 0);
                }

                if (worldIn.getEntityById(getVoodooID(stack)) == null || !worldIn.getEntityById(getVoodooID(stack)).isAlive())
                {
                    setVoodooID(stack, 0);
                }
            }

            if (getVoodooID(stack) != 0)
            {
                Entity entity = worldIn.getEntityById(getVoodooID(stack));

                if (entity != null)
                {
                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(entity.getX(), entity.getEyeY() + 0.5D, entity.getZ()), new Vec3d(0.0D, 0.0D, 0.0D), 1, 3, 0.0F);
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeInt(spawnParticleMessage.getMessageType());
                    spawnParticleMessage.encode(passedData);
                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(entity.world);
                    playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                }
            }
        } else
        {
            if (entityIn instanceof PlayerEntity)
            {
                ((PlayerEntity) entityIn).getItemCooldownManager().set(stack.getItem(), 0); //TODO do it in the constructor? via mixins
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:death1")).setStyle(Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:death2") + ((getMarkCoolDown(stack) > 0) ? " [" + (getMarkCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getMarkCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:death3") + ((getVoodooCoolDown(stack) > 0) ? " [" + (getVoodooCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getVoodooCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:death4") + ((getDeathCoolDown(stack) > 0) ? " [" + (getDeathCoolDown(stack) / 20) + "s]" : "")).setStyle(Style.EMPTY.withItalic(true).withFormatting(((getDeathCoolDown(stack) > 0) ? Formatting.DARK_GRAY : Formatting.GRAY))));
    }
}