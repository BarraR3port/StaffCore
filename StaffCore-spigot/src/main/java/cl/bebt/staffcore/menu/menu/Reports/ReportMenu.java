/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Reports;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.ReportPlayerMenu;
import cl.bebt.staffcore.utils.ReportPlayer;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.utils.Utils;
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

public class ReportMenu extends ReportPlayerMenu {
    private final main plugin;
    
    public ReportMenu( PlayerMenuUtility playerMenuUtility , main plugin , String p2 ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.reported = p2;
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "reports.menu.name" , "menu" , null ).replace( "%player%" , reported ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ConfigurationSection inventorySection = Api.items.getConfig( ).getConfigurationSection( "punish_items" );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "other" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            PlayerData.set( new NamespacedKey( main.plugin , "reportmsg" ) , PersistentDataType.STRING , reported );
            Utils.tell( p , Utils.getString( "bans.other_reason" , "lg" , "sv" ) );
        } else {
            for ( String key : inventorySection.getKeys( false ) ) {
                String reason = Utils.getString( "punish_items." + key + ".reason" , "item" , null );
                if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , key ) , PersistentDataType.STRING ) ) {
                    new ReportPlayer( p , reason , reported );
                    p.closeInventory( );
                }
            }
        }
        if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ConfigurationSection inventorySection = Api.items.getConfig( ).getConfigurationSection( "punish_items" );
        for ( String key : inventorySection.getKeys( false ) ) {
            String name = Utils.getString( "punish_items." + key + ".name" , "item" , null );
            String material = Utils.getString( "punish_items." + key + ".material" , "item" , null );
            String reason = Utils.getString( "punish_items." + key + ".reason" , "item" , null );
            ArrayList < String > lore = new ArrayList <>( );
            ItemStack item = new ItemStack( Material.valueOf( material ) );
            ItemMeta meta = item.getItemMeta( );
            for ( String key2 : Utils.getStringList( "punish_items." + key + ".lore" , "item" ) ) {
                key2 = key2.replace( "%punish%" , "Report" );
                key2 = key2.replace( "%player%" , reported );
                lore.add( Utils.chat( key2 ) );
            }
            meta.setLore( lore );
            meta.setDisplayName( Utils.chat( name ) );
            if ( key.equalsIgnoreCase( "other" ) ) {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , "other" );
            } else {
                meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , key ) , PersistentDataType.STRING , reason );
            }
            item.setItemMeta( meta );
            inventory.addItem( item );
            
        }
    }
}
