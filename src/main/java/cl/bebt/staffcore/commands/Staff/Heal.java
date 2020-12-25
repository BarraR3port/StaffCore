package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor{

    private final main plugin;

    public Heal( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "heal" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.tell( sender , "&4&lUse feed <player>" );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player jugador = Bukkit.getPlayer( args[0] );
                    jugador.setFoodLevel( 20 );
                    jugador.setHealth( 20 );
                    jugador.setSaturation( 5f );
                    utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7You've healed and feed: " + jugador.getName( ) );
                    jugador.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7You've been healed and feed" ) );
                }
            }
        } else {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.heal" ) ) {
                if ( args.length == 0 ) {
                    p.setFoodLevel( 20 );
                    p.setHealth( 20 );
                    p.setSaturation( 5f );
                    utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7You've been healed and feed" );
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player jugador = Bukkit.getPlayer( args[0] );
                        if ( jugador == p ) {
                            p.setFoodLevel( 20 );
                            p.setHealth( 20 );
                            p.setSaturation( 5f );
                            utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + "&7You've been healed and feed" );

                        } else {
                            jugador.setFoodLevel( 20 );
                            jugador.setHealth( 20 );
                            jugador.setSaturation( 5f );
                            utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + "&7You've healed and feed: " + jugador.getName( ) );
                            jugador.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7You've been healed and feed" ) );
                        }
                    } else if ( !(Bukkit.getPlayer( args[0] ) instanceof Player) ) {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            } else {
                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
            }
        }
        return true;
    }

}