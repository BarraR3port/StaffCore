package cl.bebt.staffcore.menu;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class BanPlayerMenu extends Menu {
    
    protected String baned;
    protected Player baner;
    protected int page = 0;
    protected int maxItemsPerPage = 55;
    protected int index = 0;
    protected String reason;
    protected DateTimeFormatter time;
    
    
    public BanPlayerMenu( PlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }
    
    public void addMenuBorder( ){
        for ( int i = 0; i < 10; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
        for ( int i = 10; i < 17; i++ ) {
            if ( i != 13 ) {
                if ( inventory.getItem( i ) == null ) {
                    inventory.setItem( i , super.greenPanel( ) );
                }
            }
        }
        ItemStack p_head = utils.getPlayerHead( baned );
        ItemMeta meta = p_head.getItemMeta( );
        ArrayList < String > lore = new ArrayList <>( );
        meta.setDisplayName( utils.chat( "&5" + baned ) );
        lore.add( utils.chat( utils.chat( "&7Why you wanna ban &c" ) + baned + "&7?" ) );
        meta.setLore( lore );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        p_head.setItemMeta( meta );
        inventory.setItem( 13 , p_head );
        inventory.setItem( 17 , super.redPanel( ) );
        inventory.setItem( 18 , super.redPanel( ) );
        inventory.setItem( 19 , super.greenPanel( ) );
        inventory.setItem( 25 , super.greenPanel( ) );
        inventory.setItem( 26 , super.redPanel( ) );
        inventory.setItem( 27 , super.redPanel( ) );
        inventory.setItem( 28 , super.greenPanel( ) );
        inventory.setItem( 34 , super.greenPanel( ) );
        inventory.setItem( 35 , super.redPanel( ) );
        inventory.setItem( 36 , super.redPanel( ) );
        for ( int i = 37; i < 44; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.greenPanel( ) );
                
            }
        }
        inventory.setItem( 49 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );
        for ( int i = 44; i < 54; i++ ) {
            if ( inventory.getItem( i ) == null ) {
                inventory.setItem( i , super.redPanel( ) );
            }
        }
    }
    
    public int getMaxItemsPerPage( ){
        return maxItemsPerPage;
    }
}

