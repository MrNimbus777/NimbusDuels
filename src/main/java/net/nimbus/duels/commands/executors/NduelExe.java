package net.nimbus.duels.commands.executors;

import com.google.errorprone.annotations.Var;
import net.nimbus.duels.NDuels;
import net.nimbus.duels.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NduelExe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player p) if(!p.hasPermission("nduel.admin")){
            sender.sendMessage("No permission");
            return true;
        }
        if(args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload" : {
                    NDuels.a.loadConfig(true);
                    Vars.init();
                    return true;
                }
                case "setkit" : {
                    if(!(sender instanceof Player p)) return true;
                    for(int i = 0; i < 41; i++){
                        NDuels.a.getConfig().set("kit.item_"+i, p.getInventory().getContents()[i].clone());
                    }
                    NDuels.a.saveConfig();
                    Vars.initKit();
                }
            }
        }
        return true;
    }
}
