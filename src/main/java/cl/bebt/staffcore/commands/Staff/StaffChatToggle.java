package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class StaffChatToggle implements CommandExecutor, Listener{
    
    private final main plugin;
    
    public StaffChatToggle( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "togglestaffchat" ).setExecutor( this );
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( !(sender instanceof Player) ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player p = Bukkit.getPlayer( args[0] );
                    PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                    if ( PlayerData.has( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
                        utils.tell( p , "&8[&3&lSC&r&8]&r" + " &cOff" );
                        PlayerData.remove( new NamespacedKey( plugin , "staffchat" ) );
                    } else {
                        utils.tell( p , "&8[&3&lSC&r&8]&r" + " &aOn" );
                        PlayerData.set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
                
            } else {
                Player p = ( Player ) sender;
                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                if ( p.hasPermission( "staffcore.tsc" ) ) {
                    if ( PlayerData.has( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING ) ) {
                        utils.tell( p , "&8[&3&lSC&r&8]&r" + " &cOff" );
                        PlayerData.remove( new NamespacedKey( plugin , "staffchat" ) );
                    } else {
                        utils.tell( p , "&8[&3&lSC&r&8]&r" + " &aOn" );
                        PlayerData.set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                }
                
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
    }
    
}
