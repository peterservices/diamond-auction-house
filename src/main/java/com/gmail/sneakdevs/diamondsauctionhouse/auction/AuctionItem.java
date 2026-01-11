package com.gmail.sneakdevs.diamondsauctionhouse.auction;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;


public class AuctionItem {
    private final ItemStack itemStack;
    private final String uuid;
    private final String owner;
    private final String tag;
    private final int price;
    private final int id;
    private int secondsLeft;

    public AuctionItem(MinecraftServer server, int id, String playerUuid, String owner, ItemStack stack, int price, int secondsLeft) {
        this.id = id;
        this.itemStack = stack;
        this.uuid = playerUuid;
        this.owner = owner;
        DataComponentMap components = itemStack.getComponents();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        this.tag = DataComponentMap.CODEC.encodeStart(registryOps, components).getOrThrow().toString();
        this.price = price;
        this.secondsLeft = secondsLeft;
    }

    public AuctionItem(MinecraftServer server, int id, String playerUuid, String owner, String tag, String item, int count, int price, int secondsLeft) {
        ItemStack itemStack1;
        this.id = id;
        itemStack1 = new ItemStack(BuiltInRegistries.ITEM.get(Identifier.tryParse(item)).get().value(), count);
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        DataComponentMap components = DataComponentMap.CODEC.parse(registryOps, JsonParser.parseString(tag)).result().orElse(DataComponentMap.EMPTY);
        itemStack1.applyComponents(components);
        this.itemStack = itemStack1;
        this.tag = tag;
        this.uuid = playerUuid;
        this.owner = owner;
        this.price = price;
        this.secondsLeft = secondsLeft;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return String.valueOf(BuiltInRegistries.ITEM.getKey(itemStack.getItem()));
    }

    public int getPrice() {
        return price;
    }

    public String getTimeLeft() {
        int seconds = secondsLeft;
        int days = seconds / 86400;
        seconds -= days * 86400;
        int hours = seconds / 3600;
        seconds -= hours * 3600;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        if (days > 0) {
            return String.format("%02d:%02d:%02d" + "m", days, hours, minutes);
        } else {
            if (hours > 0) {
                return String.format("%02d:%02d:%02d" + "s", hours, minutes, seconds);
            }
        }
        return (minutes > 0) ? String.format("%02d:%02d" + "s", minutes, seconds) : (seconds + "s");
    }

    public boolean tickDeath() {
        if (secondsLeft > 0) {
            secondsLeft--;
        }
        return secondsLeft == 0;
    }
}
