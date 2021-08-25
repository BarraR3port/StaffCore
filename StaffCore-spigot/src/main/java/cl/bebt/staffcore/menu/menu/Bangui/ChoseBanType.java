/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.Items.Items;
import cl.bebt.staffcoreapi.utils.BanManager;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
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
        if ( item.equals( Items.ban_ip( uuid , banned ) ) ) {
        
        }
        if ( item.equals( Items.tempBan( uuid , banned ) ) ) {
            p.closeInventory( );
            new AmountBanned( main.getPlayerMenuUtility( p ) , plugin , p , banned , reason ).open( );
        } else if ( item.equals( Items.permBan( uuid , banned ) ) ) {
            p.closeInventory( );
            try {
                BanManager.Ban( p.getUniqueId( ) , Utils.getUUIDFromName( banned ) , reason , false );
            } catch ( ParseException ex ) {
                ex.printStackTrace( );
            }
        } else if ( item.equals( Items.ban_normal( uuid , banned ) ) ) {
            p.closeInventory( );
            PersistentDataUtils.remove( p.getUniqueId( ) , "ban-normal" );
            PersistentDataUtils.save( "ban-ip" , "ban-ip" , p.getUniqueId( ) , PersistentDataType.STRING );
            new ChoseBanType( playerMenuUtility , plugin , player , banned , reason ).open( );
        } else if ( item.equals( Items.ban_ip( uuid , banned ) ) ) {
            PersistentDataUtils.remove( p.getUniqueId( ) , "ban-ip" );
            PersistentDataUtils.save( "ban-normal" , "ban-normal" , p.getUniqueId( ) , PersistentDataType.STRING );
            p.closeInventory( );
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
        inventory.setItem( 20 , Items.tempBan( uuid , banned ) );
        inventory.setItem( 21 , super.redPanel( ) );
        if ( PersistentDataUtils.has( uuid , "ban-ip" ) ) {
            inventory.setItem( 22 , Items.ban_ip( uuid , banned ) );
        } else {
            inventory.setItem( 22 , Items.ban_normal( uuid , banned ) );
        }
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , Items.permBan( uuid , banned ) );
        inventory.setItem( 25 , super.redPanel( ) );
    }
    
    
}
