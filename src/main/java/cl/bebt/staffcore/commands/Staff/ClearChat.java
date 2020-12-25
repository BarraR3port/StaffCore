package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChat implements CommandExecutor{

    private final main plugin;

    public ClearChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "clearchat" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command command , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.ccAll( );
                Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The console cleared the chat! " ) );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player jugador = Bukkit.getPlayer( args[0] );
                    utils.ccPlayer( jugador );
                    sender.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4You cleaned the chat of: " + jugador.getName( ) ) ) );
                    jugador.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The console cleared your chat! " ) ) );
                } else if ( !(Bukkit.getPlayer( args[0] ) instanceof Player) ) {
                    sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
                }
            }
        }
        if ( sender instanceof Player ) {
            if ( sender.hasPermission( "staffcore.clearchat" ) ) {
                if ( args.length == 0 ) {
                    utils.ccAll( );
                    Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The player&r " + sender.getName( ) + " &4cleared the chat!" ) );
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player jugador = Bukkit.getPlayer( args[0] );
                        if ( sender.getName( ).equals( jugador.getName( ) ) ) {
                            utils.ccPlayer( jugador );
                            sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4You cleaned your chat" ) );
                            return true;
                        } else {
                            utils.ccPlayer( jugador );
                            sender.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4You cleaned the chat of: " + jugador.getName( ) ) ) );
                            jugador.sendMessage( utils.chat( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4The player&r " + sender.getName( ) + " &4cleaned the chat!" ) ) );
                        }
                    } else {
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
                    }
                }
            } else {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                return false;
            }
        }
        return true;
    }


}