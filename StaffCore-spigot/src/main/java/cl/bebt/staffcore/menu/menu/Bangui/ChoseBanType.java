/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.BanManager;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ChoseBanType extends Menu {
    
    private final main plugin;
    
    private final Player player;
    
    private final String banned;
    
    private final String reason;
    
    private final UUID uuid;
    
    public ChoseBanType( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String banned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.banned = banned;
        this.reason = reason;
        this.uuid = player.getUniqueId( );
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "bangui.chose_ban_type.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ItemStack item = e.getCurrentItem( );
        if ( PersistentDataUtils.has( item , "tempban" , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new AmountBanned( main.getPlayerMenuUtility( p ) , plugin , p , banned , reason ).open( );
        } else if ( PersistentDataUtils.has( item , "permban" , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            try {
                BanManager.Ban( p.getUniqueId( ) , Utils.getUUIDFromName( banned ) , reason , false );
            } catch ( ParseException ex ) {
                ex.printStackTrace( );
            }
        } else if ( PersistentDataUtils.has( item , "ban-normal" , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataUtils.save( "ban-ip" , "ban-ip" , p.getUniqueId( ) , PersistentDataType.STRING );
            new ChoseBanType( playerMenuUtility , plugin , player , banned , reason ).open( );
        } else if ( PersistentDataUtils.has( item , "ban-ip" , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataUtils.remove( p.getUniqueId( ) , "ban-ip" );
            new ChoseBanType( playerMenuUtility , plugin , player , banned , reason ).open( );
        } else if ( Objects.equals( e.getCurrentItem( ) , close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new BanMenu( playerMenuUtility , main.plugin , player , banned ).open( );
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
        inventory.setItem( 20 , tempBan( ) );
        inventory.setItem( 21 , super.redPanel( ) );
        if ( PersistentDataUtils.has( uuid , "ban-ip" ) ) {
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
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "quantity.temp.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : Utils.getStringList( "quantity.temp.lore" , "item" ) ) {
            key = key.replace( "%player%" , banned );
            key = key.replace( "%type%" , "Ban" );
            lore.add( Utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( Utils.chat( Utils.getString( "quantity.temp.name" , "item" , null ).replace( "%type%" , "Ban" ) ) );
        item.setItemMeta( meta );
        PersistentDataUtils.save( "tempban" , "tempban" , item , uuid , PersistentDataType.STRING );
        return item;
    }
    
    public ItemStack permBan( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "quantity.perm.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : Utils.getStringList( "quantity.perm.lore" , "item" ) ) {
            key = key.replace( "%player%" , banned );
            key = key.replace( "%type%" , "Ban" );
            lore.add( Utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( Utils.chat( Utils.getString( "quantity.perm.name" , "item" , null ).replace( "%type%" , "Ban" ) ) );
        item.setItemMeta( meta );
        PersistentDataUtils.save( "permban" , "permban" , item , uuid , PersistentDataType.STRING );
        return item;
    }
    
    public ItemStack ban_ip( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "quantity.ip.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        meta.addEnchant( Enchantment.PROTECTION_ENVIRONMENTAL , 1 , true );
        meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        for ( String key : Utils.getStringList( "quantity.ip.lore" , "item" ) ) {
            key = key.replace( "%player%" , banned );
            key = key.replace( "%type%" , "Ban" );
            lore.add( Utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( Utils.chat( Utils.getString( "quantity.ip.name" , "item" , null ).replace( "%type%" , "Ban" ) ) );
        item.setItemMeta( meta );
        PersistentDataUtils.save( "ban-ip" , "ban-ip" , item , uuid , PersistentDataType.STRING );
        return item;
    }
    
    public ItemStack ban_normal( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "quantity.normal.item" , "item" , null ) ) );
        ItemMeta meta = item.getItemMeta( );
        for ( String key : Utils.getStringList( "quantity.normal.lore" , "item" ) ) {
            key = key.replace( "%player%" , banned );
            key = key.replace( "%type%" , "Ban" );
            lore.add( Utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.setDisplayName( Utils.chat( Utils.getString( "quantity.normal.name" , "item" , null ).replace( "%type%" , "Ban" ) ) );
        item.setItemMeta( meta );
        PersistentDataUtils.save( "ban-normal" , "ban-normal" , item , uuid , PersistentDataType.STRING );
        return item;
    }
    
}
