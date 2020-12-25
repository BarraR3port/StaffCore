package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BanPlayer {
    
    public static void unBan( CommandSender p , int Id ){
        String player = "";
        if ( p instanceof Player ) {
            player = p.getName( );
        } else {
            player = "CONSOLE";
        }
        String reason = null;
        String created = null;
        String exp = null;
        String baner = null;
        String baned = null;
        String status = "unbaned";
        if ( utils.mysqlEnabled( ) ) {
            reason = SQLGetter.getBaned( Id , "Reason" );
            created = SQLGetter.getBaned( Id , "Date" );
            exp = SQLGetter.getBaned( Id , "ExpDate" );
            baner = SQLGetter.getBaned( Id , "Baner" );
            baned = SQLGetter.getBaned( Id , "Name" );
            SQLGetter.deleteBans( Id );
        } else {
            main.plugin.bans.reloadConfig( );
            reason = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            created = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".date" );
            exp = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            baner = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".baned_by" );
            baned = main.plugin.bans.getConfig( ).getString( "bans." + Id + ".name" );
            main.plugin.bans.getConfig( ).set( "bans." + Id , null );
            main.plugin.bans.getConfig( ).set( "current" , currentBans( ) );
            main.plugin.bans.saveConfig( );
        }
        SendMsg.sendBanChangeAlert( Id , p.getName( ) , baner , baned , reason , exp , created , status , main.plugin.getConfig( ).getString( "bungeecord.server" ) );
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.ban" ) ) {
                utils.PlaySound( people , "un_ban" );
                for ( String key : main.plugin.getConfig( ).getStringList( "ban.ban_change" ) ) {
                    key = key.replace( "%changed_by%" , player );
                    key = key.replace( "%baner%" , baner );
                    key = key.replace( "%baned%" , baned );
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
        int current = 0;
        for ( int i = 0; i <= main.plugin.bans.getConfig( ).getInt( "count" ) + main.plugin.bans.getConfig( ).getInt( "current" ); i++ ) {
            try {
                if ( main.plugin.bans.getConfig( ).get( "bans." + i + ".status" ) != null ) {
                    current++;
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        main.plugin.bans.getConfig( ).set( "current" , current );
        return current;
    }
    
    public static void BanPlayer( CommandSender p , String baned , String reason ){
        createBan( ( Player ) p , baned , reason , 283605 , "d" , true );
    }
    
    
    public static void BanCooldown( CommandSender sender , String baned , String reason , long amount , String time ){
        if ( time.equalsIgnoreCase( "s" ) || time.equalsIgnoreCase( "m" ) ||
                time.equalsIgnoreCase( "h" ) || time.equalsIgnoreCase( "d" ) ) {
            createBan( ( Player ) sender , baned , reason , amount , time , false );
        } else {
            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cWrong usage." );
            utils.tell( sender , main.plugin.getConfig( ).getString( "server_prefix" ) + "&cExample /ban &a" + baned );
        }
    }
    
    public static int id( ){
        if ( utils.mysqlEnabled( ) ) {
            return main.plugin.data.getBanId( );
        } else {
            int id = main.plugin.bans.getConfig( ).getInt( "count" );
            id++;
            return id;
        }
    }
    
    
    private static void createBan( Player p , String baned , String reason , long amount , String time , Boolean permanent ){
        int id = id( );
        Date now = new Date( );
        Calendar cal = Calendar.getInstance( );
        cal.setTime( now );
        String IP = null;
        if ( Bukkit.getPlayer( baned ) instanceof Player ) {
            Player baned_player = Bukkit.getPlayer( baned );
            IP = baned_player.getAddress( ).getAddress( ).toString( );
            IP = IP.replace( "/" , "" );
        } else {
            try {
                if ( utils.mysqlEnabled( ) ) {
                    List < String > ips = utils.makeList( SQLGetter.getAlts( baned ) );
                    IP = ips.get( 0 );
                } else {
                    List < ? extends String > ips = main.plugin.alts.getConfig( ).getStringList( "alts." + baned );
                    IP = ips.subList( ips.size( ) - 1 , ips.size( ) ).toString( );
                }
                IP = IP.replace( "[" , "" );
                IP = IP.replace( "]" , "" );
            } catch ( NullPointerException | IndexOutOfBoundsException ignored ) {
                String msg = main.plugin.getConfig( ).getString( "staff.never_seen" );
                msg = msg.replace( "%player%" , baned );
                utils.tell( p , main.plugin.getConfig( ).getString( "staff.staff_prefix" ) + msg );
                
            }
        }
        if ( IP != null ) {
            if ( time.equals( "s" ) ) {
                cal.add( Calendar.SECOND , ( int ) amount );
            } else if ( time.equals( "m" ) ) {
                cal.add( Calendar.MINUTE , ( int ) amount );
            } else if ( time.equals( "h" ) ) {
                cal.add( Calendar.HOUR , ( int ) amount );
            } else if ( time.equals( "d" ) ) {
                cal.add( Calendar.DAY_OF_MONTH , ( int ) amount );
            }
            Date ExpDate = cal.getTime( );
            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
            if ( utils.mysqlEnabled( ) ) {
                if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
                    main.plugin.data.createBan( baned , p.getName( ) , reason , format.format( now ) , format.format( ExpDate ) , IP , "true" , "open" );
                } else {
                    main.plugin.data.createBan( baned , p.getName( ) , reason , format.format( now ) , format.format( ExpDate ) , IP , "false" , "open" );
                }
            }
            if ( main.plugin.bans.getConfig( ).contains( "count" ) ) {
                main.plugin.bans.getConfig( ).set( "count" , id );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".name" , baned );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".baned_by" , p.getName( ) );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".reason" , reason );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".date" , format.format( now ) );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".expdate" , format.format( ExpDate ) );
                if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
                    main.plugin.bans.getConfig( ).set( "bans." + id + ".IP-Baned" , true );
                } else {
                    main.plugin.bans.getConfig( ).set( "bans." + id + ".IP-Baned" , false );
                }
                main.plugin.bans.getConfig( ).set( "bans." + id + ".IP" , IP );
                main.plugin.bans.getConfig( ).set( "bans." + id + ".status" , "open" );
                main.plugin.bans.getConfig( ).set( "current" , new PlayerMenuUtility( p ).currentBans( ) );
                main.plugin.bans.saveConfig( );
            }
            Boolean Ip = false;
            if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
                Ip = true;
            }
            SendMsg.sendBanAlert( p.getName( ) , baned , reason , permanent , Ip , amount , time , format.format( ExpDate ) , format.format( now ) , main.plugin.getConfig( ).getString( "bungeecord.server" ) );
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( utils.getBoolean( "alerts.ban" ) || people.hasPermission( "staffcore.staff" ) ) {
                    utils.PlaySound( people , "ban_alerts" );
                    for ( String key : main.plugin.getConfig( ).getStringList( "ban.ban_alerts" ) ) {
                        key = key.replace( "%baner%" , p.getName( ) );
                        key = key.replace( "%baned%" , baned );
                        key = key.replace( "%reason%" , reason );
                        if ( permanent ) {
                            key = key.replace( "%amount%" , "&4PERMANENT" );
                            key = key.replace( "%time%" , "" );
                        } else {
                            key = key.replace( "%amount%" , String.valueOf( amount ) );
                            key = key.replace( "%time%" , time );
                        }
                        if ( Ip ) {
                            key = key.replace( "%IP_BANED%" , "&atrue" );
                        } else {
                            key = key.replace( "%IP_BANED%" , "&cfalse" );
                        }
                        key = key.replace( "%exp_date%" , format.format( ExpDate ) );
                        key = key.replace( "%date%" , format.format( now ) );
                        utils.tell( people , key );
                    }
                }
            }
            if ( Bukkit.getPlayer( baned ) instanceof Player ) {
                String ban_msg = "\n";
                for ( String msg : main.plugin.getConfig( ).getStringList( "ban.ban_msg" ) ) {
                    msg = msg.replace( "%baner%" , p.getName( ) );
                    msg = msg.replace( "%baned%" , baned );
                    msg = msg.replace( "%reason%" , reason );
                    if ( permanent ) {
                        msg = msg.replace( "%amount%" , "&4PERMANENT" );
                        msg = msg.replace( "%time%" , "" );
                    } else {
                        msg = msg.replace( "%amount%" , String.valueOf( amount ) );
                        msg = msg.replace( "%time%" , time );
                    }
                    if ( p.getPersistentDataContainer( ).has( new NamespacedKey( main.plugin , "ban-ip" ) , PersistentDataType.STRING ) ) {
                        msg = msg.replace( "%IP_BANED%" , "&atrue" );
                    } else {
                        msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                    }
                    msg = msg.replace( "%exp_date%" , format.format( ExpDate ) );
                    msg = msg.replace( "%date%" , format.format( now ) );
                    ban_msg = ban_msg + msg + "\n";
                }
                utils.PlayParticle( Bukkit.getPlayer( baned ) , "ban" );
                if ( main.plugin.getConfig( ).getBoolean( "wipe.wipe_on_ban" ) ) {
                    wipePlayer.WipeOnBan( main.plugin , baned );
                }
                String finalBan_msg = ban_msg;
                Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> Bukkit.getPlayer( baned ).kickPlayer( utils.chat( finalBan_msg ) ) , 7l );
            }
        }
    }
}
