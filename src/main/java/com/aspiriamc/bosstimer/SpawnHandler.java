package main.java.com.aspiriamc.bosstimer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class SpawnHandler {
	
	BossBarHandler bb = new BossBarHandler();
	BossBar bar;
	
	private static Plugin plugin = Main.getInstance();
	
	public void mainSpawnSelector(Player player, String[] args) {
		/*
		 * This is the second part of the main menu system.  Right now, it only houses the ender dragon but can house every
		 * other boss we make as well.  When the 'stop' command is issued, it sets the respawn-enabled boolean to false,
		 * which will stop the listener from activating the next spawn.
		 * 
		 * When we start a spawn, it will immediately spawn that boss and set the respawn-enabled boolean to true.  This
		 * happens per boss in the config file, so we can start/stop individual bosses without stopping the whole thing.
		 * 
		 * I may need to find a better home for this at some point... -.-;
		 */
		switch (args[1]) {
		case "start":
			switch(args[2]) {
			case "enderdragon":
				player.sendMessage(ChatColor.GREEN + "[BT] The Ender Dragon's schedule has been enabled");
				plugin.getConfig().set("enderdragon.respawn-enabled", true);
				plugin.saveConfig();
				spawnEnderDragon();
				break;
			default:
				player.sendMessage(ChatColor.RED + "[BT] You must enter a boss that you want to schedule!");
			}
			break;
		case "stop":
			switch(args[2]) {
			case "enderdragon":
				player.sendMessage(ChatColor.GREEN + "[BT] The Ender Dragon's schedule has been disabled");
				plugin.getConfig().set("enderdragon.respawn-enabled", false);
				plugin.saveConfig();
				bar = Main.bars.get(EntityType.ENDER_DRAGON);
				bb.resetBar(bar, EntityType.ENDER_DRAGON);
				if (!(Main.dragonTask == null)) {
					Bukkit.getScheduler().cancelTask(Main.dragonTask.getTaskId());}
				break;
			default:
				player.sendMessage(ChatColor.RED + "[BT] You must enter a boss that you want to unschedule!");
			}
			break;			
		default:
			player.sendMessage(ChatColor.RED + "Invalid command, please run the help for assistance: " + ChatColor.UNDERLINE + "/bt help");
		}
	}

	public void spawnEnderDragon() {
		/*  
		 * We're going to spawn an Ender Dragon. To do this, we want the fight to be
		 * as 'realistic' as possible without using the normal spawn mechanics.
		 * This will involved a bit of flair (though not much for now).
		 * 
		 * We loop through the configuration in order to replace the end crystals, and
		 * call lightning down on them.  Once I figure out how to time that out better
		 * we can get fancier, but for now this will work. 
		 * 
		 * Most other bosses, honestly, will probably be very simple by comparison, but
		 * the Ender Dragon requires some... setup.
		 */		
		World endWorld = Bukkit.getWorld(plugin.getConfig().getString("end-world"));
		for (String l : plugin.getConfig().getConfigurationSection("enderdragon.end-crystal-locations").getKeys(false)) {
			Location location = new Location(endWorld,
					plugin.getConfig().getDouble("enderdragon.end-crystal-locations." + l + ".x"),
					plugin.getConfig().getDouble("enderdragon.end-crystal-locations." + l + ".y"),
					plugin.getConfig().getDouble("enderdragon.end-crystal-locations." + l + ".z"));
			
			for (Entity entity : location.getWorld().getNearbyEntities(location, 2, 2, 2)) {
				if (entity.getType() == EntityType.ENDER_CRYSTAL) {
					entity.remove();
				}
			}
			
			location.getWorld().strikeLightningEffect(location);
			location.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);
		}

		/*
		 * Now that the end crystals are in place, we can create the BossBar for the
		 * Ender Dragon. We store it in a HashMap in Main for portability with the
		 * Namespaced Key of the entity type (Ender_Dragon).  This way we can have one
		 * BossBar per enemy type.
		 */
		if (!(Main.bars.containsKey(EntityType.ENDER_DRAGON))) {
			Main.bars.put(EntityType.ENDER_DRAGON,
					Bukkit.createBossBar("Ender Dragon", BarColor.PURPLE, BarStyle.SEGMENTED_10, BarFlag.PLAY_BOSS_MUSIC));
		}
		
		/*
		 * This is basic, we're just getting the Ender Dragon location from the config
		 * and spawning her.  She will show up without combat metadata, but will gain
		 * some when she takes damage and go into an actual DragonPhase 0.
		 * 
		 * If use-message is true, we'll send whatever the config has for dragon-use-message
		 * to chat.
		 */
		
		Location dragonLoc = new Location(endWorld,
				plugin.getConfig().getDouble("enderdragon.dragon-spawn-location.x"),
				plugin.getConfig().getDouble("enderdragon.dragon-spawn-location.y"),
				plugin.getConfig().getDouble("enderdragon.dragon-spawn-location.z"));
		
		EnderDragon dragon = (EnderDragon) dragonLoc.getWorld().spawnEntity(dragonLoc, EntityType.ENDER_DRAGON);
		dragon.setPhase(Phase.CIRCLING);
		
		if (plugin.getConfig().getString("use-messages") == "true") {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("enderdragon.messages.spawn-message"));
			}
		}
	}
	
}
