package net.nimbus.duels;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public String getString(String table, String key_column, String key_value, String column) {
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

    public List<String> getColumnAsList(String table, String key_column, String key_value, String column) {
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

    public List<String> getColumnAsList(String table, String column) {
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


    public void set(String table, String key_column, String key_value, String column, String value) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            st.execute("INSERT INTO " + table + "(" + key_column + ", " + column + ") VALUES ('" + key_value + "', '" + value + "') ON DUPLICATE KEY UPDATE " + column + " = '" + value + "'");
            st.close();
        } catch (SQLException ignored) {
        }
    }

    public boolean exists(String table, String key_column, String key_value) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            return statement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
    public void remove(String table, String key_column, String key_value) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM " + table);
            st.execute("DELETE FROM " + table + " WHERE " + key_column + " = '" + key_value + "'");
            st.close();
        } catch (SQLException ignored) {}
    }
}