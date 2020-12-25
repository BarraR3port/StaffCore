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
    private static final SQLGetter data = main.plugin.data;
    private final main plugin;
    
    public StaffMysql( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "staff" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage" ) );
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: staff <player>" ) );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    String is = SQLGetter.isTrue( p , "staff" );
                    if ( is.equals( "true" ) ) {
                        SetStaffItems.Off( p );
                        utils.tell( sender , "&7You remove &r" + p.getDisplayName( ) + " &7from staff mode" );
                    } else if ( is.equals( "false" ) ) {
                        SetStaffItems.On( p );
                        utils.tell( sender , "&7You put &r" + p.getDisplayName( ) + " &7in staff mode" );
                    }
                }
            }
        } else {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                String is = SQLGetter.isTrue( p , "staff" );
                if ( p.hasPermission( "staffcore.staff" ) ) {
                    if ( is.equals( "true" ) ) {
                        SetStaffItems.Off( p );
                    } else if ( is.equals( "false" ) ) {
                        SetStaffItems.On( p );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                }
            } else if ( args.length == 1 ) {
                if ( sender instanceof Player ) {
                    if ( sender.hasPermission( "staffcore.staff" ) ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            Player p = Bukkit.getPlayer( args[0] );
                            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                            if ( PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                SetStaffItems.Off( p );
                                utils.tell( sender , "&7You remove &r" + p.getDisplayName( ) + " &7from staff mode" );
                                
                            } else if ( !(PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING )) ) {
                                SetStaffItems.On( p );
                                utils.tell( sender , "&7You put &r" + p.getDisplayName( ) + " &7in staff mode" );
                            }
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                    }
                }
            }
        }
        return true;
    }
}