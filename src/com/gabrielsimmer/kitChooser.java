package com.gabrielsimmer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class kitChooser extends JavaPlugin implements Listener {

	public static void openGUI(Player player){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Nitro UHC Kit Select");

		//Move this to a config file at some point
		ItemStack tools = new ItemStack (Material.WOOD_AXE);
		ItemMeta toolsMeta = tools.getItemMeta();
		ItemStack food = new ItemStack (Material.APPLE);
		ItemMeta foodMeta = food.getItemMeta();

		toolsMeta.setDisplayName(ChatColor.DARK_RED + "Tool kit");
		foodMeta.setDisplayName(ChatColor.RED + "Food kit");
		tools.setItemMeta(toolsMeta);
		food.setItemMeta(foodMeta);

		inv.setItem(3, tools);
		inv.setItem(5, food);

		player.openInventory(inv);

	}
	@EventHandler //MAKE SURE YOU HAVE THIS
	public void InventoryClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();

		if(e.getInventory().getTitle().equalsIgnoreCase("Nitro UHC Kit Select")){
			e.setCancelled(true); //Cancel the event so they can't take items out of the GUI

			if(e.getCurrentItem() == null){
				return;
			}
			else if(e.getCurrentItem().getType() == Material.WOOD_AXE){
				//gets called when the current item's type (The item the player clicked) is a diamond
				p.sendMessage("You clicked the axe!");//send the player a message when they click it
				p.closeInventory(); //call if you want to close the inventory when they click it
			}
		}
	}

}
