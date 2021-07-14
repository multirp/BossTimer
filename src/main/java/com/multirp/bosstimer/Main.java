package main.java.com.multirp.bosstimer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin {
	
	public static Main instance;
	public static Location dragonLocation;
	public static Map<EntityType, BossBar> bars;
	private static FileConfiguration cfg;
	File cFile;
	
	public static BukkitTask dragonTask;

	public void onEnable() {
		/*
		 * onEnable creates an instance of the Main class (for the configuration file), and
		 * creates the HashMap for the bars that it needs to hold.  Then it creates the config
		 * files necessary, and sets up the commands and listeners.
		 * 
		 * Pretty basic, really
		 */
		Main.instance = this;
		bars = new HashMap<EntityType, BossBar>();
		
		createConfig();
		
		BossBarHandler bb = new BossBarHandler();
		bb.initializeBossBars();

		getServer().getPluginManager().registerEvents(new ListenerHandler(), this);
		getCommand("bosstimer").setExecutor(new CommandHandler());
		getCommand("bosstimer").setTabCompleter(new TabHandler());
	}

	public void onDisable() {
		/*
		 * When the plugin disables, we're going to remove all players from all bossbars (if applicable), null them out,
		 * then null out the instance
		 */
		BossBarHandler bb = new BossBarHandler();
		for (Map.Entry entry : bars.entrySet()) {
			bb.resetBar(entry.getValue(), entry.getKey());
		}

		Main.instance = null;

	}
	
	private void createConfig() {
		// Creates the configuration file if it doesn't exist
		cfg = getConfig();
		cfg.options().copyDefaults();
		saveDefaultConfig();
		cFile = new File(getDataFolder(), "config.yml");
	}
	
	public static Main getInstance() {
		return instance;
	}

}
