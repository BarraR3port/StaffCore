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
            if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                Player p = Bukkit.getPlayer( args[0] );
                String is = SQLGetter.isTrue( p , "staffchat" );
                if ( is.equals( "true" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r &cOff" );
                    SQLGetter.set( p.getName( ) , "staffchat" , "false" );
                    main.toggledStaffChat.remove(p.getName( ) );
                    if ( !utils.isOlderVersion( ) ) {
                        p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                    }
                } else if ( is.equals( "false" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r &aOn" );
                    SQLGetter.set( p.getName( ) , "staffchat" , "true" );
                    main.toggledStaffChat.add(p.getName( ) );
                    if ( !utils.isOlderVersion( ) ) {
                        p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                    }
                }
            } else {
                utils.tell( sender , this.plugin.getConfig( ).getString( "server_prefix" ) + this.plugin.getConfig( ).getString( "player_dont_exist" ) );
            }
        } else {
            Player p = ( Player ) sender;
            String is = SQLGetter.isTrue( p , "staffchat" );
            if ( p.hasPermission( "staffcore.tsc" ) ) {
                if ( is.equals( "true" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r &cOff" );
                    SQLGetter.set( p.getName( ) , "staffchat" , "false" );
                    main.toggledStaffChat.remove(p.getName( ) );
                    if ( !utils.isOlderVersion( ) ) {
                        p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
                    }
                } else if ( is.equals( "false" ) ) {
                    utils.tell( p , "&8[&3&lSC&r&8]&r &aOn" );
                    SQLGetter.set( p.getName( ) , "staffchat" , "true" );
                    main.toggledStaffChat.add(p.getName( ) );
                    if ( !utils.isOlderVersion( ) ) {
                        p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                    }
                }
            } else {
                utils.tell( sender , this.plugin.getConfig( ).getString( "server_prefix" ) + this.plugin.getConfig( ).getString( "no_permissions" ) );
            }
        }
        return true;
    }
}
