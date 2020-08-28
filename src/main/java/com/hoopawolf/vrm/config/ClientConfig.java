package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Client GUI")
public class ClientConfig implements ConfigData
{

    @Comment(value = "\nScreen Height Offset for sin mask warning text. Use [<] & [>] to change in game")
    @ConfigEntry.BoundedDiscrete(min = -500, max = 500)
    public int sinMaskWarningHeightOffset = 0;


    @Comment(value = "\nScreen Width Offset for sin mask warning text. Use [;] & ['] to change in game")
    @ConfigEntry.BoundedDiscrete(min = -500, max = 500)
    public int sinMaskWarningWidthOffset = 0;


    @Comment(value = "\nShow warning for Sin mask")
    public boolean sinMaskWarning = true;
}
