package me.blueslime.slimerepair.economy;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;

import org.bukkit.OfflinePlayer;

public interface EconomyProvider extends AdvancedModule {

    /**
     * Checks if the player account has the amount
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(OfflinePlayer player, double amount);

    /**
     * Withdraw an amount from a player
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     */
    void withdrawPlayer(OfflinePlayer player, double amount);

    /**
     * Deposit an amount to a player
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     */
    void depositPlayer(OfflinePlayer player, double amount);

}
