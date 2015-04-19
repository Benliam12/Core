package ca.benliam12.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
	SettingManager settingManager = SettingManager.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(label.equalsIgnoreCase("setgroup"))
			{
				if(args.length == 2)
				{
					if(player.isOp())
					{
						@SuppressWarnings("deprecation")
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
						FileConfiguration config = null;
						if(target.isOnline())
						{
							Session session = this.sessionManager.getSession(target.getName());
							config = (FileConfiguration) session.getData("core-file");
							config.set("infos.group", args[1]);
							session.updateData("srv-group", args[1]);
							this.settingManager.saveConfig(target.getUniqueId().toString(), config);
						}
						else
						{
							if(this.settingManager.getConfig(target.getUniqueId().toString()) != null)
							{
								config = this.settingManager.getConfig(target.getUniqueId().toString());
							}
							else
							{
								this.settingManager.addConfig(target.getUniqueId().toString(), "plugins/Core/data/players");
								config = this.settingManager.getConfig(target.getUniqueId().toString());
							}
							config.set("infos.group", args[1]);
							this.settingManager.saveConfig(target.getUniqueId().toString(), config);
						}
						
						player.sendMessage(ChatColor.GREEN + target.getName() + ChatColor.GRAY + " is now " + ChatColor.GOLD + args[1]);
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
