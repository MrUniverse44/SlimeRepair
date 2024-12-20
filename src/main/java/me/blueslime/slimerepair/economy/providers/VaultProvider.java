package me.blueslime.slimerepair.economy.providers;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.slimerepair.SlimeRepair;
import me.blueslime.slimerepair.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultProvider implements EconomyProvider, AdvancedModule {

    private final Economy economy;

    public VaultProvider() {
        RegisteredServiceProvider<Economy> serviceProvider = fetch(SlimeRepair.class)
                .getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (serviceProvider == null) {
            this.economy = null;
            return;
        }

        this.economy = serviceProvider.getProvider();
    }

    /**
     * Checks if the player account has the amount
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        if (economy == null) {
            return false;
        }
        boolean result = economy.has(player, amount);
        fetch(MeteorLogger.class).debug("Balance debug: check if player " + player.getName() + " has economy of " + amount + " is: " + result);
        return result;
    }

    /**
     * Withdraw an amount from a player
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     */
    @Override
    public void withdrawPlayer(OfflinePlayer player, double amount) {
        if (economy == null) {
            return;
        }
        fetch(MeteorLogger.class).debug("Balance debug: withdraw for " + player.getName() + " of " + amount + ".");
        economy.withdrawPlayer(player, amount);
    }

    /**
     * Deposit an amount to a player
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     */
    @Override
    public void depositPlayer(OfflinePlayer player, double amount) {
        if (economy == null) {
            return;
        }
        fetch(MeteorLogger.class).debug("Balance debug: deposit for " + player.getName() + " of " + amount + ".");
        economy.depositPlayer(player, amount);
    }
}
