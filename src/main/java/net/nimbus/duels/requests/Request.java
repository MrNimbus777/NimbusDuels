package net.nimbus.duels.requests;

import net.nimbus.duels.NDuels;
import net.nimbus.duels.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Request {
    final UUID sender;
    final UUID receiver;
    BukkitRunnable runnable;

    public Request(UUID sender, UUID receiver){
        this.sender = sender;
        this.receiver = receiver;
        runnable = new BukkitRunnable(){
            @Override
            public void run() {
                Requests.unregister(Request.this);
            }
        };
    }

    public UUID getReceiver() {
        return receiver;
    }
    public UUID getSender() {
        return sender;
    }
    public void accept(){
        if(isActive()) return;
        Player p = Bukkit.getPlayer(getSender());
        p.teleport(Vars.LOCATION_FIRST);
        Vars.putPlayerContents(p);
        p.getInventory().setContents(Vars.KIT);

        p = Bukkit.getPlayer(getReceiver());
        p.teleport(Vars.LOCATION_SECOND);
        Vars.putPlayerContents(p);
        p.getInventory().setContents(Vars.KIT);
        runnable.cancel();
    }
    public void end(){
        runnable.cancel();

        Player p = Bukkit.getPlayer(getSender());
        p.teleport(p.getWorld().getSpawnLocation());
        p.getInventory().setContents(Vars.getPlayerContents(p));
        Vars.removePlayerContents(p);

        p = Bukkit.getPlayer(getReceiver());
        p.teleport(p.getWorld().getSpawnLocation());
        p.getInventory().setContents(Vars.getPlayerContents(p));
        Vars.removePlayerContents(p);

        Requests.unregister(this);
    }
    public boolean isActive() {
        return runnable.isCancelled();
    }
    public void run(){
        Player receiver = Bukkit.getPlayer(getReceiver());
        String name = Bukkit.getOfflinePlayer(getSender()).getName();
        if(receiver != null) receiver.sendMessage("You got a duel from " + name + ". Type /duel " + name);
        runnable.runTaskLater(NDuels.a, 1200);
    }
}
