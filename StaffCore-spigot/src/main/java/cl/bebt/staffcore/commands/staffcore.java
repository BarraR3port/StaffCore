/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.UpdateChecker;
import cl.bebt.staffcore.utils.utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class staffcore implements TabExecutor {
    
    private final main plugin;
    
    public staffcore( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "staffcore" ).setExecutor( this );
    }
    
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        if ( args.length == 1 ) {
            List < String > placeholders = new ArrayList <>( );
            if ( !utils.isOlderVersion( ) ) {
                placeholders.add( "vanished" );
                placeholders.add( "staff" );
                placeholders.add( "changelanguage" );
            }
            placeholders.add( "version" );
            placeholders.add( "reload" );
            placeholders.add( "link" );
            placeholders.add( "unlink" );
            return placeholders;
        } else if ( args.length == 2 ) {
            List < String > placeholders = new ArrayList <>( );
            if ( args[0].equalsIgnoreCase( "changelanguage" ) ) {
                placeholders.add( "EN_NA" );
                placeholders.add( "ES_CL" );
            } else if ( args[0].equalsIgnoreCase( "link" ) || args[0].equalsIgnoreCase( "unlink" ) ) {
                placeholders.add( utils.getWebServer( ) );
            }
            return placeholders;
        } else if ( args.length == 3 ) {
            List < String > placeholders = new ArrayList <>( );
            if ( args[0].equalsIgnoreCase( "link" ) ) {
                placeholders.add( sender.getName( ) );
            } else if ( args[0].equalsIgnoreCase( "unlink" ) ) {
                placeholders.add( sender.getName( ) );
            }
            return placeholders;
        } else if ( args.length == 4 ) {
            List < String > placeholders = new ArrayList <>( );
            placeholders.add( "password" );
            return placeholders;
        }
        return null;
    }
    
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !(sender instanceof Player) ) {
            if ( args.length == 0 ) {
                utils.tell( sender , " " );
                utils.tell( sender , "                 &6&5StaffCore  &6" );
                utils.tell( sender , " " );
                utils.tell( sender , "          &aVersion: &d" + plugin.getDescription( ).getVersion( ) );
                utils.tell( sender , "          &aAuthor: &dBarrar3port" );
                utils.tell( sender , "          &aMysql: &d" + utils.mysqlEnabled( ) );
                utils.tell( sender , "          &aBungeeCord: &d" + utils.getBoolean( "bungeecord.enabled" ) );
                utils.tell( sender , "          &aServer Version: &d" + utils.getServerVersion( ) );
                utils.tell( sender , "          &aTPS: &d" + ( int ) utils.getTPS( ) );
                utils.tell( sender , " " );
            }
            if ( args.length == 1 ) {
                if ( args[0].equalsIgnoreCase( "version" ) ) {
                    utils.tell( sender , " " );
                    utils.tell( sender , "                 &aAuthor: &dBarraR3port" );
                    utils.tell( sender , " " );
                    utils.tell( sender , "                 &aStaff Core Version: &7" + plugin.getDescription( ).getVersion( ) );
                    utils.tell( sender , " " );
                } else if ( args[0].equalsIgnoreCase( "reload" ) ) {
                    utils.reloadConfigs( );
                    utils.tell( sender , "&aStaffCore Reloaded!" );
                } else if ( args[0].equalsIgnoreCase( "vanished" ) ) {
                    if ( !utils.isOlderVersion( ) ) {
                        utils.tell( sender , " " );
                        utils.tell( sender , "&b                     &5Vanished Players:" );
                        utils.tell( sender , " " );
                        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                            if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                ComponentBuilder cb = new ComponentBuilder( utils.chat( "&aClick to teleport!" ) );
                                TextComponent v = new TextComponent( utils.chat( "&a            &r" + people.getDisplayName( ) ) );
                                v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
                                v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + people.getName( ) ) );
                                sender.spigot( ).sendMessage( v );
                            }
                        }
                        if ( StaffCoreAPI.getVanishedPlayers( ).size( ) == 0 )
                            utils.tell( sender , "             &cNo players vanished!" );
                        utils.tell( sender , " " );
                    } else {
                        utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                    }
                } else if ( args[0].equalsIgnoreCase( "staff" ) ) {
                    if ( !utils.isOlderVersion( ) ) {
                        utils.tell( sender , " " );
                        utils.tell( sender , "&b                 &5Players in Staff mode:" );
                        utils.tell( sender , " " );
                        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                            if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                ComponentBuilder cb = new ComponentBuilder( utils.chat( "&aClick to teleport!" ) );
                                TextComponent v = new TextComponent( utils.chat( "&a            &r" + people.getDisplayName( ) ) );
                                v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
                                v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + people.getName( ) ) );
                                sender.spigot( ).sendMessage( v );
                            }
                        }
                        if ( StaffCoreAPI.getStaffPlayers( ).size( ) == 0 )
                            utils.tell( sender , "             &cNo players in staff mode!" );
                        utils.tell( sender , " " );
                    } else {
                        utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                }
            } else if ( args.length == 2 ) {
                if ( args[0].equalsIgnoreCase( "changelanguage" ) ) {
                    if ( args[1].equalsIgnoreCase( "EN_NA" ) ) {
                        plugin.getConfig( ).set( "language" , "EN_NA" );
                        utils.tell( sender , utils.getString( "language_changed" , "lg" , "sv" ).replace( "%language%" , "EN_NA" ) );
                    } else if ( args[1].equalsIgnoreCase( "ES_CL" ) ) {
                        plugin.getConfig( ).set( "language" , "ES_CL" );
                        utils.tell( sender , utils.getString( "language_changed" , "lg" , "sv" ).replace( "%language%" , "ES_CL" ) );
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
            }
        } else {
            if ( sender.hasPermission( "staffcore.staff" ) ) {
                if ( args.length == 0 ) {
                    utils.tell( sender , " " );
                    utils.tell( sender , "                 &6&5StaffCore  &6" );
                    utils.tell( sender , " " );
                    utils.tell( sender , "          &aVersion: &d" + plugin.getDescription( ).getVersion( ) );
                    utils.tell( sender , "          &aAuthor: &d" + plugin.getDescription( ).getAuthors( ) );
                    utils.tell( sender , "          &aMysql: &d" + utils.mysqlEnabled( ) );
                    utils.tell( sender , "          &aBungeeCord: &d" + utils.getBoolean( "bungeecord.enabled" ) );
                    utils.tell( sender , "          &aWeb Linked: &d" + utils.isWebServerLinked( ) );
                    utils.tell( sender , "          &aServer Version: &d" + utils.getServerVersion( ) );
                    utils.tell( sender , "          &aTPS: &d" + ( int ) utils.getTPS( ) );
                    utils.tell( sender , " " );
                } else if ( args.length == 1 ) {
                    if ( args[0].equalsIgnoreCase( "version" ) ) {
                        utils.tell( sender , " " );
                        utils.tell( sender , "                 &bAuthor: &dBarraR3port" );
                        utils.tell( sender , " " );
                        utils.tell( sender , "                 &aStaff Core Version: &7" + plugin.getDescription( ).getVersion( ) );
                        new UpdateChecker( plugin ).getLatestVersion( version -> {
                            if ( plugin.getDescription( ).getVersion( ).equalsIgnoreCase( version ) ) {
                                utils.tell( sender , "&a                 You are using the latest version!" );
                            } else {
                                ComponentBuilder cb = new ComponentBuilder( utils.chat( "&aClick to download!" ) );
                                TextComponent v = new TextComponent( utils.chat( "&c                 Hey, there is a new version out!" ) );
                                v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
                                v.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , "https://www.spigotmc.org/resources/staffcore.82324/" ) );
                                sender.spigot( ).sendMessage( v );
                            }
                        } );
                        utils.tell( sender , " " );
                    } else if ( args[0].equalsIgnoreCase( "reload" ) ) {
                        if ( sender.hasPermission( "staffcore.reload" ) ) {
                            utils.reloadConfigs( );
                            utils.tell( sender , "&aStaffCore Reloaded!" );
                        } else {
                            utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                        }
                    } else if ( args[0].equalsIgnoreCase( "vanished" ) ) {
                        if ( !utils.isOlderVersion( ) ) {
                            Player p = ( Player ) sender;
                            p.sendMessage( " " );
                            utils.tell( p , "&b                     &5Vanished Players:" );
                            p.sendMessage( " " );
                            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                                if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                    ComponentBuilder cb = new ComponentBuilder( utils.chat( "&aClick to teleport!" ) );
                                    TextComponent v = new TextComponent( utils.chat( "&a            &r" + people.getDisplayName( ) ) );
                                    v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
                                    v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + people.getName( ) ) );
                                    sender.spigot( ).sendMessage( v );
                                }
                            }
                            if ( StaffCoreAPI.getVanishedPlayers( ).size( ) == 0 )
                                utils.tell( sender , "             &cNo players vanished!" );
                            utils.tell( sender , " " );
                        } else {
                            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                        }
                    } else if ( args[0].equalsIgnoreCase( "staff" ) ) {
                        if ( !utils.isOlderVersion( ) ) {
                            Player p = ( Player ) sender;
                            p.sendMessage( " " );
                            utils.tell( p , "&b                 &5Players in Staff mode:" );
                            p.sendMessage( " " );
                            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                                if ( people.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                    ComponentBuilder cb = new ComponentBuilder( utils.chat( "&aClick to teleport!" ) );
                                    TextComponent v = new TextComponent( utils.chat( "&a            &r" + people.getDisplayName( ) ) );
                                    v.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
                                    v.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/tp " + people.getName( ) ) );
                                    sender.spigot( ).sendMessage( v );
                                }
                            }
                            if ( StaffCoreAPI.getStaffPlayers( ).size( ) == 0 )
                                utils.tell( sender , "             &cNo players in staff mode!" );
                            utils.tell( sender , " " );
                        } else {
                            utils.tell( sender , utils.getString( "not_for_older_versions" , "lg" , "sv" ) );
                        }
                    } else if ( !args[0].equalsIgnoreCase( "version" ) && !args[0].equalsIgnoreCase( "vanished" ) && !args[0].equalsIgnoreCase( "staff" ) ) {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                    }
                } else if ( args.length == 2 ) {
                    if ( args[0].equalsIgnoreCase( "changelanguage" ) ) {
                        if ( args[1].equalsIgnoreCase( "EN_NA" ) ) {
                            plugin.getConfig( ).set( "language" , "EN_NA" );
                            utils.tell( sender , utils.getString( "language_changed" , "lg" , "sv" ).replace( "%language%" , "EN_NA" ) );
                        } else if ( args[1].equalsIgnoreCase( "ES_CL" ) ) {
                            plugin.getConfig( ).set( "language" , "ES_CL" );
                            utils.tell( sender , utils.getString( "language_changed" , "lg" , "sv" ).replace( "%language%" , "ES_CL" ) );
                        } else if ( args[1].equalsIgnoreCase( "FR" ) ) {
                            plugin.getConfig( ).set( "language" , "FR" );
                            utils.tell( sender , utils.getString( "language_changed" , "lg" , "sv" ).replace( "%language%" , "FR" ) );
                        } else {
                            utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                    }
                } else if ( args.length == 4 ) {
                    if ( args[0].equalsIgnoreCase( "link" ) ) {
                        if ( sender.hasPermission( "staffcore.linkweb" ) ) {
                            Player p = ( Player ) sender;
                            utils.linkWeb( p , args[1] , args[2] , args[3] );
                        } else {
                            utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                        }
                    } else if ( args[0].equalsIgnoreCase( "unlink" ) ) {
                        if ( sender.hasPermission( "staffcore.linkweb" ) ) {
                            Player p = ( Player ) sender;
                            utils.unlinkWeb( p , args[1] , args[2] , args[3] );
                        } else {
                            utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
                        }
                    } else {
                        utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                    }
                } else {
                    utils.tell( sender , utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "staffcore <version/vanished/staff/reload/changelanguage/link>" ) );
                }
            } else {
                utils.tell( sender , utils.getString( "no_permission" , "lg" , "sv" ) );
            }
        }
        
        return false;
    }
}
