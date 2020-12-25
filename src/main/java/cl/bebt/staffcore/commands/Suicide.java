package cl.bebt.staffcore.commands;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Suicide implements CommandExecutor{

    private final main plugin;

    public Suicide( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "suicide" ).setExecutor( this );
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            sender.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&cYou can't suicide, you are the console lol" ) );
            return true;
        }
        Player p = ( Player ) sender;
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( p.hasPermission( "staffcore.suicide" ) ) {
            PlayerData.set( new NamespacedKey( plugin , "suicide" ) , PersistentDataType.STRING , "suicide" );
            p.setHealth( 0 );
        } else {
            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
        }

        return true;
    }
}