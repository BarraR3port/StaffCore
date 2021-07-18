package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.Queries.ReportsQuery;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONObject;

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
        if ( utils.mysqlEnabled( ) ) {
            ArrayList < Integer > openReports = ReportsQuery.getClosedReports( );
            for ( Integer openReport : openReports ) {
                num++;
                reports.put( num , openReport );
            }
        } else {
            for ( int id = 0; id <= utils.count( "reports" ); ) {
                id++;
                try {
                    if ( plugin.reports.getConfig( ).getString( "reports." + id + ".status" ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                    
                } catch ( NullPointerException ignored ) {
                }
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int id = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "close-id" ) , PersistentDataType.INTEGER );
                new EditReport( main.getPlayerMenuUtility( p ) , main.plugin , jugador , id ).open( );
            } else if ( e.getClick( ).isRightClick( ) ) {
                TpPlayers.tpToPlayer( p , jugador );
            }
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                utils.tell( p , utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= reports.size( ) ) {
                page++;
                p.closeInventory( );
                open( );
            } else {
                utils.tell( p , utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        HashMap < Integer, Integer > reports = new HashMap <>( );
        int num = 0;
        JSONObject json = new JSONObject( );
        if ( utils.mysqlEnabled( ) ) {
            ArrayList < Integer > openReports = ReportsQuery.getClosedReports( );
            for ( Integer openReport : openReports ) {
                num++;
                reports.put( num , openReport );
            }
            json = ReportsQuery.getClosedReportInfo( );
        } else {
            for ( int id = 0; id <= utils.count( "reports" ); ) {
                id++;
                try {
                    if ( plugin.reports.getConfig( ).getString( "reports." + id + ".status" ).equals( "close" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                    
                } catch ( NullPointerException ignored ) {
                }
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
                        String rawReportInfo = json.get( String.valueOf( reports.get( index ) ) ).toString( )
                                .replace( "[" , "" )
                                .replace( "]" , "" );
                        
                        JSONObject reportInfo = new JSONObject( rawReportInfo );
                        ItemStack p_head = utils.getPlayerHead( reportInfo.getString( "Name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( reportInfo.getString( "Name" ) );
                        lore.add( utils.chat( "&7Reported by: " + reportInfo.getString( "Reporter" ) ) );
                        lore.add( utils.chat( "&7Reason: &b" + reportInfo.getString( "Reason" ) ) );
                        lore.add( utils.chat( "&7Date: &c" + reportInfo.getString( "Date" ) ) );
                        lore.add( utils.chat( "&7Status: &c" + reportInfo.getString( "Status" ) ) );
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
