package cl.bebt.staffcore.menu.menu.Others;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class StaffListGui extends PaginatedMenu {
    
    private final main plugin;
    
    public StaffListGui( PlayerMenuUtility playerMenuUtility , main plugin ){
        super( playerMenuUtility );
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( "&cStaffList" );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < Player > players = new ArrayList <>( );
        p.getInventory( );
        for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
            if ( player.hasPermission( "staffcore.staff" ) ) {
                players.add( player );
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String target = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING );
            TpPlayers.tpToPlayer( p , target );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( page == 0 ) {
                    p.sendMessage( ChatColor.GRAY + "You are already on the first page." );
                } else {
                    page = page - 1;
                    super.open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                if ( !((index + 1) >= players.size( )) ) {
                    page = page + 1;
                    super.open( p );
                } else {
                    p.sendMessage( ChatColor.GRAY + "You are on the last page." );
                }
            }
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ArrayList < Player > players = new ArrayList <>( );
        for ( Player p : Bukkit.getServer( ).getOnlinePlayers( ) ) {
            if ( p.hasPermission( "staffcore.staff" ) ) {
                players.add( p );
            }
        }
        if ( players != null && !players.isEmpty( ) ) {
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= players.size( ) ) break;
                if ( players.get( index ) != null ) {
                    //////////////////////////////
                    ItemStack p_head = utils.getPlayerHead( players.get( index ).getName( ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( utils.chat( "&a" + players.get( index ).getName( ) ) );
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                        lore.add( utils.chat( "&7Staff Mode: &aTrue" ) );
                    } else {
                        lore.add( utils.chat( "&7Staff Mode: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                        lore.add( utils.chat( "&7Vanished: &aTrue" ) );
                    } else {
                        lore.add( utils.chat( "&7Vanished: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
                        lore.add( utils.chat( "&7Staff Chat: &aTrue" ) );
                    } else {
                        lore.add( utils.chat( "&7Staff Chat: &cFalse" ) );
                    }
                    if ( players.get( index ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) || players.get( index ).getGameMode( ).equals( GameMode.CREATIVE ) ) {
                        lore.add( utils.chat( "&7Flying: &aTrue" ) );
                    } else {
                        lore.add( utils.chat( "&7Flying: &cFalse" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.CREATIVE ) ) {
                        lore.add( utils.chat( "&7Gamemode: &aCreative" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.SURVIVAL ) ) {
                        lore.add( utils.chat( "&7Gamemode: &aSurvival" ) );
                    }
                    if ( players.get( index ).getGameMode( ).equals( GameMode.SPECTATOR ) ) {
                        lore.add( utils.chat( "&7Gamemode: &aSpectator" ) );
                    }
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING , "staff" );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING , players.get( index ).getName( ) );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                }
            }
        }
    }
}
