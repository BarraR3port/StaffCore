/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.SQL.Queries.AltsQuery;
import cl.bebt.staffcoreapi.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckAlts implements TabExecutor {
    private static main plugin;
    
    public CheckAlts( main plugin ){
        CheckAlts.plugin = plugin;
        plugin.getCommand( "alts" ).setExecutor( this );
    }
    
    public static List < String > ips( UUID uuid ){
        if ( Utils.mysqlEnabled( ) ) {
            return Utils.makeList( AltsQuery.getAltsIps( uuid ) );
        } else {
            return new ArrayList <>( UserUtils.getIpsAsHash( uuid ).values( ) );
        }
    }
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( sender.hasPermission( "staffcore.alts" ) ) {
            if ( args.length == 1 ) {
                try {
                    UUID uuid = Utils.getUUIDFromName( args[0] );
                    List < String > alts = Utils.getAlts( uuid );
                    if ( alts.isEmpty( ) ) {
                        Utils.tell( sender , Utils.getString( "alts.no_alts" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                    } else {
                        Utils.tell( sender , Utils.getString( "alts.alts" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                        for ( String alt : alts ) {
                            List < String > ips = ips( uuid );
                            if ( !alt.equalsIgnoreCase( args[0] ) ) {
                                Utils.tell( sender , "&7  ► &a" + alt );
                                for ( String ip : ips ) {
                                    Utils.tell( sender , "&7    ► &a" + ip );
                                }
                            }
                        }
                    }
                } catch ( NullPointerException error ) {
                    Utils.tell( sender , Utils.getString( "never_seen" , "lg" , "staff" ).replace( "%player%" , args[0] ) );
                }
            } else {
                Utils.tell( sender , Utils.getString( "wrong_usage" , "lg" , "staff" ).replace( "%command%" , "alts <player>" ) );
            }
        }
        return true;
    }
    
    @Override
    public List < String > onTabComplete( CommandSender sender , Command command , String alias , String[] args ){
        List < String > version = new ArrayList <>( );
        if ( args.length == 1 ) {
            ArrayList < String > Players = Utils.getUsers( );
            if ( !Players.isEmpty( ) ) {
                Players.remove( sender.getName( ) );
                version.addAll( Players );
            }
        }
        return version;
    }
}
