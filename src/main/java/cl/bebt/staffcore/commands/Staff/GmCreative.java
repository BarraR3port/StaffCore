package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCreative implements CommandExecutor{
    private final main plugin;

    public GmCreative( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "gmc" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage, use:" ) );
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: gmc <player>" ) );
            } else if ( args.length == 1 ) {
                Player p = Bukkit.getPlayer( args[0] );

                if ( p instanceof Player ) {
                    if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                        p.setGameMode( GameMode.CREATIVE );
                        if ( p.isFlying( ) ) {
                            p.setFlying( true );
                        }
                        p.setAllowFlight( true );
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7You set Creative mode to: " + p.getName( ) ) );
                    } else {
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player: " + p.getName( ) + " &7is already in creative" ) );
                    }
                    return false;
                }
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
            }
            return false;
        }
        if ( (sender instanceof Player) ) {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;

                if ( p.hasPermission( "staffcore.gmc" ) ) {
                    if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                        p.setGameMode( GameMode.CREATIVE );
                        if ( p.isFlying( ) ) {
                            p.setFlying( true );
                        }
                        p.setAllowFlight( true );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "creative" ) ) );
                    } else {
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "creative_already" ) ) );
                    }
                } else {
                    sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                }
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );

                    if ( sender.hasPermission( "staffcore.gmc" ) ) {
                        if ( !(p.getGameMode( ) == GameMode.CREATIVE) ) {
                            p.setGameMode( GameMode.CREATIVE );
                            if ( p.isFlying( ) ) {
                                p.setFlying( true );
                            }
                            p.setAllowFlight( true );
                            if ( !(sender == p) ) {
                                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7You set Creative mode to: " + p.getName( ) ) );
                            }
                            p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "creative" ) ) );

                        } else {
                            if ( !(sender == p) ) {
                                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The player: " + p.getName( ) + " &7is already in creative" ) );
                            }
                            p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "creative_already" ) ) );
                        }
                    } else {
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                    }
                    return false;
                }
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
            }
        }
        return true;
    }
}

