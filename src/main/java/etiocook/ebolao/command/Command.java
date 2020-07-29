package etiocook.ebolao.command;

import etiocook.ebolao.CiberConfig;
import etiocook.ebolao.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    final Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Economy economy = main.getEconomy();
        CiberConfig configurations = main.getConfigurations();
        if (args.length == 0) {

            if (!main.getNames().contains(player.getName())) {
                if (!main.isState()) {
                    player.sendMessage(main.colorize(configurations.getString("closed")));
                    return false;
                }

                if (!economy.has(player, configurations.getInt("cost"))) {
                    player.sendMessage(main.colorize(configurations.getString("insufficient-money")));
                    return false;
                }

                main.getNames().add(player.getName());
                economy.withdrawPlayer(player, configurations.getInt("cost"));
                player.sendMessage(main.colorize(configurations.getString("participating")));
                return false;
            }
            player.sendMessage(main.colorize(configurations.getString("already-participating")));
        }

        if (args.length == 1 && player.hasPermission("bolao.admin")) {
            if ("start".equalsIgnoreCase(args[0]) || "iniciar".equalsIgnoreCase(args[0])) {

                if (main.isState()) {
                    player.sendMessage(main.colorize("&fevent is already started"));
                    return false;
                }

                main.setState(true);
                main.checkTimer();

            }

        }
        return false;
    }
}
