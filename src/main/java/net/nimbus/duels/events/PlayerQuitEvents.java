package net.nimbus.duels.events;

import net.nimbus.duels.MySQLUtils;
import net.nimbus.duels.requests.Request;
import net.nimbus.duels.requests.Requests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEvents implements Listener {
    @EventHandler
    public void onEvent(PlayerQuitEvent e){
        Player quited = e.getPlayer();
        Request req = Requests.getActiveBySender(quited.getUniqueId());
        if(req != null){
            MySQLUtils.inc(quited, false);
            MySQLUtils.inc(quited.getUniqueId().equals(req.getSender()) ? req.getSender() : req.getReceiver(), true);
            req.end();
            return;
        }
        req = Requests.getActiveByReceiver(quited.getUniqueId());
        if(req != null) {
            MySQLUtils.inc(quited, false);
            MySQLUtils.inc(quited.getUniqueId().equals(req.getSender()) ? req.getSender() : req.getReceiver(), true);
            req.end();
        }
    }
}
