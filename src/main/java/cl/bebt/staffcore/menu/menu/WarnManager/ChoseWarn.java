package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.WarnPlayer;
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

public class ChoseWarn extends ReportMenu {
    
    private static PlayerMenuUtility playerMenuUtility;
    private final main plugin;
    private final int Id;
    private final String warned;
    private final Player p;
    
    public ChoseWarn( PlayerMenuUtility playerMenuUtility , main plugin , Player p , String warned , int Id ){
        super( playerMenuUtility , plugin , warned );
        ChoseWarn.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;
        this.p = p;
        this.Id = Id;
        this.warned = warned;
    }
    
    public static PlayerMenuUtility getPlayerMenuUtility( ){
        return playerMenuUtility;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "warns.chose.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "delete_warn" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.unWarn( p , plugin , Id );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close_warn" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            CloseWarn( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new Warnings( main.getPlayerMenuUtility( p ) , plugin , p , warned ).open( p );
            }
        }
    }
    
    private void CloseWarn( Player p ){
        String reason = null;
        String created = null;
        String exp = null;
        String warner = null;
        String warned = null;
        String status = "closed";
        if ( utils.mysqlEnabled( ) ) {
            reason = SQLGetter.getWarned( Id , "Reason" );
            created = SQLGetter.getWarned( Id , "Date" );
            exp = SQLGetter.getWarned( Id , "ExpDate" );
            warner = SQLGetter.getWarned( Id , "Baner" );
            warned = SQLGetter.getWarned( Id , "Name" );
            SQLGetter.setWarn( Id , "closed" );
        } else {
            plugin.warns.reloadConfig( );
            reason = plugin.warns.getConfig( ).getString( "warns." + Id + ".reason" );
            created = plugin.warns.getConfig( ).getString( "warns." + Id + ".date" );
            exp = plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" );
            warner = plugin.warns.getConfig( ).getString( "warns." + Id + ".warned_by" );
            warned = plugin.warns.getConfig( ).getString( "warns." + Id + ".name" );
            plugin.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
            plugin.warns.getConfig( ).set( "count" , StaffCoreAPI.getCurrentWarns( ) );
            plugin.warns.saveConfig( );
        }
        SendMsg.sendWarnChangeAlert( Id , p.getName( ) , warner , warned , reason , exp , created , status , utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_ban" );
                for ( String key : utils.getStringList( "warns.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%warner%" , warner );
                    key = key.replace( "%warned%" , warned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%warn_status%" , "CLOSED" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack delete = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeWarn = new ItemStack( Material.NAME_TAG , 1 );
        
        ItemMeta delete_meta = delete.getItemMeta( );
        ItemMeta closeWarn_meta = closeWarn.getItemMeta( );
        
        delete_meta.setDisplayName( utils.chat( "&4UN WARN" ) );
        closeWarn_meta.setDisplayName( utils.chat( "&4CLOSE WARN" ) );
        
        delete_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        delete_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        delete_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        
        closeWarn_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        closeWarn_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        closeWarn_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        
        lore.add( utils.chat( "&8Click to &aUn Warn" ) );
        delete_meta.setLore( lore );
        lore.clear( );
        
        lore.add( utils.chat( "&8Click to &aClose &8the Warn" ) );
        closeWarn_meta.setLore( lore );
        lore.clear( );
        
        
        delete_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "delete_warn" ) , PersistentDataType.STRING , "delete_warn" );
        closeWarn_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close_warn" ) , PersistentDataType.STRING , "close_warn" );
        
        delete.setItemMeta( delete_meta );
        closeWarn.setItemMeta( closeWarn_meta );
        
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
        if ( utils.mysqlEnabled( ) ) {
            if ( SQLGetter.getWarned( Id , "Status" ).equals( "open" ) ) {
                inventory.setItem( 20 , delete );
                inventory.setItem( 21 , super.redPanel( ) );
                inventory.setItem( 22 , close( ) );
                inventory.setItem( 23 , super.redPanel( ) );
                inventory.setItem( 24 , closeWarn );
            } else if ( SQLGetter.getWarned( Id , "Status" ).equals( "closed" ) ) {
                inventory.setItem( 20 , super.redPanel( ) );
                inventory.setItem( 21 , delete );
                inventory.setItem( 22 , super.redPanel( ) );
                inventory.setItem( 23 , close( ) );
                inventory.setItem( 24 , super.redPanel( ) );
            }
        } else {
            if ( plugin.warns.getConfig( ).get( "warns." + Id + ".status" ).equals( "open" ) ) {
                inventory.setItem( 20 , delete );
                inventory.setItem( 21 , super.redPanel( ) );
                inventory.setItem( 22 , close( ) );
                inventory.setItem( 23 , super.redPanel( ) );
                inventory.setItem( 24 , closeWarn );
            } else if ( plugin.warns.getConfig( ).get( "warns." + Id + ".status" ).equals( "closed" ) ) {
                inventory.setItem( 20 , super.redPanel( ) );
                inventory.setItem( 21 , delete );
                inventory.setItem( 22 , super.redPanel( ) );
                inventory.setItem( 23 , close( ) );
                inventory.setItem( 24 , super.redPanel( ) );
            }
        }
    }
}
