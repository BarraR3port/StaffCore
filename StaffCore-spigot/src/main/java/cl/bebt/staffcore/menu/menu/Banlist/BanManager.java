/*
 * Copyright (c) 2021-2022. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Staff.ServerManager;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

public class BanManager extends Menu {
    private final main plugin;
    
    public BanManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "banlist.ban_manager.name" , "menu" , null ) );
    }
    
    public int getSlots( ){
        return 45;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ItemStack item = e.getCurrentItem();
        if (PersistentDataUtils.has( item, "openBans", PersistentDataType.STRING ) ){
            p.closeInventory( );
            new openBansMenu( main.getPlayerMenuUtility( p ) , this.plugin ).open( );
            e.setCancelled( true );
        } else if (PersistentDataUtils.has( item, "closeBans", PersistentDataType.STRING ) ){
            p.closeInventory( );
            new closedBansMenu( main.getPlayerMenuUtility( p ) , this.plugin ).open( );
            e.setCancelled( true );
        } else if ( PersistentDataUtils.has( item, "panel", PersistentDataType.STRING ) ){
            e.setCancelled( true );
        } else if ( Objects.equals( e.getCurrentItem( ) , close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ServerManager( main.getPlayerMenuUtility( p ) , this.plugin ).open( p );
                e.setCancelled( true );
            }
        }
    }
    
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack openBans = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeBans = new ItemStack( Material.NAME_TAG , 1 );
        ItemMeta o_meta = openBans.getItemMeta( );
        ItemMeta c_meta = closeBans.getItemMeta( );
        o_meta.setDisplayName( Utils.chat( "&aOpen Bans" ) );
        c_meta.setDisplayName( Utils.chat( "&cClosed Bans" ) );
        lore.add( Utils.chat( "&8&lClick to open all the opened Bans" ) );
        lore.add( Utils.chat( "&8&lCurrent Opened: &a" + Utils.getOpen( UpdateType.BAN ) ) );
        o_meta.setLore( lore );
        lore.clear( );
        lore.add( Utils.chat( "&8&lClick to open all the closed Bans" ) );
        lore.add( Utils.chat( "&8&lCurrent Closed: &a" + Utils.getClosed( UpdateType.BAN ) ) );
        c_meta.setLore( lore );
        lore.clear( );
        openBans.setItemMeta( o_meta );
        closeBans.setItemMeta( c_meta );
        PersistentDataUtils.save("openBans", "openBans", openBans, uuid, PersistentDataType.STRING );
        PersistentDataUtils.save("closeBans", "closeBans", closeBans, uuid, PersistentDataType.STRING );
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
        this.inventory.setItem( 20 , openBans );
        this.inventory.setItem( 21 , redPanel( ) );
        this.inventory.setItem( 22 , close( ) );
        this.inventory.setItem( 23 , redPanel( ) );
        this.inventory.setItem( 24 , closeBans );
    }
}
