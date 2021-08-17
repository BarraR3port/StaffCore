/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.Items.Items;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.DataExporter;
import cl.bebt.staffcore.sql.Queries.StaffQuery;
import cl.bebt.staffcore.sql.Queries.VanishQuery;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VanishManager {
    
    private static main plugin;
    
    public VanishManager( main plugin ){
        VanishManager.plugin = plugin;
    }
    
    public static void enable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            if ( utils.mysqlEnabled( ) ) {
                for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                    if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                        if ( UserUtils.getVanish( uuid ) || UserUtils.getStaff( uuid ) || VanishQuery.isVanished( p.getName( ) ).equals( "true" ) || StaffQuery.isStaff( p.getName( ) ).equals( "true" ) ) {
                            p.showPlayer( plugin , player );
                        } else {
                            player.hidePlayer( plugin , p );
                        }
                    } else {
                        p.showPlayer( plugin , player );
                    }
                }
                VanishQuery.enable( p.getName( ) );
            } else {
                for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                    if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                        if ( UserUtils.getVanish( uuid ) || UserUtils.getStaff( uuid ) ) {
                            p.showPlayer( plugin , player );
                        } else {
                            player.hidePlayer( plugin , p );
                        }
                    } else {
                        p.showPlayer( plugin , player );
                    }
                }
            }
            p.setAllowFlight( true );
            p.setFlying( true );
            p.setInvulnerable( true );
            p.setFoodLevel( 20 );
            p.setHealth( 20 );
            p.setSaturation( 5f );
            p.setCollidable( false );
            if ( p.getInventory( ).contains( Items.vanishOff( ) ) ) {
                p.getInventory( ).remove( Items.vanishOff( ) );
                p.getInventory( ).addItem( Items.vanishOn( ) );
            }
            if ( UserUtils.getFakeJoinLeave( uuid ) ) {
                for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                    if ( utils.getBoolean( "alerts.fake_join_leave_msg" ) ) {
                        utils.tell( players , utils.getString( "fake_join_leave_msg.leave_msg" , "lg" , null ).replace( "%player%" , p.getName( ) ) );
                    }
                }
            }
            UserUtils.setVanish( uuid , true );
            DataExporter.updateServerStats( "vanish" );
        } catch ( PlayerNotFundException | NullPointerException ignored ) {
            ignored.printStackTrace( );
        }
    }
    
    public static void disable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            if ( (p.getGameMode( ).equals( GameMode.SURVIVAL ) ||
                    p.getGameMode( ).equals( GameMode.ADVENTURE )) &&
                    (!UserUtils.getFly( uuid ) &&
                            !UserUtils.getStaff( uuid )) ) {
                p.setAllowFlight( false );
                p.setFlying( false );
                p.setInvulnerable( false );
            }
            if ( p.getInventory( ).contains( Items.vanishOn( ) ) ) {
                p.getInventory( ).remove( Items.vanishOn( ) );
                p.getInventory( ).addItem( Items.vanishOff( ) );
            }
            p.setHealth( 20 );
            p.setSaturation( 5f );
            p.setCollidable( true );
            if ( utils.getBoolean( "alerts.fake_join_leave_msg" ) ) {
                if ( UserUtils.getFakeJoinLeave( uuid ) ) {
                    for ( Player players : Bukkit.getOnlinePlayers( ) ) {
                        utils.tell( players , utils.getString( "fake_join_leave_msg.join_msg" , "lg" , null ).replace( "%player%" , p.getName( ) ) );
                    }
                }
            }
            if ( utils.mysqlEnabled( ) ) {
                VanishQuery.disable( p.getName( ) );
            }
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                people.showPlayer( plugin , p );
                if ( !p.hasPermission( "staffcore.vanish" ) || !p.hasPermission( "staffcore.staff" ) ) {
                    for ( String players : StaffCoreAPI.getVanishedPlayers( ) ) {
                        try {
                            p.hidePlayer( plugin , Bukkit.getPlayer( players ) );
                        } catch ( IllegalArgumentException | NullPointerException ignored ) {
                        }
                    }
                }
            }
            UserUtils.setVanish( uuid , false );
            DataExporter.updateServerStats( "vanish" );
        } catch ( PlayerNotFundException | NullPointerException error ) {
            error.printStackTrace( );
        }
    }
    
    
}
