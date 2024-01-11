package net.nimbus.duels.requests;

import java.util.*;

public class Requests {
    static Map<UUID, Request> map = new HashMap<>();

    public static void clear(){
        Requests.map = new HashMap<>();
    }
    public static Request get(UUID sender){
        return Requests.map.getOrDefault(sender, null);
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
