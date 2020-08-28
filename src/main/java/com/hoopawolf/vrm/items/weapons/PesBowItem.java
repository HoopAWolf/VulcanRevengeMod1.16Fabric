package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.VulcanRevengeMod;
import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PesBowItem extends BowItem
{
    public PesBowItem(Settings builder)
    {
        super(builder.rarity(Rarity.UNCOMMON));
    }

    @Override
    public Predicate<ItemStack> getProjectiles()
    {
        return null;
    }

    @Override
    public void usageTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count)
    {
        if (livingEntityIn.isSneaking() && livingEntityIn.age % 50 == 0)
        {
            livingEntityIn.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 0.5F, 0.1F);
            if (!worldIn.isClient)
            {
                for (int i = 1; i <= 180; ++i)
                {
                    double yaw = (double) i * 360.D / 180.D;
                    double speed = 0.3;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vec3d(livingEntityIn.getX(), livingEntityIn.getY() + 0.5F, livingEntityIn.getZ()), new Vec3d(xSpeed, 0.0D, zSpeed), 1, 5, 0.0F);
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeInt(spawnParticleMessage.getMessageType());
                    spawnParticleMessage.encode(passedData);
                    Stream<PlayerEntity> playerInDimension = PlayerStream.world(livingEntityIn.world);
                    playerInDimension.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, VulcanRevengeMod.CLIENT_PACKET_ID, passedData));
                }
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;

            int i = this.getMaxUseTime(stack) - timeLeft;

            float f = getPullProgress(i);
            if (!((double) f < 0.1D))
            {
                if (!worldIn.isClient)
                {
                    int _iteration = 1;
                    float _spread = 0.0F;

                    if (playerentity.isSneaking())
                    {
                        _iteration = 3;
                        _spread = -5.0F;
                    }

                    for (int iteration = 0; iteration < _iteration; ++iteration)
                    {
                        PersistentProjectileEntity abstractarrowentity = new PesArrowEntity(worldIn, playerentity);
                        abstractarrowentity.setProperties(playerentity, playerentity.pitch, playerentity.yaw + _spread, 0.0F, f * 3.0F, (_iteration == 3) ? 3.0F : 1.0F);
                        if (f == 1.0F)
                        {
                            abstractarrowentity.setCritical(true);
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0)
                        {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0)
                        {
                            abstractarrowentity.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0)
                        {
                            abstractarrowentity.setOnFireFor(100);
                        }

                        stack.damage(1, playerentity, (p_220009_1_) ->
                        {
                            p_220009_1_.sendToolBreakStatus(playerentity.getActiveHand());
                        });

                        worldIn.spawnEntity(abstractarrowentity);
                        _spread += 5.0F;
                    }

                    worldIn.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    playerentity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isClient)
        {
            if (entityIn instanceof LivingEntity && isSelected)
            {
                if (!((LivingEntity) entityIn).getStatusEffects().isEmpty())
                {
                    ArrayList<StatusEffect> array = new ArrayList<>();
                    for (StatusEffectInstance eff : ((LivingEntity) entityIn).getStatusEffects())
                    {
                        if (!eff.getEffectType().isBeneficial())
                        {
                            array.add(eff.getEffectType());
                        }
                    }

                    if (array.size() > 0)
                    {
                        for (StatusEffect eff : array)
                        {
                            ((LivingEntity) entityIn).removeStatusEffect(eff);
                        }
                    }
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getStackInHand(handIn);

        playerIn.setCurrentHand(handIn);
        return TypedActionResult.consume(itemstack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes1")).setStyle(Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes1.1")).setStyle(Style.EMPTY.withFormatting(Formatting.LIGHT_PURPLE)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes2")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes3")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes3.1")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
        tooltip.add(new TranslatableText(I18n.translate("tooltip.vrm:pes4")).setStyle(Style.EMPTY.withItalic(true).withFormatting(Formatting.GRAY)));
    }
}
