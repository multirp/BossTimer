package main.java.com.aspiriamc.bosstimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor {
	
	SpawnHandler sh = new SpawnHandler();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Bukkit.getLogger().info("[DragonTimer]: This command cannot be used at the console");
		} else {
			Player player = (Player) sender;
			
			switch(args[0]) {
			case "admin":
				sh.mainSpawnSelector(player, args);
				break;
			case "help":
			default:
				player.sendMessage(ChatColor.YELLOW + "Welcome to BossTimer, where we time your bosses! o.o");
				player.sendMessage(ChatColor.GREEN + "  /bt admin start <boss>" + ChatColor.YELLOW + ": Spawn that boss and start the " +
						"schedule");
				player.sendMessage(ChatColor.GREEN + "  /bt admin stop <boss>" + ChatColor.YELLOW + ": Stop a boss schedule");
			}
		}
		
		return false;
	}
	
}
