/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcore.utils.WarnPlayer;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class EditWarn extends ReportMenu {
    
    private static PlayerMenuUtility playerMenuUtility;
    private final main plugin;
    private final int Id;
    private final String warned;
    private final Player p;
    private final String status;
    
    public EditWarn( PlayerMenuUtility playerMenuUtility , main plugin , Player p , String warned , String status , int Id ){
        super( playerMenuUtility , plugin , warned );
        EditWarn.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;
        this.p = p;
        this.Id = Id;
        this.warned = warned;
        this.status = status;
    }
    
    public static PlayerMenuUtility getPlayerMenuUtility( ){
        return playerMenuUtility;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "warns.edit.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.CloseWarn( p , Id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "closed" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.OpenWarn( p , Id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "delete" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.DeleteWarn( p , Id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new Warnings( main.getPlayerMenuUtility( p ) , plugin , p , warned ).open( );
            }
        }
    }
    
    
    @Override
    public void setMenuItems( ){
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
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , super.close( ) );
        inventory.setItem( 23 , super.redPanel( ) );
        
        if ( status.equalsIgnoreCase( "open" ) ) {
            String name = Utils.getString( "edit.open.name" , "item" , null ).replace( "%punishment%" , "Warn" ).replace( "%id%" , String.valueOf( Id ) );
            ItemStack item = Utils.getDecorationHead( "check" );
            ItemMeta meta = item.getItemMeta( );
            meta.setDisplayName( Utils.chat( name ) );
            ArrayList < String > lore = new ArrayList <>( );
            for ( String key2 : Utils.getStringList( "edit.open.lore" , "item" ) ) {
                key2 = key2.replace( "%punishment%" , "Warn" );
                lore.add( Utils.chat( key2 ) );
            }
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
            item.setItemMeta( meta );
            inventory.setItem( 20 , item );
        } else {
            String name = Utils.getString( "edit.closed.name" , "item" , null ).replace( "%punishment%" , "Warn" ).replace( "%id%" , String.valueOf( Id ) );
            ItemStack item = Utils.getDecorationHead( "delete" );
            ItemMeta meta = item.getItemMeta( );
            meta.setDisplayName( Utils.chat( name ) );
            ArrayList < String > lore = new ArrayList <>( );
            for ( String key2 : Utils.getStringList( "edit.closed.lore" , "item" ) ) {
                key2 = key2.replace( "%punishment%" , "Warn" );
                lore.add( Utils.chat( key2 ) );
            }
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "closed" ) , PersistentDataType.STRING , "closed" );
            item.setItemMeta( meta );
            inventory.setItem( 20 , item );
        }
        
        
        String name = Utils.getString( "edit.delete.name" , "item" , null ).replace( "%punishment%" , "Warn" ).replace( "%id%" , String.valueOf( Id ) );
        ItemStack item = Utils.getDecorationHead( "delete" );
        ItemMeta meta = item.getItemMeta( );
        meta.setDisplayName( Utils.chat( name ) );
        ArrayList < String > lore = new ArrayList <>( );
        for ( String key2 : Utils.getStringList( "edit.delete.lore" , "item" ) ) {
            key2 = key2.replace( "%punishment%" , "Warn" );
            lore.add( Utils.chat( key2 ) );
        }
        meta.setLore( lore );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "delete" ) , PersistentDataType.STRING , "delete" );
        item.setItemMeta( meta );
        inventory.setItem( 24 , item );
    }
}
