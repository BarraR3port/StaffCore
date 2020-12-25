package cl.bebt.staffcore.menu.menu.Banlist;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.BanPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class ChoseBan extends ReportMenu{

    private static PlayerMenuUtility playerMenuUtility;
    private final main plugin;
    private final int Id;

    public ChoseBan( PlayerMenuUtility playerMenuUtility , main plugin , String p2 , int Id ){
        super( playerMenuUtility , plugin , p2 );
        ChoseBan.playerMenuUtility = playerMenuUtility;
        this.plugin = plugin;
        this.Id = Id;
    }

    public static PlayerMenuUtility getPlayerMenuUtility( ){
        return playerMenuUtility;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cUn Ban or Close the ban?" );
    }

    @Override
    public int getSlots( ){
        return 45;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "delete_ban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            BanPlayer.unBan( p , Id );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "close_ban" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            CloseBan( p );
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "panel" ) , PersistentDataType.STRING ) ) {
            e.setCancelled( true );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
            if ( e.getClick( ).isLeftClick( ) ) {
                p.closeInventory( );
                new BanManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            }
        }
    }

    private void CloseBan( Player p ){
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String baned = null;
        String status = "closed";
        if ( utils.mysqlEnabled( ) ) {
            reason = SQLGetter.getBaned( Id , "Reason" );
            created = SQLGetter.getBaned( Id , "Date" );
            exp = SQLGetter.getBaned( Id , "ExpDate" );
            baner = SQLGetter.getBaned( Id , "Baner" );
            baned = SQLGetter.getBaned( Id , "Name" );
            plugin.data.setBan( Id , "closed" );
        } else {
            plugin.baned.reloadConfig( );
            reason = plugin.baned.getConfig( ).getString( "bans." + Id + ".reason" );
            created = plugin.baned.getConfig( ).getString( "bans." + Id + ".date" );
            exp = plugin.baned.getConfig( ).getString( "bans." + Id + ".expdate" );
            baner = plugin.baned.getConfig( ).getString( "bans." + Id + ".baned_by" );
            baned = plugin.baned.getConfig( ).getString( "bans." + Id + ".name" );
            plugin.baned.getConfig( ).set( "bans." + Id + ".status" , "closed" );
            plugin.baned.getConfig( ).set( "count" , playerMenuUtility.currentBans( ) );
            plugin.baned.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( Id , p.getName( ) , baner , baned , reason , exp , created , status , plugin.getConfig( ).getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( p , "close_ban" );
                for ( String key : main.plugin.getConfig( ).getStringList( "ban.ban_change" ) ) {
                    key = key.replace( "%changed_by%" , p.getName( ) );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%baned%" , baned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%ban_status%" , "CLOSED" );
                    utils.tell( people , key );
                }
            }
        }
    }

    @Override
    public void setMenuItems( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack delete = new ItemStack( Material.FLOWER_BANNER_PATTERN , 1 );
        ItemStack closeBan = new ItemStack( Material.NAME_TAG , 1 );

        ItemMeta delete_meta = delete.getItemMeta( );
        ItemMeta closeBan_meta = closeBan.getItemMeta( );

        delete_meta.setDisplayName( utils.chat( "&4UNBAN" ) );
        closeBan_meta.setDisplayName( utils.chat( "&4CLOSE BAN" ) );

        delete_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        delete_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        delete_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );

        closeBan_meta.addEnchant( Enchantment.DURABILITY , 1 , true );
        closeBan_meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        closeBan_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );

        lore.add( utils.chat( "&8Click to &aUn Ban" ) );
        delete_meta.setLore( lore );
        lore.clear( );

        lore.add( utils.chat( "&8Click to &aClose &8the Ban" ) );
        closeBan_meta.setLore( lore );
        lore.clear( );


        delete_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "delete_ban" ) , PersistentDataType.STRING , "delete_ban" );
        closeBan_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "close_ban" ) , PersistentDataType.STRING , "close_ban" );

        delete.setItemMeta( delete_meta );
        closeBan.setItemMeta( closeBan_meta );

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
        if ( utils.mysqlEnabled( ) ) {
            if ( SQLGetter.getBaned( Id , "Status" ).equals( "open" ) ) {
                inventory.setItem( 20 , delete );
                inventory.setItem( 21 , super.redPanel( ) );
                inventory.setItem( 22 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "closed" ) );
                inventory.setItem( 23 , super.redPanel( ) );
                inventory.setItem( 24 , closeBan );
            } else if ( SQLGetter.getBaned( Id , "Status" ).equals( "closed" ) ) {
                inventory.setItem( 20 , super.redPanel( ) );
                inventory.setItem( 21 , delete );
                inventory.setItem( 22 , super.redPanel( ) );
                inventory.setItem( 23 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "closed" ) );
                inventory.setItem( 24 , super.redPanel( ) );
            }
        } else {
            if ( plugin.baned.getConfig( ).get( "bans." + Id + ".status" ).equals( "open" ) ) {
                inventory.setItem( 20 , delete );
                inventory.setItem( 21 , super.redPanel( ) );
                inventory.setItem( 22 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "closed" ) );
                inventory.setItem( 23 , super.redPanel( ) );
                inventory.setItem( 24 , closeBan );
            } else if ( plugin.baned.getConfig( ).get( "bans." + Id + ".status" ).equals( "closed" ) ) {
                inventory.setItem( 20 , super.redPanel( ) );
                inventory.setItem( 21 , delete );
                inventory.setItem( 22 , super.redPanel( ) );
                inventory.setItem( 23 , makeItem( Material.BARRIER , ChatColor.DARK_RED + "closed" ) );
                inventory.setItem( 24 , super.redPanel( ) );
            }
        }
    }
}
