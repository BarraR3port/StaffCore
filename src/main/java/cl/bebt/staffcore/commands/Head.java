package cl.bebt.staffcore.commands;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Head implements CommandExecutor{
    private final main plugin;

    public Head( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "head" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + " " );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    if ( args.length == 0 ) {
                        utils.tell( sender , "&cYou gave " + p + " his own head" );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7There you have your head!" ) );
                        p.getInventory( ).addItem( utils.getPlayerHead( p.getName( ) ) );
                        return true;
                    } else if ( args.length == 1 ) {
                        utils.tell( sender , "&cYou gave " + p + " his own head" );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7There you have the head of: &a" + args[0] ) );
                        p.getInventory( ).addItem( utils.getPlayerHead( args[0] ) );
                        return true;
                    }
                }
            }
        } else if ( sender instanceof Player ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.Head" ) ) {
                if ( args.length == 0 ) {
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7There you have your head!" ) );
                    p.getInventory( ).addItem( utils.getPlayerHead( p.getName( ) ) );
                    return true;
                } else if ( args.length == 1 ) {
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7There you have the head of: &a" + args[0] ) );
                    p.getInventory( ).addItem( utils.getPlayerHead( args[0] ) );
                    return true;
                }

            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }
        }
        return true;
    }
}