package ca.mobnetwork.core.sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.SettingManager;

public class Session
{
	
	private String name;
	private UUID uuid;
	private HashMap<String,Object> data = new HashMap<String,Object>();
	private SettingManager settingManager = SettingManager.getInstance();
	
	public Session(Player player)
	{
		this.name = player.getName();
		this.settingManager.addConfig(player.getUniqueId().toString(), "plugins/Core/data/players");
		FileConfiguration config = this.settingManager.getConfig(player.getUniqueId().toString());
		this.uuid = player.getUniqueId();
		try
		{
			this.putData("core-file", this.settingManager.getConfig(player.getUniqueId().toString()));
		}
		catch (SessionException se)
		{
			
		}
		List<String> names = new ArrayList<>();
		if(config.get("names") != null)
		{
			names = config.getStringList("names");
			if(!names.contains(this.name))
			{
				names.add(this.name);
			}
		} else
		{
			names.add(this.name);
		}
		config.set("names", names);
		this.settingManager.saveConfig(this.uuid.toString(), config);
	}
	
	public void closeSession()
	{
		if(this.name == null) 
		{
			return;
		}
		this.settingManager.removeConfig(this.name);
	}
	
	public void putData(String datakey, Object data) throws SessionException
	{
		if(!this.isData(datakey))
		{
			this.data.put(datakey,data);
		}
		else
		{
			throw new SessionException("This key was already registed !",this);
		}
	}
	
	public void updateData(String key, Object data)
	{
		if(this.isData(key))
		{
			this.data.put(key,data);
		}
		else
		{
			try 
			{
				this.putData(key, data);
			} 
			catch (SessionException se) 
			{
				
			}
		}
	}

	
	public boolean isData(String datakey)
	{
		return this.data.containsKey(datakey);
	}
	
	public Object getData(String datakey)
	{
		return this.data.get(datakey);
	}
	
	public String getName()
	{
		return this.name;
	}
	
}
