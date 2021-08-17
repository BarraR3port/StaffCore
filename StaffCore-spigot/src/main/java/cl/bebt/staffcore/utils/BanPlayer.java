/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.BansQuery;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class BanPlayer {
    
    public static void unBan( CommandSender p , int Id ){
        String player = "";
        if ( p instanceof Player ) {
            player = p.getName( );
        } else {
            player = utils.getConsoleName( );
        }
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String banned = null;
        String status = "unbanned";
        if ( utils.mysqlEnabled( ) ) {
            JSONObject json = BansQuery.getBanInfo( Id );
            if ( !json.getBoolean( "error" ) ) {
                reason = json.getString( "Reason" );
                created = json.getString( "Date" );
                exp = json.getString( "ExpDate" );
                baner = json.getString( "Banner" );
                banned = json.getString( "Name" );
                BansQuery.deleteBan( Id );
            }
        } else {
            main.plugin.bans.reloadConfig( );
            reason = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            created = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".date" );
            exp = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            baner = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".banned_by" );
            banned = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".name" );
            main.plugin.bans.getConfig( ).set( "bans." + Id , null );
            main.plugin.bans.getConfig( ).set( "current" , currentBans( ) );
            main.plugin.bans.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( Id , p.getName( ) , baner , banned , reason , exp , created , status , utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.ban" ) ) {
                utils.PlaySound( people , "un_ban" );
                for ( String key : utils.getStringList( "ban.change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , player );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%banned%" , banned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%ban_status%" , "UNBANED" );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public static int currentBans( ){
        try {
            if ( utils.mysqlEnabled( ) ) {
                return BansQuery.getCurrentBans( );
            } else {
                ConfigurationSection inventorySection = main.plugin.bans.getConfig( ).getConfigurationSection( "bans" );
                return inventorySection.getKeys( false ).size( );
            }
        } catch ( NullPointerException ignored ) {
            return 0;
        }
        
    }
    
}
