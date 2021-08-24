/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public abstract class WarnPlayerMenu extends Menu {
    
    public String warned;
    protected int page = 0;
    protected int maxItemsPerPage = 55;
    protected int index = 0;
    
    public WarnPlayerMenu( PlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    public void addMenuBorder( ){
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , redPanel( ) );
            }
        }
        for ( int i = 10; i < 17; i++ ) {
            if ( i != 13 ) {
                if ( inventory.getItem( i ) == null ) {
                    inventory.setItem( i , greenPanel( ) );
                }
            }
        }
        ItemStack p_head = Utils.getPlayerHead( warned );
        ItemMeta meta = p_head.getItemMeta( );
        ArrayList < String > lore = new ArrayList <>( );
        meta.setDisplayName( Utils.chat( Utils.getString( "warn.warn_menu.name" , "menu" , null ).replace( "%player%" , warned ) ) );
        for ( String key : Utils.getStringList( "warn.warn_menu.player_lore" , "menu" ) ) {
            key = key.replace( "%warned%" , warned );
            lore.add( Utils.chat( key ) );
        }
        meta.setLore( lore );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        p_head.setItemMeta( meta );
        inventory.setItem( 13 , p_head );
        inventory.setItem( 17 , redPanel( ) );
        inventory.setItem( 18 , redPanel( ) );
        inventory.setItem( 19 , greenPanel( ) );
        inventory.setItem( 25 , greenPanel( ) );
        inventory.setItem( 26 , redPanel( ) );
        inventory.setItem( 27 , redPanel( ) );
        inventory.setItem( 28 , greenPanel( ) );
        inventory.setItem( 34 , greenPanel( ) );
        inventory.setItem( 35 , redPanel( ) );
        inventory.setItem( 36 , redPanel( ) );
        for ( int i = 37; i < 44; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , greenPanel( ) );
                
            }
        }
        inventory.setItem( 49 , close( ) );
        for ( int i = 44; i < 54; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , redPanel( ) );
            }
        }
    }
    
    public int getMaxItemsPerPage( ){
        return maxItemsPerPage;
    }
}

