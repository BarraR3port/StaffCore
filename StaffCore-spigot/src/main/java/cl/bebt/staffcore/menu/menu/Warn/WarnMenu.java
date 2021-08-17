/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Warn;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.WarnPlayerMenu;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class WarnMenu extends WarnPlayerMenu {
    private final main plugin;
    
    public WarnMenu( PlayerMenuUtility playerMenuUtility , main plugin , String p2 ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.warned = p2;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "warns.menu.name" , "menu" , null ).replace( "%player%" , warned ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "punish_items" );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "other" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            PlayerData.set( new NamespacedKey( main.plugin , "warnmsg" ) , PersistentDataType.STRING , warned );
            utils.tell( p , utils.getString( "bans.other_reason" , "lg" , "sv" ) );
        } else {
            for ( String key : inventorySection.getKeys( false ) ) {
                if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , key ) , PersistentDataType.STRING ) ) {
                    p.closeInventory( );
                    String reason = plugin.items.getConfig( ).getString( "punish_items." + key + ".reason" );
                    new WarnTimeChose( playerMenuUtility , plugin , p , warned , reason ).open( );
                }
            }
        }
        if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "punish_items" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = utils.getString( "punish_items." + key + ".name" , "item" , null );
            String material = utils.getString( "punish_items." + key + ".material" , "item" , null );
            String reason = utils.getString( "punish_items." + key + ".reason" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : utils.getStringList( "punish_items." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Warn" );
                key2 = key2.replace( "%player%" , warned );
                lore.add( utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( utils.chat( name ) );
            if ( key.equalsIgnoreCase( "other" ) ) {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , "other" );
            } else {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , reason );
            }
            item.setItemMeta( meta );
            inventory.addItem( item );
            
        }
    }
}
