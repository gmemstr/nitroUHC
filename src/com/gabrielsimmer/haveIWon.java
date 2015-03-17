package com.gabrielsimmer;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gabrielsimmer.nitroUHC.importantVars;

public class haveIWon implements Listener{

	nitroUHC plugin;

	public haveIWon(nitroUHC passedPlugin){
		this.plugin = passedPlugin;
	}


	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		
		String hubWorld = plugin.getConfig().getString("spawnWorld");
		
		int onlinePlayers = Bukkit.getOnlinePlayers().size();
		String prefixString = plugin.getConfig().getString("prefix");
		//Bukkit.broadcastMessage(String.valueOf(onlinePlayers));
		if (onlinePlayers == 2 && importantVars.gameInProgress == true){
			importantVars.gameInProgress = false;
			Bukkit.broadcastMessage(prefixString + ChatColor.GOLD + "You've won UHC! Congrats!");

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				public void run() {

					Collection<? extends Player> players = Bukkit.getOnlinePlayers();

					for(Player p : players)
					{
						p.teleport(Bukkit.getWorld(hubWorld).getSpawnLocation());
					}
				}
			}, 120L);
		}
	}
}
