package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class QuantityBanned extends Menu {
    private final main plugin;
    private final Player player;
    private final String banned;
    private final String reason;
    
    public QuantityBanned( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String banned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.banned = banned;
        this.reason = reason;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "bangui.quantity_banned.name" , "menu" , null ) );
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
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "time" );
        for ( String key : inventorySection.getKeys( false ) ) {
            if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , key ) , PersistentDataType.STRING ) ) {
                p.closeInventory( );
                BanPlayer.BanCooldown( sender , banned , reason , time , "s" );
                p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , key ) );
                p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "amount" ) );
            }
        }
        if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new AmountBanned( playerMenuUtility , plugin , player , banned , reason ).open( );
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
        inventory.setItem( 22 , close( ) );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "time" );
        for ( String key : inventorySection.getKeys( false ) ) {
            long time = player.getPersistentDataContainer( ).get( new NamespacedKey( plugin , "amount" ) , PersistentDataType.INTEGER );
            String name = utils.getString( "time." + key + ".name" , "item" , null );
            String material = utils.getString( "time." + key + ".material" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : utils.getStringList( "time." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Ban" );
                key2 = key2.replace( "%time%" , String.valueOf( time ) );
                key2 = key2.replace( "%player%" , banned );
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
            utils.tell( player , "&0[&5Warning&0] &7Try to delete the StaffCore/items.yml file and restart the server" );
        }
    }
    
}
