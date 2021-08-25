/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.utils.BanManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class QuantityBanned extends Menu {
    
    private final main plugin;
    
    private final Player player;
    
    private final String banned;
    
    private final String reason;
    
    private final Integer amount;
    
    private final UUID uuid;
    
    public QuantityBanned( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String banned , String reason , Integer amount ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.banned = banned;
        this.reason = reason;
        this.amount = amount;
        this.uuid = player.getUniqueId( );
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "bangui.quantity_banned.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ItemStack item = e.getCurrentItem( );
        if ( PersistentDataUtils.has( item , "timeType" , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String key = PersistentDataUtils.getString( item , "timeType" );
            BanManager.TempBan( uuid , Utils.getUUIDFromName( banned ) , reason , Utils.ConvertDate( amount , key ) , false );
            PersistentDataUtils.clearItem( item );
        }
        
        if ( Objects.equals( e.getCurrentItem( ) , close( ) ) ) {
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
        ConfigurationSection inventorySection = Api.items.getConfig( ).getConfigurationSection( "time" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = Utils.getString( "time." + key + ".name" , "item" , null );
            String material = Utils.getString( "time." + key + ".material" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : Utils.getStringList( "time." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Ban" );
                key2 = key2.replace( "%time%" , String.valueOf( amount ) );
                key2 = key2.replace( "%player%" , banned );
                lore.add( Utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( Utils.chat( name ) );
            meta.setLore( lore );
            item.setItemMeta( meta );
            PersistentDataUtils.save( "timeType" , key , item , uuid , PersistentDataType.STRING );
            inventory.addItem( item );
        }
        if ( inventory.getItem( 20 ) == null ) {
            Utils.tell( player , "&0[&5Warning&0] &7Try to delete the StaffCore/items.yml file and restart the server" );
        }
    }
    
}
