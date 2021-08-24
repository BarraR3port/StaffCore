/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcoreapi.EntitiesUtils.PersistentDataUtils;
import cl.bebt.staffcoreapi.Enums.PersistentDataType;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AmountBanned extends PaginatedMenu {
    
    private final main plugin;
    
    private final Player player;
    
    private final String banned;
    
    private final String reason;
    
    private final UUID uuid;
    
    public AmountBanned( PlayerMenuUtility playerMenuUtility , main plugin , Player player , String banned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.player = player;
        this.banned = banned;
        this.reason = reason;
        this.uuid = player.getUniqueId( );
    }
    
    @Override
    public String getMenuName( ){
        return Utils.chat( Utils.getString( "bangui.amount_banned.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        int amount = Utils.getInt( "inventories.time_limit" , null ) + 1;
        ItemStack item = e.getCurrentItem( );
        if ( PersistentDataUtils.has( item , "amount" , PersistentDataType.INTEGER ) ) {
            p.closeInventory( );
            Integer time = PersistentDataUtils.getInteger( item , "amount" );
            new QuantityBanned( playerMenuUtility , plugin , player , banned , reason , time ).open( );
        }
        if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ChoseBanType( playerMenuUtility , plugin , player , banned , reason ).open( );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                Utils.tell( p , Utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= amount ) {
                page++;
                p.closeInventory( );
                open( );
            } else {
                Utils.tell( p , Utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
        
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        int amount = Utils.getInt( "inventories.time_limit" , null ) + 1;
        PersistentDataUtils.remove( uuid , "seconds" );
        PersistentDataUtils.remove( uuid , "amount" );
        for ( int i = 1; i < getMaxItemsPerPage( ) + 1; i++ ) {
            index = getMaxItemsPerPage( ) * page + i;
            if ( index >= amount ) break;
            //////////////////////////////
            ItemStack clock = new ItemStack( Material.CLOCK );
            ItemMeta meta = clock.getItemMeta( );
            meta.setDisplayName( Utils.chat( "&a" ) + index );
            clock.setItemMeta( meta );
            PersistentDataUtils.save( "amount" , index , clock , uuid , PersistentDataType.INTEGER );
            inventory.addItem( clock );
            /////////////////////////////
        }
        
        
    }
}
