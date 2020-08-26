package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Items")
public class ItemConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nSloth Mask allow setting to night when sleep in day")
    public boolean slothMaskTurnNight = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nSloth Mask allow setting to day when sleep in night")
    public boolean slothMaskTurnDay = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nGreed Mask allowed to double drops")
    public boolean greedDoubleDrop = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nItem used to get Pestilence's Bow")
    public String pesBowItem = "item.minecraft.bow";

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nItem used to get Makharia (War Sword)")
    public String warSwordItem = "item.minecraft.diamond_sword";

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nItem used to get Death Scythe")
    public String deathScytheItem = "item.minecraft.diamond_hoe";

    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nItem used to get Famine's Scale")
    public String famineScaleItem = "item.minecraft.diamond_pickaxe";


}
