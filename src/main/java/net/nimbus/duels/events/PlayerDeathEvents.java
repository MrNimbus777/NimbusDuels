package net.nimbus.duels.events;

import net.nimbus.duels.MySQLUtils;
import net.nimbus.duels.requests.Request;
import net.nimbus.duels.requests.Requests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEvents implements Listener {
    @EventHandler
    public void onEvent(PlayerDeathEvent e) {
        Player killed = e.getEntity();
        Request req = Requests.getActiveBySender(killed.getUniqueId());
        if(req != null){
            MySQLUtils.inc(killed, false);
            MySQLUtils.inc(killed.getUniqueId().equals(req.getSender()) ? req.getSender() : req.getReceiver(), true);
            req.end();
            return;
        }
        req = Requests.getActiveByReceiver(killed.getUniqueId());
        if(req != null) {
            MySQLUtils.inc(killed, false);
            MySQLUtils.inc(killed.getUniqueId().equals(req.getSender()) ? req.getSender() : req.getReceiver(), true);
            req.end();
        }
    }
}
