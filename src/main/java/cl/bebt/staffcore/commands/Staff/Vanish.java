package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.SetVanish;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class Vanish implements CommandExecutor{

    private final main plugin;

    public Vanish( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "vanish" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                sender.sendMessage( utils.chat( "&4&lWrong usage" ) );
                sender.sendMessage( utils.chat( "&4&lUse: vanish <player>" ) );
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( p.hasPermission( "staffcore.vanish" ) ) {
                        if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                            SetVanish.setVanish( p , false );
                            if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                            }
                            if ( p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.staff_disabled" ) ) );
                            } else {
                                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) ) );
                            }
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )) ) {
                            SetVanish.setVanish( p , true );
                            if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&aYou vanished&r " + p.getDisplayName( ) );
                            }
                            if ( p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.staff_enabled" ) ) );
                            } else {
                                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) ) );
                            }
                        }
                    } else if ( p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                        SetVanish.setVanish( p , false );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) ) );
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
            }
        } else {
            if ( sender.hasPermission( "staffcore.vanish" ) ) {
                if ( args.length == 0 ) {
                    Player p = ( Player ) sender;
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                        SetVanish.setVanish( p , false );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) ) );
                    } else if ( !(PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )) ) {
                        SetVanish.setVanish( p , true );
                        p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                        if ( !(p.hasPermission( "staffcore.vanish" )) ) {
                            if ( p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                SetVanish.setVanish( p , false );
                                p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) ) );
                                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                            } else if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cThe player&r " + p.getDisplayName( ) + " &cDon't have permissions to use vanish!" );
                            }
                        } else if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                            SetVanish.setVanish( p , false );
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cYou unvanished&r " + p.getDisplayName( ) );
                            p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.un_vanished" ) ) );
                        } else if ( !(PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING )) ) {
                            SetVanish.setVanish( p , true );
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&aYou vanished&r " + p.getDisplayName( ) );
                            p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "staff.vanished" ) ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );

            }

        }
        return true;
    }
}

