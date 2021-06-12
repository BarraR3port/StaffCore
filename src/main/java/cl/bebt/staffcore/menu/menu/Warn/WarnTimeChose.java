package cl.bebt.staffcore.menu.menu.Warn;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.Menu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.WarnPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class WarnTimeChose extends Menu {
    
    
    private final main plugin;
    
    public String warned;
    
    public String reason;
    
    public Player p;
    
    public WarnTimeChose( PlayerMenuUtility playerMenuUtility , main plugin , Player p , String warned , String reason ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.p = p;
        this.warned = warned;
        this.reason = reason;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "warns.time.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "default" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            long amount = utils.getInt( "warns.expire_after" , null );
            String time = utils.getString( "warns.expire_after_quantity" );
            WarnPlayer.createWarn( p , warned , reason , amount , time );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "specific" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new WarnAmount( main.getPlayerMenuUtility( p ) , plugin , p , warned , reason ).open( );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new WarnMenu( playerMenuUtility , plugin , warned ).open( );
                e.setCancelled( true );
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack head = utils.getPlayerHead( warned );
        ItemStack defaultTime = new ItemStack( Material.NAME_TAG , 1 );
        ItemStack specificTime = new ItemStack( Material.NAME_TAG , 1 );
        
        ItemMeta or_meta = defaultTime.getItemMeta( );
        ItemMeta cr_meta = specificTime.getItemMeta( );
        ItemMeta head_meta = head.getItemMeta( );
        
        or_meta.setDisplayName( utils.chat( "&aDefault Time" ) );
        cr_meta.setDisplayName( utils.chat( "&cSpecific Time" ) );
        head_meta.setDisplayName( utils.chat( "&5" + warned ) );
        int currentWarns = utils.currentPlayerWarns( warned );
        lore.add( utils.chat( "&5Click to Warn " + warned ) );
        lore.add( utils.chat( "for the the default time." ) );
        lore.add( utils.chat( "&a(&c" + utils.getString( "warns.expire_after" ) + utils.getString( "warns.expire_after_quantity" ) + "&a)" ) );
        
        or_meta.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&5Click to Warn " + warned ) );
        lore.add( utils.chat( "for an specific time." ) );
        
        cr_meta.setLore( lore );
        lore.clear( );
        lore.add( utils.chat( "&cCurrents warns: &6" + currentWarns ) );
        
        if ( currentWarns < utils.getInt( "warns.max_warns" , null ) && utils.getBoolean( "warns.ban_on_exceeded" ) ) {
            lore.add( utils.chat( "&cWarns left: &6" + (utils.getInt( "warns.max_warns" , null ) - currentWarns) ) );
        }
        
        head_meta.setLore( lore );
        lore.clear( );
        
        or_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "default" ) , PersistentDataType.STRING , "default" );
        cr_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "specific" ) , PersistentDataType.STRING , "specific" );
        head_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "head" ) , PersistentDataType.STRING , "head" );
        
        
        cr_meta.addEnchant( Enchantment.CHANNELING , 1 , true );
        cr_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        
        defaultTime.setItemMeta( or_meta );
        specificTime.setItemMeta( cr_meta );
        head.setItemMeta( head_meta );
        
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
        inventory.setItem( 20 , defaultTime );
        inventory.setItem( 21 , super.redPanel( ) );
        inventory.setItem( 22 , close( ) );
        inventory.setItem( 23 , super.redPanel( ) );
        inventory.setItem( 24 , specificTime );
        inventory.setItem( 13 , head );
    }
    
}
