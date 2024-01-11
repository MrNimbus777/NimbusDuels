package net.nimbus.duels;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Vars {
    public static Location LOCATION_FIRST;
    public static Location LOCATION_SECOND;
    static Map<UUID, ItemStack[]> contentsMap;


    public static void init(){
        String[] var = NDuels.a.getConfig().getString("Locations.first", null).split(",");
        LOCATION_FIRST = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]));
        var = NDuels.a.getConfig().getString("Locations.second", null).split(",");
        LOCATION_SECOND = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]));
        contentsMap = new HashMap<>();
    }

    public static ItemStack[] getPlayerContents(Player p){
        return contentsMap.getOrDefault(p.getUniqueId(), new ItemStack[41]);
    }
    public static void putPlayerContents(Player p, ItemStack[] items){
        contentsMap.put(p.getUniqueId(), items);
    }
    public static void removePlayerContents(Player p){
        contentsMap.remove(p.getUniqueId());
    }
}
