package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.Queries.ReportsQuery;
import cl.bebt.staffcore.utils.ReportPlayer;
import cl.bebt.staffcore.utils.utils;
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

public class EditReport extends ReportMenu {
    
    private final main plugin;
    private final int id;
    
    public EditReport( PlayerMenuUtility playerMenuUtility , main plugin , String p2 , int id ){
        super( playerMenuUtility , plugin , p2 );
        this.plugin = plugin;
        this.id = id;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "reports.edit.name" , "menu" , null ) );
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
            ReportPlayer.DeleteReport( p , id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close_report" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ReportPlayer.CloseReport( p , id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "open_report" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ReportPlayer.OpenReport( p , id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( );
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
            if ( ReportsQuery.isOpen( id ) ) {
                inventory.setItem( 24 , closeReport );
            } else {
                inventory.setItem( 24 , openReport );
            }
        } else {
            if ( plugin.reports.getConfig( ).get( "reports." + id + ".status" ).equals( "open" ) ) {
                inventory.setItem( 24 , closeReport );
            } else {
                inventory.setItem( 24 , openReport );
            }
        }
    }
}
