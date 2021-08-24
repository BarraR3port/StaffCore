/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Warn;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.WarnPlayer;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class WarnQuantity extends PaginatedMenu {
    private final main plugin;
    private final String warned;
    private final String reason;
    private final int time;
    
    public WarnQuantity( PlayerMenuUtility playerMenuUtility , main plugin , String warned , String reason , int amount ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.warned = warned;
        this.reason = reason;
        this.time = amount;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "chat.quantity.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        int amount = Utils.getInt( "inventories.time_limit" , null ) + 1;
        
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "seconds" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "s" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "minutes" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "m" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "hours" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "h" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "days" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "d" );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new WarnAmount( playerMenuUtility , main.plugin , p , warned , reason ).open( );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                Utils.tell( p , Utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= amount ) {
                page++;
                p.closeInventory( );
                open( );
            } else {
                Utils.tell( p , Utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        Player p = playerMenuUtility.getOwner( );
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.bluePanel( ) );
            }
        }
        for ( int i = 10; i < 17; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        inventory.setItem( 17 , super.bluePanel( ) );
        inventory.setItem( 18 , super.bluePanel( ) );
        inventory.setItem( 19 , super.redPanel( ) );
        inventory.setItem( 25 , super.redPanel( ) );
        inventory.setItem( 26 , super.bluePanel( ) );
        inventory.setItem( 27 , super.bluePanel( ) );
        for ( int i = 28; i < 35; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        for ( int i = 35; i < 45; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.bluePanel( ) );
            }
        }
        inventory.setItem( 22 , close( ) );
        ConfigurationSection inventorySection = Api.items.getConfig( ).getConfigurationSection( "time" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = Utils.getString( "time." + key + ".name" , "item" , null );
            String material = Utils.getString( "time." + key + ".material" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : Utils.getStringList( "time." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Warn" );
                key2 = key2.replace( "%time%" , String.valueOf( time ) );
                key2 = key2.replace( "%player%" , warned );
                lore.add( Utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( Utils.chat( name ) );
            meta.setLore( lore );
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , key );
            item.setItemMeta( meta );
            inventory.addItem( item );
        }
        if ( inventory.getItem( 20 ) == null ) {
            Utils.tell( p , "&0[&5Warning&0] &7Try to delete the StaffCore/items.yml file and restart the server" );
        }
    }
    
    
}
