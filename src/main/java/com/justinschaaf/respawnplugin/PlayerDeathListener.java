package com.justinschaaf.respawnplugin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {
	
	RespawnPlugin plugin;
	
	public PlayerDeathListener(RespawnPlugin instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (event.getEntityType() == EntityType.PLAYER) {
			
			final Player player = (Player) event.getEntity();
			
			//if (player.getHealth() <= 1) {
			
			// Update Gamemode
			player.setGameMode(GameMode.SPECTATOR);
			
			// Update Inventory
			player.getInventory().clear();
			if (plugin.getConfig().getBoolean("giveItemToRespawn") == true) {
				if (plugin.getConfig().getBoolean("addItemToMainHand") == true) {
					player.getInventory().addItem(new ItemStack(Material.matchMaterial(plugin.getConfig().getString("itemToRespawn")), plugin.getConfig().getInt("respawnItemAmount")));
				} else {
					player.getInventory().setItem(9, new ItemStack(Material.matchMaterial(plugin.getConfig().getString("itemToRespawn")), plugin.getConfig().getInt("respawnItemAmount")));
				}
			}
			player.updateInventory();
			
			// Update Health
			player.setHealth(20);
			
			// Send Title
			if (plugin.getConfig().getBoolean("titleEnabled") == true) {
				player.sendTitle(plugin.getConfig().getString("titleMainTitle"), plugin.getConfig().getString("titleSubTitle"), 20, plugin.getConfig().getInt("titleDuration"), 20);
			}
			
			if (plugin.getConfig().getBoolean("automaticRespawn") == true) {
				
				final int timer = plugin.getConfig().getInt("automaticRespawnTimer");
				for (int i = 0; i < timer; i++) {
					final int j = i;
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
						  public void run() {
							  if (plugin.getConfig().getBoolean("titleEnabled") == true) {
								  player.sendTitle(plugin.getConfig().getString("titleMainTitle"), plugin.getConfig().getString("titleSubTitle").replace("%t%", timer - j + ""), 20, plugin.getConfig().getInt("titleDuration"), 20);
							  }
						  }
						}, i * 20L);
					
				}
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					  public void run() {
						  respawn(player);
					  }
					}, timer * 20L);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		if (plugin.getConfig().getBoolean("disallowMovement") == true) {
			
			Location playerpos = event.getPlayer().getLocation();
			
			if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
				
				event.getPlayer().teleport(playerpos);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
			
			if (plugin.getConfig().getBoolean("closeInventory") == true) {
				
				event.setCancelled(true);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		if (plugin.getConfig().getBoolean("respawnClick") == true) {
			
			if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) respawn(event.getPlayer());
			
		}
		
	}
	
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
		if (plugin.getConfig().getBoolean("disallowMovement") == true) {
	        if (event.getCause().equals(TeleportCause.SPECTATE)) {
	            event.setCancelled(true);
	        }
	    }
    }
    
    private void respawn(Player player) {
    	
		// Automatic Gamemode Update
		if (plugin.getConfig().getBoolean("automaticGamemodeUpdate") == true) {
			player.setGameMode(GameMode.getByValue(plugin.getConfig().getInt("gamemode")));
		}
		
		// Execute Commands
		List<String> commands = plugin.getConfig().getStringList("respawnCommands");
		
		for (int i = 0; i < commands.size(); i++) {
			
			player.performCommand(commands.get(i));
			
		}
		
    }
    
}
