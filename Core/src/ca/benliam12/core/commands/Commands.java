package ca.benliam12.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ca.benliam12.core.data.SettingManager;
import ca.benliam12.core.sessions.Session;
import ca.benliam12.core.sessions.SessionManager;

public class Commands implements CommandExecutor
{
	SessionManager sessionManager = SessionManager.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(label.equalsIgnoreCase("setgroup"))
			{
				if(args.length == 2)
				{
					Session session = this.sessionManager.getSession(args[0]);
					if(session != null && player.isOp())
					{
						FileConfiguration config = (FileConfiguration) session.getData("core-file");
						config.set("infos.group", args[0]);
						SettingManager.getInstance().saveConfig(args[0], config);
						session.updateData("srv-group", args[1]);
						player.sendMessage(ChatColor.GREEN + args[0] + ChatColor.GRAY + " is now " + ChatColor.GOLD + args[1]);
					}
					else
					{
						player.sendMessage("User not found");
					}
				}
			}
		}
		
		return false;
	}
	
}
