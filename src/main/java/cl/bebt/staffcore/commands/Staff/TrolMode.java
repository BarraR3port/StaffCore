package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TrolMode implements CommandExecutor {
    
    private final main plugin;
    
    public TrolMode( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "trolmode" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command command , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( sender instanceof Player ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    StaffCoreAPI.setTrolMode( p , !StaffCoreAPI.getTrolStatus( p.getName( ) ) );
                    if ( StaffCoreAPI.getTrolStatus( p.getName( ) ) ) {
                        utils.tell( p , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &aON" );
                    } else {
                        utils.tell( p , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &cOFF" );
                    }
                }
                if ( args.length == 1 ){
                    if ( Bukkit.getPlayer( args[0] ) != null ){
                        Player target = Bukkit.getPlayer( args[0] );
                        StaffCoreAPI.setTrolMode( target, !StaffCoreAPI.getTrolStatus( args[0] ) );
                        if ( StaffCoreAPI.getTrolStatus( args[0] ) ) {
                            utils.tell( target , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &aON" );
                            utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&aEnabled &7Trol Mode to &9"+ args[0] );
                        } else {
                            utils.tell( target , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &cOFF" );
                            utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cDisabled &7Trol Mode to &9"+ args[0] );
                        }
                    }
                }
            } else {
                if ( args.length == 0 ) {
                    utils.tell( sender, utils.getString( "server_prefix" ) + "&cYou need a target" );
                }
                if ( args.length == 1 ){
                    if ( Bukkit.getPlayer( args[0] ) != null ){
                         Player target = Bukkit.getPlayer( args[0] );
                         StaffCoreAPI.setTrolMode( target, !StaffCoreAPI.getTrolStatus( args[0] ) );
                        if ( StaffCoreAPI.getTrolStatus( args[0] ) ) {
                            utils.tell( target , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &aON" );
                            utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&aEnabled &7Trol Mode to &9"+ args[0] );
                        } else {
                            utils.tell( target , utils.getString( "staff.staff_prefix" ) + "&7Trol Mode &cOFF" );
                            utils.tell( sender , utils.getString( "staff.staff_prefix" ) + "&cDisabled &7Trol Mode to &9"+ args[0] );
                        }
                    }
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return false;
    }
}
