package cl.bebt.staffcore.menu.menu.Warn;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.WarnPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class WarnQuantity extends PaginatedMenu {
    private final main plugin;
    private final String warned;
    private final String reason;
    private final Long time;
    
    public WarnQuantity( PlayerMenuUtility playerMenuUtility , main plugin , String warned , String reason , Long amount ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.warned = warned;
        this.reason = reason;
        this.time = amount;
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
        int amount = 49;
        
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "seconds" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "s" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "minutes" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "m" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "hours" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "h" );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "days" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            WarnPlayer.createWarn( p , warned , reason , time , "d" );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new WarnAmount( playerMenuUtility , main.plugin , p , warned , reason ).open( );
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
        Player p = playerMenuUtility.getOwner( );
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
        inventory.setItem( 22 , close( ) );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "time" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = utils.getString( "time." + key + ".name" , "item" , null );
            String material = utils.getString( "time." + key + ".material" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : utils.getStringList( "time." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Warn" );
                key2 = key2.replace( "%time%" , String.valueOf( time ) );
                key2 = key2.replace( "%player%" , warned );
                lore.add( utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( utils.chat( name ) );
            meta.setLore( lore );
            meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , key );
            item.setItemMeta( meta );
            inventory.addItem( item );
        }
        if ( inventory.getItem( 20 ) == null ) {
            utils.tell( p , "&0[&5Warning&0] &7Try to delete the StaffCore/items.yml file and restart the server" );
        }
    }
    
    
}
