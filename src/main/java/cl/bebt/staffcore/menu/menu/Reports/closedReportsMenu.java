package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;

public class closedReportsMenu extends PaginatedMenu {
    private final main plugin;
    
    public closedReportsMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "reports.closed.name" , "menu" , null ) );
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
                    if ( SQLGetter.getReportStatus( id ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                } else {
                    if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int id = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "close-id" ) , PersistentDataType.INTEGER );
                new chose( main.getPlayerMenuUtility( p ) , main.plugin , jugador , id ).open( p );
            } else if ( e.getClick( ).isRightClick( ) ) {
                TpPlayers.tpToPlayer( p , jugador );
            }
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                utils.tell( p , utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( p );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= reports.size( ) ) {
                page++;
                p.closeInventory( );
                open( p );
            } else {
                utils.tell( p , utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
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
                    if ( SQLGetter.getReportStatus( id ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                } else {
                    if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        if ( reports != null && !reports.isEmpty( ) ) {
            for ( int i = 1; i <= getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index > reports.size( ) ) break;
                if ( reports.get( index ) != null ) {
                    //////////////////////////////
                    plugin.reports.reloadConfig( );
                    if ( utils.mysqlEnabled( ) ) {
                        ItemStack p_head = utils.getPlayerHead( SQLGetter.get( "reports" , reports.get( index ) , "name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( SQLGetter.get( "reports" , reports.get( index ) , "name" ) );
                        lore.add( utils.chat( "&7Reported by: " + SQLGetter.get( "reports" , reports.get( index ) , "Reporter" ) ) );
                        lore.add( utils.chat( "&7Reason: &b" + SQLGetter.get( "reports" , reports.get( index ) , "Reason" ) ) );
                        lore.add( utils.chat( "&7Date: &c" + SQLGetter.get( "reports" , reports.get( index ) , "Date" ) ) );
                        lore.add( utils.chat( "&7Status: &c" + SQLGetter.get( "reports" , reports.get( index ) , "Status" ) ) );
                        lore.add( utils.chat( "&7Report ID:&a " + reports.get( index ) ) );
                        lore.add( utils.chat( "&aLeft click delete or open" ) );
                        lore.add( utils.chat( "&aRight click to tp" ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close-id" ) , PersistentDataType.INTEGER , reports.get( index ) );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close" ) , PersistentDataType.STRING , "close" );
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
                        lore.add( utils.chat( "&aLeft click delete or open" ) );
                        lore.add( utils.chat( "&aRight click to tp" ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close-id" ) , PersistentDataType.INTEGER , reports.get( index ) );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close" ) , PersistentDataType.STRING , "close" );
                        p_head.setItemMeta( meta );
                        inventory.addItem( p_head );
                    }
                    /////////////////////////////
                }
            }
        }
    }
}
