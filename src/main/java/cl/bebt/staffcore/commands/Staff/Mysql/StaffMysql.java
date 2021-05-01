package cl.bebt.staffcore.commands.Staff.Mysql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.SetStaffItems;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class StaffMysql implements CommandExecutor {
    
    private final main plugin;
    
    public StaffMysql( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "staff" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( !(sender instanceof Player) ) {
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        String is = SQLGetter.isTrue( p , "staff" );
                        if ( is.equals( "true" ) ) {
                            SetStaffItems.Off( p );
                            utils.tell( sender , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                        } else if ( is.equals( "false" ) ) {
                            SetStaffItems.On( p );
                            utils.tell( sender , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff | staff <player>" ) );
                }
            } else {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    String is = SQLGetter.isTrue( p , "staff" );
                    if ( p.hasPermission( "staffcore.staff" ) ) {
                        if ( is.equals( "true" ) ) {
                            SetStaffItems.Off( p );
                            utils.tell( sender , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                        } else if ( is.equals( "false" ) ) {
                            SetStaffItems.On( p );
                            utils.tell( sender , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( sender.hasPermission( "staffcore.staff" ) ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            Player p = Bukkit.getPlayer( args[0] );
                            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                            if ( PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                SetStaffItems.Off( p );
                                utils.tell( sender , utils.getString( "staff.disabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                                utils.tell( p , utils.getString( "staff.disabled" , "lg" , "staff" ) );
                            } else if ( !(PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )) ) {
                                SetStaffItems.On( p );
                                utils.tell( sender , utils.getString( "staff.enabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                                utils.tell( p , utils.getString( "staff.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staff | staff <player>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}