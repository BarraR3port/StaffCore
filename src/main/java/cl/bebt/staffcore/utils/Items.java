package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.ServerQuery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Items {
    
    public static ItemStack head( Player target ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack head = utils.getPlayerHead( target.getName( ) );
        ItemMeta head_meta = head.getItemMeta( );
        head_meta.setDisplayName( utils.chat( "&a" + target.getName( ) + "'s &7Stats:" ) );
        lore.add( utils.chat( "&a► &7Gamemode: &b" + target.getGameMode( ) ) );
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
            if ( StaffCoreAPI.getTrollStatus( target.getName( ) ) ) {
                lore.add( utils.chat( "&a► &7Troll Mode: &aON" ) );
            }
            if ( !StaffCoreAPI.getTrollStatus( target.getName( ) ) ) {
                lore.add( utils.chat( "&a► &7Troll Mode: &cOFF" ) );
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
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime( );
        if ( tps > 20 ) {
            tps = 20D;
        }
        lore.add( utils.chat( "&a► &7Tps: &a" + tps ) );
        lore.add( utils.chat( "&a► &7Online players: &a" + Bukkit.getOnlinePlayers( ).size( ) + "/" + Bukkit.getMaxPlayers( ) ) );
        lore.add( utils.chat( "&a► &7Ram in use: &a" + (instance.totalMemory( ) - instance.freeMemory( )) / mb ) + "/" + instance.maxMemory( ) / mb );
        lore.add( utils.chat( "&5PUNISHMENTS STATUS:" ) );
        if ( utils.mysqlEnabled( ) ) {
            HashMap < String, Integer > serverStatus = ServerQuery.getServerStatus( );
            lore.add( utils.chat( "&a► &7Current Bans: &a" + serverStatus.get( "currentBans" ) ) );
            lore.add( utils.chat( "&a► &7Current Reports: &a" + serverStatus.get( "currentReports" ) ) );
            lore.add( utils.chat( "&a► &7Current Warns: &a" + serverStatus.get( "currentWarns" ) ) );
            
        } else {
            lore.add( utils.chat( "&a► &7Current Bans: &a" + utils.count( "bans" ) ) );
            lore.add( utils.chat( "&a► &7Current Report: &a" + utils.count( "reports" ) ) );
            lore.add( utils.chat( "&a► &7Current Warns: &a" + utils.count( "warns" ) ) );
        }
        metaServer.setLore( lore );
        server.setItemMeta( metaServer );
        return server;
    }
    
    public static ItemStack PlayerStats( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack playerHead = utils.getPlayerHead( p.getName( ) );
        ItemMeta metaPlayerHead = playerHead.getItemMeta( );
        metaPlayerHead.setDisplayName( utils.chat( "&5" + p.getName( ).toUpperCase( ) + "'S STATS:" ) );
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
        if ( StaffCoreAPI.getTrollStatus( p.getName( ) ) ) {
            lore.add( utils.chat( "&a► &7Troll Mode: &aON" ) );
        }
        if ( !StaffCoreAPI.getTrollStatus( p.getName( ) ) ) {
            lore.add( utils.chat( "&a► &7Troll Mode: &cOFF" ) );
        }
        lore.add( utils.chat( "&a► &7Gamemode: &b" + p.getGameMode( ) ) );
        lore.add( utils.chat( "&a► &7Ping: &a" + utils.getPing( p ) ) );
        lore.add( utils.chat( "&a► &7Location: &d" + ( int ) p.getLocation( ).getX( ) + " &3" + ( int ) p.getLocation( ).getY( ) + " &d" + ( int ) p.getLocation( ).getZ( ) ) );
        metaPlayerHead.setLore( lore );
        playerHead.setItemMeta( metaPlayerHead );
        return playerHead;
    }
    
    public static ItemStack FakeJoinOrLeave( boolean enabled ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item;
        if ( enabled ) {
            item = new ItemStack( Material.LIME_DYE );
        } else {
            item = new ItemStack( Material.GRAY_DYE );
        }
        ItemMeta itemMeta = item.getItemMeta( );
        for ( String key : utils.getStringList( "fake_join_leave_msg.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        if ( enabled ) {
            itemMeta.setDisplayName( utils.chat( "&8Fake Join/Leave msg &aOn " ) );
            itemMeta.addEnchant( Enchantment.DAMAGE_ALL , 1 , true );
            itemMeta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
            lore.add( utils.chat( "&7Click to: &cDisable" ) );
        } else {
            itemMeta.setDisplayName( utils.chat( "&8Fake Join/Leave msg &cOff" ) );
            lore.add( utils.chat( "&7Click to: &aEnable" ) );
        }
        itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "FakeJoinOrLeave" ) , PersistentDataType.STRING , "FakeJoinOrLeave" );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    public static ItemStack ComingSoon( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "menu_items.coming_soon.material" , "item" , null ) ) );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( utils.chat( utils.getString( "menu_items.coming_soon.name" , "item" , null ) ) );
        for ( String key : utils.getStringList( "menu_items.coming_soon.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        itemMeta.addEnchant( Enchantment.DAMAGE_ALL , 1 , true );
        itemMeta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ComingSoon" ) , PersistentDataType.STRING , "ComingSoon" );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    public static ItemStack WebServerStatus( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( utils.getString( "menu_items.coming_soon.material" , "item" , null ) ) );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( utils.chat( utils.getString( "menu_items.coming_soon.name" , "item" , null ) ) );
        for ( String key : utils.getStringList( "menu_items.coming_soon.lore" , "item" ) ) {
            lore.add( utils.chat( key ) );
        }
        itemMeta.addEnchant( Enchantment.DAMAGE_ALL , 1 , true );
        itemMeta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ComingSoon" ) , PersistentDataType.STRING , "ComingSoon" );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
}
