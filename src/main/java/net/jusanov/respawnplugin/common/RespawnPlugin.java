package net.jusanov.respawnplugin.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RespawnPlugin extends JavaPlugin {
	
	FileConfiguration config = getConfig();
	
	@Override
	public void onEnable() {
		
		// Setup Config
		config.addDefault("titleEnabled", true);
		config.addDefault("titleMainTitle", "§4You have died!");
		config.addDefault("titleSubTitle", "§7Click to respawn.");
		config.addDefault("titleDuration", 200);
		config.addDefault("disallowMovement", true);
		config.addDefault("giveItemToRespawn", true);
		config.addDefault("itemToRespawn", "coal");
		config.addDefault("respawnItemAmount", 64);
		config.addDefault("closeInventory", true);
		
		config.options().copyDefaults(true);
		saveConfig();
		
		// Setup event handler
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
