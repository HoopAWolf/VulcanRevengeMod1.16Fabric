package com.hoopawolf.vrm.data;

public class EatItemData
{
    String itemID;
    int duration,
            amplifier,
            foodAmount;
    String[] listOfEffects;

    public String getItemID()
    {
        return itemID;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getAmplifier()
    {
        return amplifier;
    }

    public int getFoodAmount()
    {
        return foodAmount;
    }

    public String[] getListOfEffects()
    {
        return listOfEffects;
    }
}
