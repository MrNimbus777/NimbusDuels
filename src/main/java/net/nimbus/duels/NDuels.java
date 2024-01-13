package net.nimbus.duels;

import net.nimbus.duels.commands.completers.DuelCompleter;
import net.nimbus.duels.commands.executors.DuelExe;
import net.nimbus.duels.commands.executors.NduelExe;
import net.nimbus.duels.events.PlayerDeathEvents;
import net.nimbus.duels.events.PlayerQuitEvents;
import net.nimbus.duels.requests.Requests;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class NDuels extends JavaPlugin {
    public static NDuels a;

    public void loadConfig(boolean reload){
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            getLogger().info("Created new config.yml file at " + config.getPath());
        } else if (reload) {
            try {
                getConfig().load(config);
                getLogger().info("Config reloaded.");
            } catch (Exception exception) {
                getLogger().severe("Failed to reload config.");
            }
        }
    }
    public void onEnable() {
        NDuels.a = this;
        loadConfig(false);
        Vars.init();

        loadCommand("duel", new DuelExe(), new DuelCompleter());
        loadCommand("nduels", new NduelExe());
        loadEvent(new PlayerQuitEvents());
        loadEvent(new PlayerDeathEvents());
        MySQLUtils.connect();
        MySQLUtils.createTable(Vars.TABLE, "uuid", "uuid varchar(255) NOT NULL", "wins int", "losses int");
    }

    public void onDisable() {
        Requests.clear();
        MySQLUtils.close();
    }

    void loadCommand(String cmd, CommandExecutor executor){
        try {
            getCommand(cmd).setExecutor(executor);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    void loadCommand(String cmd, CommandExecutor executor, TabCompleter completer){
        try {
            loadCommand(cmd, executor);
            getCommand(cmd).setTabCompleter(completer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void loadEvent(Listener listener){
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
