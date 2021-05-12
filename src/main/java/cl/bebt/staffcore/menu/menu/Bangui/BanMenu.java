package cl.bebt.staffcore.menu.menu.Bangui;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.BanPlayerMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BanMenu extends BanPlayerMenu {
    private final main plugin;
    
    public BanMenu( PlayerMenuUtility playerMenuUtility , main plugin , Player p , String p2 ){
        super( playerMenuUtility );
        this.plugin = plugin;
        super.banned = p2;
        super.baner = p;
    }
    
    @Override
    public String getMenuName( ){
        return utils.chat( utils.getString( "ban_menu.name" , "menu", null).replace( "%player%" , banned ) );
    }
    
    @Override
    public int getSlots( ){
        return 54;
    }
    
    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "hacking" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Hacking" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "killaura" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Killaura" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Flying" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "speed" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Speed" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "griefing" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Griefing" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "spamming" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "Spamming" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "bhop" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            new ChoseBanType( main.getPlayerMenuUtility( p ) , plugin , baner , banned , "BunnyHop" ).open( );
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "other" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
            PlayerData.set( new NamespacedKey( main.plugin , "banmsg" ) , PersistentDataType.STRING , banned );
            utils.tell( p , utils.getString( "bans.other_reason" , "lg" , "sv" ) );
        } else if ( e.getCurrentItem( ).equals( close( ) ) ) {
            p.closeInventory( );
        }
    }
    
    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        inventory.addItem( hacking( ) );
        inventory.addItem( killaura( ) );
        inventory.addItem( flying( ) );
        inventory.addItem( speed( ) );
        inventory.addItem( griefing( ) );
        inventory.addItem( spamming( ) );
        inventory.addItem( bhop( ) );
        inventory.addItem( other( ) );
        baner.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "ban-ip" ) );
    }
    
    public ItemStack hacking( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.RED_DYE );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor hacking" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Hacking" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "hacking" ) , PersistentDataType.STRING , "hacking" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack killaura( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.DIAMOND_SWORD );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor KillAura" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4KillAura" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "killaura" ) , PersistentDataType.STRING , "killaura" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack flying( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.FEATHER );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor Flying" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Flying" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "flying" ) , PersistentDataType.STRING , "flying" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack speed( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.SUGAR );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor Hacking" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Speed" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "speed" ) , PersistentDataType.STRING , "speed" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack spamming( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.PAPER );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor Spamming" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Spamming" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "spamming" ) , PersistentDataType.STRING , "spamming" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack griefing( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.TNT );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor Griefing" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Griefing" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "griefing" ) , PersistentDataType.STRING , "griefing" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack bhop( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.RABBIT_HIDE );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor BunnyHop" ) );
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4BunnyHop" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "bhop" ) , PersistentDataType.STRING , "bhop" );
        item.setItemMeta( meta );
        return item;
    }
    
    public ItemStack other( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.FLOWER_BANNER_PATTERN );
        ItemMeta meta = item.getItemMeta( );
        lore.add( utils.chat( "&dBan &r" + banned + " &dfor other reason" ) );
        lore.add( utils.chat( "&dType in chat the reason." ) );
        assert meta != null;
        meta.setLore( lore );
        meta.setDisplayName( utils.chat( "&4Other reason" ) );
        meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "other" ) , PersistentDataType.STRING , "other" );
        item.setItemMeta( meta );
        return item;
    }
    
}
