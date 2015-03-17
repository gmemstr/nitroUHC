/* Nitro UHC (c) Gabriel Simmer 2015
 * Nitro UHC created for N2O UHC Network (c) Gabriel Simmer 2015
 * 
 */

package com.gabrielsimmer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class nitroUHC extends JavaPlugin implements Listener{

	ScoreboardManager manager; //Field
	Scoreboard board; //Field
	Objective obj; //Field
	int i = 0;
	int teamNumber = 0;

	private static Connection connection;


	public void loadConfig(){
		this.getConfig().addDefault("teamsEnabled", false);
		this.getConfig().addDefault("minPlayers", 15);
		this.getConfig().addDefault("randomArenas", true);
		this.getConfig().addDefault("worldBorder.enabled", true);
		this.getConfig().addDefault("worldBorder.size", 500);
		this.getConfig().addDefault("prefix", "§2[UHC]§a ");
		this.getConfig().addDefault("mysql.ip", "localhost");
		this.getConfig().addDefault("mysql.databaseName", "nitrouhc");
		this.getConfig().addDefault("mysql.username", "username");
		this.getConfig().addDefault("mysql.password", "password");

		getConfig().options().copyDefaults(true);
		saveConfig();

	}

	public static class importantVars {
		public static Player teamSende;
		public static Statement st;
		public static boolean gameInProgress = false;
		public static World templateworld;
	}

	//This connects to the database as configured in the config file
	public void openConnection(){

		String dbIP = getConfig().getString("mysql.ip");
		String dbDB = getConfig().getString("mysql.databaseName");
		String dbUN = getConfig().getString("mysql.username");
		String dbPW = getConfig().getString("mysql.password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + dbIP + "/" + dbDB, dbUN, dbPW);
			getLogger().info("Connected to databse!");
		} catch (SQLException ex) {
			// handle any errors
			getLogger().info("SQLException: " + ex.getMessage());
			getLogger().info("SQLState: " + ex.getSQLState());
			getLogger().info("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			getLogger().info("Gabe, you done goof'd!");
			e.printStackTrace();
		}

	}
	public void closeConnection(){

		try {
			connection.close();
		} catch (SQLException ex) {
			// handle any errors
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized static boolean playerDataContainsPlayer(Player player){
		try{
			PreparedStatement sql = connection.prepareStatement("SELECT * FROM `players` WHERE isTeamLeader=true;");

			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public void onEnable(){
		loadConfig();
		getServer().getPluginManager().registerEvents(new joinGame(this), this);
		getServer().getPluginManager().registerEvents(new cancelRegen(), this);
		getServer().getPluginManager().registerEvents(new haveIWon(this), this);
		getServer().getPluginManager().registerEvents(new deathEvent(this), this);
		getServer().getPluginManager().registerEvents(new leaveGame(this), this);
		openConnection();
		importantVars.templateworld = Bukkit.getServer().createWorld(new WorldCreator("uhcland"));
		loadScoreboard();
		board.registerNewObjective("showhealth", "dummy");
		//onEnable

	}
	@Override
	public void onDisable(){
		try{
			if (connection != null || !connection.isClosed())
				connection.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void loadScoreboard(){ //Actually define the fields (Else errors)
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		obj = board.registerNewObjective("Something", "Something");
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if (cmd.getName().equalsIgnoreCase("uhc") && sender instanceof Player){

			Player player = (Player) sender;
			String prefixString = getConfig().getString("prefix");

			if (args.length == 0){

				player.sendMessage(prefixString + "Nitro UHC Info");
				player.sendMessage(prefixString + "Welcome to the Nitro UHC alpha phase!");
				player.sendMessage(prefixString + "We're currently developing the UHC plugin, so some things are missing.");
				player.sendMessage(prefixString + "You can also use /buy to help support the server!");

			}

			/*
			else if (args[0].equalsIgnoreCase("kit")){

				kitChooser.openGUI(player); // /uhc kit

			}
			 */

			else if (args[0].equalsIgnoreCase("team")){ // /uhc team <player>

				if (importantVars.gameInProgress == false){
					try{
						Player teamE = Bukkit.getPlayerExact(args[1]); //getPlayer depreciated - replace this sometime

						if (teamE == player){
							player.sendMessage(prefixString + "You can't team with yourself silly!");
						}

						if (teamE != null && teamE != player){

							player.sendMessage(prefixString + "Sending team request to " + teamE.getDisplayName());

							importantVars.teamSende = player;

							teamE.sendMessage(prefixString + player.getDisplayName() + ChatColor.GREEN + " wants to team with you! /uhc teamaccept to accept.");

						}
						else if (teamE == null){

							player.sendMessage(prefixString + "That player is not online!");

						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						player.sendMessage(prefixString + "Please enter a player's name!");
					}
				}
				else{
					player.sendMessage(prefixString + "Game already in progress!");
				}

			}

			else if (args[0].equalsIgnoreCase("teamaccept")){ // /uhc teamaccept
				//Probably
				Player teamSender = importantVars.teamSende;
				player.sendMessage(prefixString + "You've teamed with " + teamSender.getDisplayName() + "!");
				Team i = board.registerNewTeam("Team " + teamNumber);
				teamNumber = teamNumber + 1;
				i.addPlayer(player);
				i.addPlayer(teamSender);
				i.setAllowFriendlyFire(false);
			}
			return true;
		}

		return false;
	}

}
