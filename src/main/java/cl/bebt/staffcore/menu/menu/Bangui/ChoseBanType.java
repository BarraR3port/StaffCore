package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.BanPlayer;
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

public class ChoseBanType extends Menu{
    private final main plugin;
    private final Player player;
    private final String baned;
    private final String reason;

    public ChoseBanType( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String baned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.baned = baned;
        this.reason = reason;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cChose Ban Type" );
    }

    @Override
    public int getSlots( ){
        return 45;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "tempban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new AmountBaned( main.getPlayerMenuUtility( p ) , plugin , p , baned , reason ).open( p );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "permban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.BanPlayer( p , baned , reason );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "ban-normal" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "ban-ip" ) , PersistentDataType.STRING , "ban-ip" );
            new ChoseBanType( playerMenuUtility , plugin , player , baned , reason ).open( p );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "ban-ip" ) );
            new ChoseBanType( playerMenuUtility , plugin , player , baned , reason ).open( p );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
            new BanMenu( playerMenuUtility , main.plugin , player , baned ).open( p );
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
        inventory.setItem( 20 , tempBan( ) );
        inventory.setItem( 21 , super.redPanel( ) );
        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
            inventory.setItem( 22 , ban_ip( ) );
        } else {
            inventory.setItem( 22 , ban_normal( ) );
        }
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , permBan( ) );
        inventory.setItem( 25 , super.redPanel( ) );
    }

    public ItemStack tempBan( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.CARVED_PUMPKIN );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&cClick to &aTemp Ban &r" + baned ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&aTemp Ban" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "tempban" ) , PersistentDataType.STRING , "tempban" );
        item.setItemMeta( meta );
        return item;
    }

    public ItemStack permBan( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.JACK_O_LANTERN );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&cClick to &4Perm Ban &r" + baned ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Perm Ban" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "permban" ) , PersistentDataType.STRING , "permban" );
        item.setItemMeta( meta );
        return item;
    }

    public ItemStack ban_ip( ){
        ItemStack item = new ItemStack( Material.REDSTONE );
        ItemMeta meta = item.getItemMeta( );
        meta.addEnchant( Enchantment.PROTECTION_ENVIRONMENTAL , 1 , true );
        meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        meta.setDisplayName( utils.chat( "&7Ban IP: &aTrue" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ban-ip" ) , PersistentDataType.STRING , "ban-ip" );
        item.setItemMeta( meta );
        return item;
    }

    public ItemStack ban_normal( ){
        ItemStack item = new ItemStack( Material.REDSTONE );
        ItemMeta meta = item.getItemMeta( );
        meta.setDisplayName( utils.chat( "&7Ban IP: &cFalse" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ban-normal" ) , PersistentDataType.STRING , "ban-normal" );
        item.setItemMeta( meta );
        return item;
    }

}
