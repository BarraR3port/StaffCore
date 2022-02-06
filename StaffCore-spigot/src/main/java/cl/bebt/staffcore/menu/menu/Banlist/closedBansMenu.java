/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.TpManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class closedBansMenu extends PaginatedMenu {
    private final main plugin;
    
    public closedBansMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    public String getMenuName( ){
        return Utils.chat( "&cClosed Bans" );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ItemStack item = e.getCurrentItem( );
        HashMap < Integer, Integer > bans = new HashMap <>( );
        int num = 0;
        if ( Utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedBans = BansQuery.getClosedBans( );
            for ( Integer closedBan : closedBans ) {
                num++;
                bans.put( num , closedBan );
            }
        } else {
            for ( int id = 0; id <= Utils.count( UpdateType.BAN ); ) {
                id++;
                try {
                    if ( Api.bans.getConfig( ).getString( "bans." + id + ".status" ).equals( "close" ) ) {
                        num++;
                        bans.put( num , id );
                    }
                    
                } catch ( NullPointerException ignored ) {
                }
            }
        }
        if ( PersistentDataUtils.has( item, "closed", PersistentDataType.STRING ) ){
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int i = PersistentDataUtils.getInteger( item,"closed-id" );
                new EditBan( main.getPlayerMenuUtility( p ) , main.plugin , jugador , i ).open( );
            } else if ( e.getClick( ).isRightClick( ) ) {
                TpManager.tpToPlayer( p , jugador );
            }
        } else if ( Objects.equals( e.getCurrentItem( ) , close( ) ) ) {
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                new BanManager( main.getPlayerMenuUtility( p ) , this.plugin ).open( );
                e.setCancelled( true );
            } else if ( e.getClick( ).isRightClick( ) ) {
                p.closeInventory( );
            }
        } else if ( Objects.equals( e.getCurrentItem( ) , back( ) ) ) {
            if ( page == 0 ) {
                Utils.tell( p , Utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( Objects.equals( e.getCurrentItem( ) , next( ) ) ) {
            e.setCancelled( true );
            if ( index + 1 <= bans.size( ) ) {
                page++;
                open( );
            } else {
                Utils.tell( p , Utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    public void setMenuItems( ){
        addMenuBorder( );
        HashMap < Integer, Integer > bans = new HashMap <>( );
        int num = 0;
        JSONObject json = new JSONObject( );
        if ( Utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedBans = BansQuery.getClosedBans( );
            for ( Integer closedBan : closedBans ) {
                num++;
                bans.put( num , closedBan );
            }
            json = BansQuery.getClosedBanInfo( );
        } else {
            for ( int id = 0; id <= Utils.count( UpdateType.BAN ); ) {
                id++;
                try {
                    if ( Api.bans.getConfig( ).getString( "bans." + id + ".status" ).equals( "closed" ) ) {
                        num++;
                        bans.put( num , id );
                    }
                    
                } catch ( NullPointerException ignored ) {
                }
            }
        }
        if ( bans != null && !bans.isEmpty( ) )
            for ( int i = 1; i <= getMaxItemsPerPage( ); i++ ) {
                try {
                    this.index = getMaxItemsPerPage( ) * this.page + i;
                    if ( this.index > bans.size( ) )
                        break;
                    if ( bans.get( this.index ) != null ) {
                        if ( Utils.mysqlEnabled( ) ) {
                            String rawBanInfo = json.get( String.valueOf( bans.get( index ) ) ).toString( )
                                    .replace( "[" , "" )
                                    .replace( "]" , "" );
                            
                            JSONObject banInfo = new JSONObject( rawBanInfo );
                            String exp = banInfo.getString( "ExpDate" );
                            Date now = new Date( );
                            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                            Date d2 = null;
                            d2 = format.parse( exp );
                            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
                            long Days = TimeUnit.SECONDS.toDays( remaining );
                            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                            long Hours = TimeUnit.SECONDS.toHours( Seconds );
                            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
                            ItemStack p_head = Utils.getPlayerHead( banInfo.getString( "Name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( banInfo.getString( "Name" ) );
                            lore.add( Utils.chat( "&7Banned by: " + banInfo.getString( "Baner" ) ) );
                            lore.add( Utils.chat( "&7Reason: &b" + banInfo.getString( "Reason" ) ) );
                            lore.add( Utils.chat( "&7Created date: &c" + banInfo.getString( "Date" ) ) );
                            lore.add( Utils.chat( "&7Expiry date: &c" + banInfo.getString( "ExpDate" ) ) );
                            if ( banInfo.getString( "Status" ).equalsIgnoreCase( "open" ) ) {
                                lore.add( Utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365L ) {
                                    lore.add( Utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( Utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( Utils.chat( "&7Status: &cClosed" ) );
                            }
                            if ( banInfo.getString( "IP_Banned" ).equalsIgnoreCase( "true" ) ) {
                                lore.add( Utils.chat( "&7Ip Banned: &aTrue" ) );
                            } else {
                                lore.add( Utils.chat( "&7Ip Banned: &cFalse" ) );
                            }
                            lore.add( Utils.chat( "&7Ban ID:&a #" + bans.get( this.index ) ) );
                            lore.add( Utils.chat( "&aLeft click delete" ) );
                            lore.add( Utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            p_head.setItemMeta( meta );
                            PersistentDataUtils.save( "closed-id", bans.get( this.index ), p_head, uuid, PersistentDataType.INTEGER );
                            PersistentDataUtils.save( "closed", "closed", p_head, uuid, PersistentDataType.STRING );
                            this.inventory.addItem( p_head );
                        } else {
                            Date now = new Date( );
                            String exp = Api.bans.getConfig( ).getString( "bans." + bans.get( index ) + ".expdate" );
                            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                            Date d2 = null;
                            d2 = format.parse( exp );
                            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
                            long Days = TimeUnit.SECONDS.toDays( remaining );
                            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                            long Hours = TimeUnit.SECONDS.toHours( Seconds );
                            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
                            ItemStack p_head = Utils.getPlayerHead( Api.bans.getConfig( ).getString( "bans." + bans.get( index ) + ".name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( Api.bans.getConfig( ).get( "bans." + bans.get( this.index ) + ".name" ).toString( ) );
                            lore.add( Utils.chat( "&7Banned by: &a" + Api.bans.getConfig( ).getString( "bans." + bans.get( this.index ) + ".banned_by" ) ) );
                            lore.add( Utils.chat( "&7Reason: &b" + Api.bans.getConfig( ).getString( "bans." + bans.get( this.index ) + ".reason" ) ) );
                            lore.add( Utils.chat( "&7Created date: &a" + Api.bans.getConfig( ).getString( "bans." + bans.get( this.index ) + ".date" ) ) );
                            lore.add( Utils.chat( "&7Expiry date: &c" + Api.bans.getConfig( ).getString( "bans." + bans.get( this.index ) + ".expdate" ) ) );
                            lore.add( Utils.chat( "&7Status: &cClosed" ) );
                            if ( Utils.isStillBanned( bans.get( index ) ) ) {
                                lore.add( Utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365L ) {
                                    lore.add( Utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( Utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( Utils.chat( "&7Status: &c" + Api.bans.getConfig( ).getString( "bans." + bans.get( index ) + ".status" ) ) );
                            }
                            if ( Api.bans.getConfig( ).getBoolean( "bans." + bans.get( index ) + ".IP-Banned" ) ) {
                                lore.add( Utils.chat( "&7Ip Banned: &aTrue" ) );
                            } else {
                                lore.add( Utils.chat( "&7Ip Banned: &cFalse" ) );
                            }
                            lore.add( Utils.chat( "&7Ban ID:&a #" + bans.get( this.index ) ) );
                            lore.add( Utils.chat( "&aLeft click delete" ) );
                            lore.add( Utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            p_head.setItemMeta( meta );
                            PersistentDataUtils.save( "closed-id", bans.get( this.index ), p_head, uuid, PersistentDataType.INTEGER );
                            PersistentDataUtils.save( "closed", "closed", p_head, uuid, PersistentDataType.STRING );
                            this.inventory.addItem( p_head );
                        }
                    }
                } catch ( ParseException ignored ) {
                }
            }
    }
}
