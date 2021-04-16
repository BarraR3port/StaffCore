package cl.bebt.staffcore.menu.menu.Others;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.MenuC;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Banlist.BanManager;
import cl.bebt.staffcore.menu.menu.Chat.ChatManager;
import cl.bebt.staffcore.menu.menu.ClientSettings.ClientSettings;
import cl.bebt.staffcore.menu.menu.Reports.ReportManager;
import cl.bebt.staffcore.menu.menu.WarnManager.WarnManager;
import cl.bebt.staffcore.utils.Items;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ServerManager extends MenuC {
    
    private final main plugin;
    
    public ServerManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( "&cServer Manager" );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem().getType( ) == Material.PLAYER_HEAD ){
            p.closeInventory();
            new ClientSettings( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "bans" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new BanManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "reports" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ReportManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "chat" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChatManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "warn" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new WarnManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            e.setCancelled( true );
        }
    }
    
    @Override
    public void setMenuItemsPlayer( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack BanManager = new ItemStack( Material.TOTEM_OF_UNDYING , 1 );
        ItemStack ReportManager = new ItemStack( Material.BLAZE_ROD , 1 );
        ItemStack ChatManager = new ItemStack( Material.PAPER , 1 );
        ItemStack WarnManager = new ItemStack( Material.LEAD , 1 );
        
        
        ItemMeta metaBans = BanManager.getItemMeta( );
        ItemMeta metaReports = ReportManager.getItemMeta( );
        ItemMeta metaChat = ChatManager.getItemMeta( );
        ItemMeta metaWarn = WarnManager.getItemMeta( );
        
        
        metaBans.setDisplayName( utils.chat( "&cBan Manager" ) );
        metaReports.setDisplayName( utils.chat( "&cReport Manager" ) );
        metaChat.setDisplayName( utils.chat( "&cChat Manager" ) );
        metaWarn.setDisplayName( utils.chat( "&cWarn Manager" ) );
        
        
        lore.add( utils.chat( "&7Click open the Ban Manager." ) );
        metaBans.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click open the Report Manager." ) );
        metaReports.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click to open the Chat Manager." ) );
        metaChat.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&7Click to open the Warn Manager." ) );
        metaWarn.setLore( lore );
        lore.clear( );
        
        metaBans.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "bans" ) , PersistentDataType.STRING , "bans" );
        metaReports.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "reports" ) , PersistentDataType.STRING , "reports" );
        metaChat.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "chat" ) , PersistentDataType.STRING , "chat" );
        metaWarn.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "warn" ) , PersistentDataType.STRING , "warn" );
        
        BanManager.setItemMeta( metaBans );
        ReportManager.setItemMeta( metaReports );
        ChatManager.setItemMeta( metaChat );
        WarnManager.setItemMeta( metaWarn );
        
        
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
        inventory.setItem( 20 , BanManager );
        inventory.setItem( 21 , ReportManager );
        inventory.setItem( 22 , super.redPanel( ) );
        inventory.setItem( 23 , ChatManager );
        inventory.setItem( 24 , WarnManager );
        
        inventory.setItem( 13 , Items.PlayerStatus( p ) );
        inventory.setItem( 31 , Items.ServerStatus( ) );
    }
}
