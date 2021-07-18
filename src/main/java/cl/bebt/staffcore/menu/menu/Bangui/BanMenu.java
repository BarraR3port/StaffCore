package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.BanPlayerMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BanMenu extends BanPlayerMenu {
    private final main plugin;
    
    public BanMenu( PlayerMenuUtility playerMenuUtility , main plugin , Player p , String p2 ){
        super( playerMenuUtility );
        this.plugin = plugin;
        super.banned = p2;
        super.baner = p;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "ban_menu.name" , "menu" , null ).replace( "%player%" , banned ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "punish_items" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String reason = utils.getString( "punish_items." + key + ".reason" , "item" , null );
            if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , key ) , PersistentDataType.STRING ) ) {
                p.closeInventory( );
                new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , reason ).open( );
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "other" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            PlayerData.set( new NamespacedKey( main.plugin , "banmsg" ) , PersistentDataType.STRING , banned );
            utils.tell( p , utils.getString( "bans.other_reason" , "lg" , "sv" ) );
        }
        if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ConfigurationSection inventorySection = plugin.items.getConfig( ).getConfigurationSection( "punish_items" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = utils.getString( "punish_items." + key + ".name" , "item" , null );
            String material = utils.getString( "punish_items." + key + ".material" , "item" , null );
            String reason = utils.getString( "punish_items." + key + ".reason" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : utils.getStringList( "punish_items." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Ban" );
                key2 = key2.replace( "%player%" , banned );
                lore.add( utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( utils.chat( name ) );
            if ( key.equalsIgnoreCase( "other" ) ) {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , "other" );
            } else {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , reason );
            }
            item.setItemMeta( meta );
            inventory.addItem( item );
            
        }
        baner.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "ban-ip" ) );
    }
    
}
