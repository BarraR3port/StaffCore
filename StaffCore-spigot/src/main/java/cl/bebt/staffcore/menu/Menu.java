/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu;

import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public abstract class Menu implements InventoryHolder {
    
    protected Inventory inventory;
    
    protected PlayerMenuUtility playerMenuUtility;
    
    public Menu( PlayerMenuUtility playerMenuUtility ){
        this.playerMenuUtility = playerMenuUtility;
    }
    
    protected ItemStack redPanel( ){
        ItemStack panel = new ItemStack( Material.RED_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel.setItemMeta( panel_meta );
        PersistentDataUtils.save("panel", "panel", panel, uuid, PersistentDataType.STRING );
        return panel;
    }
    
    protected ItemStack greenPanel( ){
        ItemStack panel = new ItemStack( Material.LIME_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel.setItemMeta( panel_meta );
        PersistentDataUtils.save("panel", "panel", panel, uuid, PersistentDataType.STRING );
        return panel;
    }
    
    protected ItemStack bluePanel( ){
        ItemStack panel = new ItemStack( Material.CYAN_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel.setItemMeta( panel_meta );
        PersistentDataUtils.save("panel", "panel", panel, uuid, PersistentDataType.STRING );
        return panel;
    }
    
    protected ItemStack next( ){
        ItemStack next = Utils.getDecorationHead( "next" );
        ItemMeta next_meta = next.getItemMeta( );
        next_meta.setDisplayName( Utils.chat( Utils.getString( "menu_items.next.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : Utils.getStringList( "menu_items.next.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
        }
        next_meta.setLore( lore );
        next.setItemMeta( next_meta );
        PersistentDataUtils.save("next", "next", next, uuid, PersistentDataType.STRING );
        return next;
    }
    
    protected ItemStack back( ){
        ItemStack back = Utils.getDecorationHead( "previous" );
        ItemMeta back_meta = back.getItemMeta( );
        back_meta.setDisplayName( Utils.chat( Utils.getString( "menu_items.back.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : Utils.getStringList( "menu_items.back.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
        }
        back_meta.setLore( lore );
        back.setItemMeta( back_meta );
        PersistentDataUtils.save("back", "back", back, uuid, PersistentDataType.STRING );
        return back;
    }
    
    protected ItemStack close( ){
        ItemStack close = new ItemStack( Material.getMaterial( Utils.getString( "menu_items.close.material" , "item" , null ) ) );
        ItemMeta close_meta = close.getItemMeta( );
        close_meta.setDisplayName( Utils.chat( Utils.getString( "menu_items.close.name" , "item" , null ) ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key : Utils.getStringList( "menu_items.close.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
        }
        close_meta.setLore( lore );
        close.setItemMeta( close_meta );
        PersistentDataUtils.save("close", "close", close, uuid, PersistentDataType.STRING );
        return close;
    }
    
    public abstract String getMenuName( );
    
    public abstract int getSlots( );
    
    public abstract void handleMenu( InventoryClickEvent e );
    
    public abstract void setMenuItems( );
    
    public void open( ){
        inventory = Bukkit.createInventory( this , getSlots( ) , getMenuName( ) );
        this.setMenuItems( );
        playerMenuUtility.getOwner( ).openInventory( inventory );
    }
    
    public ItemStack makeItem( Material material , String displayName , String... lore ){
        ItemStack item = new ItemStack( material );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( displayName );
        itemMeta.setLore( Arrays.asList( lore ) );
        item.setItemMeta( itemMeta );
        return item;
    }
    public UUID uuid = playerMenuUtility.getOwner().getUniqueId();
    
    @Override
    public Inventory getInventory( ){
        return inventory;
    }
    
}
