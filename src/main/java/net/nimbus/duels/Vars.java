package net.nimbus.duels;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Vars {
    public static Location LOCATION_FIRST;
    public static Location LOCATION_SECOND;
    public static ItemStack[] KIT;
    static Map<UUID, ItemStack[]> contentsMap;
    static String TABLE;



    public static void init(){
        String[] var = NDuels.a.getConfig().getString("Locations.first", null).split(",");
        LOCATION_FIRST = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]));
        var = NDuels.a.getConfig().getString("Locations.second", null).split(",");
        LOCATION_SECOND = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]));
        contentsMap = new HashMap<>();
        TABLE = "nduels_players";
        initKit();
    }

    public static void initKit() {
        KIT = new ItemStack[41];
        ConfigurationSection section = NDuels.a.getConfig().getConfigurationSection("kit");
        if(section != null) {
            for (int i = 0; i < 41; i++) {
                ItemStack item = section.getItemStack("item_"+i, null);
                if(item == null) {
                    KIT[i] = null;
                } else KIT[i] = item.clone();
            }
        }
    }

    public static ItemStack[] getPlayerContents(Player p){
        return contentsMap.getOrDefault(p.getUniqueId(), new ItemStack[41]);
    }
    public static void putPlayerContents(Player p){
        contentsMap.put(p.getUniqueId(), p.getInventory().getContents());
    }
    public static void removePlayerContents(Player p){
        contentsMap.remove(p.getUniqueId());
    }
}
