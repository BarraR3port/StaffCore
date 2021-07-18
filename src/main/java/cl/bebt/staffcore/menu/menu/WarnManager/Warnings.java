package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.Queries.WarnsQuery;
import cl.bebt.staffcore.utils.Items;
import cl.bebt.staffcore.utils.utils;
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
            return utils.chat( utils.getString( "warns.warnings.owns" , "menu" , null ) );
        } else {
            return utils.chat( utils.getString( "warns.warnings.others" , "menu" , null ).replace( "%player%" , warned ) );
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
        
        if ( utils.getInt( "warns.max_warns" , null ) == 3 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 11 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" , null ) == 4 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 15 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" , null ) == 5 ) {
            inventory.setItem( 10 , super.bluePanel( ) );
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        if ( utils.getInt( "warns.max_warns" , null ) == 6 ) {
            inventory.setItem( 16 , super.bluePanel( ) );
        }
        JSONObject json = new JSONObject( );
        HashMap < Integer, Integer > warns = new HashMap <>( );
        int num = 0;
        if ( utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedReports = WarnsQuery.getPlayerWarns( warned );
            for ( Integer closedReport : closedReports ) {
                warns.put( num , closedReport );
                num++;
            }
            json = WarnsQuery.getPlayerWarnsInfo( warned );
        }
        if ( utils.getInt( "warns.max_warns" , null ) < 3 || utils.getInt( "warns.max_warns" , null ) > 7 ) {
            utils.tell( p , utils.getString( "warns.bad_configuration" , "lg" , "staff" ) );
        } else {
            if ( utils.mysqlEnabled( ) ) {
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
                        
                        ItemStack p_head = utils.getPlayerHead( warnsInfo.getString( "Name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( utils.chat( "&5" + warnsInfo.getString( "Name" ) ) );
                        lore.add( utils.chat( "&7Warned by: " + warnsInfo.getString( "Warner" ) ) );
                        lore.add( utils.chat( "&7Reason: &b" + warnsInfo.getString( "Reason" ) ) );
                        lore.add( utils.chat( "&7Created date: &c" + warnsInfo.getString( "Date" ) ) );
                        lore.add( utils.chat( "&7Expiry date: &c" + warnsInfo.getString( "ExpDate" ) ) );
                        if ( StaffCoreAPI.isStillWarned( warns.get( i ) ) ) {
                            lore.add( utils.chat( "&7Status: &aOpen" ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "status" ) , PersistentDataType.STRING , "open" );
                            if ( Days > 365 ) {
                                lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                            } else {
                                lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                            }
                        } else {
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "status" ) , PersistentDataType.STRING , "closed" );
                            lore.add( utils.chat( "&7Status: &cClosed" ) );
                        }
                        lore.add( utils.chat( "&7Warn ID:&a #" + warns.get( i ) ) );
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
