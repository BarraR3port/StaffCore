/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.SQL.Queries.BansQuery;
import cl.bebt.staffcoreapi.utils.Utils;
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
            player = Utils.getConsoleName( );
        }
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String banned = null;
        String status = "unbanned";
        if ( Utils.mysqlEnabled( ) ) {
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
            Api.bans.reloadConfig( );
            reason = Api.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            created = Api.bans.getConfig( ).getString( "bans." + Id + ".date" );
            exp = Api.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            baner = Api.bans.getConfig( ).getString( "bans." + Id + ".banned_by" );
            banned = Api.bans.getConfig( ).getString( "bans." + Id + ".name" );
            Api.bans.getConfig( ).set( "bans." + Id , null );
            Api.bans.getConfig( ).set( "current" , currentBans( ) );
            Api.bans.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( Id , p.getName( ) , baner , banned , reason , exp , created , status , Utils.getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || Utils.getBoolean( "alerts.ban" ) ) {
                Utils.PlaySound( people , "un_ban" );
                for ( String key : Utils.getStringList( "ban.change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , player );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%banned%" , banned );
                    key = key.replace( "%id%" , String.valueOf( Id ) );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , created );
                    key = key.replace( "%exp_date%" , exp );
                    key = key.replace( "%ban_status%" , "UNBANED" );
                    Utils.tell( people , key );
                }
            }
        }
    }
    
    public static int currentBans( ){
        try {
            if ( Utils.mysqlEnabled( ) ) {
                return BansQuery.getCurrentBans( );
            } else {
                ConfigurationSection inventorySection = Api.bans.getConfig( ).getConfigurationSection( "bans" );
                return inventorySection.getKeys( false ).size( );
            }
        } catch ( NullPointerException ignored ) {
            return 0;
        }
        
    }
    
}
