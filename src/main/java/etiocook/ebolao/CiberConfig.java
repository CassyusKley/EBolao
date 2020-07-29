package etiocook.ebolao;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class CiberConfig {

    private File file;
    private Plugin plugin;
    private String name;
    private YamlConfiguration configuration;

    public CiberConfig(Plugin plugin, String name) {
        this.plugin = plugin;
        setName(name);
        reloadConfig();
    }

    public void reloadConfig() {
        file = new File(getPlugin().getDataFolder(),getName());
        configuration = YamlConfiguration.loadConfiguration(getFile());
    }

    public void saveConfig() {
        try {
            getConfiguration().save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        getConfiguration().options().copyDefaults(true);
    }
    public void saveDefaultConfig() {
        getPlugin().saveResource(getName(), false);
    }

    public void deleteConfig() {
        getFile().delete();
    }
    public boolean existeConfig() {
        return getFile().exists();
    }

    public String getString(String path) {
        return getConfiguration().getString(path);
    }

    public int getInt(String path) {
        return getConfiguration().getInt(path);
    }

    public boolean getBoolean(String path) {
        return getConfiguration().getBoolean(path);
    }

    public double getDouble(String path) {
        return getConfiguration().getDouble(path);
    }

    public List<?> getList(String path){
        return getConfiguration().getList(path);
    }
    public boolean contains(String path) {
        return getConfiguration().contains(path);
    }

    public void set(String path, Object value) {
        getConfiguration().set(path, value);
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(YamlConfiguration configuration) {
        this.configuration = configuration;
    }
}
