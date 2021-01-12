package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.Items;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            return utils.chat( "&cYour Warnings:" );
        } else {
            return utils.chat( "&c" + warned + "'s Warnings:" );
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
                int id = item.getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( main.plugin , "id" ) , PersistentDataType.INTEGER );
                p.closeInventory( );
                new ChoseWarn( playerMenuUtility , plugin , p , warned , id ).open( p );
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
        
        if ( utils.getInt( "warns.max_warns" ) == 3 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 11 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" ) == 4 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" ) == 5 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" ) == 6 ) {
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" ) < 3 || utils.getInt( "warns.max_warns" ) > 7 ) {
            utils.tell( p , utils.getString( "server_prefix" ) + "&cThe warning config is wrong, configure the 'max_warns' " );
        } else {
            if ( utils.mysqlEnabled( ) ) {
                ArrayList < Integer > ids = SQLGetter.getWarnIds( warned );
                for ( int i : ids ) {
                    Date now = new Date( );
                    String exp = SQLGetter.getWarned( i , "ExpDate" );
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
                    
                    ItemStack p_head = utils.getPlayerHead( SQLGetter.getWarned( i , "Name" ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( utils.chat( "&5" + SQLGetter.getWarned( i , "name" ) ) );
                    lore.add( utils.chat( "&7Warned by: " + SQLGetter.getWarned( i , "Warner" ) ) );
                    lore.add( utils.chat( "&7Reason: &b" + SQLGetter.getWarned( i , "Reason" ) ) );
                    lore.add( utils.chat( "&7Created date: &c" + SQLGetter.getWarned( i , "Date" ) ) );
                    lore.add( utils.chat( "&7Expiry date: &c" + SQLGetter.getWarned( i , "ExpDate" ) ) );
                    if ( StaffCoreAPI.isStillWarned( i ) ) {
                        lore.add( utils.chat( "&7Status: &aOpen" ) );
                        if ( Days > 365 ) {
                            lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                        } else {
                            lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                        }
                    } else {
                        lore.add( utils.chat( "&7Status: &cClosed" ) );
                    }
                    lore.add( utils.chat( "&7Warn ID:&a #" + i ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "id" ) , PersistentDataType.INTEGER , i );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                }
            } else {
                ArrayList < Integer > ids = getWarnIds( );
                for ( int i : ids ) {
                    Date now = new Date( );
                    String exp = plugin.warns.getConfig( ).getString( "warns." + i + ".expdate" );
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
                    
                    ItemStack p_head = utils.getPlayerHead( plugin.warns.getConfig( ).getString( "warns." + i + ".name" ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( utils.chat( "&5" + plugin.warns.getConfig( ).getString( "warns." + i + ".name" ) ) );
                    lore.add( utils.chat( "&7Warned by: " + plugin.warns.getConfig( ).getString( "warns." + i + ".warned_by" ) ) );
                    lore.add( utils.chat( "&7Reason: &b" + plugin.warns.getConfig( ).getString( "warns." + i + ".reason" ) ) );
                    lore.add( utils.chat( "&7Created date: &c" + plugin.warns.getConfig( ).getString( "warns." + i + ".date" ) ) );
                    lore.add( utils.chat( "&7Expiry date: &c" + plugin.warns.getConfig( ).getString( "warns." + i + ".expdate" ) ) );
                    if ( StaffCoreAPI.isStillWarned( i ) ) {
                        lore.add( utils.chat( "&7Status: &aOpen" ) );
                        if ( Days > 365 ) {
                            lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                        } else {
                            lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                        }
                    } else {
                        lore.add( utils.chat( "&7Status: &c" + plugin.warns.getConfig( ).getString( "warns." + i + ".status" ) ) );
                    }
                    lore.add( utils.chat( "&7Warn ID:&a #" + i ) );
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
        for ( int i = 0; i < (plugin.warns.getConfig( ).getInt( "current" ) + plugin.warns.getConfig( ).getInt( "count" )); i++ ) {
            try {
                if ( plugin.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) ) {
                    warnIds.add( i );
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return warnIds;
    }
    
}
