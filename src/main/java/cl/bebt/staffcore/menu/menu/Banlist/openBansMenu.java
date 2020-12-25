package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.listeners.onPlayerJoin;
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

public class openBansMenu extends PaginatedMenu{
    private final main plugin;

    public openBansMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cOpen Bans:" );
    }

    @Override
    public int getSlots( ){
        return 54;
    }

    @Override
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
                        bans.put( num , id );
                    }
                } else {
                    if ( Objects.equals( plugin.baned.getConfig( ).getString( "bans." + id + ".status" ) , "open" ) ) {
                        num++;
                        bans.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int id = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "open-id" ) , PersistentDataType.INTEGER );
                new ChoseBan( main.getPlayerMenuUtility( p ) , main.plugin , jugador , id ).open( p );
            } else if ( e.getClick( ).isRightClick( ) ) {
                try {
                    p.teleport( Bukkit.getPlayer( jugador ).getLocation( ) );
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "tp.teleport_to" ) + jugador );
                } catch ( NullPointerException err ) {
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cThe player is not online, or not exist" );
                }
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                new BanManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
                e.setCancelled( true );
            } else if ( e.getClick( ).isRightClick( ) ) {
                p.closeInventory( );
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( page == 0 ) {
                    utils.tell( p , "&7You are already in the first page" );
                } else {
                    page = page - 1;
                    super.open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                e.setCancelled( true );
                if ( !((index + 1) > bans.size( )) ) {
                    page = page + 1;
                    super.open( p );
                } else {
                    utils.tell( p , "&7You are already in the last page" );
                }
            }
        }
    }

    private int count( ){
        if ( utils.mysqlEnabled( ) ) {
            return SQLGetter.getCurrents( "bans" ) + plugin.data.getBanId( );
        } else {
            return plugin.baned.getConfig( ).getInt( "current" ) + plugin.baned.getConfig( ).getInt( "count" );
        }
    }

    @Override
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
                            bans.put( num , id );
                        }
                    } else {
                        if ( plugin.baned.getConfig( ).getString( "bans." + id + ".status" ).equals( "open" ) ) {
                            num++;
                            bans.put( num , id );
                        }
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            if ( bans != null && !bans.isEmpty( ) ) {
                for ( int i = 1; i <= getMaxItemsPerPage( ); i++ ) {
                    index = getMaxItemsPerPage( ) * page + i;
                    if ( index > bans.size( ) ) break;
                    if ( bans.get( index ) != null ) {
                        //////////////////////////////
                        plugin.baned.reloadConfig( );
                        if ( utils.mysqlEnabled( ) ) {
                            Date now = new Date( );
                            String exp = SQLGetter.getBaned( bans.get( index ) , "ExpDate" );
                            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                            Date d2 = null;
                            d2 = format.parse( exp );
                            long remaining = (d2.getTime( ) - now.getTime( )) / 1000;
                            long Days = TimeUnit.SECONDS.toDays( remaining );
                            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                            long Hours = TimeUnit.SECONDS.toHours( Seconds );
                            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );

                            ItemStack p_head = utils.getPlayerHead( SQLGetter.getBaned( bans.get( index ) , "Name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( SQLGetter.getBaned( bans.get( index ) , "name" ) );
                            lore.add( utils.chat( "&7Baned by: " + SQLGetter.getBaned( bans.get( index ) , "Baner" ) ) );
                            lore.add( utils.chat( "&7Reason: &b" + SQLGetter.getBaned( bans.get( index ) , "Reason" ) ) );
                            lore.add( utils.chat( "&7Created date: &c" + SQLGetter.getBaned( bans.get( index ) , "Date" ) ) );
                            lore.add( utils.chat( "&7Expiry date: &c" + SQLGetter.getBaned( bans.get( index ) , "ExpDate" ) ) );
                            if ( onPlayerJoin.isStillBaned( bans.get( index ) ) ) {
                                lore.add( utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365 ) {
                                    lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( utils.chat( "&7Status: &cClosed" ) );
                            }
                            if ( SQLGetter.getBaned( bans.get( index ) , "IP_Baned" ).equals( "true" ) ) {
                                lore.add( utils.chat( "&7Ip Baned: &aTrue" ) );
                            } else {
                                lore.add( utils.chat( "&7Ip Baned: &cFalse" ) );
                            }
                            lore.add( utils.chat( "&7Ban ID:&a #" + bans.get( index ) ) );
                            lore.add( utils.chat( "&aLeft click delete or close" ) );
                            lore.add( utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , bans.get( index ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                            p_head.setItemMeta( meta );
                            inventory.addItem( p_head );
                        } else {
                            Date now = new Date( );
                            String exp = plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".expdate" );
                            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
                            Date d2 = null;
                            d2 = format.parse( exp );
                            long remaining = (d2.getTime( ) - now.getTime( )) / 1000;
                            long Days = TimeUnit.SECONDS.toDays( remaining );
                            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
                            long Hours = TimeUnit.SECONDS.toHours( Seconds );
                            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
                            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
                            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
                            ItemStack p_head = utils.getPlayerHead( plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".name" ) );
                            ItemMeta meta = p_head.getItemMeta( );
                            ArrayList < String > lore = new ArrayList <>( );
                            meta.setDisplayName( plugin.baned.getConfig( ).get( "bans." + bans.get( index ) + ".name" ).toString( ) );
                            lore.add( utils.chat( "&7Baned by: " + plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".baned_by" ) ) );
                            lore.add( utils.chat( "&7Reason: &b" + plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".reason" ) ) );
                            lore.add( utils.chat( "&7Created date: &c" + plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".date" ) ) );
                            lore.add( utils.chat( "&7Expiry date: &c" + plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".expdate" ) ) );
                            if ( onPlayerJoin.isStillBaned( bans.get( index ) ) ) {
                                lore.add( utils.chat( "&7Status: &aOpen" ) );
                                if ( Days > 365 ) {
                                    lore.add( utils.chat( "&7Time left: &4PERMANENT" ) );
                                } else {
                                    lore.add( utils.chat( "&7Time left: &c" + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" ) );
                                }
                            } else {
                                lore.add( utils.chat( "&7Status: &c" + plugin.baned.getConfig( ).getString( "bans." + bans.get( index ) + ".status" ) ) );
                            }
                            if ( plugin.baned.getConfig( ).getBoolean( "bans." + bans.get( index ) + ".IP-Baned" ) ) {
                                lore.add( utils.chat( "&7Ip Baned: &aTrue" ) );
                            } else {
                                lore.add( utils.chat( "&7Ip Baned: &cFalse" ) );
                            }
                            lore.add( utils.chat( "&7Ban ID:&a #" + bans.get( index ) ) );
                            lore.add( utils.chat( "&aLeft click delete or close" ) );
                            lore.add( utils.chat( "&aRight click to tp" ) );
                            meta.setLore( lore );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , bans.get( index ) );
                            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                            p_head.setItemMeta( meta );
                            inventory.addItem( p_head );
                        }
                        /////////////////////////////
                    }
                }
            }
        } catch ( ParseException ignored ) {
        }
    }
}
