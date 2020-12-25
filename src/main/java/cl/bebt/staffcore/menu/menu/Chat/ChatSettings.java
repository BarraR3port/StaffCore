package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.MenuC;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ChatSettings extends MenuC{
    private final main plugin;

    public ChatSettings( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cClear Chat Manager" );
    }

    @Override
    public int getSlots( ){
        return 45;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "head" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ClearChat( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            p.updateInventory( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "clearAll" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            utils.ccAll( );
            Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The player&r " + p.getName( ) + " &4cleared the chat!" ) );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ChatManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        }
    }

    @Override
    public void setMenuItemsPlayer( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        lore.add( utils.chat( "&c" ) );
        ItemStack ClearChat = new ItemStack( Material.END_CRYSTAL , 1 );
        ItemStack Head = utils.getPlayerHead( p.getName( ) );

        ItemMeta metaTChat = ClearChat.getItemMeta( );
        ItemMeta metaHead = Head.getItemMeta( );

        metaTChat.setDisplayName( utils.chat( "&cClear Server Chat" ) );
        metaHead.setDisplayName( utils.chat( "&5Clear player chat" ) );

        metaTChat.setLore( lore );
        metaHead.setLore( lore );
        metaTChat.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "clearAll" ) , PersistentDataType.STRING , "clearAll" );
        metaHead.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "head" ) , PersistentDataType.STRING , "head" );

        ClearChat.setItemMeta( metaTChat );
        Head.setItemMeta( metaHead );

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
        inventory.setItem( 20 , ClearChat );
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , Head );
        inventory.setItem( 25 , super.redPanel( ) );
    }
}
