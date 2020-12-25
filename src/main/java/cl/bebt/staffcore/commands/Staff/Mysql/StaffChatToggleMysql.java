package cl.bebt.staffcore.commands.Staff.Mysql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class StaffChatToggleMysql implements CommandExecutor, Listener{

    private static final SQLGetter data = main.plugin.data;
    private final main plugin;

    public StaffChatToggleMysql( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "togglestaffchat" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                Player p = Bukkit.getPlayer( args[0] );
                String is = SQLGetter.isTrue( p , "staffchat" );
                if ( is.equals( "true" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r" + " &cOff" );
                    data.setTrue( p , "staffchat" , "false" );
                } else if ( is.equals( "false" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r" + " &aOn" );
                    data.setTrue( p , "staffchat" , "true" );
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
            }

        } else {
            Player p = ( Player ) sender;
            String is = SQLGetter.isTrue( p , "staffchat" );
            if ( p.hasPermission( "staffcore.tsc" ) ) {
                if ( is.equals( "true" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r" + " &cOff" );
                    data.setTrue( p , "staffchat" , "false" );
                } else if ( is.equals( "false" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r" + " &aOn" );
                    data.setTrue( p , "staffchat" , "true" );
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }

        }
        return true;
    }

}
