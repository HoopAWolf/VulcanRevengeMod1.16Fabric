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
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int minStructureAway = 7;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nMax distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int maxStructureAway = 12;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nSword Structure spawning percentage")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int structureSpawnChance = 40;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nMin distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int minSinStructureAway = 7;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nMax distance away from chosen spot")
    @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
    public int maxSinStructureAway = 12;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nSin Structure spawning percentage")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int structureSinSpawnChance = 40;
}
