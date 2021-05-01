package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Reports.ReportManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportList implements CommandExecutor {
    
    public ReportList( main plugin ){
        plugin.getCommand( "reportlist" ).setExecutor( this );
    }
    
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( sender instanceof Player ) {
                if ( sender.hasPermission( "staffcore.reportlist" ) ) {
                    Player p = ( Player ) sender;
                    new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "only_players" , "lg" , "sv" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return false;
    }
    
}
