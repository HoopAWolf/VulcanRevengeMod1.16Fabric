//package com.hoopawolf.vrm.util;
//
//import com.hoopawolf.vrm.config.ConfigHandler;
//import com.hoopawolf.vrm.data.EatItemData;
//import com.hoopawolf.vrm.data.EnvyEntityData;
//import com.hoopawolf.vrm.entities.SlothPetEntity;
//import com.hoopawolf.vrm.entities.ai.*;
//import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
//import com.hoopawolf.vrm.helper.VRMEatItemDataHandler;
//import com.hoopawolf.vrm.helper.VRMEnvyEntityDataHandler;
//import com.hoopawolf.vrm.helper.VRMGreedItemDataHandler;
//import com.hoopawolf.vrm.items.armors.SinsArmorItem;
//import com.hoopawolf.vrm.items.weapons.DeathSwordItem;
//import com.hoopawolf.vrm.network.VRMPacketHandler;
//import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
//import com.hoopawolf.vrm.network.packets.client.SendPlayerMessageMessage;
//import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
//import com.hoopawolf.vrm.ref.Reference;
//import net.minecraft.entity.CreatureEntity;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.item.ItemEntity;
//import net.minecraft.entity.passive.AnimalEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.potion.Effect;
//import net.minecraft.potion.EffectInstance;
//import net.minecraft.potion.Effects;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.Hand;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.GameRules;
//import net.minecraft.world.World;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.event.entity.EntityJoinWorldEvent;
//import net.minecraftforge.event.entity.item.ItemTossEvent;
//import net.minecraftforge.event.entity.living.*;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
//import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
//public class VRMServerEventHandler
//{
//    @SubscribeEvent
//    public static void DeathEvent(LivingDeathEvent event)
//    {
//        if (!event.getEntity().world.isRemote)
//        {
//            if (event.getEntity() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntity();
//                LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
//
//                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()) &&
//                        DeathSwordItem.getDeathCoolDown(player.getHeldItemMainhand()) <= 0)
//                {
//                    event.setCanceled(true);
//                    attacker.attackEntityFrom(new DamageSource("death"), attacker.getMaxHealth() * 0.5F);
//                    player.setHealth(player.getMaxHealth() * 0.5F);
//                    player.getFoodStats().setFoodLevel(20);
//                    DeathSwordItem.setDeathCoolDown(player.getHeldItemMainhand(), 600);
//                    PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playVexSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                }
//            }
//
//            if (event.getEntity() instanceof LivingEntity)
//            {
//                LivingEntity target = (LivingEntity) event.getEntity();
//
//                if (target.isPotionActive(PotionRegistryHandler.PLAGUE_EFFECT) || event.getSource().getImmediateSource() instanceof PesArrowEntity)
//                {
//                    PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(target.getEntityId(), 6, 0.5F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(target.world.func_234923_W_(), playVexSoundMessage);
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360D / 180D;
//                        double speed = 0.7;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(target.getPosX(), target.getPosY() + 0.5F, target.getPosZ()), new Vector3d(xSpeed, 0.9D, zSpeed), 3, 5, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(target.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onLivingJump(LivingEvent.LivingJumpEvent event)
//    {
//        if (event.getEntity() instanceof LivingEntity)
//        {
//            LivingEntity entity = (LivingEntity) event.getEntity();
//
//            if (entity.isPotionActive(PotionRegistryHandler.DAZED_EFFECT.get()) && entity.collidedVertically)
//            {
//                entity.setMotion(entity.getMotion().getX(), 0, entity.getMotion().getZ());
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
//    {
//        if (event.player.isPotionActive(PotionRegistryHandler.FLIGHT_EFFECT.get()))
//        {
//            if (!event.player.abilities.allowFlying)
//            {
//                event.player.abilities.allowFlying = true;
//                event.player.abilities.isFlying = true;
//            }
//        } else
//        {
//            if (!event.player.abilities.isCreativeMode && !event.player.isSpectator())
//            {
//                event.player.abilities.allowFlying = false;
//                event.player.abilities.isFlying = false;
//            }
//        }
//
//        if (event.player.isPotionActive(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.get()))
//        {
//            if (event.player.getFoodStats().getFoodLevel() > 3)
//            {
//                event.player.getFoodStats().setFoodLevel(3);
//                event.player.getFoodStats().addExhaustion(40);
//            }
//
//            if (!event.player.world.isRemote)
//            {
//                event.player.addPotionEffect(new EffectInstance(Effects.HUNGER, 1, 3));
//            }
//        }
//
//        if (!event.player.world.isRemote)
//        {
//            if (event.player.isPotionActive(PotionRegistryHandler.GREED_TRIAL_EFFECT.get()))
//            {
//                int totalItems = 0;
//                for (ItemStack itemstack : event.player.inventory.mainInventory)
//                {
//                    totalItems += itemstack.getCount();
//                }
//
//                float invenSize = ((float) totalItems / (float) (event.player.inventory.mainInventory.size() * 64));
//
//                event.player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, (int) (4.0F * (1.0F - invenSize))));
//                event.player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 10, (int) (4.0F * (1.0F - invenSize))));
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void HurtEvent(LivingHurtEvent event)
//    {
//        if (!event.getEntityLiving().world.isRemote)
//        {
//            if (event.getSource().isExplosion())
//            {
//                if (event.getEntityLiving().isPotionActive(PotionRegistryHandler.EXPLOSIVE_RESISTANCE_EFFECT.get()))
//                {
//                    event.setCanceled(true);
//                }
//            }
//
//            if (event.getEntityLiving() instanceof CreatureEntity)
//            {
//                if (event.getEntityLiving().isPotionActive(PotionRegistryHandler.DAZED_EFFECT.get()))
//                {
//                    event.getEntityLiving().removePotionEffect(PotionRegistryHandler.DAZED_EFFECT.get());
//                }
//
//                if (event.getSource().getImmediateSource() instanceof PlayerEntity)
//                {
//                    PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
//
//                    if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.WRATH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
//                    {
//                        SinsArmorItem.increaseFulfilment(player.inventory.armorInventory.get(3), 2, SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
//
//                        if (player.inventory.armorInventory.get(3).getItem().getDurabilityForDisplay(player.inventory.armorInventory.get(3)) < 0.2D)
//                        {
//                            event.getEntityLiving().setFire(100);
//                        }
//                    }
//
//                    if (player.isPotionActive(PotionRegistryHandler.POISON_ATTACK_EFFECT.get()))
//                    {
//                        event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, 200, 0));
//                    }
//
//                    if (player.isPotionActive(PotionRegistryHandler.WITHER_ATTACK_EFFECT.get()))
//                    {
//                        event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.WITHER, 200, 0));
//                    }
//
//                    if (player.isPotionActive(PotionRegistryHandler.PRIDE_TRIAL_EFFECT.get()))
//                    {
//                        if (event.getEntityLiving().getPosY() + 1 > player.getPosY())
//                        {
//                            event.setCanceled(true);
//                        }
//                    }
//
//                    if (player.isPotionActive(PotionRegistryHandler.ENVY_TRIAL_EFFECT.get()))
//                    {
//                        event.setAmount(1);
//                    }
//                }
//            }
//
//            if (event.getEntityLiving() instanceof PlayerEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
//
//                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()))
//                {
//                    if (DeathSwordItem.getVoodooID(player.getHeldItemMainhand()) != 0 &&
//                            player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())) != null &&
//                            player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).isAlive())
//                    {
//                        event.setCanceled(true);
//                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).attackEntityFrom(new DamageSource("reaper"), event.getAmount());
//                        PlaySoundEffectMessage playVexSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 4, 1.0F, 0.2F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playVexSoundMessage);
//                    }
//                } else if ((player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE) || player.getHeldItemOffhand().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE)))
//                {
//                    if (player.world.rand.nextInt(100) < 40)
//                    {
//                        if (event.getSource().getTrueSource() instanceof CreatureEntity)
//                        {
//                            ((CreatureEntity) event.getSource().getTrueSource()).addPotionEffect(new EffectInstance(new EffectInstance(PotionRegistryHandler.DAZED_EFFECT.get(), 100)));
//                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(event.getSource().getTrueSource().getPosX(), event.getSource().getTrueSource().getPosY() + 0.5F, event.getSource().getTrueSource().getPosZ()), new Vector3d(0.0F, 0.0D, 0.0F), 3, 9, event.getSource().getTrueSource().getWidth());
//                            VRMPacketHandler.packetHandler.sendToDimension(event.getSource().getTrueSource().world.func_234923_W_(), spawnParticleMessage);
//                        }
//
//                        PlaySoundEffectMessage playBanjoSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 3, 1.0F, 1.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playBanjoSoundMessage);
//                    }
//                } else if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.WAR_SWORD.get()))
//                {
//                    if (player.getHealth() < player.getMaxHealth() * 0.3F && event.getSource().getTrueSource() instanceof LivingEntity)
//                    {
//                        event.getSource().getTrueSource().setFire(10);
//                        PlaySoundEffectMessage playCrackleSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 2, 1.0F, 1.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playCrackleSoundMessage);
//                    }
//                }
//
//                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
//                {
//                    if (player.world.rand.nextInt(100) < 40)
//                    {
//                        SinsArmorItem.increaseFulfilment(player.inventory.armorInventory.get(3), (int) event.getAmount() * 2, SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
//                        player.getFoodStats().setFoodLevel(MathHelper.clamp(player.getFoodStats().getFoodLevel() + (int) event.getAmount(), 0, 20));
//                        PlaySoundEffectMessage playEatSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 0, 1.0F, 1.0F);
//                        PlaySoundEffectMessage playBurpSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 1, 1.0F, 1.0F);
//
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playEatSoundMessage);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playBurpSoundMessage);
//                        event.setCanceled(true);
//                    }
//                } else if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.WRATH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
//                {
//                    SinsArmorItem.increaseFulfilment(player.inventory.armorInventory.get(3), 2, SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
//                } else if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.PRIDE_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
//                {
//                    SinsArmorItem.decreaseFulfilment(player.inventory.armorInventory.get(3), 5, SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void ApplyPotionEvent(PotionEvent.PotionApplicableEvent event)
//    {
//        if (!event.getEntity().world.isRemote)
//        {
//            if (event.getEntity() instanceof PlayerEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntity();
//
//                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.PES_BOW.get()))
//                {
//                    if (!event.getPotionEffect().getPotion().isBeneficial())
//                    {
//                        event.setResult(Event.Result.DENY);
//                    }
//
//                } else if (event.getPotionEffect().getPotion().equals(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.ENVY_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.SLOTH_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.WRATH_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.LUST_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.PRIDE_TRIAL_EFFECT.get()) ||
//                        event.getPotionEffect().getPotion().equals(PotionRegistryHandler.GREED_TRIAL_EFFECT.get()))
//                {
//                    if (event.getEntityLiving().isPotionActive(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.ENVY_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.SLOTH_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.WRATH_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.LUST_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.PRIDE_TRIAL_EFFECT.get()) ||
//                            event.getEntityLiving().isPotionActive(PotionRegistryHandler.GREED_TRIAL_EFFECT.get()))
//                    {
//                        event.setResult(Event.Result.DENY);
//                    }
//                } else if (event.getEntityLiving().isPotionActive(PotionRegistryHandler.MILK_EFFECT.get()))
//                {
//                    if (!event.getPotionEffect().getPotion().equals(PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.ENVY_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.SLOTH_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.WRATH_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.LUST_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.PRIDE_TRIAL_EFFECT.get()) &&
//                            !event.getPotionEffect().getPotion().equals(PotionRegistryHandler.GREED_TRIAL_EFFECT.get()))
//                    {
//                        event.setResult(Event.Result.DENY);
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void PotionExpireEvent(PotionEvent.PotionExpiryEvent event)
//    {
//        if (!event.getEntity().world.isRemote)
//        {
//            if (event.getEntity() instanceof PlayerEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntity();
//
//                Effect potion = event.getPotionEffect().getPotion();
//                if (PotionRegistryHandler.LUST_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 7, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.LUST_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.GLUTTONY_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 6, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.ENVY_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 11, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.ENVY_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.GREED_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 1, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.GREED_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.PRIDE_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 12, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.PRIDE_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.SLOTH_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 13, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()), true);
//                } else if (PotionRegistryHandler.WRATH_TRIAL_EFFECT.get().equals(potion))
//                {
//                    PlaySoundEffectMessage playSoundMessage = new PlaySoundEffectMessage(player.getEntityId(), 5, 5.0F, 0.1F);
//                    VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), playSoundMessage);
//
//                    for (int i = 1; i <= 180; ++i)
//                    {
//                        double yaw = (double) i * 360.D / 180.D;
//                        double speed = 0.3;
//                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
//                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));
//
//                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
//                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
//                    }
//                    player.dropItem(new ItemStack(ArmorRegistryHandler.WRATH_MASK_ARMOR.get()), true);
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void CheckSleepingTime(SleepingTimeCheckEvent event)
//    {
//        if (!event.getPlayer().world.isRemote)
//        {
//            if (event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)) ||
//                    event.getPlayer().isPotionActive(PotionRegistryHandler.SLEEP_EFFECT.get()))
//            {
//                event.setResult(Event.Result.ALLOW);
//
//                if (event.getPlayer().getSleepTimer() >= 100)
//                {
//                    long i = event.getPlayer().world.getDayTime() + 24000L;
//                    if (event.getPlayer().world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
//                    {
//                        if (event.getPlayer().world.isDaytime())
//                        {
//                            if (ConfigHandler.COMMON.slothMaskTurnNight.get())
//                            {
//                                ((ServerWorld) event.getPlayer().world).func_241114_a_((i - i % 24000L) - 11000L);
//                            }
//                        } else
//                        {
//                            if (ConfigHandler.COMMON.slothMaskTurnDay.get())
//                            {
//                                ((ServerWorld) event.getPlayer().world).func_241114_a_((i - i % 24000L));
//                            }
//                        }
//                    }
//
//                    event.getPlayer().wakeUp();
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void CheckSleepingLocation(SleepingLocationCheckEvent event)
//    {
//        if (!event.getEntity().world.isRemote)
//        {
//            if (event.getEntity() instanceof PlayerEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntity();
//
//                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)) ||
//                        player.isPotionActive(PotionRegistryHandler.SLEEP_EFFECT.get()))
//                {
//                    event.setResult(Event.Result.ALLOW);
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onJoinWorld(EntityJoinWorldEvent event)
//    {
//        Entity entity = event.getEntity();
//
//        World world = entity.world;
//
//        if (!world.isRemote)
//        {
//            if (entity instanceof CreatureEntity && !(entity instanceof SlothPetEntity))
//            {
//                ((CreatureEntity) entity).goalSelector.addGoal(0, new DazedGoal(((CreatureEntity) entity)));
//                ((CreatureEntity) entity).goalSelector.addGoal(0, new LustAvoidEntityGoal(((CreatureEntity) entity), PlayerEntity.class, 12.0F, 1.2D, 2.2D));
//                ((CreatureEntity) entity).goalSelector.addGoal(1, new FearPanicGoal((CreatureEntity) entity, 1.5D));
//            }
//
//            if (entity instanceof AnimalEntity)
//            {
//                ((AnimalEntity) entity).goalSelector.addGoal(1, new AnimalAttackGoal(((AnimalEntity) entity), 1.0D, true, 2, 1));
//                ((AnimalEntity) entity).goalSelector.addGoal(1, new LustSinTemptGoal(((AnimalEntity) entity), 1.0D));
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onEatFinish(LivingEntityUseItemEvent.Finish event)
//    {
//        if (!event.getEntityLiving().world.isRemote)
//        {
//            if (event.getItem().isFood() && event.getEntityLiving() instanceof PlayerEntity)
//            {
//                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
//
//                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
//                {
//                    SinsArmorItem.increaseFulfilment(player.inventory.armorInventory.get(3), event.getItem().getItem().getFood().getHealing(), SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onItemPickUp(PlayerEvent.ItemPickupEvent event)
//    {
//        if (!event.getPlayer().world.isRemote)
//        {
//            if (ConfigHandler.COMMON.greedDoubleDrop.get() && event.getEntityLiving().world.rand.nextInt(50) < 10)
//            {
//                ArrayList<String> blackList = VRMGreedItemDataHandler.INSTANCE.blackList;
//
//                if (!blackList.contains(event.getStack().getItem().getTranslationKey()))
//                {
//                    if (event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GREED_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)))
//                    {
//                        if (event.getOriginalEntity().getOwnerId() == null && event.getOriginalEntity().getThrowerId() == null)
//                        {
//                            int calculatedAmount = (int) ((1.0D - (float) event.getPlayer().inventory.armorInventory.get(3).getItem().getDurabilityForDisplay(event.getPlayer().inventory.armorInventory.get(3))) * 6);
//
//                            if (calculatedAmount > 0)
//                            {
//                                int additionalAmount = event.getPlayer().world.rand.nextInt(calculatedAmount);
//
//                                if (additionalAmount > 0)
//                                {
//                                    PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(event.getPlayer().getEntityId(), 8, 1.0F, 1.0F);
//                                    VRMPacketHandler.packetHandler.sendToDimension(event.getPlayer().world.func_234923_W_(), playDingSoundMessage);
//                                    event.getOriginalEntity().getItem().setCount(event.getOriginalEntity().getItem().getCount() + additionalAmount);
//                                }
//                                event.getOriginalEntity().setOwnerId(event.getPlayer().getUniqueID());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onItemTossed(ItemTossEvent event)
//    {
//        if (event.getEntityItem().getThrowerId() == null)
//        {
//            event.getEntityItem().setThrowerId(event.getPlayer().getUniqueID());
//        }
//    }
//
//    @SubscribeEvent
//    public static void onRightClickWithItem(PlayerInteractEvent.RightClickItem event)
//    {
//        if (!event.getPlayer().world.isRemote)
//        {
//            if (event.getPlayer().isCrouching() && event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)))
//            {
//                if (!event.getItemStack().isEmpty() && !event.getPlayer().getHeldItemMainhand().isFood())
//                {
//                    if (consumeItem(event.getPlayer(), event.getItemStack()))
//                    {
//                        event.setCanceled(true);
//                    } else
//                    {
//                        SendPlayerMessageMessage message = new SendPlayerMessageMessage(event.getPlayer().getUniqueID(), "cannoteat", TextFormatting.GRAY.getColorIndex());
//                        VRMPacketHandler.packetHandler.sendToPlayer((ServerPlayerEntity) event.getPlayer(), message);
//                    }
//                }
//            }
//
//            if (event.getPlayer().isPotionActive(PotionRegistryHandler.MILK_EFFECT.get()) && event.getItemStack().getItem().equals(Items.BUCKET))
//            {
//                event.getItemStack().shrink(1);
//                event.getPlayer().dropItem(new ItemStack(Items.MILK_BUCKET), true);
//            }
//
//            if (event.getPlayer().isPotionActive(PotionRegistryHandler.STEW_EFFECT.get()) && event.getItemStack().getItem().equals(Items.BOWL))
//            {
//                event.getItemStack().shrink(1);
//                event.getPlayer().dropItem(new ItemStack(Items.SUSPICIOUS_STEW), true);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void RightClickEntity(PlayerInteractEvent.EntityInteract event)
//    {
//        if (!event.getPlayer().world.isRemote)
//        {
//            if (event.getItemStack().isEmpty())
//            {
//                if (event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)))
//                {
//                    if (SinsArmorItem.getSlothPetID(event.getPlayer().inventory.armorInventory.get(3)) != 0)
//                    {
//                        if (event.getTarget() instanceof CreatureEntity && !(event.getTarget() instanceof SlothPetEntity))
//                        {
//                            if (event.getPlayer().world.getEntityByID(SinsArmorItem.getSlothPetID(event.getPlayer().inventory.armorInventory.get(3))) instanceof SlothPetEntity)
//                            {
//                                PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(event.getPlayer().getEntityId(), 7, 2.0F, 1.0F);
//                                VRMPacketHandler.packetHandler.sendToDimension(event.getPlayer().world.func_234923_W_(), playDingSoundMessage);
//
//                                ((SlothPetEntity) event.getPlayer().world.getEntityByID(SinsArmorItem.getSlothPetID(event.getPlayer().inventory.armorInventory.get(3)))).setAttackTarget((CreatureEntity) event.getTarget());
//                            }
//                        }
//                    }
//                } else if (event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.LUST_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)))
//                {
//                    SinsArmorItem.increaseFulfilment(event.getPlayer().inventory.armorInventory.get(3), 5, SinsArmorItem.getSin(event.getPlayer().inventory.armorInventory.get(3)).getMaxUse());
//
//                    if (event.getTarget() instanceof AnimalEntity)
//                    {
//                        if (!((AnimalEntity) event.getTarget()).isChild() && !((AnimalEntity) event.getTarget()).isInLove())
//                        {
//                            ((AnimalEntity) event.getTarget()).setInLove(600);
//                            ((AnimalEntity) event.getTarget()).setGrowingAge(0);
//                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(event.getTarget().getPosX(), event.getTarget().getPosY() + 0.85F, event.getTarget().getPosZ()), new Vector3d(0.0F, 0.0D, 0.0F), 3, 7, event.getTarget().getWidth());
//                            VRMPacketHandler.packetHandler.sendToDimension(event.getTarget().world.func_234923_W_(), spawnParticleMessage);
//                        }
//                    }
//                } else if (event.getPlayer().inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.ENVY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(event.getPlayer().inventory.armorInventory.get(3)))
//                {
//                    if (event.getTarget() instanceof LivingEntity && event.getPlayer().inventory.armorInventory.get(3).getItem().getDurabilityForDisplay(event.getPlayer().inventory.armorInventory.get(3)) > 0.85D)
//                    {
//                        if (VRMEnvyEntityDataHandler.INSTANCE.data.get(event.getTarget().getEntityString()) != null)
//                        {
//                            int milkRemainingDuration = 0;
//
//                            SinsArmorItem.increaseFulfilment(event.getPlayer().inventory.armorInventory.get(3), SinsArmorItem.getSin(event.getPlayer().inventory.armorInventory.get(3)).getMaxUse(), SinsArmorItem.getSin(event.getPlayer().inventory.armorInventory.get(3)).getMaxUse());
//                            EnvyEntityData data = VRMEnvyEntityDataHandler.INSTANCE.data.get(event.getTarget().getEntityString());
//                            PlaySoundEffectMessage playDingSoundMessage = new PlaySoundEffectMessage(event.getPlayer().getEntityId(), 8, 1.0F, 1.0F);
//                            VRMPacketHandler.packetHandler.sendToDimension(event.getPlayer().world.func_234923_W_(), playDingSoundMessage);
//
//                            if (event.getPlayer().isPotionActive(PotionRegistryHandler.MILK_EFFECT.get()))
//                            {
//                                milkRemainingDuration = event.getPlayer().getActivePotionEffect(PotionRegistryHandler.MILK_EFFECT.get()).getDuration();
//                                event.getPlayer().removeActivePotionEffect(PotionRegistryHandler.MILK_EFFECT.get());
//                            }
//
//                            for (String effects : data.getListOfEffects())
//                            {
//                                for (Map.Entry<ResourceLocation, Effect> list : ForgeRegistries.POTIONS.getEntries())
//                                {
//                                    if (list.getKey().toString().equals(effects))
//                                    {
//                                        event.getPlayer().addPotionEffect(new EffectInstance(list.getValue(), (list.getValue().equals(Effects.INSTANT_DAMAGE) || list.getValue().equals(Effects.INSTANT_HEALTH)) ? 1 : 500, data.getAmplifier()));
//                                        break;
//                                    }
//                                }
//                            }
//
//                            if (milkRemainingDuration > 0)
//                            {
//                                event.getPlayer().addPotionEffect(new EffectInstance(PotionRegistryHandler.MILK_EFFECT.get(), milkRemainingDuration, 0));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void OnLivingDrop(LivingDropsEvent event)
//    {
//        if (!event.getEntityLiving().world.isRemote)
//        {
//            if (event.getSource().getImmediateSource() instanceof SlothPetEntity)
//            {
//                for (ItemEntity drop : event.getDrops())
//                {
//                    drop.setLocationAndAngles(((SlothPetEntity) event.getSource().getImmediateSource()).getOwner().getPosX(),
//                            ((SlothPetEntity) event.getSource().getImmediateSource()).getOwner().getPosY(),
//                            ((SlothPetEntity) event.getSource().getImmediateSource()).getOwner().getPosZ(), drop.rotationYaw, drop.rotationPitch);
//                }
//            }
//        }
//    }
//
//    private static boolean consumeItem(PlayerEntity playerIn, ItemStack itemStackIn)
//    {
//        if (VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey()) != null)
//        {
//            EatItemData data = VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey());
//
//            PlaySoundEffectMessage playEatSoundMessage = new PlaySoundEffectMessage(playerIn.getEntityId(), 0, 1.0F, 1.0F);
//            PlaySoundEffectMessage playBurpSoundMessage = new PlaySoundEffectMessage(playerIn.getEntityId(), 1, 1.0F, 1.0F);
//
//            VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), playEatSoundMessage);
//            VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), playBurpSoundMessage);
//
//            if (itemStackIn.isDamageable())
//            {
//                itemStackIn.damageItem((int) (itemStackIn.getMaxDamage() * 0.3F), playerIn, (p_220287_1_) ->
//                        p_220287_1_.sendBreakAnimation(Hand.MAIN_HAND));
//            } else
//            {
//                itemStackIn.shrink(1);
//                playerIn.sendBreakAnimation(Hand.MAIN_HAND);
//            }
//
//            playerIn.getFoodStats().setFoodLevel(MathHelper.clamp(playerIn.getFoodStats().getFoodLevel() + data.getFoodAmount(), 0, 20));
//            SinsArmorItem.increaseFulfilment(playerIn.inventory.armorInventory.get(3), data.getFoodAmount(), SinsArmorItem.getSin(playerIn.inventory.armorInventory.get(3)).getMaxUse());
//
//            for (String effects : data.getListOfEffects())
//            {
//                for (Map.Entry<ResourceLocation, Effect> list : ForgeRegistries.POTIONS.getEntries())
//                {
//                    if (list.getKey().toString().equals(effects))
//                    {
//                        playerIn.addPotionEffect(new EffectInstance(list.getValue(), (list.getValue().equals(Effects.INSTANT_DAMAGE) || list.getValue().equals(Effects.INSTANT_HEALTH)) ? 1 : data.getDuration(), data.getAmplifier()));
//                        break;
//                    }
//                }
//            }
//
//            return true;
//        }
//
//        return false;
//    }
//}