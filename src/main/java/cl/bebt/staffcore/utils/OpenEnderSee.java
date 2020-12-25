package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Inventory.enderSee;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OpenEnderSee{

    public OpenEnderSee( Player p , Player target ){
        if ( !main.enderSee.containsValue( target ) ) {
            main.enderSee.put( p , target );
        }
        new enderSee( new PlayerMenuUtility( p ) , target ).open( p );
    }

    public static void updateInventory( Player p , Player target ){
        HashMap < Integer, ItemStack > hotbar = new HashMap <>( );
        for ( int i = 0; i < 9; i++ ) {
            try {
                hotbar.put( i , target.getInventory( ).getItem( i ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        HashMap < Integer, ItemStack > inv = new HashMap <>( );
        for ( int c = 0; c <= 35; c++ ) {
            try {
                inv.put( c , target.getEnderChest( ).getItem( c ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        for ( int a = 0; a < hotbar.size( ); a++ ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( a + 36 , hotbar.get( a ) );
        }
        for ( int b = 0; b < inv.size( ); b++ ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( b , inv.get( b ) );
        }
        p.getOpenInventory( ).getTopInventory( ).setItem( 27 , Items.greenPanel( ) );
        p.getOpenInventory( ).getTopInventory( ).setItem( 28 , Items.potions( target ) );
        p.getOpenInventory( ).getTopInventory( ).setItem( 29 , Items.food( target ) );
        if ( target.getInventory( ).getItemInMainHand( ) != null ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( 30 , target.getInventory( ).getItemInMainHand( ) );
        }
        p.getOpenInventory( ).getTopInventory( ).setItem( 31 , Items.head( target ) );
        if ( target.getInventory( ).getHelmet( ) != null ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( 32 , target.getInventory( ).getHelmet( ) );
        }
        if ( target.getInventory( ).getChestplate( ) != null ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( 33 , target.getInventory( ).getChestplate( ) );
        }
        if ( target.getInventory( ).getLeggings( ) != null ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( 34 , target.getInventory( ).getLeggings( ) );
        }
        if ( target.getInventory( ).getBoots( ) != null ) {
            p.getOpenInventory( ).getTopInventory( ).setItem( 35 , target.getInventory( ).getBoots( ) );
        }
    }

    public static void removeItem( Player target , int slot ){
        //main.itemHold.put( p , item );
        target.getEnderChest( ).setItem( slot , new ItemStack( Material.AIR ) );
    }

    public static void setItem( Player p , Player target , int slot ){
        //ItemStack item = main.itemHold.get( p );
        ItemStack item = p.getItemOnCursor( );
        target.getEnderChest( ).setItem( slot , item );
        Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> {
            if ( target.getInventory( ).getItemInMainHand( ) != null ) {
                p.getOpenInventory( ).getTopInventory( ).setItem( 30 , target.getInventory( ).getItemInMainHand( ) );
            }
        } , 6L );
        //main.itemHold.remove( p );
    }

    public static void setItem( Player p , Player target , int slot , ItemStack item ){
        //ItemStack item = main.itemHold.get( p );
        target.getEnderChest( ).setItem( slot , item );
        Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> {
            if ( target.getInventory( ).getItemInMainHand( ) != null ) {
                p.getOpenInventory( ).getTopInventory( ).setItem( 30 , target.getInventory( ).getItemInMainHand( ) );
            }
        } , 6L );
        //main.itemHold.remove( p );
    }

    public static Player getOwner( Player target ){
        Player owner = null;
        try {
            for ( Map.Entry < Player, Player > players : main.enderSee.entrySet( ) ) {
                if ( main.enderSee.get( players.getKey( ) ).equals( target ) ) {
                    owner = players.getKey( );
                }
            }
        } catch ( NullPointerException ignored ) {
        }
        return owner;
    }

}
