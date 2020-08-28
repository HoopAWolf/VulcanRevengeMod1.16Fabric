package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Items")
public class ItemConfig implements ConfigData
{

    @Comment(value = "\nSloth Mask allow setting to night when sleep in day")
    public boolean slothMaskTurnNight = true;


    @Comment(value = "\nSloth Mask allow setting to day when sleep in night")
    public boolean slothMaskTurnDay = true;


    @Comment(value = "\nGreed Mask allowed to double drops")
    public boolean greedDoubleDrop = true;


    @Comment(value = "\nItem used to get Pestilence's Bow")
    public String pesBowItem = "item.minecraft.bow";


    @Comment(value = "\nItem used to get Makharia (War Sword)")
    public String warSwordItem = "item.minecraft.diamond_sword";


    @Comment(value = "\nItem used to get Death Scythe")
    public String deathScytheItem = "item.minecraft.diamond_hoe";


    @Comment(value = "\nItem used to get Famine's Scale")
    public String famineScaleItem = "item.minecraft.diamond_pickaxe";


    @Comment(value = "\nItem used to get Gluttony's Mask")
    public String gluttonyItemOne = "item.minecraft.cooked_beef";


    @Comment(value = "\nItem used to get Gluttony's Mask")
    public String gluttonyItemTwo = "item.minecraft.cooked_cod";


    @Comment(value = "\nItem used to get Gluttony's Mask")
    public String gluttonyItemThree = "item.minecraft.cooked_rabbit";


    @Comment(value = "\nItem used to get Envy's Mask")
    public String envyItemOne = "item.minecraft.slime_ball";


    @Comment(value = "\nItem used to get Envy's Mask")
    public String envyItemTwo = "item.minecraft.magma_cream";


    @Comment(value = "\nItem used to get Envy's Mask")
    public String envyItemThree = "block.minecraft.slime_block";


    @Comment(value = "\nItem used to get Lust's Mask")
    public String lustItemOne = "item.minecraft.wheat";


    @Comment(value = "\nItem used to get Lust's Mask")
    public String lustItemTwo = "item.minecraft.wheat_seeds";


    @Comment(value = "\nItem used to get Lust's Mask")
    public String lustItemThree = "item.minecraft.carrot";


    @Comment(value = "\nItem used to get Greed's Mask")
    public String greedItemOne = "item.minecraft.diamond";


    @Comment(value = "\nItem used to get Greed's Mask")
    public String greedItemTwo = "item.minecraft.gold_ingot";


    @Comment(value = "\nItem used to get Greed's Mask")
    public String greedItemThree = "item.minecraft.emerald";


    @Comment(value = "\nItem used to get Sloth's Mask")
    public String slothItemOne = "block.minecraft.white_bed";


    @Comment(value = "\nItem used to get Sloth's Mask")
    public String slothItemTwo = "block.minecraft.brown_bed";


    @Comment(value = "\nItem used to get Sloth's Mask")
    public String slothItemThree = "block.minecraft.lime_bed";


    @Comment(value = "\nItem used to get Wrath's Mask")
    public String wrathItemOne = "item.minecraft.diamond_sword";


    @Comment(value = "\nItem used to get Wrath's Mask")
    public String wrathItemTwo = "item.minecraft.shield";


    @Comment(value = "\nItem used to get Wrath's Mask")
    public String wrathItemThree = "item.minecraft.diamond_chestplate";


    @Comment(value = "\nItem used to get Pride's Mask")
    public String prideItemOne = "item.minecraft.bow";


    @Comment(value = "\nItem used to get Pride's Mask")
    public String prideItemTwo = "item.minecraft.golden_apple";


    @Comment(value = "\nItem used to get Pride's Mask")
    public String prideItemThree = "item.minecraft.ender_eye";
}
