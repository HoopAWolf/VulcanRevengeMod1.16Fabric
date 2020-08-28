package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.potion.*;
import com.hoopawolf.vrm.potion.sin.EnvyTrialEffect;
import com.hoopawolf.vrm.potion.sin.PrideTrialEffect;
import com.hoopawolf.vrm.potion.sin.SlothTrialEffect;
import com.hoopawolf.vrm.potion.sin.WrathTrialEffect;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionRegistryHandler
{
    //EFFECTS
    public static final StatusEffect PLAGUE_EFFECT = new PotionEffectBase(StatusEffectType.HARMFUL, 3035801);
    public static final StatusEffect DAZED_EFFECT = new PotionEffectBase(StatusEffectType.HARMFUL, 5578058).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -7.15F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect FEAR_EFFECT = new PotionEffectBase(StatusEffectType.HARMFUL, 3012801);
    public static final StatusEffect FLIGHT_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 3012801);
    public static final StatusEffect ECHO_LOCATION_EFFECT = new EchoLocationEffect(StatusEffectType.BENEFICIAL, 2012801);
    public static final StatusEffect POISON_ATTACK_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4012801);
    public static final StatusEffect FLAME_CHARGE_EFFECT = new FireChargeEffect(StatusEffectType.BENEFICIAL, 1012801);
    public static final StatusEffect EXPLOSIVE_FLAME_CHARGE_EFFECT = new ExplosiveFireChargeEffect(StatusEffectType.BENEFICIAL, 1012801);
    public static final StatusEffect FALL_RESISTANCE_EFFECT = new FallResistanceEffect(StatusEffectType.BENEFICIAL, 3232801);
    public static final StatusEffect POISON_RESISTANCE_EFFECT = new PoisonResistanceEffect(StatusEffectType.BENEFICIAL, 4122801);
    public static final StatusEffect CLIMB_EFFECT = new ClimbEffect(StatusEffectType.BENEFICIAL, 3442801);
    public static final StatusEffect EGG_ATTACK_EFFECT = new EggAttackEffect(StatusEffectType.BENEFICIAL, 3652801);
    public static final StatusEffect SWIMMING_SPEED_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 1112801);
    public static final StatusEffect MILK_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4212801);
    public static final StatusEffect RAGE_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4123801);
    public static final StatusEffect EXPLOSIVE_RESISTANCE_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4451801);
    public static final StatusEffect HUNGER_RESISTANCE_EFFECT = new HungerResistanceEffect(StatusEffectType.BENEFICIAL, 4321801);
    public static final StatusEffect TELEPORTATION_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 1253801);
    public static final StatusEffect EVOKE_EFFECT = new EvokeEffect(StatusEffectType.BENEFICIAL, 2513801);
    public static final StatusEffect STEW_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 2513801);
    public static final StatusEffect SLEEP_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 253380);
    public static final StatusEffect ARROW_ATTACK_EFFECT = new BowAttackEffect(StatusEffectType.BENEFICIAL, 5513801);
    public static final StatusEffect INK_EFFECT = new InkEffect(StatusEffectType.BENEFICIAL, 4413801);
    public static final StatusEffect FROST_WALK_EFFECT = new FrostWalkEffect(StatusEffectType.BENEFICIAL, 4413801);
    public static final StatusEffect WITHER_ATTACK_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4413801);
    public static final StatusEffect GLUTTONY_TRIAL_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 3035801);
    public static final StatusEffect ENVY_TRIAL_EFFECT = new EnvyTrialEffect(StatusEffectType.BENEFICIAL, 1012801);
    public static final StatusEffect LUST_TRIAL_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 4413801);
    public static final StatusEffect GREED_TRIAL_EFFECT = new PotionEffectBase(StatusEffectType.BENEFICIAL, 2513801);
    public static final StatusEffect SLOTH_TRIAL_EFFECT = new SlothTrialEffect(StatusEffectType.BENEFICIAL, 4413801);
    public static final StatusEffect WRATH_TRIAL_EFFECT = new WrathTrialEffect(StatusEffectType.BENEFICIAL, 1112801);
    public static final StatusEffect PRIDE_TRIAL_EFFECT = new PrideTrialEffect(StatusEffectType.BENEFICIAL, 3442801);

    //POTION
    public static final Potion GLUTTONY_TRIAL_POTION = new Potion("gluttonytrialpotion", new StatusEffectInstance(GLUTTONY_TRIAL_EFFECT, 30090));
    public static final Potion ENVY_TRIAL_POTION = new Potion("envytrialpotion", new StatusEffectInstance(ENVY_TRIAL_EFFECT, 30090));
    public static final Potion LUST_TRIAL_POTION = new Potion("lusttrialpotion", new StatusEffectInstance(LUST_TRIAL_EFFECT, 30090));
    public static final Potion GREED_TRIAL_POTION = new Potion("greedtrialpotion", new StatusEffectInstance(GREED_TRIAL_EFFECT, 30090));
    public static final Potion SLOTH_TRIAL_POTION = new Potion("slothtrialpotion", new StatusEffectInstance(SLOTH_TRIAL_EFFECT, 30090));
    public static final Potion WRATH_TRIAL_POTION = new Potion("wrathtrialpotion", new StatusEffectInstance(WRATH_TRIAL_EFFECT, 30090));
    public static final Potion PRIDE_TRIAL_POTION = new Potion("pridetrialpotion", new StatusEffectInstance(PRIDE_TRIAL_EFFECT, 30090));

    public static void init()
    {
        //EFFECTS
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "plagueeffect"), PLAGUE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "dazedeffect"), DAZED_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "feareffect"), FEAR_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "flighteffect"), FLIGHT_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "echolocationeffect"), ECHO_LOCATION_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "poisonattackeffect"), POISON_ATTACK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "flamechargeeffect"), FLAME_CHARGE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "explosivefirechargeeffect"), EXPLOSIVE_FLAME_CHARGE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "fallresisteffect"), FALL_RESISTANCE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "poisonresisteffect"), POISON_RESISTANCE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "climbeffect"), CLIMB_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "eggattackeffect"), EGG_ATTACK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "swimmingspeedeffect"), SWIMMING_SPEED_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "milkeffect"), MILK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "rageeffect"), RAGE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "explosiveresisteffect"), EXPLOSIVE_RESISTANCE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "hungerresisteffect"), HUNGER_RESISTANCE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "teleportationeffect"), TELEPORTATION_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "evokeeffect"), EVOKE_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "steweffect"), STEW_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "sleepeffect"), SLEEP_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "arrowattackeffect"), ARROW_ATTACK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "inkeffect"), INK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "frostwalkeffect"), FROST_WALK_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "witherattackeffect"), WITHER_ATTACK_EFFECT);

        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "gluttonytrialeffect"), GLUTTONY_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "envytrialeffect"), ENVY_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "lusttrialeffect"), LUST_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "greedtrialeffect"), GREED_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "slothtrialeffect"), SLOTH_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "wrathtrialeffect"), WRATH_TRIAL_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "pridetrialeffect"), PRIDE_TRIAL_EFFECT);

        //POTION
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "gluttonytrialpotion"), GLUTTONY_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "envytrialpotion"), ENVY_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "lusttrialpotion"), LUST_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "greedtrialpotion"), GREED_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "slothtrialpotion"), SLOTH_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "wrathtrialpotion"), WRATH_TRIAL_POTION);
        Registry.register(Registry.POTION, new Identifier(Reference.MOD_ID, "pridetrialpotion"), PRIDE_TRIAL_POTION);
    }
}
