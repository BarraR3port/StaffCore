package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.menu.Reports.ReportManager;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportList implements CommandExecutor {
    
    private final main plugin;
    
    public ReportList( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "reportlist" ).setExecutor( this );
    }
    
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            utils.tell( sender , "&aHey, you need to be a player to execute this command!" );
        } else {
            if ( sender.hasPermission( "staffcore.reportlist" ) ) {
                Player p = ( Player ) sender;
                new ReportManager( main.getPlayerMenuUtility( p ) , main.plugin ).open( p );
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }
        }
        return false;
    }
    
}
