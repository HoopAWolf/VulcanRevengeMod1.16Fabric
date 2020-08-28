package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Structures")
public class StructureConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nMin distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int minStructureAway = 7;


    @Comment(value = "\nMax distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int maxStructureAway = 12;


    @Comment(value = "\nSword Structure spawning percentage")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int structureSpawnChance = 40;


    @Comment(value = "\nMin distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int minSinStructureAway = 7;


    @Comment(value = "\nMax distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int maxSinStructureAway = 12;


    @Comment(value = "\nSin Structure spawning percentage")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int structureSinSpawnChance = 40;
}
