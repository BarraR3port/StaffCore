/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.MSGChanel.SendMsg;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONObject;

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
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "delete_ban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            
            //TODO FIX THIS
            //BanManager.unBan( p , this.Id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "close_ban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            CloseBan( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new BanManager( main.getPlayerMenuUtility( p ) , plugin ).open( );
            }
        }
    }
    
    private void CloseBan( Player p ){
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String banned = null;
        if ( Utils.mysqlEnabled( ) ) {
            JSONObject json = BansQuery.getBanInfo( this.Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                baner = json.getString( "Banner" );
                banned = json.getString( "Name" );
                BansQuery.closeBan( this.Id );
            }
        } else {
            Api.bans.reloadConfig( );
            reason = Api.bans.getConfig( ).getString( "bans." + this.Id + ".reason" );
            created = Api.bans.getConfig( ).getString( "bans." + this.Id + ".date" );
            exp = Api.bans.getConfig( ).getString( "bans." + this.Id + ".expdate" );
            baner = Api.bans.getConfig( ).getString( "bans." + this.Id + ".banned_by" );
            banned = Api.bans.getConfig( ).getString( "bans." + this.Id + ".name" );
            Api.bans.getConfig( ).set( "bans." + this.Id + ".status" , "closed" );
            Api.bans.getConfig( ).set( "count" , Utils.getCurrentBans( ) );
            Api.bans.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( this.Id , p.getName( ) , baner , banned , reason , exp , created , "closed" , Utils.getServer( ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                Utils.PlaySound( p , "close_ban" );
                for ( String key : Utils.getStringList( "ban.change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%banned%" , banned );
                    key = key.replace( "%id%" , String.valueOf( this.Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%ban_status%" , "Closed" );
                    Utils.tell( people , key );
                }
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
        delete_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "delete_ban" ) , PersistentDataType.STRING , "delete_ban" );
        closeBan_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close_ban" ) , PersistentDataType.STRING , "close_ban" );
        delete.setItemMeta( delete_meta );
        closeBan.setItemMeta( closeBan_meta );
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
