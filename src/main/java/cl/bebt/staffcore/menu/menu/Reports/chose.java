package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class chose extends ReportMenu {
    
    private final main plugin;
    private final int id;
    
    public chose( PlayerMenuUtility playerMenuUtility , main plugin , String p2 , int id ){
        super( playerMenuUtility , plugin , p2 );
        this.plugin = plugin;
        this.id = id;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "reports.chose.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "delete_report" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            DeleteReport( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close_report" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            CloseReport( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open_report" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            OpenReport( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        }
    }
    
    private void CloseReport( Player p ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "closed";
        if ( utils.mysqlEnabled( ) ) {
            reporter = SQLGetter.get( "reports" , id , "Reporter" );
            reported = SQLGetter.get( "reports" , id , "Name" );
            reason = SQLGetter.get( "reports" , id , "Reason" );
            date = SQLGetter.get( "reports" , id , "Date" );
            plugin.data.set( "reports" , id , "close" );
        } else {
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id + ".status" , "close" );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "count" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%report_status%" , status );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    private void OpenReport( Player p ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "open";
        if ( utils.mysqlEnabled( ) ) {
            reporter = SQLGetter.get( "reports" , id , "Reporter" );
            reported = SQLGetter.get( "reports" , id , "Name" );
            reason = SQLGetter.get( "reports" , id , "Reason" );
            date = SQLGetter.get( "reports" , id , "Date" );
            plugin.data.set( "reports" , id , status );
        } else {
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id + ".status" , status );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "count" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "open_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , status );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public void DeleteReport( Player p ){
        String reporter = null;
        String reported = null;
        String reason = null;
        String date = null;
        String status = "deleted";
        if ( utils.mysqlEnabled( ) ) {
            reporter = SQLGetter.get( "reports" , id , "Reporter" );
            reported = SQLGetter.get( "reports" , id , "Name" );
            reason = SQLGetter.get( "reports" , id , "Reason" );
            date = SQLGetter.get( "reports" , id , "Date" );
            SQLGetter.deleteReport( id );
        } else {
            plugin.reports.reloadConfig( );
            reason = plugin.reports.getConfig( ).getString( "reports." + id + ".reason" );
            date = plugin.reports.getConfig( ).getString( "reports." + id + ".time" );
            reporter = plugin.reports.getConfig( ).getString( "reports." + id + ".reported_by" );
            reported = plugin.reports.getConfig( ).getString( "reports." + id + ".name" );
            plugin.reports.getConfig( ).set( "reports." + id , null );
            plugin.reports.saveConfig( );
            plugin.reports.getConfig( ).set( "current" , StaffCoreAPI.getCurrentReports( ) );
            plugin.reports.saveConfig( );
        }
        SendMsg.sendReportChangeAlert( id , p.getName( ) , reporter , reported , reason , date , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "delete_report" );
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%reporter%" , reporter );
                    key = key.replace( "%reported%" , reported );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , "DELETED" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack delete = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeReport = new ItemStack( Material.NAME_TAG , 1 );
        ItemStack openReport = new ItemStack( Material.NAME_TAG , 1 );
        
        ItemMeta delete_meta = delete.getItemMeta( );
        ItemMeta closeReport_meta = closeReport.getItemMeta( );
        ItemMeta openReport_meta = openReport.getItemMeta( );
        
        delete_meta.setDisplayName( utils.chat( "&4DELETE REPORT" ) );
        closeReport_meta.setDisplayName( utils.chat( "&4CLOSE REPORT" ) );
        openReport_meta.setDisplayName( utils.chat( "&aOPEN REPORT" ) );
        
        delete_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        delete_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        delete_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        
        closeReport_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        closeReport_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        closeReport_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        
        openReport_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        openReport_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        openReport_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        
        lore.add( utils.chat( "&8Click to &4Delete &8the report" ) );
        delete_meta.setLore( lore );
        lore.clear( );
        
        lore.add( utils.chat( "&8Click to &aClose &8the report" ) );
        closeReport_meta.setLore( lore );
        lore.clear( );
        
        lore.add( utils.chat( "&8Click to &aOpen &8the report" ) );
        openReport_meta.setLore( lore );
        lore.clear( );
        
        delete_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "delete_report" ) , PersistentDataType.STRING , "delete_report" );
        closeReport_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close_report" ) , PersistentDataType.STRING , "close_report" );
        openReport_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "open_report" ) , PersistentDataType.STRING , "open_report" );
        
        delete.setItemMeta( delete_meta );
        closeReport.setItemMeta( closeReport_meta );
        openReport.setItemMeta( openReport_meta );
        
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.bluePanel( ) );
            }
        }
        for ( int i = 10; i < 17; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        inventory.setItem( 17 , super.bluePanel( ) );
        inventory.setItem( 18 , super.bluePanel( ) );
        inventory.setItem( 19 , super.redPanel( ) );
        inventory.setItem( 25 , super.redPanel( ) );
        inventory.setItem( 26 , super.bluePanel( ) );
        inventory.setItem( 27 , super.bluePanel( ) );
        for ( int i = 28; i < 35; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        for ( int i = 35; i < 45; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.bluePanel( ) );
            }
        }
        inventory.setItem( 20 , delete );
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , close( ) );
        inventory.setItem( 23 , super.redPanel( ) );
        if ( utils.mysqlEnabled( ) ) {
            if ( SQLGetter.get( "reports" , id , "Status" ).equals( "open" ) ) {
                inventory.setItem( 24 , closeReport );
            } else if ( SQLGetter.get( "reports" , id , "Status" ).equals( "close" ) ) {
                inventory.setItem( 24 , openReport );
            }
        } else {
            if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "open" ) ) {
                inventory.setItem( 24 , closeReport );
            } else if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "close" ) ) {
                inventory.setItem( 24 , openReport );
            }
        }
    }
}
