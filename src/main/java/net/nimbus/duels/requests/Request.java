package net.nimbus.duels.requests;

import net.nimbus.duels.NDuels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Request {
    final UUID sender;
    final UUID receiver;

    public Request(UUID sender, UUID receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

    public UUID getReceiver() {
        return receiver;
    }
    public UUID getSender() {
        return sender;
    }

    public void run(){
        Player receiver = Bukkit.getPlayer(getReceiver());
        String name = Bukkit.getOfflinePlayer(getSender()).getName();
        if(receiver != null) receiver.sendMessage("You got a duel from " + name + ". Type /duel " + name);
        new BukkitRunnable(){
            @Override
            public void run() {
                Requests.unregister(Request.this);
            }
        }.runTaskLater(NDuels.a, 1200);
    }
}
