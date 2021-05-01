package cl.bebt.staffcore.listeners;

import cl.bebt.staffcore.API.StaffCoreAPI;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class onPlayerJoin implements Listener {
    
    
    private static main plugin;
    
    public onPlayerJoin( main plugin ){
        onPlayerJoin.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerPreJoin( PlayerLoginEvent e ){
        Player p = e.getPlayer( );
        String IP = String.valueOf( e.getAddress( ) );
        IP = IP.replace( "/" , "" );
        int currents = BanPlayer.id( );
        if ( utils.mysqlEnabled( ) ) {
            try {
                if ( !SQLGetter.PlayerExists( "alts" , p.getName( ) ) ) {
                    SQLGetter.createAlts( p.getName( ) , IP );
                } else {
                    List < String > ips = utils.makeList( SQLGetter.getAlts( p.getName( ) ) );
                    SQLGetter.addIps( p.getName( ) , utils.stringify( ips , IP ) );
                }
            } catch ( NullPointerException | IndexOutOfBoundsException Exception ) {
                Exception.printStackTrace( );
            }
            for ( int i = 1; i <= currents; i++ ) {
                try {
                    if ( SQLGetter.getBanned( i , "Name" ).equals( p.getName( ) ) &&
                            SQLGetter.getBanned( i , "Status" ).equals( "open" ) &&
                            StaffCoreAPI.isStillBanned( i ) ) {
                        e.disallow( PlayerLoginEvent.Result.KICK_OTHER , KickBannedPlayerSql( i ) );
                        break;
                    }
                    if ( SQLGetter.getBannedIp( i ).equals( IP ) &&
                            SQLGetter.getBanned( i , "Status" ).equals( "open" ) && SQLGetter.getBanned( i , "IP_Banned" ).equals( "true" ) &&
                            StaffCoreAPI.isStillBanned( i ) ) {
                        e.disallow( PlayerLoginEvent.Result.KICK_OTHER , KickBannedPlayerSql( i ) );
                        break;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
        } else {
            try {
                List < String > ips = plugin.alts.getConfig( ).getStringList( "alts." + p.getName( ) );
                int size = plugin.alts.getConfig( ).getStringList( "alts." + p.getName( ) ).size( );
                if ( size > 0 ) {
                    if ( !ips.contains( IP ) )
                        ips.add( IP );
                } else {
                    ips.add( IP );
                }
                plugin.alts.getConfig( ).set( "alts." + p.getName( ) , ips );
                plugin.alts.saveConfig( );
            } catch ( NullPointerException | IndexOutOfBoundsException Exception ) {
                Exception.printStackTrace( );
            }
            for ( int i = 1; i <= currents; i++ ) {
                try {
                    if ( Objects.equals( plugin.bans.getConfig( ).getString( "bans." + i + ".name" ) , p.getName( ) ) &&
                            Objects.equals( plugin.bans.getConfig( ).getString( "bans." + i + ".status" ) , "open" ) &&
                            StaffCoreAPI.isStillBanned( i ) ) {
                        e.disallow( PlayerLoginEvent.Result.KICK_OTHER , KickBannedPlayer( i ) );
                        break;
                    }
                    if ( plugin.bans.getConfig( ).getBoolean( "bans." + i + ".IP-Banned" ) &&
                            Objects.equals( plugin.bans.getConfig( ).getString( "bans." + i + ".IP" ) , IP ) &&
                            Objects.equals( plugin.bans.getConfig( ).getString( "bans." + i + ".status" ) , "open" ) &&
                            StaffCoreAPI.isStillBanned( i ) ) {
                        e.disallow( PlayerLoginEvent.Result.KICK_OTHER , KickBannedPlayer( i ) );
                        break;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoinEvent( PlayerJoinEvent e ){
        Player p = e.getPlayer( );
        if ( p.hasPermission( "staffcore.vanish" ) ) {
            SetVanish.setVanish( p , true );
        }
        if ( utils.currentPlayerWarns( p.getName( ) ) != 0 && utils.getBoolean( "warns.notify" , null ) ) {
            ComponentBuilder cb = new ComponentBuilder( utils.chat( utils.getString( "warns.join_msg" , "lg" , null ) ) );
            TextComponent dis = new TextComponent( utils.chat( utils.getString( "warns.notify" , "lg" , "staff" ).replace( "%amount%" , String.valueOf( utils.currentPlayerWarns( p.getName( ) ) ) ) ) );
            dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
            dis.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND , "/warningns" ) );
            p.spigot( ).sendMessage( dis );
        }
        try {
            if ( utils.mysqlEnabled( ) ) {
                if ( SQLGetter.isTrue( p , "frozen" ).equals( "true" ) ) {
                    if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) )
                        FreezePlayer.FreezePlayer( p , "CONSOLE" , true );
                } else if ( SQLGetter.isTrue( p , "frozen" ).equals( "false" ) &&
                        p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING ) ) {
                    FreezePlayer.FreezePlayer( p , "CONSOLE" , false );
                }
                if ( SQLGetter.isTrue( p , "staff" ).equals( "true" ) ) {
                    if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                        SetStaffItems.On( p );
                    } else {
                        p.setAllowFlight( true );
                        p.setFlying( true );
                    }
                } else if ( SQLGetter.isTrue( p , "staff" ).equals( "false" ) &&
                        p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                    SetStaffItems.Off( p );
                }
                if ( SQLGetter.isTrue( p , "vanish" ).equals( "true" ) ) {
                    if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) )
                        SetVanish.setVanish( p , true );
                    for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                        if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || SQLGetter.isTrue( player , "vanish" ).equals( "false" ) ) {
                            if ( !player.hasPermission( "staffcore.vanish.see" ) ) {
                                player.hidePlayer( plugin , p );
                                return;
                            }
                        }
                        player.showPlayer( plugin , p );
                        utils.PlaySound( player , "vanished_join" );
                    }
                }
                if ( SQLGetter.isTrue( p , "vanish" ).equals( "false" ) ) {
                    if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) )
                        SetVanish.setVanish( p , false );
                    for ( Player player : Bukkit.getServer( ).getOnlinePlayers( ) ) {
                        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ||
                                SQLGetter.isTrue( player , "vanish" ).equals( "true" ) ) {
                            if ( p.hasPermission( "staffcore.vanish.see" ) )
                                return;
                            p.hidePlayer( plugin , player );
                        }
                    }
                }
                if ( !p.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                    SetFly.SetFly( p , true );
                } else {
                    p.setAllowFlight( true );
                    p.setFlying( true );
                }
                if ( SQLGetter.isTrue( p , "staffchat" ).equals( "true" ) )
                    p.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
                if ( SQLGetter.isTrue( p , "staffchat" ).equals( "false" ) )
                    p.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
            } else {
                PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
                if ( PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) ) {
                    SetFly.SetFly( p , true );
                } else if ( !PlayerData.has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING ) &&
                        !PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) &&
                        !PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                    SetFly.SetFly( p , false );
                }
                if ( PlayerData.has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || PlayerData.has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                    p.setAllowFlight( true );
                    p.setFlying( true );
                    for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                        if ( !player.hasPermission( "staffcore.vanish.see" ) && !player.hasPermission( "staffcore.vanish" ) ) {
                            if ( !player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
                                player.hidePlayer( plugin , p );
                                continue;
                            }
                            p.showPlayer( plugin , player );
                            continue;
                        }
                        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || player
                                .getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) || player
                                .hasPermission( "staffcore.vanish.see" ) ) {
                            p.showPlayer( plugin , player );
                            player.showPlayer( plugin , p );
                            utils.PlaySound( player , "vanished_join" );
                            continue;
                        }
                        player.hidePlayer( plugin , p );
                    }
                } else {
                    for ( Player player : Bukkit.getOnlinePlayers( ) ) {
                        if ( p.hasPermission( "staffcore.vanish.see" ) && p.hasPermission( "staffcore.vanish" ) ) {
                            if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                                p.showPlayer( plugin , player );
                                player.showPlayer( plugin , p );
                            }
                            continue;
                        }
                        if ( player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) || player.getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING ) ) {
                            p.hidePlayer( plugin , player );
                            continue;
                        }
                        p.showPlayer( plugin , player );
                    }
                }
            }
        } catch ( NoSuchMethodError ignored ) {
        }
    }
    
    String KickBannedPlayerSql( int Id ){
        try {
            String reason = SQLGetter.getBanned( Id , "Reason" );
            Date now = new Date( );
            String created = SQLGetter.getBanned( Id , "Date" );
            String exp = SQLGetter.getBanned( Id , "ExpDate" );
            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
            String baner = SQLGetter.getBanned( Id , "Baner" );
            String banned = SQLGetter.getBanned( Id , "Name" );
            Date d2 = null;
            d2 = format.parse( exp );
            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
            long Days = TimeUnit.SECONDS.toDays( remaining );
            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
            long Hours = TimeUnit.SECONDS.toHours( Seconds );
            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
            String ban_msg = "\n";
            for ( String msg : utils.getStringList( "ban.join" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , baner );
                msg = msg.replace( "%banned%" , banned );
                msg = msg.replace( "%reason%" , reason );
                if ( Days >= 365L ) {
                    msg = msg.replace( "%time_left%" , "&4PERMANENT" );
                } else {
                    msg = msg.replace( "%time_left%" , Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" );
                }
                if ( SQLGetter.getBanned( Id , "IP_Banned" ).equals( "true" ) ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else if ( SQLGetter.getBanned( Id , "IP_Banned" ).equals( "false" ) ) {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , exp );
                msg = msg.replace( "%date%" , created );
                ban_msg = ban_msg + msg + "\n";
            }
            return utils.chat( ban_msg );
        } catch ( ParseException | NullPointerException ignored ) {
            ignored.printStackTrace( );
            return null;
        }
    }
    
    String KickBannedPlayer( int Id ){
        try {
            String p = plugin.bans.getConfig( ).getString( "bans." + Id + ".banned_by" );
            String banned = plugin.bans.getConfig( ).getString( "bans." + Id + ".name" );
            String reason = plugin.bans.getConfig( ).getString( "bans." + Id + ".reason" );
            Date now = new Date( );
            String created = plugin.bans.getConfig( ).getString( "bans." + Id + ".date" );
            String exp = plugin.bans.getConfig( ).getString( "bans." + Id + ".expdate" );
            SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
            Date d2 = null;
            d2 = format.parse( exp );
            long remaining = (d2.getTime( ) - now.getTime( )) / 1000L;
            long Days = TimeUnit.SECONDS.toDays( remaining );
            long Seconds = remaining - TimeUnit.DAYS.toSeconds( Days );
            long Hours = TimeUnit.SECONDS.toHours( Seconds );
            Seconds -= TimeUnit.HOURS.toSeconds( Hours );
            long Minutes = TimeUnit.SECONDS.toMinutes( Seconds );
            Seconds -= TimeUnit.MINUTES.toSeconds( Minutes );
            String ban_msg = "\n";
            for ( String msg : utils.getStringList( "ban.join" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , p );
                msg = msg.replace( "%banned%" , banned );
                msg = msg.replace( "%reason%" , reason );
                if ( Days >= 365L ) {
                    msg = msg.replace( "%time_left%" , "&4PERMANENT" );
                } else {
                    msg = msg.replace( "%time_left%" , Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s" );
                }
                if ( plugin.bans.getConfig( ).getBoolean( "bans." + Id + ".IP-Banned" ) ) {
                    msg = msg.replace( "%IP_BANED%" , "&atrue" );
                } else {
                    msg = msg.replace( "%IP_BANED%" , "&cfalse" );
                }
                msg = msg.replace( "%exp_date%" , exp );
                msg = msg.replace( "%date%" , created );
                ban_msg = ban_msg + msg + "\n";
            }
            return utils.chat( ban_msg );
        } catch ( ParseException | NullPointerException error ) {
            error.printStackTrace( );
            return null;
        }
    }
}
