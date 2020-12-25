package cl.bebt.staffcore.commands.Staff.Mysql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.SetVanish;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class VanishMysql implements CommandExecutor{

    private static final SQLGetter data = main.plugin.data;
    private final main plugin;

    public VanishMysql( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "vanish" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage" ) );
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: vanish <player>" ) );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    String is = SQLGetter.isTrue( p , "vanish" );
                    if ( is.equals( "true" ) ) {
                        SetVanish.setVanish( p , false );
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                        utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) );
                    } else if ( is.equals( "false" ) ) {
                        SetVanish.setVanish( p , true );
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&aYou vanished&r " + p.getDisplayName( ) );
                        utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
            }
        } else {
            if ( sender.hasPermission( "staffcore.vanish" ) ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    String is = SQLGetter.isTrue( p , "vanish" );
                    if ( is.equals( "true" ) ) {
                        SetVanish.setVanish( p , false );
                        utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) );
                    } else if ( is.equals( "false" ) ) {
                        SetVanish.setVanish( p , true );
                        utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        String is = SQLGetter.isTrue( p , "vanish" );
                        if ( is.equals( "true" ) ) {
                            SetVanish.setVanish( p , false );
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                            utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) );
                        } else if ( is.equals( "false" ) ) {
                            SetVanish.setVanish( p , true );
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&aYou vanished&r " + p.getDisplayName( ) );
                            utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }
        }
        return true;
    }
}

