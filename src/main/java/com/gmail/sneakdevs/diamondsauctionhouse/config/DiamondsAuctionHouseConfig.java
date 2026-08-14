package com.gmail.sneakdevs.diamondsauctionhouse.config;

import com.gmail.sneakdevs.diamondsauctionhouse.DiamondsAuctionHouse;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.permission.v1.PermissionContext;

@Config(name = DiamondsAuctionHouse.MODID)
public class DiamondsAuctionHouseConfig implements ConfigData {
    @Comment("Name of the command to open the auction house GUI")
    public String auctionHouseCommandName = "auc";
    @Comment("Name of the sub-command to put something up for auction")
    public String auctionCommandName = "auc";
    @Comment("Use the base diamond economy command")
    public boolean useBaseCommand = false;
    @Comment("Maximum items a player can have up for auction (expired included)")
    public int maxPlayerItems = 5;
    @Comment("Maximum number of pages the auction house can have")
    public int maxPages = 8;
    @Comment("Seconds the item is put on the auction for")
    public int auctionSeconds = 259200;

    public static DiamondsAuctionHouseConfig getInstance() {
        return AutoConfig.getConfigHolder(DiamondsAuctionHouseConfig.class).getConfig();
    }

    public static int getPlayerMaxItems(ServerPlayer player) {
        PermissionContext context = player.getPermissionContext();
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":infiniteitems")).get()) {
            return -1;
        }
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":noitems")).get()) {
            return 0;
        }
        int items = getInstance().maxPlayerItems;
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":quintupleitems")).get()) {
            return items * 5;
        }
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":quadrupleitems")).get()) {
            return items * 4;
        }
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":tripleitems")).get()) {
            return items * 3;
        }
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":doubleitems")).get()) {
            return items * 2;
        }
        if (context.checkPermission(Identifier.parse(DiamondsAuctionHouse.MODID + ":halfitems")).get()) {
            return items / 2;
        }
        return items;
    }
}