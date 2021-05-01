package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCreative implements CommandExecutor {
    
    public GmCreative( main plugin ){
        plugin.getCommand( "gmc" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 1 ) {
                Player p = Bukkit.getPlayer( args[0] );
                if ( p instanceof Player ) {
                    if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                        p.setGameMode( GameMode.CREATIVE );
                        if ( p.isFlying( ) ) {
                            p.setFlying( true );
                        }
                        p.setAllowFlight( true );
                        utils.tell( sender , utils.getString( "creative.enabled_to" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    } else {
                        utils.tell( sender , utils.getString( "creative.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    }
                    return false;
                }
                utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "sv" ).replace( "%command%" , "gmc | gmc <player>" ) );
            }
        }
        if ( sender instanceof Player ) {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.gmc" ) ) {
                    if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                        p.setGameMode( GameMode.CREATIVE );
                        if ( p.isFlying( ) ) {
                            p.setFlying( true );
                        }
                        p.setAllowFlight( true );
                        utils.tell( sender , utils.getString( "creative.enabled" , "lg" , "sv" ) );
                    } else {
                        utils.tell( sender , utils.getString( "creative.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                }
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    if ( sender.hasPermission( "staffcore.gmc" ) ) {
                        if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                            p.setGameMode( GameMode.CREATIVE );
                            p.setFlying( true );
                            p.setAllowFlight( true );
                            if ( !(sender == p) ) {
                                utils.tell( sender , utils.getString( "creative.enabled_to" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                            }
                            utils.tell( sender , utils.getString( "creative.enabled" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                        } else {
                            utils.tell( sender , utils.getString( "creative.already" , "lg" , "sv" ).replace( "%player%" , p.getName( ) ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                    }
                    return false;
                }
                utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "sv" ).replace( "%command%" , "gmc | gmc <player>" ) );
            }
        }
        return true;
    }
}

