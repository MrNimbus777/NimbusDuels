package net.nimbus.duels.commands.executors;

import net.nimbus.duels.requests.Request;
import net.nimbus.duels.requests.Requests;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelExe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player p)) return true;
        if(args.length == 0) return true;
        Request request = Requests.get(p.getUniqueId());
        if(request != null) {
            p.sendMessage("You have already sent a duel request!");
            return true;
        }
        Player receiver = Bukkit.getPlayer(args[0]);
        if(receiver == null) {
            p.sendMessage("Player not found");
            return true;
        }
        request = Requests.get(receiver.getUniqueId());
        if(request == null) {
            request = new Request(p.getUniqueId(), receiver.getUniqueId());
            Requests.register(request);
            request.run();
            p.sendMessage("You have send a duel request to player " + receiver.getName());
        } else {
            if(request.getReceiver().equals(receiver.getUniqueId())) request.accept();
            else {
                request = new Request(p.getUniqueId(), receiver.getUniqueId());
                Requests.register(request);
                request.run();
                p.sendMessage("You have send a duel request to player " + receiver.getName());
            }
        }
        return true;
    }
}
