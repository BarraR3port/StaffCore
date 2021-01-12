package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.ToggleChat;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteChat implements TabExecutor {
    private final main plugin;
    
    public MuteChat( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "mute" ).setExecutor( this );
    }
    
    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        if ( args.length == 2 ) {
            String current = args[1];
            List < String > version = new ArrayList <>( );
            version.add( current + "s" );
            version.add( current + "m" );
            version.add( current + "h" );
            version.add( current + "d" );
            return version;
        }
        return null;
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( !(sender instanceof Player) ) {
                if ( args.length == 0 ) {
                    if ( plugin.chatMuted == false ) {
                        Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The CONSOLE &cMuted &7the chat!" ) );
                        ToggleChat.Mute( true );
                    } else {
                        Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The CONSOLE &cUn Muted &7the chat!" ) );
                        ToggleChat.Mute( false );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        Player muted = Bukkit.getPlayer( args[0] );
                        ToggleChat.MutePlayer( sender , muted );
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                } else if ( args.length == 2 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        try {
                            Player player = Bukkit.getPlayer( args[0] );
                            String time = args[1].substring( args[1].length( ) - 1 );
                            StringBuffer sb = new StringBuffer( args[1] );
                            sb.deleteCharAt( sb.length( ) - 1 );
                            int ammount = Integer.parseInt( sb.toString( ) );
                            ToggleChat.MuteCooldown( sender , player , time , ammount );
                        } catch ( NumberFormatException e ) {
                            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
                            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cExample /mute &a" + args[0] + " &d10<s/m/h/d>" );
                        }
                    }
                    if ( args[1] == "unmute" ) {
                        Player player = Bukkit.getPlayer( args[0] );
                        ToggleChat.MuteCooldown( sender , player , "s" , 0 );
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                    
                }
            } else {
                Player p = ( Player ) sender;
                if ( p.hasPermission( "staffcore.mute" ) ) {
                    if ( args.length == 0 ) {
                        if ( plugin.chatMuted == false ) {
                            Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The player &r" + p.getDisplayName( ) + " &cMuted the Chat!" ) );
                            ToggleChat.Mute( true );
                        } else {
                            Bukkit.broadcastMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + "&7The player &r" + p.getDisplayName( ) + " &aUnMuted the chat" ) );
                            ToggleChat.Mute( false );
                        }
                    } else if ( args.length == 1 ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            Player muted = Bukkit.getPlayer( args[0] );
                            ToggleChat.MutePlayer( p , muted );
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                        }
                    } else if ( args.length == 2 ) {
                        if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                            try {
                                Player player = Bukkit.getPlayer( args[0] );
                                String time = args[1].substring( args[1].length( ) - 1 );
                                StringBuffer sb = new StringBuffer( args[1] );
                                sb.deleteCharAt( sb.length( ) - 1 );
                                int ammount = Integer.parseInt( sb.toString( ) );
                                ToggleChat.MuteCooldown( p , player , time , ammount );
                            } catch ( NumberFormatException e ) {
                                utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
                                utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cExample /mute &a" + args[0] + " &d10<s/m/h/d>" );
                            }
                        } else if ( args[1] == "unmute" ) {
                            Player player = Bukkit.getPlayer( args[0] );
                            ToggleChat.MuteCooldown( p , player , "s" , 0 );
                            return true;
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                        }
                    }
                    
                } else {
                    p.sendMessage( utils.chat( plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) ) );
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return true;
        
    }
    
}
