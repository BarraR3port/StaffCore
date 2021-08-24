/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class StaffListGui extends PaginatedMenu {
    
    private final main plugin;
    
    public StaffListGui( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "others.staff_list.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < Player > players = new ArrayList <>( );
        p.getInventory( );
        for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
            if ( player.hasPermission( "staffcore.staff" ) ) {
                players.add( player );
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String target = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING );
            TpPlayers.tpToPlayer( p , target );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                Utils.tell( p , Utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= players.size( ) ) {
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
        addMenuBorder( );
        ArrayList < Player > players = new ArrayList <>( );
        for ( Player p : Bukkit.getServer( ).getOnlinePlayers( ) ) {
            if ( p.hasPermission( "staffcore.staff" ) ) {
                players.add( p );
            }
        }
        if ( players != null && !players.isEmpty( ) ) {
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= players.size( ) ) break;
                if ( players.get( index ) != null ) {
                    //////////////////////////////
                    ItemStack p_head = Utils.getPlayerHead( players.get( index ).getName( ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( Utils.chat( "&a" + players.get( index ).getName( ) ) );
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                        lore.add( Utils.chat( "&7Staff Mode: &aTrue" ) );
                    } else {
                        lore.add( Utils.chat( "&7Staff Mode: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                        lore.add( Utils.chat( "&7Vanished: &aTrue" ) );
                    } else {
                        lore.add( Utils.chat( "&7Vanished: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
                        lore.add( Utils.chat( "&7Staff Chat: &aTrue" ) );
                    } else {
                        lore.add( Utils.chat( "&7Staff Chat: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) || players.get( index ).getGameMode( ).equals( GameMode.CREATIVE ) ) {
                        lore.add( Utils.chat( "&7Flying: &aTrue" ) );
                    } else {
                        lore.add( Utils.chat( "&7Flying: &cFalse" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.CREATIVE ) ) {
                        lore.add( Utils.chat( "&7Gamemode: &aCreative" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.SURVIVAL ) ) {
                        lore.add( Utils.chat( "&7Gamemode: &aSurvival" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.SPECTATOR ) ) {
                        lore.add( Utils.chat( "&7Gamemode: &aSpectator" ) );
                    }
                    if ( Utils.getTrollStatus( players.get( index ).getName( ) ) ) {
                        lore.add( Utils.chat( "&7Troll Mode: &aON" ) );
                    }
                    if ( !Utils.getTrollStatus( players.get( index ).getName( ) ) ) {
                        lore.add( Utils.chat( "&7Troll Mode: &cOFF" ) );
                    }
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING , "staff" );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING , players.get( index ).getName( ) );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                }
            }
        }
    }
}
