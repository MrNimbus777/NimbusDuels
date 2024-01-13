package net.nimbus.duels.requests;

import java.util.*;

public class Requests {
    static Map<UUID, Request> map = new HashMap<>();

    public static void clear(){
        for(Request request : getAll()){
            request.end();
        }
        Requests.map = new HashMap<>();
    }
    public static Request get(UUID sender){
        return Requests.map.getOrDefault(sender, null);
    }

    public static Request getActiveBySender(UUID sender){
        if(get(sender) == null) return null;
        return get(sender).isActive() ? get(sender) : null;
    }
    public static Request getActiveByReceiver(UUID receiver){
        for(Request request : getAll()){
            if(request.getReceiver().equals(receiver)) if(request.isActive()) return request;
        }
        return null;
    }

    public static List<Request> getAll(){
        return new ArrayList<>(Requests.map.values());
    }

    public static void register(Request request){
        Requests.map.put(request.getSender(), request);
    }
    public static void unregister(Request request){
        Requests.map.remove(request.getSender());
    }
}
