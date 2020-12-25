package cl.bebt.staffcore.menu.menu.Chat;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.MenuC;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Others.ServerManager;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ChatManager extends MenuC{
    private static final SQLGetter data = main.plugin.data;
    private final main plugin;

    public ChatManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cChat Manager" );
    }

    @Override
    public int getSlots( ){
        return 45;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "mcmanager" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new MuteChatManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            p.updateInventory( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "TStaffOn" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PlayerData.remove( new NamespacedKey( plugin , "staffchat" ) );
            if ( utils.mysqlEnabled( ) ) {
                data.setTrue( p , "staffchat" , "false" );
            }
            utils.tell( p , "&8[&3&lSC&r&8]&r" + " &cOff" );
            new ChatManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            p.updateInventory( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "TStaffOff" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PlayerData.set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
            if ( utils.mysqlEnabled( ) ) {

                data.setTrue( p , "staffchat" , "true" );
            }
            utils.tell( p , "&8[&3&lSC&r&8]&r" + " &aOn" );
            new ChatManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            p.updateInventory( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "ccmanager" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChatSettings( main.getPlayerMenuUtility( p ) , plugin ).open( p );
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

    @Override
    public void setMenuItemsPlayer( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack TStaffChatOn = new ItemStack( Material.ENDER_EYE , 1 );
        ItemStack TStaffChatOff = new ItemStack( Material.ENDER_EYE , 1 );

        ItemStack ClearChat = new ItemStack( Material.PAPER , 1 );
        ItemStack MuteChat = new ItemStack( Material.MAP , 1 );

        ItemMeta metaTStaffOn = TStaffChatOn.getItemMeta( );
        ItemMeta metaTStaffOff = TStaffChatOff.getItemMeta( );
        ItemMeta metaTChat = ClearChat.getItemMeta( );
        ItemMeta metaMChat = MuteChat.getItemMeta( );

        metaTStaffOn.setDisplayName( utils.chat( "&8Staff Chat &aOn" ) );
        metaTStaffOff.setDisplayName( utils.chat( "&8Staff Chat &cOff" ) );
        metaTChat.setDisplayName( utils.chat( "&cClear Chat Manager" ) );
        metaMChat.setDisplayName( utils.chat( "&5Mute Chat Manager" ) );

        lore.add( utils.chat( "&7Click to turn &cOFF &7the Staff Chat." ) );
        metaTStaffOn.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click to turn &aON &7the Staff Chat." ) );
        metaTStaffOff.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click to open the &dClear Chat Manager." ) );
        metaTChat.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click to open the &dMute Chat Manager." ) );
        metaMChat.setLore( lore );

        metaTStaffOn.addEnchant( Enchantment.MENDING , 1 , false );
        metaTStaffOn.addItemFlags( ItemFlag.HIDE_ENCHANTS );

        metaTStaffOn.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "TStaffOn" ) , PersistentDataType.STRING , "TStaffOn" );
        metaTStaffOff.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "TStaffOff" ) , PersistentDataType.STRING , "TStaffOff" );
        metaTChat.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ccmanager" ) , PersistentDataType.STRING , "ccmanager" );
        metaMChat.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "mcmanager" ) , PersistentDataType.STRING , "mcmanager" );

        TStaffChatOn.setItemMeta( metaTStaffOn );
        TStaffChatOff.setItemMeta( metaTStaffOff );
        ClearChat.setItemMeta( metaTChat );
        MuteChat.setItemMeta( metaMChat );

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
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            inventory.setItem( 20 , TStaffChatOn );
        } else if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            inventory.setItem( 20 , TStaffChatOff );
        }
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , MuteChat );
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , ClearChat );
        inventory.setItem( 25 , super.redPanel( ) );
        inventory.setItem( 31 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "Close" ) );
    }
}
