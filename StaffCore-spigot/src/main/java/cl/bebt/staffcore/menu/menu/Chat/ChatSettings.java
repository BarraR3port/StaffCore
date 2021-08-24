/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.MenuC;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ChatSettings extends MenuC {
    private final main plugin;
    
    public ChatSettings( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "chat.clear_chat_manager.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "head" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ClearChat( main.getPlayerMenuUtility( p ) , plugin ).open( );
            p.updateInventory( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "clearAll" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            Utils.ccAll( );
            Bukkit.broadcastMessage( Utils.chat( Utils.getString( "clear_chat.global" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) ) );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ChatManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            }
        }
    }
    
    @Override
    public void setMenuItemsPlayer( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        lore.add( Utils.chat( "&c" ) );
        ItemStack ClearChat = new ItemStack( Material.END_CRYSTAL , 1 );
        ItemStack Head = Utils.getPlayerHead( p.getName( ) );
        
        ItemMeta metaTChat = ClearChat.getItemMeta( );
        ItemMeta metaHead = Head.getItemMeta( );
        
        metaTChat.setDisplayName( Utils.chat( "&cClear Server Chat" ) );
        metaHead.setDisplayName( Utils.chat( "&5Clear player chat" ) );
        
        metaTChat.setLore( lore );
        metaHead.setLore( lore );
        metaTChat.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "clearAll" ) , PersistentDataType.STRING , "clearAll" );
        metaHead.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "head" ) , PersistentDataType.STRING , "head" );
        
        ClearChat.setItemMeta( metaTChat );
        Head.setItemMeta( metaHead );
        
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
        inventory.setItem( 20 , ClearChat );
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , close( ) );
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , Head );
        inventory.setItem( 25 , super.redPanel( ) );
    }
}
