package main.java.com.multirp.bosstimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor {
	
	SpawnHandler sh = new SpawnHandler();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		/*
		 * I'm trying something a bit different with command handling.  Because of the issues with
		 * array index out of bounds errors, I want a better way to handle it rather than constant
		 * comparisons, preferably in a nice, clean fashion.
		 * 
		 * So what I'm doing is this is the 'main' command handler.  When a command is input, with
		 * help from the tabhandler, it should cycle through each 'level' of the command in turn
		 * and continue to send the args to the next method on the list.  That's the hope, at least.
		 */
		if (!(sender instanceof Player)) {
			Bukkit.getLogger().info("[BossTimer]: This command cannot be used at the console");
		} else {
			Player player = (Player) sender;
			
			try {
				switch(args[0]) {
				case "admin":
					if (player.hasPermission("bosstimer.admin")) {this.arg1Command(player, args);}
						else {this.noPermission(player, "bosstimer.admin");}
					break;
				case "help":
				default:
					this.sendCmdHelp(player);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				this.sendCmdHelp(player);
			}
		}	
		return false;
	}
	
	private void arg1Command(Player player, String[] args) {
		try {
			switch(args[1]) {
			case "start":
			case "stop": 
				if (player.hasPermission("bosstimer.admin")) {this.arg2Command(player, args);}
					else {this.noPermission(player, "bosstimer.admin");}
				break;
			default: this.sendCmdHelp(player);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			this.sendCmdHelp(player);
		}
	}
	
	private void arg2Command(Player player, String[] args) {
		try {
			switch(args[2]) {
			case "enderdragon":
				String spawnDragon = args[1];
				if (player.hasPermission("bosstimer.admin")) {
					if (spawnDragon.equalsIgnoreCase("start")) {sh.spawnEnderDragon();}
					sh.toggleEnderDragon(spawnDragon);
					this.spawnMessage(player, args);
				} else {this.noPermission(player, "bosstimer.admin");}
				break;
			default: this.sendCmdHelp(player);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			this.sendCmdHelp(player);
		}
	}
	
	private void spawnMessage(Player player, String[] args) {
		player.sendMessage(ChatColor.GREEN + "[BT]: The schedule for boss '" + args[2] + "' has been set to '" + args[1] + "'");
	}
	
	private void sendCmdHelp(Player player) {
		player.sendMessage(ChatColor.YELLOW + "Welcome to BossTimer, where we time your bosses! o.o");
		player.sendMessage(ChatColor.GREEN + "  /bosstimer admin start <boss>" + ChatColor.YELLOW +
				": Spawn that boss and start the " + "schedule");
		player.sendMessage(ChatColor.GREEN + "  /bosstimer admin stop <boss>" + ChatColor.YELLOW +
				": Stop a boss schedule");
	}
	
	private void noPermission(Player player, String permission) {
		player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		player.sendMessage(ChatColor.RED + "Required permission: " + ChatColor.UNDERLINE + permission);
	}
}
