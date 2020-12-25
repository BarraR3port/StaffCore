package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Others.ServerManager;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BanManager extends Menu{
    private final main plugin;

    public BanManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cBan manager" );
    }

    @Override
    public int getSlots( ){
        return 45;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "openBans" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new openBansMenu( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "closeBans" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new closedBansMenu( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                new ServerManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
                e.setCancelled( true );
            } else if ( e.getClick( ).isRightClick( ) ) {
                p.closeInventory( );
            }
        }
    }

    private int Closed( ){
        int close = 0;
        if ( utils.mysqlEnabled( ) ) {
            int count = SQLGetter.getCurrents( "bans" ) + plugin.data.getBanId( );
            for ( int id = 0; id <= count; ) {
                id++;
                try {
                    if ( SQLGetter.getBaned( id , "Status" ).equals( "closed" ) ) {
                        close++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return close;
        } else {
            int count = plugin.baned.getConfig( ).getInt( "current" ) + plugin.baned.getConfig( ).getInt( "count" );
            for ( int id = 0; id <= count; ) {
                plugin.baned.reloadConfig( );
                id++;
                try {
                    if ( plugin.baned.getConfig( ).get( "bans." + id + ".status" ).equals( "closed" ) ) {
                        close++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return close;
        }
    }

    private int Opens( ){
        int opens = 0;
        if ( utils.mysqlEnabled( ) ) {
            int count = SQLGetter.getCurrents( "bans" ) + plugin.data.getBanId( );
            for ( int id = 0; id <= count + 1; ) {
                id++;
                try {
                    if ( SQLGetter.getBaned( id , "Status" ).equals( "open" ) ) {
                        opens++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return opens;
        } else {
            int count = plugin.baned.getConfig( ).getInt( "current" ) + plugin.baned.getConfig( ).getInt( "count" );
            for ( int id = 0; id <= count + 1; ) {
                plugin.baned.reloadConfig( );
                id++;
                try {
                    if ( plugin.baned.getConfig( ).get( "bans." + id + ".status" ).equals( "open" ) ) {
                        opens++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return opens;
        }
    }

    @Override
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
        inventory.setItem( 20 , openBans );
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , closeBans );
    }

}
