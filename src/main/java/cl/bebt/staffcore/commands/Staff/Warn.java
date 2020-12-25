package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.WarnPlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Warn implements TabExecutor{

    private final main plugin;

    public Warn( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "warn" ).setExecutor( this );
    }


    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {

        } else {
            if ( args.length == 1 ) {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.warn" ) ) {
                    if ( utils.isRegistered( args[0] ) ) {
                        new WarnPlayer( p , args[0] , plugin );
                    } else {
                        utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cThe player &a" + args[0] + " &cis not registered." );
                    }
                } else {
                    utils.tell( sender , utils.getString( "server_prefix" ) + utils.getString( "no_permissions" ) );
                }

            } else {
                utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cWrong use, use /warn <player>" );
            }
        }
        return false;
    }

    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        if ( args.length == 1 ) {
            ArrayList < String > Players = utils.getUsers( );
            if ( !Players.isEmpty( ) ) {
                Players.remove( sender.getName( ) );
                version.addAll( Players );
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&cNo players saved!" );
            }
        }
        return version;
    }
}
