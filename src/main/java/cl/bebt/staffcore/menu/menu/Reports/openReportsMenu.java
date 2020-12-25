package cl.bebt.staffcore.menu.menu.Reports;

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

import java.util.ArrayList;
import java.util.HashMap;

public class openReportsMenu extends PaginatedMenu{
    private final main plugin;

    public openReportsMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cOpen Reports" );
    }

    @Override
    public int getSlots( ){
        return 54;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        HashMap < Integer, Integer > reports = new HashMap <>( );
        int num = 0;
        for ( int id = 0; id <= count( ); ) {
            id++;
            try {
                if ( utils.mysqlEnabled( ) ) {
                    if ( SQLGetter.getReportStatus( id ).equals( "open" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                } else {
                    if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "open" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                }
            } catch ( NullPointerException err ) {
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int id = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "open-id" ) , PersistentDataType.INTEGER );
                new chose( main.getPlayerMenuUtility( p ) , main.plugin , jugador , id ).open( p );
            } else if ( e.getClick( ).isRightClick( ) ) {
                try {
                    Bukkit.getPlayer( jugador ).getLocation( );
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "tp.teleport_to" ) + jugador );
                } catch ( NullPointerException err ) {
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cThe player is not online, or don't exist" );
                }
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
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
                if ( !((index + 1) > reports.size( )) ) {
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
            return SQLGetter.getCurrents( "reports" ) + plugin.data.getReportId( );
        } else {
            return plugin.reports.getConfig( ).getInt( "current" ) + plugin.reports.getConfig( ).getInt( "count" );
        }
    }

    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        HashMap < Integer, Integer > reports = new HashMap <>( );
        int num = 0;
        for ( int id = 0; id <= count( ); ) {
            id++;
            try {
                if ( utils.mysqlEnabled( ) ) {
                    if ( SQLGetter.getReportStatus( id ).equals( "open" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                } else {
                    if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "open" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                }
            } catch ( NullPointerException err ) {
            }
        }
        if ( reports != null && !reports.isEmpty( ) ) {
            for ( int i = 1; i <= getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index > reports.size( ) ) break;
                if ( reports.get( index ) != null ) {
                    //////////////////////////////
                    if ( utils.mysqlEnabled( ) ) {
                        ItemStack p_head = utils.getPlayerHead( SQLGetter.get( "reports" , reports.get( index ) , "name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( SQLGetter.get( "reports" , reports.get( index ) , "name" ) );
                        lore.add( utils.chat( "&7Reported by: " + SQLGetter.get( "reports" , reports.get( index ) , "Reporter" ) ) );
                        lore.add( utils.chat( "&7Reason: &b" + SQLGetter.get( "reports" , reports.get( index ) , "Reason" ) ) );
                        lore.add( utils.chat( "&7Date: &c" + SQLGetter.get( "reports" , reports.get( index ) , "Date" ) ) );
                        lore.add( utils.chat( "&7Status: &a" + SQLGetter.get( "reports" , reports.get( index ) , "Status" ) ) );
                        lore.add( utils.chat( "&7Report ID:&a " + reports.get( index ) ) );
                        lore.add( utils.chat( "&aLeft click delete or close" ) );
                        lore.add( utils.chat( "&aRight click to tp" ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , reports.get( index ) );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                        p_head.setItemMeta( meta );
                        inventory.addItem( p_head );
                    } else {
                        plugin.reports.reloadConfig( );
                        ItemStack p_head = utils.getPlayerHead( plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".name" ).toString( ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".name" ).toString( ) );
                        lore.add( utils.chat( "&7Reported by: " + plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".reported_by" ) ) );
                        lore.add( utils.chat( "&7Reason: &b" + plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".reason" ) ) );
                        lore.add( utils.chat( "&7Date: &c" + plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".time" ) ) );
                        lore.add( utils.chat( "&7Status: &a" + plugin.reports.getConfig( ).get( "reports." + reports.get( index ) + ".status" ) ) );
                        lore.add( utils.chat( "&7Report ID:&a " + reports.get( index ) ) );
                        lore.add( utils.chat( "&aLeft click delete or close" ) );
                        lore.add( utils.chat( "&aRight click to tp" ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , reports.get( index ) );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                        p_head.setItemMeta( meta );
                        inventory.addItem( p_head );
                    }
                    /////////////////////////////
                }
            }
        }
    }
}
