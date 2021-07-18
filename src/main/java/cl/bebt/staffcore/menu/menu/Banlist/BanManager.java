package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Staff.ServerManager;
import cl.bebt.staffcore.sql.Queries.BansQuery;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BanManager extends Menu {
    private final main plugin;
    
    public BanManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    public String getMenuName( ){
        return utils.chat( utils.getString( "banlist.ban_manager.name" , "menu" , null ) );
    }
    
    public int getSlots( ){
        return 45;
    }
    
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "openBans" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new openBansMenu( main.getPlayerMenuUtility( p ) , this.plugin ).open( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "closeBans" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new closedBansMenu( main.getPlayerMenuUtility( p ) , this.plugin ).open( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( this.plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ServerManager( main.getPlayerMenuUtility( p ) , this.plugin ).open( p );
                e.setCancelled( true );
            }
        }
    }
    
    private int Closed( ){
        int close = 0;
        if ( utils.mysqlEnabled( ) ) {
            return BansQuery.getClosedBans( ).size( );
        }
        int count = this.plugin.bans.getConfig( ).getInt( "current" ) + this.plugin.bans.getConfig( ).getInt( "count" );
        for ( int id = 0; id <= count; ) {
            this.plugin.bans.reloadConfig( );
            id++;
            try {
                if ( this.plugin.bans.getConfig( ).get( "bans." + id + ".status" ).equals( "closed" ) )
                    close++;
            } catch ( NullPointerException ignored ) {
            }
        }
        return close;
    }
    
    private int Opens( ){
        int opens = 0;
        if ( utils.mysqlEnabled( ) ) {
            return BansQuery.getOpenBans( ).size( );
        }
        int count = this.plugin.bans.getConfig( ).getInt( "current" ) + this.plugin.bans.getConfig( ).getInt( "count" );
        for ( int id = 0; id <= count + 1; ) {
            this.plugin.bans.reloadConfig( );
            id++;
            try {
                if ( this.plugin.bans.getConfig( ).get( "bans." + id + ".status" ).equals( "open" ) )
                    opens++;
            } catch ( NullPointerException ignored ) {
            }
        }
        return opens;
    }
    
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack openBans = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeBans = new ItemStack( Material.NAME_TAG , 1 );
        ItemMeta o_meta = openBans.getItemMeta( );
        ItemMeta c_meta = closeBans.getItemMeta( );
        o_meta.setDisplayName( utils.chat( "&aOpen Bans" ) );
        c_meta.setDisplayName( utils.chat( "&cClosed Bans" ) );
        lore.add( utils.chat( "&8&lClick to open all the opened Bans" ) );
        lore.add( utils.chat( "&8&lCurrent Opened: &a" + Opens( ) ) );
        o_meta.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&8&lClick to open all the closed Bans" ) );
        lore.add( utils.chat( "&8&lCurrent Closed: &a" + Closed( ) ) );
        c_meta.setLore( lore );
        lore.clear( );
        o_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "openBans" ) , PersistentDataType.STRING , "openBans" );
        c_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "closeBans" ) , PersistentDataType.STRING , "closeBans" );
        openBans.setItemMeta( o_meta );
        closeBans.setItemMeta( c_meta );
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
