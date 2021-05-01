package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Reports.ReportMenu;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Report implements TabExecutor {
    
    public Report( main plugin ){
        plugin.getCommand( "report" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.report" ) ) {
                    if ( args.length == 1 ) {
                        Player p = ( Player ) sender;
                        new ReportMenu( main.getPlayerMenuUtility( p ) , main.plugin , args[0] ).open( p );
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "report <player>" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "only_players" , "lg" , "sv" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
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
            }
        }
        return version;
    }
}
