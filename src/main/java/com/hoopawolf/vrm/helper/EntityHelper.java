package com.hoopawolf.vrm.helper;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;


public class EntityHelper
{
    public static double getAngleBetweenEntities(Entity first, Entity second)
    {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * (180 / Math.PI) + 90;
    }

    public static void sendCoolDownMessage(PlayerEntity entityIn, int cooldownIn)
    {
        String message = "";

        if (cooldownIn > 0)
        {
            message = "message.vrm.cooldown";
        } else
        {
            message = "message.vrm.error";
        }

        entityIn.sendMessage(new TranslatableText(I18n.translate(message) + ((cooldownIn > 0) ? " " + (cooldownIn / 20) + "s" : "")).setStyle(Style.EMPTY.withFormatting(Formatting.RED)), true);
    }

    public static void sendMessage(PlayerEntity entityIn, String messageID, Formatting colorIn)
    {
        String message = "message.vrm." + messageID;

        entityIn.sendMessage(new TranslatableText(I18n.translate(message)).setStyle(Style.EMPTY.withFormatting(colorIn)), true);
    }

    public static List<PlayerEntity> getPlayersNearby(Entity ent, double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> nearbyEntities = ent.world.getEntities(ent, ent.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        return nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof PlayerEntity && ent.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (PlayerEntity) entityNeighbor).collect(Collectors.toList());
    }

    public static List<LivingEntity> getEntityLivingBaseNearby(Entity ent, double distanceX, double distanceY, double distanceZ, double radius)
    {
        return getEntitiesNearby(ent, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity ent, Class<T> entityClass, double r)
    {
        return ent.world.getEntities(entityClass, ent.getBoundingBox().expand(r, r, r), e -> e != ent && ent.distanceTo(e) <= r);
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity ent, Class<T> entityClass, double dX, double dY, double dZ, double r)
    {
        return ent.world.getEntities(entityClass, ent.getBoundingBox().expand(dX, dY, dZ), e -> e != ent && ent.distanceTo(e) <= r && e.getY() <= ent.getY() + dY);
    }

    public static <T extends Entity> List<T> getEntitiesNearbyWithPos(World world, Box box, BlockPos pos, Class<T> entityClass, double dX, double dY, double dZ, double r)
    {
        return world.getEntities(entityClass, box.expand(dX, dY, dZ), e -> pos.isWithinDistance(e.getPos(), r) && e.getY() <= pos.getY() + dY);
    }
}