/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.FreezeQuery;
import cl.bebt.staffcoreapi.utils.Serializer;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class FreezeManager {
    private static Plugin plugin;
    
    public FreezeManager( main plugin ){
        FreezeManager.plugin = plugin;
    }
    
    public static void enable( UUID uuid , String freezer ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            p.setAllowFlight( true );
            p.setInvulnerable( true );
            Utils.PlaySound( p , "freeze" );
            Utils.PlayParticle( p , "unfreeze_player" );
            if ( Utils.mysqlEnabled( ) ) {
                FreezeQuery.enable( p.getName( ) );
            }
            if ( Utils.getBoolean( "freeze.set_ice_block" ) ) {
                if ( p.getInventory( ).getHelmet( ) != null ) {
                    UserUtils.setFreezeHelmet( uuid , Serializer.serialize( p.getInventory( ).getHelmet( ) ) );
                    p.getInventory( ).setItem( 39 , new ItemStack( Material.BLUE_ICE ) );
                }
            }
            UserUtils.setFrozen( uuid , true );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( Utils.getBoolean( "alerts.freeze" ) || people.hasPermission( "staffcore.staff" ) ) {
                    for ( String key : Utils.getStringList( "freeze" , "alerts" ) ) {
                        key = key.replace( "%frozen%" , p.getName( ) );
                        key = key.replace( "%freezer%" , freezer );
                        key = key.replace( "%status%" , Utils.getString( "freeze.freeze" , "lg" , null ) );
                        Utils.tell( people , key );
                    }
                }
            }
            SendMsg.sendFreezeAlert( freezer , p.getName( ) , true , Utils.getServer( ) );
        } catch ( NullPointerException ignored ) {
        }
    }
    
    public static void disable( UUID uuid , String freezer ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            Utils.PlaySound( p , "un_freeze" );
            Utils.PlayParticle( p , "freeze_player" );
            if ( !(p.getGameMode( ) == GameMode.CREATIVE || p.getGameMode( ) == GameMode.SPECTATOR ||
                    UserUtils.getVanish( uuid ) || UserUtils.getFly( uuid ) || UserUtils.getStaff( uuid )) ) {
                p.setAllowFlight( false );
                p.setInvulnerable( false );
            }
            if ( Utils.mysqlEnabled( ) ) {
                FreezeQuery.disable( p.getName( ) );
            }
            if ( Utils.getBoolean( "freeze.set_ice_block" ) ) {
                try {
                    ItemStack helmet = Serializer.deserialize( UserUtils.getFreezeHelmet( uuid ) );
                    p.getInventory( ).setHelmet( helmet );
                    UserUtils.setFreezeHelmet( uuid , "" );
                } catch ( NullPointerException ignored ) {
                    p.getInventory( ).setHelmet( null );
                }
            }
            UserUtils.setFrozen( uuid , false );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( Utils.getBoolean( "alerts.freeze" ) || people.hasPermission( "staffcore.staff" ) ) {
                    for ( String key : Utils.getStringList( "freeze" , "alerts" ) ) {
                        key = key.replace( "%frozen%" , p.getName( ) );
                        key = key.replace( "%freezer%" , freezer );
                        key = key.replace( "%status%" , Utils.getString( "freeze.unfreeze" , "lg" , null ) );
                        Utils.tell( people , key );
                    }
                }
            }
            SendMsg.sendFreezeAlert( freezer , p.getName( ) , false , Utils.getServer( ) );
        } catch ( NullPointerException ignored ) {
        }
    }
    
    
}

