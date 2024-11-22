package me.blueslime.slimerepair.economy.providers;

import me.blueslime.slimerepair.economy.EconomyProvider;
import org.bukkit.OfflinePlayer;

public class NoneProvider implements EconomyProvider {
    /**
     * Checks if the player account has the amount
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return true;
    }

    /**
     * Withdraw an amount from a player
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     */
    @Override
    public void withdrawPlayer(OfflinePlayer player, double amount) {

    }

    /**
     * Deposit an amount to a player
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     */
    @Override
    public void depositPlayer(OfflinePlayer player, double amount) {

    }
}
