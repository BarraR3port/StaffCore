/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EditBan extends ReportMenu {
    private static PlayerMenuUtility playerMenuUtility;
    
    private final main plugin;
    
    private final int Id;
    
    public EditBan( PlayerMenuUtility playerMenuUtility , main plugin , String p2 , int Id ){
        super( playerMenuUtility , plugin , p2 );
        EditBan.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;
        this.Id = Id;
    }
    
    public static PlayerMenuUtility getPlayerMenuUtility( ){
        return playerMenuUtility;
    }
    
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "banlist.edit_ban.name" , "menu" , null ) );
    }
    
    public int getSlots( ){
        return 45;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ItemStack item = e.getCurrentItem( );
        if ( PersistentDataUtils.has( item, "delete_ban", PersistentDataType.STRING ) ){
            p.closeInventory( );
            
            //TODO FIX THIS
            //BanManager.unBan( p , this.Id );
            cl.bebt.staffcoreapi.utils.BanManager.CloseBan( p, this.Id );
            e.setCancelled( true );
        } else if ( PersistentDataUtils.has( item, "close_ban", PersistentDataType.STRING ) ){
            p.closeInventory( );
            cl.bebt.staffcoreapi.utils.BanManager.CloseBan( p, this.Id );
            e.setCancelled( true );
        } else if ( PersistentDataUtils.has( item, "panel", PersistentDataType.STRING ) ){
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new BanManager( main.getPlayerMenuUtility( p ) , plugin ).open( );
            }
        }
    }
    
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack delete = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeBan = new ItemStack( Material.NAME_TAG , 1 );
        ItemMeta delete_meta = delete.getItemMeta( );
        ItemMeta closeBan_meta = closeBan.getItemMeta( );
        delete_meta.setDisplayName( Utils.chat( "&4UNBAN" ) );
        closeBan_meta.setDisplayName( Utils.chat( "&4CLOSE BAN" ) );
        delete_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        delete_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        delete_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        closeBan_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        closeBan_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        closeBan_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        lore.add( Utils.chat( "&8Click to &aUn Ban" ) );
        delete_meta.setLore( lore );
        lore.clear( );
        lore.add( Utils.chat( "&8Click to &aClose &8the Ban" ) );
        closeBan_meta.setLore( lore );
        lore.clear( );
        delete.setItemMeta( delete_meta );
        closeBan.setItemMeta( closeBan_meta );
        PersistentDataUtils.save( "delete_ban","delete_ban", delete, uuid, PersistentDataType.STRING );
        PersistentDataUtils.save( "close_ban", "close_ban", closeBan, uuid, PersistentDataType.STRING );
        int i;
        for ( i = 0; i < 10; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , bluePanel( ) );
        }
        for ( i = 10; i < 17; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , redPanel( ) );
        }
        this.inventory.setItem( 17 , bluePanel( ) );
        this.inventory.setItem( 18 , bluePanel( ) );
        this.inventory.setItem( 19 , redPanel( ) );
        this.inventory.setItem( 25 , redPanel( ) );
        this.inventory.setItem( 26 , bluePanel( ) );
        this.inventory.setItem( 27 , bluePanel( ) );
        for ( i = 28; i < 35; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , redPanel( ) );
        }
        for ( i = 35; i < 45; i++ ) {
            if ( this.inventory.getItem( i ) == null )
                this.inventory.setItem( i , bluePanel( ) );
        }
        if ( Utils.mysqlEnabled( ) ) {
            if ( BansQuery.isStillBanned( this.Id ) ) {
                this.inventory.setItem( 20 , delete );
                this.inventory.setItem( 21 , redPanel( ) );
                this.inventory.setItem( 22 , close( ) );
                this.inventory.setItem( 23 , redPanel( ) );
                this.inventory.setItem( 24 , closeBan );
            } else {
                this.inventory.setItem( 20 , redPanel( ) );
                this.inventory.setItem( 21 , delete );
                this.inventory.setItem( 22 , redPanel( ) );
                this.inventory.setItem( 23 , close( ) );
                this.inventory.setItem( 24 , redPanel( ) );
            }
        } else if ( Api.bans.getConfig( ).get( "bans." + this.Id + ".status" ).equals( "open" ) ) {
            this.inventory.setItem( 20 , delete );
            this.inventory.setItem( 21 , redPanel( ) );
            this.inventory.setItem( 22 , close( ) );
            this.inventory.setItem( 23 , redPanel( ) );
            this.inventory.setItem( 24 , closeBan );
        } else {
            this.inventory.setItem( 20 , redPanel( ) );
            this.inventory.setItem( 21 , delete );
            this.inventory.setItem( 22 , redPanel( ) );
            this.inventory.setItem( 23 , close( ) );
            this.inventory.setItem( 24 , redPanel( ) );
        }
    }
}
