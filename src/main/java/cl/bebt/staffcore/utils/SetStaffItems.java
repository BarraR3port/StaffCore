package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;

public class SetStaffItems {
    
    private static main plugin;
    
    public SetStaffItems( main plugin ){
        SetStaffItems.plugin = plugin;
    }
    
    
    public static void On( Player p ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        PlayerData.set( new NamespacedKey( plugin , "staff_items" ) , PersistentDataType.STRING , Serializer.itemStackArrayToBase64( p.getInventory( ).getContents( ) ) );
        PlayerData.set( new NamespacedKey( plugin , "staff_armor" ) , PersistentDataType.STRING , Serializer.itemStackArrayToBase64( p.getInventory( ).getArmorContents( ) ) );
        p.getInventory( ).clear( );
        p.getInventory( ).setArmorContents( null );
        if ( !PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            SetVanish.setVanish( p , true );
        }
        p.getInventory( ).setItem( 0 , vanishOn( ) );
        p.getInventory( ).setItem( 2 , freeze( ) );
        p.getInventory( ).setItem( 3 , serverManager( ) );
        p.getInventory( ).setItem( 5 , randomTp( ) );
        p.getInventory( ).setItem( 6 , InvSee( ) );
        p.getInventory( ).setItem( 8 , staffOff( ) );
        PlayerData.set( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING , "staff" );
        utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.staff_enabled" ) );
        if ( utils.mysqlEnabled( ) ) {
            SQLGetter.set( p.getName( ) , "staff" , "true" );
        }
    }
    
    public static void Off( Player p ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        Bukkit.getScheduler( ).scheduleSyncDelayedTask( plugin , ( ) -> {
            p.getInventory( ).clear( );
            try {
                ItemStack[] inventory = Serializer.itemStackArrayFromBase64( PlayerData.get( new NamespacedKey( plugin , "staff_items" ) , PersistentDataType.STRING ) );
                ItemStack[] armor = Serializer.itemStackArrayFromBase64( PlayerData.get( new NamespacedKey( plugin , "staff_armor" ) , PersistentDataType.STRING ) );
                p.getInventory( ).setContents( inventory );
                p.getInventory( ).setArmorContents( armor );
            } catch ( IOException | NullPointerException ignore ) {
            }
            p.getInventory( ).removeItem( vanishOn( ) );
            p.getInventory( ).removeItem( vanishOff( ) );
            p.getInventory( ).removeItem( staffOff( ) );
            p.getInventory( ).removeItem( InvSee( ) );
            p.getInventory( ).removeItem( serverManager( ) );
            p.getInventory( ).removeItem( freeze( ) );
            p.getInventory( ).removeItem( randomTp( ) );
            p.updateInventory( );
        } , 6L );
        if ( !p.getGameMode( ).equals( GameMode.CREATIVE ) || p.getGameMode( ).equals( GameMode.SPECTATOR ) ) {
            p.setAllowFlight( false );
            p.setFlying( false );
        }
        SetVanish.setVanish( p , false );
        PlayerData.remove( new NamespacedKey( plugin , "staff" ) );
        utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.staff_disabled" ) );
        if ( utils.mysqlEnabled( ) ) {
            SQLGetter.set( p.getName( ) , "staff" , "false" );
        }
        
    }
    
    public static ItemStack vanishOn( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack vanishOn = new ItemStack( Material.LIME_DYE );
        ItemMeta vanishOn_meta = vanishOn.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        vanishOn_meta.setLore( lore );
        vanishOn_meta.setDisplayName( utils.chat( "&7Vanish &aOn" ) );
        vanishOn_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "vanishOn" ) , PersistentDataType.STRING , "vanishOn" );
        vanishOn.setItemMeta( vanishOn_meta );
        return vanishOn;
    }
    
    public static ItemStack vanishOff( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack vanishOff = new ItemStack( Material.GRAY_DYE );
        ItemMeta vanishOff_meta = vanishOff.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        vanishOff_meta.setLore( lore );
        vanishOff_meta.setDisplayName( utils.chat( "&7Vanish &cOff" ) );
        vanishOff_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "vanishOff" ) , PersistentDataType.STRING , "vanishOff" );
        vanishOff.setItemMeta( vanishOff_meta );
        return vanishOff;
    }
    
    public static ItemStack staffOff( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack staffOff = new ItemStack( Material.RED_DYE );
        ItemMeta staffOff_meta = staffOff.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        staffOff_meta.setLore( lore );
        staffOff_meta.setDisplayName( utils.chat( "&7Staff &cOFF" ) );
        staffOff_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffOff" ) , PersistentDataType.STRING , "staffOff" );
        staffOff.setItemMeta( staffOff_meta );
        return staffOff;
    }
    
    public static ItemStack serverManager( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack chatManager = new ItemStack( Material.TOTEM_OF_UNDYING );
        ItemMeta chatManager_meta = chatManager.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        chatManager_meta.setLore( lore );
        chatManager_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        chatManager_meta.setDisplayName( utils.chat( "&cServer Manager" ) );
        chatManager_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "servermanager" ) , PersistentDataType.STRING , "servermanager" );
        chatManager.setItemMeta( chatManager_meta );
        return chatManager;
    }
    
    public static ItemStack freeze( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack freeze = new ItemStack( Material.BLUE_ICE );
        ItemMeta freeze_meta = freeze.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        freeze_meta.setLore( lore );
        freeze_meta.setDisplayName( utils.chat( "&bFreeze" ) );
        freeze_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "freeze" ) , PersistentDataType.STRING , "freeze" );
        freeze.setItemMeta( freeze_meta );
        return freeze;
    }
    
    public static ItemStack randomTp( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack randomTp = new ItemStack( Material.CLOCK );
        ItemMeta randomTp_meta = randomTp.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        randomTp_meta.setLore( lore );
        randomTp_meta.setDisplayName( utils.chat( "&5Random Tp" ) );
        randomTp_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "randomTp" ) , PersistentDataType.STRING , "randomTp" );
        randomTp.setItemMeta( randomTp_meta );
        return randomTp;
    }
    
    public static ItemStack reportManager( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack report = new ItemStack( Material.BLAZE_ROD );
        ItemMeta report_meta = report.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        report_meta.setLore( lore );
        report_meta.setDisplayName( utils.chat( "&cReport Manager" ) );
        report_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "report" ) , PersistentDataType.STRING , "report" );
        report.setItemMeta( report_meta );
        return report;
    }
    
    public static ItemStack InvSee( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack InvSee = new ItemStack( Material.CHEST );
        ItemMeta InvSee_meta = InvSee.getItemMeta( );
        lore.add( utils.chat( "&dStaff utils" ) );
        InvSee_meta.setLore( lore );
        InvSee_meta.setDisplayName( utils.chat( "&bInvsee" ) );
        InvSee_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "invsee" ) , PersistentDataType.STRING , "invsee" );
        InvSee.setItemMeta( InvSee_meta );
        return InvSee;
    }
}
