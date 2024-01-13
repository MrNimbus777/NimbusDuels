package net.nimbus.duels.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 0) {
            return result;
        }
        if(args.length == 1) {
            List<String> options = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            for(String option : options){
                if(args[0].toLowerCase().startsWith(option.toLowerCase())) result.add(option);
            }
            return result;
        }
        return result;
    }
}
