package ca.mobnetwork.core.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.group.GroupManager;
import ca.mobnetwork.core.group.RankException;

public class Commands implements CommandExecutor
{
	private GroupManager groupManager = GroupManager.getInstance();
	
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
						if(!target.isOnline())
						{
							this.groupManager.checkUpUser(target.getUniqueId().toString(), true);
						}
						
						try 
						{
							this.groupManager.setUserGroup(target.getUniqueId().toString(), args[1]);
						} 
						catch (RankException e)
						{
							player.sendMessage(ChatColor.RED + "Could set the group ! (" + e.getMessage() +")");
							return true;
						}
						player.sendMessage(ChatColor.GREEN + "Player : " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + " is now " + ChatColor.GOLD + args[1]);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You don't have the permission to perform this command !");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Wrong usage !");
				}
			}
			else if(label.equalsIgnoreCase("creategroup"))
			{
				if(args.length == 1)
				{
					if(player.isOp())
					{
						try 
						{
							this.groupManager.createGroup(args[0]);
							player.sendMessage(ChatColor.GREEN + "Group created !");
						}
						catch (RankException e) 
						{
							player.sendMessage(ChatColor.RED + e.getMessage());
						} catch (SQLException e) 
						{
							player.sendMessage(ChatColor.RED + "SQL ERROR : " + e.getMessage());
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "You don't have the permission to perform this command !");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Wrong usage !");
				}
			}
			else if(label.equalsIgnoreCase("deletegroup"))
			{
				if(player.hasPermission("core.deletegroup"))
				{
					if(args.length == 1)
					{
						try
						{
							int GroupID = Integer.parseInt(args[0]);
							String groupName = this.groupManager.deleteRank(GroupID);
							player.sendMessage(ChatColor.GREEN + "The " + "'" + groupName + "' group has been deleted !");
						}
						catch (RankException ex)
						{
							player.sendMessage(ChatColor.RED + ex.getMessage());
						}
						catch (Exception ex)
						{
							try
							{
								String groupName = this.groupManager.deleteRank(args[0]);
								player.sendMessage(ChatColor.GREEN + "The " + "'" + groupName + "' group has been deleted !");
							}
							catch (RankException rankException)
							{
								player.sendMessage(ChatColor.RED + rankException.getMessage());
							}
							catch (Exception exception)
							{
								exception.printStackTrace();
							}
						}
					}
					else
					{
						player.sendMessage("Wrong usage !");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You don't have the permission to perform this command !");
				}
			}
		}
		return false;
	}
	
}
