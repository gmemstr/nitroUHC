package com.gabrielsimmer;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.gabrielsimmer.nitroUHC.importantVars;

public class joinGame implements Listener{

	nitroUHC plugin;
	
	public joinGame(nitroUHC passedPlugin){
		this.plugin = passedPlugin;
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
		for(Player all : Bukkit.getOnlinePlayers()){
			  all.setScoreboard(plugin.board);
			}
		
		int mP = plugin.getConfig().getInt("minPlayers");
		Player player = event.getPlayer();
		String prefixString = plugin.getConfig().getString("prefix");
		int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

		event.setJoinMessage(prefixString + player.getDisplayName() + " joined the game! Players: " + onlinePlayers + "/" + mP);
		
		if (importantVars.gameInProgress != true && onlinePlayers != mP){
			player.sendMessage(prefixString + "Please wait for the game to start! " + onlinePlayers + "/" + mP);
		}
		if (importantVars.gameInProgress == true){
			//Hoping this doesn't break everything
			player.kickPlayer("Game in progress!");
		}

		if (onlinePlayers == mP && importantVars.gameInProgress == false){
			Bukkit.broadcastMessage(prefixString + "UHC match starting!");


			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				public void run() {

					importantVars.gameInProgress = true;

					Collection<? extends Player> players = Bukkit.getOnlinePlayers();

					for(Player p : players)
					{
						p.setHealth(p.getMaxHealth());	
						
						int x = (int) (Math.random() * 499);
						int z = (int) (Math.random() * 499);
						int y = importantVars.templateworld.getHighestBlockYAt(x, z);
						p.teleport(new Location(importantVars.templateworld, x, y, z));
					}
				}
			}, 120L);
		}
	}

}
