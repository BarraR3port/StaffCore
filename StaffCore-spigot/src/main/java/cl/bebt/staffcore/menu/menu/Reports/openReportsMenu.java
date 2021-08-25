/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.ReportsQuery;
import cl.bebt.staffcoreapi.utils.TpManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class openReportsMenu extends PaginatedMenu {
    private final main plugin;
    
    public openReportsMenu( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "reports.open.name" , "menu" , null ) );
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
        if ( Utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedReports = ReportsQuery.getClosedReports( );
            for ( Integer closedReport : closedReports ) {
                num++;
                reports.put( num , closedReport );
            }
        } else {
            for ( int id = 0; id <= Utils.count( UpdateType.REPORT ); ) {
                id++;
                try {
                    if ( Api.reports.getConfig( ).getString( "reports." + id + ".status" ).equals( "open" ) ) {
                        num++;
                        reports.put( num , id );
                    }
                    
                } catch ( NullPointerException ignored ) {
                }
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String jugador = e.getCurrentItem( ).getItemMeta( ).getDisplayName( );
            if ( e.getClick( ).isLeftClick( ) ) {
                int id = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "open-id" ) , PersistentDataType.INTEGER );
                new EditReport( main.getPlayerMenuUtility( p ) , plugin , jugador , id ).open( );
            } else if ( e.getClick( ).isRightClick( ) ) {
                TpManager.tpToPlayer( p , jugador );
            }
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , plugin ).open( );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                Utils.tell( p , Utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
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
                Utils.tell( p , Utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        HashMap < Integer, Integer > reports = new HashMap <>( );
        int num = 0;
        JSONObject json = new JSONObject( );
        if ( Utils.mysqlEnabled( ) ) {
            ArrayList < Integer > closedReports = ReportsQuery.getOpenReports( );
            for ( Integer closedReport : closedReports ) {
                num++;
                reports.put( num , closedReport );
            }
            json = ReportsQuery.getOpenReportInfo( );
        } else {
            for ( int id = 0; id <= Utils.count( UpdateType.REPORT ); ) {
                id++;
                try {
                    if ( Api.reports.getConfig( ).getString( "reports." + id + ".status" ).equals( "open" ) ) {
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
                    if ( Utils.mysqlEnabled( ) ) {
                        String rawReportInfo = json.get( String.valueOf( reports.get( index ) ) ).toString( )
                                .replace( "[" , "" )
                                .replace( "]" , "" );
                        JSONObject reportInfo = new JSONObject( rawReportInfo );
                        ItemStack p_head = Utils.getPlayerHead( reportInfo.getString( "Name" ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( reportInfo.getString( "Name" ) );
                        lore.add( Utils.chat( "&7Reported by: " + reportInfo.getString( "Reporter" ) ) );
                        lore.add( Utils.chat( "&7Reason: &b" + reportInfo.getString( "Reason" ) ) );
                        lore.add( Utils.chat( "&7Date: &c" + reportInfo.getString( "Date" ) ) );
                        lore.add( Utils.chat( "&7Status: &a" + reportInfo.getString( "Status" ) ) );
                        lore.add( Utils.chat( "&7Report ID:&a " + reports.get( index ) ) );
                        lore.add( Utils.chat( "&aLeft click delete or open" ) );
                        lore.add( Utils.chat( "&aRight click to tp" ) );
                        meta.setLore( lore );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open-id" ) , PersistentDataType.INTEGER , reports.get( index ) );
                        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open" ) , PersistentDataType.STRING , "open" );
                        p_head.setItemMeta( meta );
                        inventory.addItem( p_head );
                    } else {
                        Api.reports.reloadConfig( );
                        ItemStack p_head = Utils.getPlayerHead( Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".name" ).toString( ) );
                        ItemMeta meta = p_head.getItemMeta( );
                        ArrayList < String > lore = new ArrayList <>( );
                        meta.setDisplayName( Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".name" ).toString( ) );
                        lore.add( Utils.chat( "&7Reported by: " + Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".reported_by" ) ) );
                        lore.add( Utils.chat( "&7Reason: &b" + Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".reason" ) ) );
                        lore.add( Utils.chat( "&7Date: &c" + Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".time" ) ) );
                        lore.add( Utils.chat( "&7Status: &a" + Api.reports.getConfig( ).get( "reports." + reports.get( index ) + ".status" ) ) );
                        lore.add( Utils.chat( "&7Report ID:&a " + reports.get( index ) ) );
                        lore.add( Utils.chat( "&aLeft click delete or close" ) );
                        lore.add( Utils.chat( "&aRight click to tp" ) );
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
