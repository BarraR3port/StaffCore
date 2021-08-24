/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Staff;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcoreapi.SQL.Queries.StaffChatQuery;
import cl.bebt.staffcoreapi.SQL.Queries.StaffQuery;
import cl.bebt.staffcoreapi.SQL.Queries.VanishQuery;
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

public class StaffListBungeeGui extends PaginatedMenu {
    
    private final main plugin;
    
    private final Player p;
    
    public StaffListBungeeGui( PlayerMenuUtility playerMenuUtility , main plugin , Player p ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.p = p;
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
        ArrayList < String > players = new ArrayList <>( );
        for ( String s : main.staffMembers ) {
            if ( !players.contains( s ) ) {
                players.add( s );
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String target = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING );
            if ( Bukkit.getPlayer( target ) instanceof Player ) {
                TpPlayers.tpToPlayer( p , target );
            } else {
                Utils.tell( p , Utils.getString( "tp.connect_to_server_where_player_is" , "lg" , "staff" ).replace( "%server%" , main.playersServerMap.get( target ) ).replace( "%player%" , target ) );
                SendMsg.connectPlayerToServer( p.getName( ) , main.playersServerMap.get( target ) );
            }
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
        ArrayList < String > players = new ArrayList <>( );
        for ( String s : main.staffMembers ) {
            if ( !players.contains( s ) ) {
                players.add( s );
            }
        }
        if ( players != null && !players.isEmpty( ) ) {
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= players.size( ) ) break;
                if ( players.get( index ) != null ) {
                    //////////////////////////////
                    ItemStack p_head = Utils.getPlayerHead( players.get( index ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    String ping = main.playersServerPingMap.get( players.get( index ) );
                    String server = main.playersServerMap.get( players.get( index ) );
                    String gm = main.playersServerGamemodesMap.get( players.get( index ) );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( Utils.chat( "&a" + players.get( index ) ) );
                    if ( Utils.getBoolean( "mysql.enabled" ) ) {
                        if ( StaffQuery.isStaff( players.get( index ) ).equals( "true" ) ) {
                            lore.add( Utils.chat( "&7Staff Mode: &aTrue" ) );
                        } else {
                            lore.add( Utils.chat( "&7Staff Mode: &cFalse" ) );
                        }
                        if ( VanishQuery.isVanished( players.get( index ) ).equals( "true" ) ) {
                            lore.add( Utils.chat( "&7Vanished: &aTrue" ) );
                        } else {
                            lore.add( Utils.chat( "&7Vanished: &cFalse" ) );
                        }
                        if ( StaffChatQuery.isStaffChat( players.get( index ) ).equals( "true" ) ) {
                            lore.add( Utils.chat( "&7Staff Chat: &aTrue" ) );
                        } else {
                            lore.add( Utils.chat( "&7Staff Chat: &cFalse" ) );
                        }
                        if ( GameMode.valueOf( gm ).equals( GameMode.CREATIVE ) || p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                            lore.add( Utils.chat( "&7Flying: &aTrue" ) );
                        } else {
                            lore.add( Utils.chat( "&7Flying: &cFalse" ) );
                        }
                        if ( Utils.getTrollStatus( players.get( index ) ) ) {
                            lore.add( Utils.chat( "&7Troll Mode: &aON" ) );
                        }
                        if ( !Utils.getTrollStatus( players.get( index ) ) ) {
                            lore.add( Utils.chat( "&7Troll Mode: &cOFF" ) );
                        }
                    }
                    lore.add( Utils.chat( "&7Gamemode: &a" + gm ) );
                    lore.add( Utils.chat( "&7Server: &a" + server ) );
                    lore.add( Utils.chat( "&7Ping: &a" + ping ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING , "staff" );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING , players.get( index ) );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                }
            }
        }
    }
}
