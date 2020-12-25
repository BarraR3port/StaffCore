package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Items{

    public static ItemStack head( Player target ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack head = utils.getPlayerHead( target.getName( ) );
        ItemMeta head_meta = head.getItemMeta( );
        head_meta.setDisplayName( utils.chat( "&a" + target.getName( ) + "'s &7Stats:" ) );
        lore.add( utils.chat( "&a► &7Gamemode: &b" + target.getGameMode( ).toString( ) ) );
        lore.add( utils.chat( "&a► &7Location: &d" + ( int ) target.getLocation( ).getX( ) + " &3" + ( int ) target.getLocation( ).getY( ) + " &d" + ( int ) target.getLocation( ).getZ( ) ) );
        lore.add( utils.chat( "&a► &a" + target.getName( ) + "'s &7ping: &a" + utils.getPing( target ) ) );
        if ( target.hasPermission( "staffcore.staff" ) ) {
            lore.add( utils.chat( "&5STAFF MEMBER" ) );
            if ( target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Staff Mode &aOn" ) );
            }
            if ( !target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Staff Mode &cOff" ) );
            }
            if ( target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Vanished &aOn" ) );
            }
            if ( !target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( utils.chat( "&a► &7Vanished &cOff" ) );
            }
        }
        head_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "head" ) , PersistentDataType.STRING , "head" );
        head_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "name" ) , PersistentDataType.STRING , target.getName( ) );
        head_meta.setLore( lore );
        head.setItemMeta( head_meta );
        return head;
    }

    public static ItemStack food( Player target ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack food = new ItemStack( Material.COOKED_BEEF );
        ItemMeta food_meta = food.getItemMeta( );
        food_meta.setDisplayName( utils.chat( "&a" + target.getName( ) + "'s &7Health Stats:" ) );
        lore.add( utils.chat( "&a► &aHealth level: &c" + ( int ) target.getHealth( ) + "&l❤" ) );
        lore.add( utils.chat( "&a► &aFood level: &6" + target.getFoodLevel( ) ) );
        food_meta.setLore( lore );
        food_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "food" ) , PersistentDataType.STRING , "food" );
        food.setItemMeta( food_meta );
        return food;
    }

    public static ItemStack potions( Player target ){
        ArrayList < PotionEffect > potions = new ArrayList <>( target.getActivePotionEffects( ) );
        if ( !potions.isEmpty( ) ) {
            ArrayList < String > lore = new ArrayList <>( );
            for ( PotionEffect a : potions ) {
                lore.add( utils.chat( "&a► &7" + a.getType( ).getName( ) + " - " + getTime( ( long ) a.getDuration( ) ) ) );
            }
            ItemStack item = new ItemStack( Material.BREWING_STAND );
            ItemMeta itemMeta = item.getItemMeta( );
            itemMeta.setDisplayName( utils.chat( "&aPotion Effects:" ) );
            itemMeta.setLore( lore );
            itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "poti" ) , PersistentDataType.STRING , "poti" );
            item.setItemMeta( itemMeta );
            return item;
        } else {
            ItemStack item = new ItemStack( Material.BREWING_STAND );
            ItemMeta itemMeta = item.getItemMeta( );
            itemMeta.setDisplayName( utils.chat( "&aPotion Effects:" ) );
            itemMeta.setLore( Arrays.asList( utils.chat( "&a► &cNo potion effects" ) ) );
            itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "poti" ) , PersistentDataType.STRING , "poti" );
            item.setItemMeta( itemMeta );
            return item;
        }
    }

    public static ItemStack EmptyItem( ){
        ItemStack item = new ItemStack( Material.BARRIER );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( utils.chat( "&cEMPTY WARN" ) );
        item.setItemMeta( itemMeta );
        return item;

    }

    public static ItemStack greenPanel( ){
        ItemStack panel = new ItemStack( Material.LIME_STAINED_GLASS_PANE );
        ItemMeta panel_meta = panel.getItemMeta( );
        panel_meta.setDisplayName( " " );
        panel_meta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "panel" ) , PersistentDataType.STRING , "panel" );
        panel.setItemMeta( panel_meta );
        return panel;
    }

    private static String getTime( Long timeLeft ){
        Long left = (timeLeft / 20) * 1000;
        SimpleDateFormat format = new SimpleDateFormat( "mm:ss" );
        Date date = new Date( left );
        return format.format( date );
    }

    public static ItemStack ServerStatus( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack server = new ItemStack( Material.COMPASS );
        ItemMeta metaServer = server.getItemMeta( );
        metaServer.setDisplayName( utils.chat( "&5SERVER STATUS:" ) );
        metaServer.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "server" ) , PersistentDataType.STRING , "server" );
        double tps = Math.round( utils.getTPS( ) * 100.0D ) / 100.0D;
        Runtime r = Runtime.getRuntime( );
        if ( tps > 20 ) {
            tps = 20D;
        }
        lore.add( utils.chat( "&a► &7Tps: &a" + tps ) );
        lore.add( utils.chat( "&a► &7Online players: &a" + Bukkit.getOnlinePlayers( ).size( ) + "/" + Bukkit.getMaxPlayers( ) ) );
        lore.add( utils.chat( "&a► &7Ram in use: &a" + +(r.totalMemory( ) - r.freeMemory( )) / 1048576 ) + "/" + (r.totalMemory( ) / 1048576) );
        lore.add( utils.chat( "&5PUNISHMENTS STATUS:" ) );
        if ( utils.mysqlEnabled( ) ) {
            lore.add( utils.chat( "&a► &7Current Bans: &a" + SQLGetter.getCurrents( "bans" ) ) );
            lore.add( utils.chat( "&a► &7Current Reports: &a" + SQLGetter.getCurrents( "reports" ) ) );
            lore.add( utils.chat( "&a► &7Current Warns: &a" + SQLGetter.getCurrents( "warns" ) ) );
        } else {
            lore.add( utils.chat( "&a► &7Current Bans: &a" + main.plugin.bans.getConfig( ).getInt( "current" ) ) );
            lore.add( utils.chat( "&a► &7Current Reports: &a" + main.plugin.reports.getConfig( ).getInt( "current" ) ) );
            lore.add( utils.chat( "&a► &7Current Warns: &a" + main.plugin.warns.getConfig( ).getInt( "current" ) ) );
        }
        metaServer.setLore( lore );
        server.setItemMeta( metaServer );
        return server;
    }

    public static ItemStack PlayerStatus( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack playerHead = utils.getPlayerHead( p.getName( ) );
        ItemMeta metaPlayerHead = playerHead.getItemMeta( );
        metaPlayerHead.setDisplayName( utils.chat( "&5" + p.getName( ).toUpperCase( ) + " STATUS:" ) );
        metaPlayerHead.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "player" ) , PersistentDataType.STRING , "players" );
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Staff Mode &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Staff Mode &cOff" ) );
        }
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Staff Chat &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Staff Chat &cOff" ) );
        }
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Vanished &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            lore.add( utils.chat( "&a► &7Vanished &cOff" ) );
        }
        lore.add( utils.chat( "&a► &7Gamemode: &b" + p.getGameMode( ).toString( ) ) );
        lore.add( utils.chat( "&a► &7Ping: &a" + utils.getPing( p ) ) );
        lore.add( utils.chat( "&a► &7Location: &d" + ( int ) p.getLocation( ).getX( ) + " &3" + ( int ) p.getLocation( ).getY( ) + " &d" + ( int ) p.getLocation( ).getZ( ) ) );
        metaPlayerHead.setLore( lore );
        playerHead.setItemMeta( metaPlayerHead );
        return playerHead;
    }
}
