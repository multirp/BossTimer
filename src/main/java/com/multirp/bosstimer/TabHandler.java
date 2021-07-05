package main.java.com.aspiriamc.bosstimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class TabHandler implements TabCompleter{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("bosstimer.admin")) {commands.add("admin");}
            if (sender.hasPermission("bosstimer.admin")) {commands.add("help");}
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (args[0].equals("admin")) {
                if (sender.hasPermission("bosstimer.admin")) {commands.add("start");}
                if (sender.hasPermission("bosstimer.admin")) {commands.add("stop");}
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3) {
            if (Arrays.asList("start", "stop").contains(args[1])) {
                if (sender.hasPermission("bosstimer.admin")) {commands.add("enderdragon");}
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
