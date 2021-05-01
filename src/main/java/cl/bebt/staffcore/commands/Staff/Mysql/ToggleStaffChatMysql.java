package cl.bebt.staffcore.commands.Staff.Mysql;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class ToggleStaffChatMysql implements CommandExecutor, Listener {
    
    
    private final main plugin;
    
    public ToggleStaffChatMysql( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "togglestaffchat" ).setExecutor( this );
    }
    
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    String is = SQLGetter.isTrue( p , "staffchat" );
                    if ( is.equals( "true" ) ) {
                        utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                        utils.tell( p , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                        SQLGetter.set( p.getName( ) , "staffchat" , "false" );
                        main.toggledStaffChat.remove( p.getName( ) );
                        if ( !utils.isOlderVersion( ) ) {
                            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                        }
                    } else if ( is.equals( "false" ) ) {
                        utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                        utils.tell( p , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                        SQLGetter.set( p.getName( ) , "staffchat" , "true" );
                        main.toggledStaffChat.add( p.getName( ) );
                        if ( !utils.isOlderVersion( ) ) {
                            p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "tsc | tsc <player>" ) );
            }
        } else {
            if ( args.length == 0 ) {
                Player p = ( Player ) sender;
                String is = SQLGetter.isTrue( p , "staffchat" );
                if ( p.hasPermission( "staffcore.tsc" ) ) {
                    if ( is.equals( "true" ) ) {
                        utils.tell( p , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                        SQLGetter.set( p.getName( ) , "staffchat" , "false" );
                        main.toggledStaffChat.remove( p.getName( ) );
                        if ( !utils.isOlderVersion( ) ) {
                            p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                        }
                    } else if ( is.equals( "false" ) ) {
                        utils.tell( p , "&8[&3&lSC&r&8]&r &aOn" );
                        SQLGetter.set( p.getName( ) , "staffchat" , "true" );
                        main.toggledStaffChat.add( p.getName( ) );
                        if ( !utils.isOlderVersion( ) ) {
                            p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                        }
                    }
                } else {
                    utils.tell( sender , utils.getString( "no_permission" , "lg" , "staff" ) );
                }
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    if ( sender.hasPermission( "staffcore.tsc" ) ) {
                        Player p = Bukkit.getPlayer( args[0] );
                        String is = SQLGetter.isTrue( p , "staffchat" );
                        if ( is.equals( "true" ) ) {
                            utils.tell( sender , utils.getString( "staff_chat.disabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                            utils.tell( p , utils.getString( "staff_chat.disabled" , "lg" , "staff" ) );
                            SQLGetter.set( p.getName( ) , "staffchat" , "false" );
                            main.toggledStaffChat.remove( p.getName( ) );
                            if ( !utils.isOlderVersion( ) ) {
                                p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                            }
                        } else if ( is.equals( "false" ) ) {
                            utils.tell( sender , utils.getString( "staff_chat.enabled_to" , "lg" , "staff" ).replace( "%player%" , p.getName( ) ) );
                            utils.tell( p , utils.getString( "staff_chat.enabled" , "lg" , "staff" ) );
                            SQLGetter.set( p.getName( ) , "staffchat" , "true" );
                            main.toggledStaffChat.add( p.getName( ) );
                            if ( !utils.isOlderVersion( ) ) {
                                p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                            }
                        }
                    } else {
                        utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "p_dont_exist" , "lg" , "sv" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "tsc | tsc <player>" ) );
            }
        }
        return true;
    }
}
