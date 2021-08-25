/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Items.Items;
import cl.bebt.staffcoreapi.SQL.Queries.WarnsQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Warnings extends Menu {
    private final main plugin;
    private final Player p;
    private final String warned;
    
    public Warnings( PlayerMenuUtility playerMenuUtility , main plugin ,
                     Player p , String warned ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.p = p;
        this.warned = warned;
    }
    
    @Override
    public String getMenuName( ){
        if ( p.getName( ).equalsIgnoreCase( warned ) ) {
            return Utils.chat( Utils.getString( "warns.warnings.owns" , "menu" , null ) );
        } else {
            return Utils.chat( Utils.getString( "warns.warnings.others" , "menu" , null ).replace( "%player%" , warned ) );
        }
    }
    
    @Override
    public int getSlots( ){
        return 27;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        ItemStack item = e.getCurrentItem( );
        if ( p.hasPermission( "staffcore.warn.change" ) ) {
            if ( item.getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "id" ) , PersistentDataType.INTEGER ) ) {
                p.closeInventory( );
                int id = item.getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( main.plugin , "id" ) , PersistentDataType.INTEGER );
                String status = item.getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( main.plugin , "status" ) , PersistentDataType.STRING );
                new EditWarn( playerMenuUtility , plugin , p , warned , status , id ).open( );
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
        
        for ( int i = 17; i < 27; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.bluePanel( ) );
            }
        }
        
        if ( Utils.getInt( "warns.max_warns" , null ) == 3 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 11 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( Utils.getInt( "warns.max_warns" , null ) == 4 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( Utils.getInt( "warns.max_warns" , null ) == 5 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( Utils.getInt( "warns.max_warns" , null ) == 6 ) {
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        JSONObject json = new JSONObject( );
        HashMap < Integer, Integer > warns = new HashMap <>( );
        int num = 0;
        if ( Utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedReports = WarnsQuery.getPlayerWarns( warned );
            for ( Integer closedReport : closedReports ) {
                warns.put( num , closedReport );
                num++;
            }
            json = WarnsQuery.getPlayerWarnsInfo( warned );
        }
        if ( Utils.getInt( "warns.max_warns" , null ) < 3 || Utils.getInt( "warns.max_warns" , null ) > 7 ) {
            Utils.tell( p , Utils.getString( "warns.bad_configuration" , "lg" , "staff" ) );
        } else {
            if ( Utils.mysqlEnabled( ) ) {
                for ( int i = 0; i < warns.size( ); i++ ) {
                    try {
                        String rawWarnsInfo = json.get( String.valueOf( warns.get( i ) ) ).toString( )
                                .replace( "[" , "" )
                                .replace( "]" , "" );
                        JSONObject warnsInfo = new JSONObject( rawWarnsInfo );
                        Date now = new Date( );
                        String exp = warnsInfo.getString( "ExpDate" );
                        SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                        Date d2 = format.parse( exp );
                        long remaining = (d2.getTime( ) - now.getTime( )) / 1000;
                        long Days = TimeUnit.SECONDS.toDays( remaining );
                        long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                        long Hours = TimeUnit.SECONDS.toHours( Seconds );
                        Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                        long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                        Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
                        
                        ItemStack p_head = Utils.getPlayerHead( warnsInfo.getString( "Name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( Utils.chat( "&5" + warnsInfo.getString( "Name" ) ) );
                        lore.add( Utils.chat( "&7Warned by: " + warnsInfo.getString( "Warner" ) ) );
                        lore.add( Utils.chat( "&7Reason: &b" + warnsInfo.getString( "Reason" ) ) );
                        lore.add( Utils.chat( "&7Created date: &c" + warnsInfo.getString( "Date" ) ) );
                        lore.add( Utils.chat( "&7Expiry date: &c" + warnsInfo.getString( "ExpDate" ) ) );
                        if ( Utils.isStillWarned( warns.get( i ) ) ) {
                            lore.add( Utils.chat( "&7Status: &aOpen" ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "status" ) , PersistentDataType.STRING , "open" );
                            if ( Days > 365 ) {
                                lore.add( Utils.chat( "&7Time left: &4PERMANENT" ) );
                            } else {
                                lore.add( Utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                            }
                        } else {
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "status" ) , PersistentDataType.STRING , "closed" );
                            lore.add( Utils.chat( "&7Status: &cClosed" ) );
                        }
                        lore.add( Utils.chat( "&7Warn ID:&a #" + warns.get( i ) ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "id" ) , PersistentDataType.INTEGER , warns.get( i ) );
                        p_head.setItemMeta( meta );
                        inventory.addItem( p_head );
                    } catch ( ParseException | JSONException ignored ) {
                    
                    }
                }
            } else {
                ArrayList < Integer > ids = getWarnIds( );
                for ( int i : ids ) {
                    Date now = new Date( );
                    String exp = Api.warns.getConfig( ).getString( "warns." + i + ".expdate" );
                    SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                    Date d2 = null;
                    try {
                        d2 = format.parse( exp );
                    } catch ( ParseException ignored ) {
                    }
                    long remaining = (d2.getTime( ) - now.getTime( )) / 1000;
                    long Days = TimeUnit.SECONDS.toDays( remaining );
                    long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                    long Hours = TimeUnit.SECONDS.toHours( Seconds );
                    Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                    long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                    Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
                    
                    ItemStack p_head = Utils.getPlayerHead( Api.warns.getConfig( ).getString( "warns." + i + ".name" ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( Utils.chat( "&5" + Api.warns.getConfig( ).getString( "warns." + i + ".name" ) ) );
                    lore.add( Utils.chat( "&7Warned by: " + Api.warns.getConfig( ).getString( "warns." + i + ".warned_by" ) ) );
                    lore.add( Utils.chat( "&7Reason: &b" + Api.warns.getConfig( ).getString( "warns." + i + ".reason" ) ) );
                    lore.add( Utils.chat( "&7Created date: &c" + Api.warns.getConfig( ).getString( "warns." + i + ".date" ) ) );
                    lore.add( Utils.chat( "&7Expiry date: &c" + Api.warns.getConfig( ).getString( "warns." + i + ".expdate" ) ) );
                    if ( Utils.isStillWarned( i ) ) {
                        lore.add( Utils.chat( "&7Status: &aOpen" ) );
                        if ( Days > 365 ) {
                            lore.add( Utils.chat( "&7Time left: &4PERMANENT" ) );
                        } else {
                            lore.add( Utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                        }
                    } else {
                        lore.add( Utils.chat( "&7Status: &c" + Api.warns.getConfig( ).getString( "warns." + i + ".status" ) ) );
                    }
                    lore.add( Utils.chat( "&7Warn ID:&a #" + i ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "id" ) , PersistentDataType.INTEGER , i );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                }
            }
        }
        for ( int i = 0; i < inventory.getSize( ); i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , Items.EmptyItem( ) );
            }
        }
    }
    
    private ArrayList < Integer > getWarnIds( ){
        ArrayList < Integer > warnIds = new ArrayList <>( );
        for ( int i = 0; i < (Api.warns.getConfig( ).getInt( "current" ) + Api.warns.getConfig( ).getInt( "count" )); i++ ) {
            try {
                if ( Api.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) ) {
                    warnIds.add( i );
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return warnIds;
    }
    
}
