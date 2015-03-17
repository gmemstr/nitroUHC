package com.gabrielsimmer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class leaveGame implements Listener {
	
	nitroUHC plugin;
	
	public leaveGame(nitroUHC passedPlugin){
		this.plugin = passedPlugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		event.setQuitMessage(null);
	}

}
