package me.theparanker.duel.utils;

import me.theparanker.duel.CoralDuel;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MessagesManager {
    
    private static FileConfiguration messagesConfig;
    
    public static void init() {
        messagesConfig = CoralDuel.get().getMessagesFile();
    }
    
    public static String getMessage(String path) {
        String message = messagesConfig.getString(path, "&cMessage not found: " + path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String getMessage(String path, String placeholder, String value) {
        String message = getMessage(path);
        return message.replace("{" + placeholder + "}", value);
    }
    
    public static String getMessage(String path, String placeholder1, String value1, String placeholder2, String value2) {
        String message = getMessage(path);
        return message.replace("{" + placeholder1 + "}", value1)
                     .replace("{" + placeholder2 + "}", value2);
    }
}
