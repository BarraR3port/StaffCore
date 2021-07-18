package cl.bebt.staffcore.menu.menu.WarnManager;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Staff.ServerManager;
import cl.bebt.staffcore.sql.Queries.ServerQuery;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;

public class WarnManager extends PaginatedMenu {
    
    private final main plugin;
    
    public WarnManager( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "warns.warnings.name" , "menu" , null ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < String > warnedPlayers = new ArrayList <>( utils.getWarnedPlayers( ) );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String warned = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING );
            new Warnings( main.getPlayerMenuUtility( p ) , plugin , p , warned ).open( );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                new ServerManager( main.getPlayerMenuUtility( p ) , plugin ).open( p );
            }
        } else if ( e.getCurrentItem( ).equals( back( ) ) ) {
            if ( page == 0 ) {
                utils.tell( p , utils.getString( "menu.already_in_first_page" , "lg" , "sv" ) );
            } else {
                page--;
                p.closeInventory( );
                open( );
            }
        } else if ( e.getCurrentItem( ).equals( next( ) ) ) {
            if ( index + 1 <= warnedPlayers.size( ) ) {
                page++;
                p.closeInventory( );
                open( );
            } else {
                utils.tell( p , utils.getString( "menu.already_in_last_page" , "lg" , "sv" ) );
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ArrayList < String > warnedPlayers = new ArrayList <>( utils.getWarnedPlayers( ) );
        if ( warnedPlayers != null && !warnedPlayers.isEmpty( ) ) {
            for ( int i = 0; i <= getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= warnedPlayers.size( ) ) break;
                if ( warnedPlayers.get( index ) != null ) {
                    //////////////////////////////
                    plugin.warns.reloadConfig( );
                    ArrayList < String > lore = new ArrayList <>( );
                    ItemStack p_head = utils.getPlayerHead( warnedPlayers.get( index ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    meta.setDisplayName( utils.chat( "&5" + warnedPlayers.get( index ) ) );
                    if ( utils.mysqlEnabled( ) ) {
                        HashMap < String, Integer > serverStatus = ServerQuery.getPlayerStatus( warnedPlayers.get( index ) );
                        lore.add( utils.chat( "&a► &7Current Bans: &a" + serverStatus.get( "currentBans" ) ) );
                        lore.add( utils.chat( "&a► &7Current Reports: &a" + serverStatus.get( "currentReports" ) ) );
                        lore.add( utils.chat( "&a► &7Current Warns: &a" + serverStatus.get( "currentWarns" ) ) );
                        
                    } else {
                        lore.add( utils.chat( "&aTotal Warnings: &c" + utils.currentPlayerWarns( warnedPlayers.get( index ) ) ) );
                        lore.add( utils.chat( "&aTotal Reports: &c" + utils.currentPlayerReports( warnedPlayers.get( index ) ) ) );
                    }
                    lore.add( " " );
                    lore.add( utils.chat( "&7Click to see &c" + warnedPlayers.get( index ) + "'s &7warns." ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING , warnedPlayers.get( index ) );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                    
                }
            }
        }
    }
    
}
