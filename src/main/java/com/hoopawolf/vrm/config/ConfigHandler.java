package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "vrm-main")
public final class ConfigHandler implements ConfigData
{
    @ConfigEntry.Category("clientgui")
    @ConfigEntry.Gui.TransitiveObject
    public ClientConfig clientconfig = new ClientConfig();

    @ConfigEntry.Category("items")
    @ConfigEntry.Gui.TransitiveObject
    public ItemConfig itemconfig = new ItemConfig();

    @ConfigEntry.Category("structures")
    @ConfigEntry.Gui.TransitiveObject
    public StructureConfig structureconfig = new StructureConfig();
}