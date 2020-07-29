package etiocook.ebolao;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class Main extends JavaPlugin {

    private CiberConfig configurations;
    private List<String> names;
    private BukkitTask scheduler;
    private Economy econ = null;

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    public List<String> getNames() {
        return names;
    }

    public boolean state = false;

    public CiberConfig getConfigurations() {
        return configurations;
    }

    public void setConfigurations(CiberConfig configurations) {
        this.configurations = configurations;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Economy getEconomy() {
        return econ;
    }

    @Override
    public void onEnable() {
        this.names = new LinkedList<>();
        setConfigurations(new CiberConfig(this, "configurations.yml"));
        configurations.saveDefaultConfig();

        autostart();
        setupEconomy();
    }

    @Override
    public void onDisable() {
    }

    public String getWinner() {

        int random = new Random().nextInt(names.size());
        return names.get(random);

    }

    public void checkTimer() {

        AtomicInteger counter = new AtomicInteger(getConfigurations().getInt("ad-quantity"));
        int cumulative_cost = names.size() != 0 ? names.size() * getConfigurations().getInt("cost") : 0;
        int ad_timer = configurations.getInt("ad-timer");
        this.scheduler = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {

            notice(counter, cumulative_cost);
            if (counter.decrementAndGet() == 0) {

                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage("§eWinner §f" + getWinner()));
                getEconomy().depositPlayer(getWinner(),cumulative_cost);

                scheduler.cancel();
                setState(false);
                if (names.size() <= 1) {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(
                            getConfigurations().getString("insuffient-players")));

                    getEconomy().depositPlayer(names.get(1), cumulative_cost);
                }

            }
        }, ad_timer, ad_timer);

    }

    public void autostart() {

        new BukkitRunnable() {
            @Override
            public void run() {

                checkTimer();
            }
        }.runTaskTimer(this, 600, 600);
    }

    private void notice(AtomicInteger counter, int cumulative_cost) {
        List<String> notice_start = configurations.getConfiguration().getStringList("notice-start");
        notice_start.forEach(
                msg -> Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
                        .replace("<ad-quantity>", "" + counter.get())
                        .replace("<amount-player>", "" + names.size())
                        .replace("<cumulative_cost>", "" + cumulative_cost)
                        .replace("<cost>", "" + getConfigurations().getInt("cost"))))
        );
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

/*    private void notice(String path,) {
        List<String> notice_start = configurations.getConfiguration().getStringList(path);
        notice_start.forEach(msg -> Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                msg.replace("<ad-quantity>" ,""+count))));
    }*/
}

