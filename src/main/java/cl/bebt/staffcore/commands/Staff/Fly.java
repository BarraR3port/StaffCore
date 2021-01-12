package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.SetFly;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Fly implements CommandExecutor {
    private final main plugin;
    
    public Fly( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "fly" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( !(sender instanceof Player) ) {
                if ( args.length == 0 ) {
                    sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage" ) );
                    sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: fly <player>" ) );
                }
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                            PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                            SetFly.SetFly( p , false );
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_disabled" ) );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                            PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                            SetFly.SetFly( p , true );
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_enabled" ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            } else {
                if ( args.length == 0 ) {
                    
                    Player p = ( Player ) sender;
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( p.hasPermission( "staffcore.fly" ) ) {
                        if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                            PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                            SetFly.SetFly( p , false );
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_disabled" ) );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                            PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                            SetFly.SetFly( p , true );
                            utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_enabled" ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( sender.hasPermission( "staffcore.fly" ) ) {
                            if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                                PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                                SetFly.SetFly( p , false );
                                utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_disabled" ) + " &7to&r " + p.getDisplayName( ) );
                                utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_disabled" ) );
                            } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                                PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                                SetFly.SetFly( p , true );
                                utils.tell( sender , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_enabled" ) + " &7to&r  " + p.getDisplayName( ) );
                                utils.tell( p , plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "staff.fly_enabled" ) );
                            }
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                        }
                    } else if ( !(Bukkit.getPlayer( args[0] ) instanceof Player) ) {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
    }
}
