package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.SetFly;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GmSurvival implements CommandExecutor {
    private final main plugin;
    
    public GmSurvival( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "gms" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage, use:" ) );
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&4&lUse: gms <player>" ) );
            } else if ( args.length == 1 ) {
                Player p = Bukkit.getPlayer( args[0] );
                if ( p instanceof Player ) {
                    if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                        p.setGameMode( GameMode.SURVIVAL );
                        if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                            if ( SQLGetter.isTrue( p , "flying" ).equalsIgnoreCase( "true" ) ) {
                                SetFly.SetFly( p , true );
                            }
                            if ( SQLGetter.isTrue( p , "vanish" ).equalsIgnoreCase( "true" ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } else {
                            try {
                                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                                if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                                    SetFly.SetFly( p , true );
                                }
                                if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                    p.setAllowFlight( true );
                                    p.setFlying( true );
                                }
                            } catch ( NoSuchMethodError ignored ){ }
                        }
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7You set Survival mode to: " + p.getName( ) ) );
                    } else {
                        sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + "&7The player: " + p.getName( ) + " &7is already in survival" ) );
                    }
                    return false;
                }
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "staff.staff_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
            }
            return false;
        }
        if ( args.length == 0 ) {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.gms" ) ) {
                if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                    p.setGameMode( GameMode.SURVIVAL );
                    if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                        if ( SQLGetter.isTrue( p , "flying" ).equalsIgnoreCase( "true" ) ) {
                            SetFly.SetFly( p , true );
                        }
                        if ( SQLGetter.isTrue( p , "vanish" ).equalsIgnoreCase( "true" ) ) {
                            p.setAllowFlight( true );
                            p.setFlying( true );
                        }
                    } else {
                        try{
                            PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                            if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                                SetFly.SetFly( p , true );
                            }
                            if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } catch ( NoSuchMethodError ignored ){ }
                    }
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "survival" ) ) );
                } else {
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "survival_already" ) ) );
                }
            } else {
                sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
            }
        } else if ( args.length == 1 ) {
            if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                Player p = Bukkit.getPlayer( args[0] );
                if ( sender.hasPermission( "staffcore.gms" ) ) {
                    if ( !(p.getGameMode( ) == GameMode.SURVIVAL) ) {
                        p.setGameMode( GameMode.SURVIVAL );
                        if ( plugin.getConfig( ).getBoolean( "mysql" ) ) {
                            if ( SQLGetter.isTrue( p , "flying" ).equalsIgnoreCase( "true" ) ) {
                                SetFly.SetFly( p , true );
                            }
                            if ( SQLGetter.isTrue( p , "vanish" ).equalsIgnoreCase( "true" ) ) {
                                p.setAllowFlight( true );
                                p.setFlying( true );
                            }
                        } else {
                            try{
                                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                                if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                                    SetFly.SetFly( p , true );
                                }
                                if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                    p.setAllowFlight( true );
                                    p.setFlying( true );
                                }
                            } catch ( NoSuchMethodError ignored ){ }
                        }
                        if ( !(sender == p) ) {
                            sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7You set Survival mode to: " + p.getName( ) ) );
                        }
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "survival" ) ) );
                        
                    } else {
                        if ( !(sender == p) ) {
                            sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The player: " + p.getName( ) + " &7is already in survival" ) );
                        }
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "survival_already" ) ) );
                    }
                } else {
                    sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                }
                return false;
            }
            sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) ) );
        }
        return true;
    }
}