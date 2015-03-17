package com.gabrielsimmer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class deathEvent implements Listener {

	nitroUHC plugin;
	
	public deathEvent(nitroUHC passedPlugin){
		this.plugin = passedPlugin;
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		
		Player player = event.getEntity();
		Player killer = player.getKiller();
		World world = Bukkit.getWorld("world");

		event.setDeathMessage(player.getDisplayName() + " died!");
		
		int y = world.getHighestBlockYAt(0, 0);
		player.teleport(new Location(world, 0, y, 0));

		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			public void run() {
				try{
					player.kickPlayer(killer.getDisplayName() + " killed you! Better luck next time...");
				}
				catch(NullPointerException e){
					player.kickPlayer("You died! Better luck next time...");
				}
			}
		}, 120L);
	}

}
