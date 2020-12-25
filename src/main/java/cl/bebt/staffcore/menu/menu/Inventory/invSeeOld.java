package cl.bebt.staffcore.menu.menu.Inventory;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.MenuC;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Deprecated
public class invSeeOld extends MenuC {
    private final Player player;
    
    private final main plugin;
    
    public invSeeOld( PlayerMenuUtility playerMenuUtility , main plugin , Player p ){
        super( playerMenuUtility );
        this.player = p;
        this.plugin = plugin;
        
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( "&c" + player.getName( ) + "'s inventory:" );
    }
    
    @Override
    public int getSlots( ){
        return 45;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "head" ) , PersistentDataType.STRING ) ) {
            try {
                p.teleport( player.getLocation( ) );
                utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "tp.teleport_to" ) + player.getName( ) );
            } catch ( NullPointerException err ) {
                utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cThe player is not online, or don't exist" );
            }
            e.setCancelled( true );
        }
    }
    
    private String getTime( Long timeLeft ){
        Long left = (timeLeft / 20) * 1000;
        SimpleDateFormat format = new SimpleDateFormat( "mm:ss" );
        Date date = new Date( left );
        return format.format( date );
    }
    
    private ItemStack potions( ){
        ArrayList < PotionEffect > potions = new ArrayList <>( player.getActivePotionEffects( ) );
        if ( !potions.isEmpty( ) ) {
            ArrayList < String > lore = new ArrayList <>( );
            for ( PotionEffect a : potions ) {
                lore.add( a.getType( ).getName( ) + " - " + getTime( ( long ) a.getDuration( ) ) );
            }
            return makeItem( Material.BREWING_STAND , "&aPotion Effects:" , lore );
        } else {
            return makeItem( Material.BREWING_STAND , "&aPotion Effects:" , "No potion effects" );
        }
    }
    
    @Override
    public void setMenuItemsPlayer( Player p ){
        utils.PlaySound( p , "invsee" );
        HashMap < Integer, ItemStack > hotbar = new HashMap <>( );
        for ( int i = 0; i < 9; i++ ) {
            try {
                hotbar.put( i , player.getInventory( ).getItem( i ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        HashMap < Integer, ItemStack > inv = new HashMap <>( );
        
        for ( int c = 0; c <= 35; c++ ) {
            try {
                inv.put( c , player.getInventory( ).getItem( c + 9 ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        for ( int a = 0; a < hotbar.size( ); a++ ) {
            inventory.setItem( a + 36 , hotbar.get( a ) );
        }
        for ( int b = 0; b < inv.size( ); b++ ) {
            inventory.setItem( b , inv.get( b ) );
        }
        
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack head = utils.getPlayerHead( player.getName( ) );
        ItemMeta head_meta = head.getItemMeta( );
        head_meta.setDisplayName( utils.chat( "&a" + player.getName( ) + "'s &7Stats:" ) );
        lore.add( utils.chat( "&7Gamemode: &b" + player.getGameMode( ).toString( ) ) );
        lore.add( " " );
        lore.add( utils.chat( "&7Location: &d" + ( int ) player.getLocation( ).getX( ) + " &3" + ( int ) player.getLocation( ).getY( ) + " &d" + ( int ) player.getLocation( ).getZ( ) ) );
        lore.add( " " );
        try {
            Object entityPlayer = player.getClass( ).getMethod( "getHandle" ).invoke( p );
            int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
            lore.add( utils.chat( "&a" + player.getName( ) + "'s &7ping: &a" + ping ) );
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException ignored ) {
        }
        if ( player.hasPermission( "staffcore.staff" ) ) {
            lore.add( " " );
            lore.add( utils.chat( "&5STAFF MEMBER" ) );
            if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Staff Mode &aOn" ) );
            }
            if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Staff Mode &cOff" ) );
            }
            if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Vanished &aOn" ) );
            }
            if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Vanished &cOff" ) );
            }
        }
        head_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "head" ) , PersistentDataType.STRING , "head" );
        head_meta.setLore( lore );
        head.setItemMeta( head_meta );
        lore.clear( );
        
        ItemStack food = new ItemStack( Material.COOKED_BEEF );
        ItemMeta food_meta = food.getItemMeta( );
        food_meta.setDisplayName( utils.chat( "&a" + player.getName( ) + "'s &7Health Stats:" ) );
        lore.add( " " );
        lore.add( utils.chat( "&aHealth level: &c" + ( int ) player.getHealth( ) + "&l❤" ) );
        lore.add( " " );
        lore.add( utils.chat( "&aFood level: &6" + player.getFoodLevel( ) ) );
        food_meta.setLore( lore );
        food.setItemMeta( food_meta );
        
        inventory.setItem( 27 , super.greenPanel( ) );
        inventory.setItem( 28 , potions( ) );
        inventory.setItem( 29 , food );
        if ( player.getInventory( ).getItemInMainHand( ) != null ) {
            inventory.setItem( 30 , player.getInventory( ).getItemInMainHand( ) );
        }
        inventory.setItem( 31 , head );
        if ( player.getInventory( ).getHelmet( ) != null ) {
            inventory.setItem( 32 , player.getInventory( ).getHelmet( ) );
        }
        if ( player.getInventory( ).getChestplate( ) != null ) {
            inventory.setItem( 33 , player.getInventory( ).getChestplate( ) );
        }
        if ( player.getInventory( ).getLeggings( ) != null ) {
            inventory.setItem( 34 , player.getInventory( ).getLeggings( ) );
        }
        if ( player.getInventory( ).getBoots( ) != null ) {
            inventory.setItem( 35 , player.getInventory( ).getBoots( ) );
        }
        
    }
}
