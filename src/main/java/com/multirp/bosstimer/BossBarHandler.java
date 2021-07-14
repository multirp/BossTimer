package main.java.com.multirp.bosstimer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BossBarHandler {
	
	private static Plugin plugin = Main.getInstance();
	
	public BossBar getBar(Entity entity) {
		/*
		 * We collect the entity from the listener event that triggered it, and grab the bar from the HashMap
		 * if it exists.  If not, it *will* return a null value.  This is why we create the BossBar before
		 * spawning the entity.
		 */
		EntityType et = entity.getType();
		BossBar bar = Main.bars.get(et);
		
		return bar;
	}
	
	public void setBar(BossBar bar, World world, Entity entity) {
		/*
		 * When we go to set the BossBar, we get the players distances from the entities location, to determine
		 * whether or not to show the bar to them.  We then add the players to it, and put the bar back in
		 * the HashMap
		 */
		for (Player player : Bukkit.getOnlinePlayers()) {
			bar.removePlayer(player);
		}
		
		for (Player player : world.getPlayers()) {
			if (player.getLocation().distance(entity.getLocation()) < plugin.getConfig().getInt("bossbar-distance")) {
				bar.addPlayer(player);
			} else {bar.removePlayer(player);}
		}
		Main.bars.put(entity.getType(), bar);
	}
	
	public void damageBar(LivingEntity entity, Double damage, BossBar bar) {
		/*
		 * When an entity is damaged, we do two things.  The first is we grab the BossBar attached to that entity to
		 * update it, which we do with Math.Max (Health - Damage / Max Health), set the bar at that percentage, then
		 * we send the bar over to setBar.
		 * 
		 * This re-runs the players that the bar is attached to, removes all players from the list, and readds the ones
		 * that need it still, then placing the bar back in the HashMap
		 */
		double percent = Math.max(0, (entity.getHealth()-damage)/entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		bar.setProgress(percent);
		setBar(bar, entity.getLocation().getWorld(), entity);
	}
	
	public void resetBar(BossBar bar, EntityType entity) {
		/*
		 * This function is simple, when a mob with an attached BossBar is killed, it is ran.  It empties the player
		 * list, to prevent a player from getting it 'stuck' later, and then puts the bar back
		 */
		if(!(bar == null)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (bar.getPlayers().contains(player)) {
					bar.removePlayer(player);
				}
			}
		}
		Main.bars.put(entity, bar);
	}
	
	public initializeBossBars() {
		
		/*
		 * During plugin load, we'll go ahead and prime all of the bossbars that we need for the mobs that we need to handle.
		 * 
		 * This will make sure that on a system reboot, we already have all of the bossbars in place for the next startup, so that
		 * it won't skip making the bossbar.
		 * 
		 * This still means the bossbars won't be right with the HP, but I can work on that later.  Maybe write the bosses hp to config
		 * on shutdown.
		 * 
		 * Jesus what did I get into?
		 */
		if (!(Main.bars.containsKey(EntityType.ENDER_DRAGON))) {
			Main.bars.put(EntityType.ENDER_DRAGON,
					Bukkit.createBossBar("Ender Dragon", BarColor.PURPLE, BarStyle.SEGMENTED_10, BarFlag.PLAY_BOSS_MUSIC));
		}
	}
}
