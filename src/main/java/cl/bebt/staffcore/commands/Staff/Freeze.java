package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.FreezePlayer;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Freeze implements CommandExecutor {
    
    private final main plugin;
    
    public Freeze( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "freeze" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ) {
            if ( !(sender instanceof Player) ) {
                if ( args.length == 1 ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( p instanceof Player ) {
                        if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                            FreezePlayer.FreezePlayer( p , "CONSOLE" , false );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING )) ) {
                            FreezePlayer.FreezePlayer( p , "CONSOLE" , true );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                }
            } else if ( sender instanceof Player ) {
                Player player = ( Player ) sender;
                if ( player.hasPermission( "staffcore.freeze" ) ) {
                    if ( args.length == 1 ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            Player p = Bukkit.getPlayer( args[0] );
                            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                            if ( p == player ) {
                                if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                                    if ( p.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                        FreezePlayer.FreezePlayer( p , player.getName( ) , false );
                                        utils.tell( p , utils.getString( "freeze.unfreeze_ur_self" , "lg" , "staff" ) );
                                    } else {
                                        utils.tell( p , utils.chat( utils.getString( "no_permission" , "lg" , "staff" ) ) );
                                    }
                                } else {
                                    utils.tell( p , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                                }
                            } else if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                                FreezePlayer.FreezePlayer( p , player.getName( ) , false );
                            } else if ( !(PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING )) ) {
                                if ( p.hasPermission( "staffcore.freeze.bypass" ) ) {
                                    utils.tell( sender , utils.getString( "freeze.freeze_bypass" , "lg" , "staff" ) );
                                    return false;
                                } else {
                                    FreezePlayer.FreezePlayer( p , player.getName( ) , true );
                                }
                            }
                        } else if ( !(Bukkit.getPlayer( args[0] ) instanceof Player) ) {
                            utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "freeze <player>" ) );
                    }
                } else {
                    sender.sendMessage( utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            }
        } else {
            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
        }
        return true;
    }
}
