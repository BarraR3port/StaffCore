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
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage, use:" ) );
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: freeze <player>" ) );
            } else if ( args.length == 1 ) {
                Player p = Bukkit.getPlayer( args[0] );
                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                if ( p instanceof Player ) {
                    if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                        FreezePlayer.FreezePlayer( p , "CONSOLE" , false );
                    } else if ( !((PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ))) ) {
                        FreezePlayer.FreezePlayer( p , "CONSOLE" , true );
                    }
                }
            }
        } else if ( sender instanceof Player ) {
            Player player = ( Player ) sender;
            if ( player.hasPermission( "staffcore.freeze" ) ) {
                if ( args.length == 0 ) {
                    player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&4&lWrong usage" ) );
                    player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&4&lUse: freeze <player>" ) );
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( p == player ) {
                            if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                                if ( p.hasPermission( "staffcore.unfreeze.himself" ) ) {
                                    FreezePlayer.FreezePlayer( p , player.getName( ) , false );
                                    p.sendMessage( utils.chat( "&a&lYou unfreeze yourself" ) );
                                } else if ( !(p.hasPermission( "staffcore.unfreeze.himself" )) ) {
                                    p.sendMessage( utils.chat( "&4&lYou have no permissions to unfreeze yourself!" ) );
                                }
                            } else {
                                player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&4&lYou can't freeze yourself" ) );
                                player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&4&lUse: freeze <other_player>" ) );
                            }
                        } else if ( PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                            FreezePlayer.FreezePlayer( p , player.getName( ) , false );
                        } else if ( !((PlayerData.has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ))) ) {
                            if ( p.hasPermission( "staffcore.freeze.bypass" ) ) {
                                player.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + p.getName( ) + plugin.getConfig( ).getString( "staff.freeze_bypass" ) ) );
                                return false;
                            } else {
                                FreezePlayer.FreezePlayer( p , player.getName( ) , true );
                            }
                        }
                    } else if ( !(Bukkit.getPlayer( args[0] ) instanceof Player) ) {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            } else if ( !(player.hasPermission( "staffcore.freeze" )) ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
            }
        }
        return true;
    }
}
