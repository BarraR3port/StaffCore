package cl.bebt.staffcore.commands;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suicide implements CommandExecutor {
    
    private final main plugin;
    
    public Suicide( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "suicide" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender instanceof Player ) {
            
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.suicide" ) ) {
                p.setHealth( 0 );
            } else {
                utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
            }
        } else {
            utils.tell( sender , utils.getString( "only_players" , "lg" , "sv" ) );
        }
        
        return true;
    }
}