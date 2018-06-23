package net.jusanov.respawnplugin.common;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {
	
	RespawnPlugin plugin;
	
	public PlayerDeathListener(RespawnPlugin instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (event.getEntityType() == EntityType.PLAYER) {
			
			Player player = (Player) event.getEntity();
			
			//if (player.getHealth() <= 1) {
			
			// Update Gamemode
			player.setGameMode(GameMode.SPECTATOR);
			
			// Update Inventory
			player.getInventory().clear();
			if (plugin.getConfig().getBoolean("giveItemToRespawn") == true) {
				player.getInventory().setItemInMainHand(new ItemStack(Material.matchMaterial(plugin.getConfig().getString("itemToRespawn")), 1));
			}
			player.updateInventory();
			
			// Update Health
			player.setHealth(20);
			
			// Send Title
			if (plugin.getConfig().getBoolean("titleEnabled") == true) {
				player.sendTitle(plugin.getConfig().getString("titleMainTitle"), plugin.getConfig().getString("titleSubTitle"), 20, plugin.getConfig().getInt("titleDuration"), 20);
			}
			
			//}
			
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
		
		if (plugin.getConfig().getBoolean("closeInventory") == true) {
			
			event.getPlayer().closeInventory();
			
		}
		
	}
	
}
