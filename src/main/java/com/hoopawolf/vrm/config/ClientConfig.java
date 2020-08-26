package com.hoopawolf.vrm.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Client GUI")
public class ClientConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip(count = 3)
    @Comment(value = "\nScreen Height Offset for sin mask warning text. Use [<] & [>] to change in game")
    @ConfigEntry.BoundedDiscrete(min = -Integer.MAX_VALUE, max = Integer.MAX_VALUE)
    public int sinMaskWarningHeightOffset = 0;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nScreen Width Offset for sin mask warning text. Use [;] & ['] to change in game")
    @ConfigEntry.BoundedDiscrete(min = -Integer.MAX_VALUE, max = Integer.MAX_VALUE)
    public int sinMaskWarningWidthOffset = 0;

    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nShow warning for Sin mask")
    public boolean sinMaskWarning = true;
}
