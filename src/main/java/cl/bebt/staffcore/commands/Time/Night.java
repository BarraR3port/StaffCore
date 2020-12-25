package cl.bebt.staffcore.commands.Time;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Night implements CommandExecutor {
    
    private final main plugin;
    
    public Night( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "night" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            for ( World world : Bukkit.getServer( ).getWorlds( ) ) {
                world.setTime( 14000 );
                world.setThundering( false );
                world.setStorm( false );
            }
            Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7Time set &8Night &r&7by console" ) );
            return true;
        }
        Player p = ( Player ) sender;
        if ( p.hasPermission( "staffcore.night" ) ) {
            for ( World world : Bukkit.getServer( ).getWorlds( ) ) {
                world.setTime( 14000 );
                world.setThundering( false );
                world.setStorm( false );
            }
            Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7Time set &8Night &r&7by &r" + p.getDisplayName( ) ) );
        } else {
            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
        }
        return true;
    }
}
