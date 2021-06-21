package main.java.com.aspiriamc.bosstimer;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;


public class ListenerHandler implements Listener {
	
	private static Plugin plugin = Main.getInstance();
	
	BossBarHandler bb = new BossBarHandler();
	
	@EventHandler
	public void dragonSummon(EntitySpawnEvent event) {	
		/*
		 * Whenever an entity is spawned, we look at it and determine if it is
		 * an EnderDragon.  If it is, we prepare the BossBar we made for it,
		 * set the progress to full, then run setBar() to get the players, and
		 * make it visible.
		 * 
		 * I make it visible/hidden in the listener functions because it isn't
		 * all that important, and this way it runs less (potentially less
		 * flickering)
		 */
		if (event.getEntity() instanceof EnderDragon) {
			BossBar bar = bb.getBar(event.getEntity());
			bar.setProgress(1.0);
			bb.setBar(bar, event.getEntity().getWorld(), event.getEntity());
			bar.setVisible(true);
		}
	}
	
	@EventHandler	
	public void dragonDamage(EntityDamageEvent event) {
		/*
		 * As the dragon takes damage, we get the bar from the HashMap and pass
		 * it to the damageBar() routine to do the necessary calculations on it
		 */
		if (event.getEntity() instanceof EnderDragon) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			BossBar bar = Main.bars.get(entity.getType());
			bb.damageBar(entity, event.getDamage(), bar);
		}
	}
		
	@EventHandler	
	public void dragonDeath(EntityDeathEvent event) {
		
		/*
		 * The dragon's death is really simple, it just grabs the bar, and passes
		 * it to resetBar() to prepare for the next summon.  Then check against the 
		 * respawn-enable flag in the config, and if she's set to respawn, start the
		 * timer for it.
		 */
		if (event.getEntity() instanceof EnderDragon) {
			BossBar bar = Main.bars.get(event.getEntityType());
			bb.resetBar(bar, event.getEntityType());
		}
		
		int respawnTime = plugin.getConfig().getInt("enderdragon.respawn-timer")*60*20;
	
		if (plugin.getConfig().getString("enderdragon.respawn-enabled").equalsIgnoreCase("true")) {
			 Main.dragonTask = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					SpawnHandler sh = new SpawnHandler();
					sh.spawnEnderDragon();		
				}
			}, respawnTime);
		}
	}
}

