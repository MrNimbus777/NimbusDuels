package net.nimbus.duels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MySQLUtils {

    private static Connection connection;

    public static void connect(){
        try {
            String address, database, username, password;
            address = NDuels.a.getConfig().getString("MySQL.address");
            database = NDuels.a.getConfig().getString("MySQL.database");
            username = NDuels.a.getConfig().getString("MySQL.username");
            password = NDuels.a.getConfig().getString("MySQL.password");

            connection = DriverManager.getConnection("jdbc:mysql://" +
                    address + "/" +
                    database + "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false",
                    username,
                    password);
        } catch (Exception e){
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

    public static void close(){
        if(connection == null) return;
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String table, String primary_key, String... columns){
        try{
            String str = Arrays.stream(columns).collect(Collectors.joining(", "));
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                    table + " (" +
                    str + ", PRIMARY KEY (" +
                    primary_key +"));");
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void inc(Player p, boolean win) {
        inc(p.getUniqueId(), win);
    }

    public static void inc(UUID uuid, boolean win) {
        inc(uuid.toString(), win);
    }

    public static void inc(String uuid_string, boolean win) {
        String column = win ? "wins" : "loses";
        set(Vars.TABLE, "uuid", uuid_string, column, ((int)getNum(Vars.TABLE, "uuid", uuid_string, column)+1)+"");
    }
    public static String getString(String table, String key_column, String key_value, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString(column);
            }
            return "";
        } catch (SQLException e) {
            return "";
        }
    }

    public static double getNum(String table, String key_column, String key_value, String column){
        return getNum(table, key_column, key_value, column, 0);
    }
    public static double getNum(String table, String key_column, String key_value, String column, double def){
        String val = getString(table, key_column, key_value, column);
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return def;
        }
    }

    public static List<String> getColumnAsList(String table, String key_column, String key_value, String column) {
        ArrayList<String> keys = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                keys.add(rs.getString(column));
            }
            return keys;
        } catch (SQLException e) {
            return keys;
        }
    }

    public static List<String> getColumnAsList(String table, String column) {
        ArrayList<String> keys = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                keys.add(rs.getString(column));
            }
            return keys;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }


    public static void set(String table, String key_column, String key_value, String column, String value) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            st.execute("INSERT INTO " + table + "(" + key_column + ", " + column + ") VALUES ('" + key_value + "', '" + value + "') ON DUPLICATE KEY UPDATE " + column + " = '" + value + "'");
            st.close();
        } catch (SQLException ignored) {
        }
    }

    public static boolean exists(String table, String key_column, String key_value) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            return statement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
    public static void remove(String table, String key_column, String key_value) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM " + table);
            st.execute("DELETE FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            st.close();
        } catch (SQLException ignored) {}
    }
}