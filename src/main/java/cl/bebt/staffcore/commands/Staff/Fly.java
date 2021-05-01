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
        if ( !utils.isOlderVersion( ) ) {
            if ( !(sender instanceof Player) ) {
                if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                            PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                            SetFly.SetFly( p , false );
                            utils.tell( sender , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                            PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                            SetFly.SetFly( p , true );
                            utils.tell( sender , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                }
            } else {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( p.hasPermission( "staffcore.fly" ) ) {
                        if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                            PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                            SetFly.SetFly( p , false );
                            utils.tell( sender , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                            PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                            SetFly.SetFly( p , true );
                            utils.tell( sender , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( sender.hasPermission( "staffcore.fly" ) ) {
                            if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                                PlayerData.remove( new NamespacedKey( plugin , "flying" ) );
                                SetFly.SetFly( p , false );
                                utils.tell( sender , utils.getString( "fly.disabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                                utils.tell( p , utils.getString( "fly.disabled" , "lg" , "staff" ) );
                            } else if ( !(PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING )) ) {
                                PlayerData.set( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING , "flying" );
                                SetFly.SetFly( p , true );
                                utils.tell( sender , utils.getString( "fly.enabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                                utils.tell( p , utils.getString( "fly.enabled" , "lg" , "staff" ) );
                            }
                        } else {
                            utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "fly | fly <player>" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
