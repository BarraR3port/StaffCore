package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.ToggleChat;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Quantity extends PaginatedMenu {
    private final main plugin;
    
    private final Player player;
    
    private final Player muted;
    
    public Quantity( PlayerMenuUtility playerMenuUtility , main plugin , Player player ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.muted = Bukkit.getPlayer( player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "muted_player" ) , PersistentDataType.STRING ) );
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "chat.quantity.name" , "menu" , null ) );
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
        int amount = 49;
        Player muted = Bukkit.getPlayer( p.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "muted_player" ) , PersistentDataType.STRING ) );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "seconds" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ToggleChat.MuteCooldown( sender , muted , "s" , time );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "seconds" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "minutes" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ToggleChat.MuteCooldown( sender , muted , "m" , time );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "minutes" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "hours" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ToggleChat.MuteCooldown( sender , muted , "h" , time );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "hours" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "days" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            ToggleChat.MuteCooldown( sender , muted , "d" , time );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "days" ) );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new Amount( playerMenuUtility , main.plugin , p ).open( );
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
            if ( index + 1 <= amount ) {
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
        inventory.setItem( 22 , close( ) );
        inventory.setItem( 23 , hours( ) );
        inventory.setItem( 24 , days( ) );
    }
    
    public ItemStack seconds( ){
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "quantity.seconds.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : utils.getStringList( "quantity.seconds.lore" , "item" ) ) {
            key = key.replace( "%player%" , muted.getName( ) );
            key = key.replace( "%seconds%" , String.valueOf( time ) );
            key = key.replace( "%type%" , "Muted" );
            lore.add( utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( utils.getString( "quantity.seconds.name" , "item" , null ).replace( "%type%" , "Muted" ) ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "seconds" ) , PersistentDataType.STRING , "seconds" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack minutes( ){
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "quantity.minutes.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : utils.getStringList( "quantity.minutes.lore" , "item" ) ) {
            key = key.replace( "%player%" , muted.getName( ) );
            key = key.replace( "%minutes%" , String.valueOf( time ) );
            key = key.replace( "%type%" , "Muted" );
            lore.add( utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( utils.getString( "quantity.minutes.name" , "item" , null ).replace( "%type%" , "Muted" ) ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "minutes" ) , PersistentDataType.STRING , "minutes" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack hours( ){
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "quantity.hours.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : utils.getStringList( "quantity.hours.lore" , "item" ) ) {
            key = key.replace( "%player%" , muted.getName( ) );
            key = key.replace( "%hours%" , String.valueOf( time ) );
            key = key.replace( "%type%" , "Muted" );
            lore.add( utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( utils.getString( "quantity.hours.name" , "item" , null ).replace( "%type%" , "Muted" ) ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "hours" ) , PersistentDataType.STRING , "hours" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack days( ){
        long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "quantity.days.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : utils.getStringList( "quantity.days.lore" , "item" ) ) {
            key = key.replace( "%player%" , muted.getName( ) );
            key = key.replace( "%days%" , String.valueOf( time ) );
            key = key.replace( "%type%" , "Muted" );
            lore.add( utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( utils.getString( "quantity.days.name" , "item" , null ).replace( "%type%" , "Muted" ) ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "days" ) , PersistentDataType.STRING , "days" );
        item.setItemMeta( meta );
        return item;
    }
    
}
