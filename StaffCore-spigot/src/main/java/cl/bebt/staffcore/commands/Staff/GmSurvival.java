/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.VanishQuery;
import cl.bebt.staffcore.utils.FlyManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmSurvival implements CommandExecutor {
    
    private final main plugin;
    
    public GmSurvival( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "gms" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 1 ) {
                Player p = Bukkit.getPlayer( args[0] );
                if ( p instanceof Player ) {
                    if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                        p.setGameMode( GameMode.SURVIVAL );
                        p.setInvulnerable( false );
                        if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                            if ( VanishQuery.isVanished( p.getName( ) ).equalsIgnoreCase( "true" ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } else {
                            try {
                                if ( UserUtils.getVanish( p.getUniqueId( ) ) ) {
                                    p.setAllowFlight( true );
                                    p.setFlying( true );
                                }
                            } catch ( NoSuchMethodError | PlayerNotFundException ignored ) {
                            }
                        }
                        utils.tell( sender , utils.getString( "survival.enabled_to" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    } else {
                        utils.tell( sender , utils.getString( "survival.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    }
                    return false;
                }
                utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "sv" ).replace( "%command%" , "gms | gms <player>" ) );
            }
            return false;
        }
        if ( args.length == 0 ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.gms" ) ) {
                if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                    p.setGameMode( GameMode.SURVIVAL );
                    p.setInvulnerable( false );
                    if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                        if ( VanishQuery.isVanished( p.getName( ) ).equalsIgnoreCase( "true" ) ) {
                            p.setAllowFlight( true );
                            p.setFlying( true );
                        }
                    } else {
                        try {
                            if ( UserUtils.getFly( p.getUniqueId( ) ) ) {
                                FlyManager.enable( p.getUniqueId( ) );
                            }
                            if ( UserUtils.getVanish( p.getUniqueId( ) ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } catch ( NoSuchMethodError | PlayerNotFundException ignored ) {
                        }
                    }
                    utils.tell( sender , utils.getString( "survival.enabled" , "lg" , "sv" ) );
                } else {
                    utils.tell( sender , utils.getString( "survival.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                }
            } else {
                utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
            }
        } else if ( args.length == 1 ) {
            if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                Player p = Bukkit.getPlayer( args[0] );
                if ( sender.hasPermission( "staffcore.gms" ) ) {
                    if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                        p.setGameMode( GameMode.SURVIVAL );
                        p.setInvulnerable( false );
                        if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                            if ( VanishQuery.isVanished( p.getName( ) ).equalsIgnoreCase( "true" ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } else {
                            try {
                                if ( UserUtils.getFly( p.getUniqueId( ) ) ) {
                                    FlyManager.enable( p.getUniqueId( ) );
                                }
                                if ( UserUtils.getVanish( p.getUniqueId( ) ) ) {
                                    p.setAllowFlight( true );
                                    p.setFlying( true );
                                }
                            } catch ( NoSuchMethodError | PlayerNotFundException ignored ) {
                            }
                        }
                        if ( !(sender == p) ) {
                            utils.tell( sender , utils.getString( "survival.enabled_to" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                        }
                        utils.tell( sender , utils.getString( "survival.enabled" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    } else {
                        utils.tell( sender , utils.getString( "survival.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                }
                return false;
            }
            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
        } else {
            utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "sv" ).replace( "%command%" , "gms | gms <player>" ) );
        }
        return true;
    }
}