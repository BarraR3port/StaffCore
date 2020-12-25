package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class QuantityBaned extends Menu {
    private final main plugin;
    private final Player player;
    private final String baned;
    private final String reason;
    
    public QuantityBaned( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String baned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.baned = baned;
        this.reason = reason;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( "&cChose between &ds/m/h/d" );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        CommandSender sender = p;
        long time = p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "seconds" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.BanCooldown( sender , baned , reason , time , "s" );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "seconds" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "minutes" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.BanCooldown( sender , baned , reason , time , "m" );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "minutes" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "hours" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.BanCooldown( sender , baned , reason , time , "h" );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "hours" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "days" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.BanCooldown( sender , baned , reason , time , "d" );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "days" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
            new ChoseBanType( playerMenuUtility , plugin , player , baned , reason ).open( p );
        }
    }
    
    @Override
    public void setMenuItems( ){
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
        inventory.setItem( 20 , seconds( ) );
        inventory.setItem( 21 , minutes( ) );
        inventory.setItem( 22 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );
        inventory.setItem( 23 , hours( ) );
        inventory.setItem( 24 , days( ) );
    }
    
    public ItemStack seconds( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.MAGENTA_CONCRETE );
        ItemMeta meta = item.getItemMeta( );
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        lore.add( utils.chat( "&cBan &r" + baned + " &c for &a" + time + " &cSeconds." ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4SECONDS" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "seconds" ) , PersistentDataType.STRING , "seconds" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack minutes( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.PURPLE_CONCRETE );
        ItemMeta meta = item.getItemMeta( );
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        lore.add( utils.chat( "&cBan &r" + baned + " &c for &a" + time + " &cMinutes." ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4MINUTES" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "minutes" ) , PersistentDataType.STRING , "minutes" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack hours( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.BLUE_CONCRETE );
        ItemMeta meta = item.getItemMeta( );
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        lore.add( utils.chat( "&cBan &r" + baned + " &c for &a" + time + " &cHours." ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4HOURS" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "hours" ) , PersistentDataType.STRING , "hours" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack days( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.RED_CONCRETE );
        ItemMeta meta = item.getItemMeta( );
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        lore.add( utils.chat( "&cBan &r" + baned + " &c for &a" + time + " &cDays." ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4DAYS" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "days" ) , PersistentDataType.STRING , "days" );
        item.setItemMeta( meta );
        return item;
    }
    
}
