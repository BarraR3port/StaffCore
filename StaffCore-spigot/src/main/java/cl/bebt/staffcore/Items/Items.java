/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Items;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.ServerQuery;
import cl.bebt.staffcoreapi.utils.Utils;
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
    
    private static main plugin;
    
    public Items( main plugin ){
        Items.plugin = plugin;
    }
    
    public static ItemStack head( Player target ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack head = Utils.getPlayerHead( target.getName( ) );
        ItemMeta head_meta = head.getItemMeta( );
        head_meta.setDisplayName( Utils.chat( "&a" + target.getName( ) + "'s &7Stats:" ) );
        lore.add( Utils.chat( "&a► &7Gamemode: &b" + target.getGameMode( ) ) );
        lore.add( Utils.chat( "&a► &7Location: &d" + ( int ) target.getLocation( ).getX( ) + " &3" + ( int ) target.getLocation( ).getY( ) + " &d" + ( int ) target.getLocation( ).getZ( ) ) );
        lore.add( Utils.chat( "&a► &a" + target.getName( ) + "'s &7ping: &a" + Utils.getPing( target ) ) );
        if ( target.hasPermission( "staffcore.staff" ) ) {
            lore.add( Utils.chat( "&5STAFF MEMBER" ) );
            if ( target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( Utils.chat( "&a► &7Staff Mode &aOn" ) );
            }
            if ( !target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
                lore.add( Utils.chat( "&a► &7Staff Mode &cOff" ) );
            }
            if ( target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( Utils.chat( "&a► &7Vanished &aOn" ) );
            }
            if ( !target.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                lore.add( Utils.chat( "&a► &7Vanished &cOff" ) );
            }
            if ( Utils.getTrollStatus( target.getName( ) ) ) {
                lore.add( Utils.chat( "&a► &7Troll Mode: &aON" ) );
            }
            if ( !Utils.getTrollStatus( target.getName( ) ) ) {
                lore.add( Utils.chat( "&a► &7Troll Mode: &cOFF" ) );
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
        food_meta.setDisplayName( Utils.chat( "&a" + target.getName( ) + "'s &7Health Stats:" ) );
        lore.add( Utils.chat( "&a► &aHealth level: &c" + ( int ) target.getHealth( ) + "&l" + "\u2764" ) );
        lore.add( Utils.chat( "&a► &aFood level: &6" + target.getFoodLevel( ) ) );
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
                lore.add( Utils.chat( "&a► &7" + a.getType( ).getName( ) + " - " + getTime( ( long ) a.getDuration( ) ) ) );
            }
            ItemStack item = new ItemStack( Material.BREWING_STAND );
            ItemMeta itemMeta = item.getItemMeta( );
            itemMeta.setDisplayName( Utils.chat( "&aPotion Effects:" ) );
            itemMeta.setLore( lore );
            itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "poti" ) , PersistentDataType.STRING , "poti" );
            item.setItemMeta( itemMeta );
            return item;
        } else {
            ItemStack item = new ItemStack( Material.BREWING_STAND );
            ItemMeta itemMeta = item.getItemMeta( );
            itemMeta.setDisplayName( Utils.chat( "&aPotion Effects:" ) );
            itemMeta.setLore( Arrays.asList( Utils.chat( "&a► &cNo potion effects" ) ) );
            itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "poti" ) , PersistentDataType.STRING , "poti" );
            item.setItemMeta( itemMeta );
            return item;
        }
    }
    
    public static ItemStack EmptyItem( ){
        ItemStack item = new ItemStack( Material.BARRIER );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( Utils.chat( "&cEMPTY WARN" ) );
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
        metaServer.setDisplayName( Utils.chat( "&5SERVER STATUS:" ) );
        metaServer.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "server" ) , PersistentDataType.STRING , "server" );
        double tps = Math.round( Utils.getTPS( ) * 100.0D ) / 100.0D;
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime( );
        if ( tps > 20 ) {
            tps = 20D;
        }
        lore.add( Utils.chat( "&a► &7Tps: &a" + tps ) );
        lore.add( Utils.chat( "&a► &7Online players: &a" + Bukkit.getOnlinePlayers( ).size( ) + "/" + Bukkit.getMaxPlayers( ) ) );
        lore.add( Utils.chat( "&a► &7Ram in use: &a" + (instance.totalMemory( ) - instance.freeMemory( )) / mb ) + "/" + instance.maxMemory( ) / mb );
        lore.add( Utils.chat( "&5PUNISHMENTS STATUS:" ) );
        if ( Utils.mysqlEnabled( ) ) {
            HashMap < String, Integer > serverStatus = ServerQuery.getServerStatus( );
            lore.add( Utils.chat( "&a► &7Current Bans: &a" + serverStatus.get( "CurrentBans" ) ) );
            lore.add( Utils.chat( "&a► &7Current Reports: &a" + serverStatus.get( "CurrentReports" ) ) );
            lore.add( Utils.chat( "&a► &7Current Warns: &a" + serverStatus.get( "CurrentWarns" ) ) );
            
        } else {
            lore.add( Utils.chat( "&a► &7Current Bans: &a" + Utils.count( UpdateType.BAN ) ) );
            lore.add( Utils.chat( "&a► &7Current Report: &a" + Utils.count( UpdateType.REPORT ) ) );
            lore.add( Utils.chat( "&a► &7Current Warns: &a" + Utils.count( UpdateType.WARN ) ) );
        }
        metaServer.setLore( lore );
        server.setItemMeta( metaServer );
        return server;
    }
    
    public static ItemStack PlayerStats( Player p ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack playerHead = Utils.getPlayerHead( p.getName( ) );
        ItemMeta metaPlayerHead = playerHead.getItemMeta( );
        metaPlayerHead.setDisplayName( Utils.chat( "&5" + p.getName( ).toUpperCase( ) + "'S STATS:" ) );
        metaPlayerHead.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "player" ) , PersistentDataType.STRING , "players" );
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Staff Mode &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staff" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Staff Mode &cOff" ) );
        }
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Staff Chat &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Staff Chat &cOff" ) );
        }
        if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Vanished &aOn" ) );
        }
        if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "vanished" ) , PersistentDataType.STRING ) ) {
            lore.add( Utils.chat( "&a► &7Vanished &cOff" ) );
        }
        if ( Utils.getTrollStatus( p.getName( ) ) ) {
            lore.add( Utils.chat( "&a► &7Troll Mode: &aON" ) );
        }
        if ( !Utils.getTrollStatus( p.getName( ) ) ) {
            lore.add( Utils.chat( "&a► &7Troll Mode: &cOFF" ) );
        }
        lore.add( Utils.chat( "&a► &7Gamemode: &b" + p.getGameMode( ) ) );
        lore.add( Utils.chat( "&a► &7Ping: &a" + Utils.getPing( p ) ) );
        lore.add( Utils.chat( "&a► &7Location: &d" + ( int ) p.getLocation( ).getX( ) + " &3" + ( int ) p.getLocation( ).getY( ) + " &d" + ( int ) p.getLocation( ).getZ( ) ) );
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
        for ( String key : Utils.getStringList( "fake_join_leave_msg.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
        }
        if ( enabled ) {
            itemMeta.setDisplayName( Utils.chat( "&8Fake Join/Leave msg &aOn " ) );
            itemMeta.addEnchant( Enchantment.DAMAGE_ALL , 1 , true );
            itemMeta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
            lore.add( Utils.chat( "&7Click to: &cDisable" ) );
        } else {
            itemMeta.setDisplayName( Utils.chat( "&8Fake Join/Leave msg &cOff" ) );
            lore.add( Utils.chat( "&7Click to: &aEnable" ) );
        }
        itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "FakeJoinOrLeave" ) , PersistentDataType.STRING , "FakeJoinOrLeave" );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    public static ItemStack ComingSoon( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "menu_items.coming_soon.material" , "item" , null ) ) );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( Utils.chat( Utils.getString( "menu_items.coming_soon.name" , "item" , null ) ) );
        for ( String key : Utils.getStringList( "menu_items.coming_soon.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
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
        ItemStack item = new ItemStack( Material.getMaterial( Utils.getString( "menu_items.coming_soon.material" , "item" , null ) ) );
        ItemMeta itemMeta = item.getItemMeta( );
        itemMeta.setDisplayName( Utils.chat( Utils.getString( "menu_items.coming_soon.name" , "item" , null ) ) );
        for ( String key : Utils.getStringList( "menu_items.coming_soon.lore" , "item" ) ) {
            lore.add( Utils.chat( key ) );
        }
        itemMeta.addEnchant( Enchantment.DAMAGE_ALL , 1 , true );
        itemMeta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        itemMeta.getPersistentDataContainer( ).set( new NamespacedKey( main.plugin , "ComingSoon" ) , PersistentDataType.STRING , "ComingSoon" );
        itemMeta.setLore( lore );
        item.setItemMeta( itemMeta );
        return item;
    }
    
    public static ItemStack vanishOn( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack vanishOn = new ItemStack( Material.LIME_DYE );
        ItemMeta vanishOn_meta = vanishOn.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        vanishOn_meta.setLore( lore );
        vanishOn_meta.setDisplayName( Utils.chat( "&7Vanish &aOn" ) );
        vanishOn_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "vanishOn" ) , PersistentDataType.STRING , "vanishOn" );
        vanishOn.setItemMeta( vanishOn_meta );
        return vanishOn;
    }
    
    public static ItemStack vanishOff( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack vanishOff = new ItemStack( Material.GRAY_DYE );
        ItemMeta vanishOff_meta = vanishOff.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        vanishOff_meta.setLore( lore );
        vanishOff_meta.setDisplayName( Utils.chat( "&7Vanish &cOff" ) );
        vanishOff_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "vanishOff" ) , PersistentDataType.STRING , "vanishOff" );
        vanishOff.setItemMeta( vanishOff_meta );
        return vanishOff;
    }
    
    public static ItemStack staffOff( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack staffOff = new ItemStack( Material.RED_DYE );
        ItemMeta staffOff_meta = staffOff.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        staffOff_meta.setLore( lore );
        staffOff_meta.setDisplayName( Utils.chat( "&7Staff &cOFF" ) );
        staffOff_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffOff" ) , PersistentDataType.STRING , "staffOff" );
        staffOff.setItemMeta( staffOff_meta );
        return staffOff;
    }
    
    public static ItemStack serverManager( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack chatManager = new ItemStack( Material.TOTEM_OF_UNDYING );
        ItemMeta chatManager_meta = chatManager.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        chatManager_meta.setLore( lore );
        chatManager_meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        chatManager_meta.setDisplayName( Utils.chat( "&cServer Manager" ) );
        chatManager_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "servermanager" ) , PersistentDataType.STRING , "servermanager" );
        chatManager.setItemMeta( chatManager_meta );
        return chatManager;
    }
    
    public static ItemStack freeze( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack freeze = new ItemStack( Material.BLUE_ICE );
        ItemMeta freeze_meta = freeze.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        freeze_meta.setLore( lore );
        freeze_meta.setDisplayName( Utils.chat( "&bFreeze" ) );
        freeze_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "freeze" ) , PersistentDataType.STRING , "freeze" );
        freeze.setItemMeta( freeze_meta );
        return freeze;
    }
    
    public static ItemStack randomTp( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack randomTp = new ItemStack( Material.CLOCK );
        ItemMeta randomTp_meta = randomTp.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        randomTp_meta.setLore( lore );
        randomTp_meta.setDisplayName( Utils.chat( "&5Random Tp" ) );
        randomTp_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "randomTp" ) , PersistentDataType.STRING , "randomTp" );
        randomTp.setItemMeta( randomTp_meta );
        return randomTp;
    }
    
    public static ItemStack reportManager( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack report = new ItemStack( Material.BLAZE_ROD );
        ItemMeta report_meta = report.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        report_meta.setLore( lore );
        report_meta.setDisplayName( Utils.chat( "&cReport Manager" ) );
        report_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "report" ) , PersistentDataType.STRING , "report" );
        report.setItemMeta( report_meta );
        return report;
    }
    
    public static ItemStack InvSee( ){
        ArrayList < String > lore = new ArrayList <>( );
        ItemStack InvSee = new ItemStack( Material.CHEST );
        ItemMeta InvSee_meta = InvSee.getItemMeta( );
        lore.add( Utils.chat( "&dStaff Utils" ) );
        InvSee_meta.setLore( lore );
        InvSee_meta.setDisplayName( Utils.chat( "&bInvsee" ) );
        InvSee_meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "invsee" ) , PersistentDataType.STRING , "invsee" );
        InvSee.setItemMeta( InvSee_meta );
        return InvSee;
    }
}
