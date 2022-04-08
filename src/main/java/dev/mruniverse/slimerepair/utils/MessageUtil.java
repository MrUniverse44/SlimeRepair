package dev.mruniverse.slimerepair.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {
    
    public static void sendMessage(Player player, String message) {
        player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }



}
