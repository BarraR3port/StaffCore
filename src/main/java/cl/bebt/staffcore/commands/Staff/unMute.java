package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.CountdownManager;
import cl.bebt.staffcore.utils.ToggleChat;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class unMute implements TabExecutor{
    private final main plugin;

    public unMute( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "unmute" ).setExecutor( this );
    }

    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > muted = new ArrayList <>( );
        if ( args.length == 1 ) {
            for ( Player p : Bukkit.getOnlinePlayers( ) ) {
                if ( p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "muted" ) , PersistentDataType.STRING ) ) {
                    muted.add( p.getName( ) );
                } else if ( !CountdownManager.checkMuteCountdown( p ) ) {
                    muted.add( p.getName( ) );
                }
            }
            if ( muted.isEmpty( ) ) {
                muted.add( utils.chat( "&cThere are no muted players!" ) );
                return muted;
            }
            return muted;
        }

        return null;
    }

    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                if ( plugin.chatMuted == false ) {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cThe chat is not muted" );
                } else {
                    Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The CONSOLE &cUnMuted &7the chat!" ) );
                    ToggleChat.Mute( false );
                }
            } else if ( args.length == 1 ) {
                if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                    Player muted = Bukkit.getPlayer( args[0] );
                    ToggleChat.unMute( sender , muted );
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cUse unmute <PlayerName>" );
            }
        } else {
            Player p = ( Player ) sender;
            if ( p.hasPermission( "staffcore.unmute" ) ) {
                if ( args.length == 0 ) {
                    if ( plugin.chatMuted == false ) {
                        utils.tell( p , plugin.getConfig( ).getString( "server_prefix" ) + "&cThe chat is not muted" );
                    } else {
                        Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The player &r" + p.getDisplayName( ) + " &aUnMuted the chat" ) );
                        ToggleChat.Mute( false );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player muted = Bukkit.getPlayer( args[0] );
                        ToggleChat.unMute( sender , muted );
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                } else {
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
                    utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&cUse unmute <PlayerName>" );
                }
            } else {
                utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
            }
        }
        return true;

    }

}
