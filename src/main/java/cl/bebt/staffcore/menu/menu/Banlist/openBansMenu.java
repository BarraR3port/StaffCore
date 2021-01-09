package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.API.API;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class openBansMenu extends PaginatedMenu {
    private final main plugin;
    
    public openBansMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    public String getMenuName( ){
        return utils.chat( "&cOpen Bans:" );
    }
    
    public int getSlots( ){
        return 54;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        HashMap < Integer, Integer > bans = new HashMap <>( );
        int num = 0;
        for ( int id = 0; id <= count( ); ) {
            id++;
            try {
                if ( utils.mysqlEnabled( ) ) {
                    if ( SQLGetter.getBanStatus( id ).equals( "open" ) ) {
                        num++;
                        bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                    }
                    continue;
                }
                if ( Objects.equals( this.plugin.bans.getConfig( ).getString( "bans." + id + ".status" ) , "open" ) ) {
                    num++;
                    bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                }
            } catch ( NullPointerException nullPointerException ) {
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "open" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int i = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( this.plugin , "open-id" ) , PersistentDataType.INTEGER ).intValue( );
                (new ChoseBan( main.getPlayerMenuUtility( p ) , main.plugin , jugador , i )).open( p );
            } else if ( e.getClick( ).isRightClick( ) ) {
                try {
                    p.teleport( Bukkit.getPlayer( jugador ).getLocation( ) );
                    utils.tell( p , this.plugin.getConfig( ).getString( "staff.staff_prefix" ) + this.plugin.getConfig( ).getString( "tp.teleport_to" ) + jugador );
                } catch ( NullPointerException err ) {
                    utils.tell( p , this.plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cThe player is not online, or not exist" );
                }
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                (new BanManager( main.getPlayerMenuUtility( p ) , this.plugin )).open( p );
                e.setCancelled( true );
            } else if ( e.getClick( ).isRightClick( ) ) {
                p.closeInventory( );
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( this.page == 0 ) {
                    utils.tell( p , "&7You are already in the first page" );
                } else {
                    this.page--;
                    open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                e.setCancelled( true );
                if ( this.index + 1 <= bans.size( ) ) {
                    this.page++;
                    open( p );
                } else {
                    utils.tell( p , "&7You are already in the last page" );
                }
            }
        }
    }
    
    private int count( ){
        if ( utils.mysqlEnabled( ) )
            return SQLGetter.getCurrents( "bans" ) + this.plugin.data.getBanId( );
        return this.plugin.bans.getConfig( ).getInt( "current" ) + this.plugin.bans.getConfig( ).getInt( "count" );
    }
    
    public void setMenuItems( ){
        try {
            addMenuBorder( );
            HashMap < Integer, Integer > bans = new HashMap <>( );
            int num = 0;
            for ( int id = 0; id <= count( ); ) {
                id++;
                try {
                    if ( utils.mysqlEnabled( ) ) {
                        if ( SQLGetter.getBanStatus( id ).equals( "open" ) ) {
                            num++;
                            bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                        }
                        continue;
                    }
                    if ( this.plugin.bans.getConfig( ).getString( "bans." + id + ".status" ).equals( "open" ) ) {
                        num++;
                        bans.put( Integer.valueOf( num ) , Integer.valueOf( id ) );
                    }
                } catch ( NullPointerException nullPointerException ) {
                }
            }
            if ( bans != null && !bans.isEmpty( ) )
                for ( int i = 1; i <= getMaxItemsPerPage( ); i++ ) {
                    this.index = getMaxItemsPerPage( ) * this.page + i;
                    if ( this.index > bans.size( ) )
                        break;
                    if ( bans.get( Integer.valueOf( this.index ) ) != null ) {
                        this.plugin.bans.reloadConfig( );
                        if ( utils.mysqlEnabled( ) ) {
                            Date now = new Date( );
                            String exp = SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "ExpDate" );
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
                            ItemStack p_head = utils.getPlayerHead( SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "Name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "name" ) );
                            lore.add( utils.chat( "&7Banned by: " + SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "Baner" ) ) );
                            lore.add( utils.chat( "&7Reason: &b" + SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "Reason" ) ) );
                            lore.add( utils.chat( "&7Created date: &c" + SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "Date" ) ) );
                            lore.add( utils.chat( "&7Expiry date: &c" + SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "ExpDate" ) ) );
                            if ( API.isStillBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) ).booleanValue( ) ) {
                                lore.add( utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365L ) {
                                    lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( utils.chat( "&7Status: &cClosed" ) );
                            }
                            if ( SQLGetter.getBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) , "IP_Banned" ).equals( "true" ) ) {
                                lore.add( utils.chat( "&7Ip Banned: &aTrue" ) );
                            } else {
                                lore.add( utils.chat( "&7Ip Banned: &cFalse" ) );
                            }
                            lore.add( utils.chat( "&7Ban ID:&a #" + bans.get( Integer.valueOf( this.index ) ) ) );
                            lore.add( utils.chat( "&aLeft click delete or close" ) );
                            lore.add( utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , bans.get( Integer.valueOf( this.index ) ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                            p_head.setItemMeta( meta );
                            this.inventory.addItem( p_head );
                        } else {
                            Date now = new Date( );
                            String exp = this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".expdate" );
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
                            ItemStack p_head = utils.getPlayerHead( this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( this.plugin.bans.getConfig( ).get( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".name" ).toString( ) );
                            lore.add( utils.chat( "&7Banned by: " + this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".banned_by" ) ) );
                            lore.add( utils.chat( "&7Reason: &b" + this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".reason" ) ) );
                            lore.add( utils.chat( "&7Created date: &c" + this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".date" ) ) );
                            lore.add( utils.chat( "&7Expiry date: &c" + this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".expdate" ) ) );
                            if ( API.isStillBanned( bans.get( Integer.valueOf( this.index ) ).intValue( ) ).booleanValue( ) ) {
                                lore.add( utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365L ) {
                                    lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( utils.chat( "&7Status: &c" + this.plugin.bans.getConfig( ).getString( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".status" ) ) );
                            }
                            if ( this.plugin.bans.getConfig( ).getBoolean( "bans." + bans.get( Integer.valueOf( this.index ) ) + ".IP-Banned" ) ) {
                                lore.add( utils.chat( "&7Ip Banned: &aTrue" ) );
                            } else {
                                lore.add( utils.chat( "&7Ip Banned: &cFalse" ) );
                            }
                            lore.add( utils.chat( "&7Ban ID:&a #" + bans.get( Integer.valueOf( this.index ) ) ) );
                            lore.add( utils.chat( "&aLeft click delete or close" ) );
                            lore.add( utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , bans.get( Integer.valueOf( this.index ) ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                            p_head.setItemMeta( meta );
                            this.inventory.addItem( p_head );
                        }
                    }
                }
        } catch ( ParseException parseException ) {
        }
    }
}
