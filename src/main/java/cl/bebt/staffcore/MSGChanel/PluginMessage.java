package cl.bebt.staffcore.MSGChanel;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.menu.menu.Others.StaffListBungeeGui;
import cl.bebt.staffcore.utils.SetStaffItems;
import cl.bebt.staffcore.utils.utils;
import cl.bebt.staffcore.utils.wipePlayer;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.Objects;

public class PluginMessage implements PluginMessageListener {
    private final main plugin = main.plugin;
    
    private int serverCount = 1;
    
    public void onPluginMessageReceived( String channel , Player p , byte[] msg ){
        if ( !this.plugin.getConfig( ).getBoolean( "bungeecord.enabled" ) )
            return;
        if ( channel.equals( "sc:alerts" ) ) {
            ByteArrayDataInput in = ByteStreams.newDataInput( msg );
            String subChannel = in.readUTF( );
            if ( subChannel.equalsIgnoreCase( "Report" ) ) {
                int id = in.readInt( );
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                String date = in.readUTF( );
                String server = in.readUTF( );
                ReportAlert( id , sender , target , reason , date , server );
            } else if ( subChannel.equalsIgnoreCase( "ReportChange" ) ) {
                int id = in.readInt( );
                String changer = in.readUTF( );
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                String date = in.readUTF( );
                String status = in.readUTF( );
                String server = in.readUTF( );
                ReportChangeAlert( id , changer , sender , target , reason , date , status , server );
            } else if ( subChannel.equalsIgnoreCase( "Ban" ) ) {
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                Boolean permanent = in.readBoolean( );
                Boolean Ip = in.readBoolean( );
                Long amount = in.readLong( );
                String time = in.readUTF( );
                String ExpDate = in.readUTF( );
                String date = in.readUTF( );
                String server = in.readUTF( );
                BanAlert( sender , target , reason , permanent , Ip , amount , time , ExpDate , date , server );
            } else if ( subChannel.equalsIgnoreCase( "Warn" ) ) {
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                Long amount = in.readLong( );
                String time = in.readUTF( );
                String ExpDate = in.readUTF( );
                String date = in.readUTF( );
                String server = in.readUTF( );
                WarnAlert( sender , target , reason , amount , time , ExpDate , date , server );
            } else if ( subChannel.equalsIgnoreCase( "BanChange" ) ) {
                int Id = in.readInt( );
                String changer = in.readUTF( );
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                String ExpDate = in.readUTF( );
                String date = in.readUTF( );
                String status = in.readUTF( );
                String server = in.readUTF( );
                BanChangeAlert( Id , changer , sender , target , reason , ExpDate , date , status , server );
            } else if ( subChannel.equalsIgnoreCase( "WarnChange" ) ) {
                int Id = in.readInt( );
                String changer = in.readUTF( );
                String sender = in.readUTF( );
                String target = in.readUTF( );
                String reason = in.readUTF( );
                String ExpDate = in.readUTF( );
                String date = in.readUTF( );
                String status = in.readUTF( );
                String server = in.readUTF( );
                WarnChangeAlert( Id , changer , sender , target , reason , ExpDate , date , status , server );
            } else if ( subChannel.equalsIgnoreCase( "Freeze" ) ) {
                String sender = in.readUTF( );
                String target = in.readUTF( );
                Boolean bool = in.readBoolean( );
                String server = in.readUTF( );
                FreezeAlert( sender , target , bool , server );
            } else if ( subChannel.equalsIgnoreCase( "Wipe" ) ) {
                String sender = in.readUTF( );
                String target = in.readUTF( );
                int bans = in.readInt( );
                int reports = in.readInt( );
                int warns = in.readInt( );
                String server = in.readUTF( );
                WipeAlert( sender , target , bans , reports , warns , server );
            } else if ( subChannel.equalsIgnoreCase( "StaffChat" ) ) {
                String sender = in.readUTF( );
                String message = in.readUTF( );
                String server = in.readUTF( );
                StaffChatMSG( sender , message , server );
            } else if ( subChannel.equalsIgnoreCase( "HelpOp" ) ) {
                String sender = in.readUTF( );
                String reason = in.readUTF( );
                String server = in.readUTF( );
                HelpOp( sender , reason , server );
            }
        }
        if ( channel.equals( "sc:stafflist" ) ) {
            ByteArrayDataInput in = ByteStreams.newDataInput( msg );
            String subChannel = in.readUTF( );
            if ( subChannel.equalsIgnoreCase( "SendSLReceive" ) ) {
                String sender = in.readUTF( );
                String server = in.readUTF( );
                SendMsg.sendSLPlayersData( sender , server );
            }
            if ( subChannel.equalsIgnoreCase( "OpenMenu" ) ) {
                String sender = in.readUTF( );
                String server = in.readUTF( );
                int count = in.readInt( );
                String staffMembers = in.readUTF( );
                String staffMembersServer = in.readUTF( );
                String staffMembersPing = in.readUTF( );
                String staffMembersGamemode = in.readUTF( );
                staffMembers = staffMembers.replace( "[" , "" ).replace( "]" , "" );
                openStaffChat( sender , server , count , staffMembers , staffMembersServer , staffMembersPing , staffMembersGamemode );
            }
        }
    }
    
    public void ReportAlert( int id , String sender , String target , String reason , String date , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.report" , null ) ) {
                utils.PlaySound( people , "reports_alerts" );
                for ( String key : utils.getStringList( "report.report_alerts" , "alerts" ) ) {
                    key = key.replace( "%reporter%" , sender + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%reported%" , target );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%date%" , date );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public void ReportChangeAlert( int id , String changer , String sender , String target , String reason , String date , String status , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.report" , null ) ) {
                if ( status.equals( "closed" ) ) {
                    utils.PlaySound( people , "close_report" );
                } else if ( status.equals( "open" ) ) {
                    utils.PlaySound( people , "open_report" );
                } else if ( status.equals( "deleted" ) ) {
                    utils.PlaySound( people , "delete_report" );
                }
                for ( String key : utils.getStringList( "report.report_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , changer + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%reporter%" , sender );
                    key = key.replace( "%reported%" , target );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%report_status%" , status );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public void BanAlert( String sender , String target , String reason , Boolean permanent , Boolean Ip , Long amount , String time , String ExpDate , String date , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.ban" , null ) ) {
                utils.PlaySound( people , "ban_alerts" );
                for ( String key : utils.getStringList( "ban.ban_alerts" , "alerts" ) ) {
                    key = key.replace( "%baner%" , sender + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%banned%" , target );
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
                    key = key.replace( "%exp_date%" , ExpDate );
                    key = key.replace( "%date%" , date );
                    utils.tell( people , key );
                }
            }
        }
        try {
            SetStaffItems.Off( Bukkit.getPlayer( target ) );
        } catch ( NullPointerException ignored ) { }
        if ( Bukkit.getPlayer( target ) instanceof Player ) {
            String ban_msg = "\n";
            for ( String key : utils.getStringList( "ban.ban_msg" , "alerts" ) ) {
                key = key.replace( "%baner%" , sender );
                key = key.replace( "%banned%" , target );
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
                key = key.replace( "%exp_date%" , ExpDate );
                key = key.replace( "%date%" , date );
                ban_msg = ban_msg + key + "\n";
            }
            utils.PlayParticle( Bukkit.getPlayer( target ) , "ban" );
            if ( main.plugin.getConfig( ).getBoolean( "wipe.wipe_on_ban" ) )
                wipePlayer.WipeOnBan( this.plugin , target );
            Bukkit.getPlayer( target ).kickPlayer( utils.chat( ban_msg ) );
        }
    }
    
    public void BanChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.ban" , null ) || people.hasPermission( "staffcore.staff" ) ) {
                if ( status.equals( "closed" ) ) {
                    utils.PlaySound( people , "close_ban" );
                } else if ( status.equals( "unbanned" ) ) {
                    utils.PlaySound( people , "un_ban" );
                }
                for ( String key : utils.getStringList( "ban.ban_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , changer + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%baner%" , sender );
                    key = key.replace( "%banned%" , target );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%%xp_date%" , ExpDate );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%ban_status%" , status );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public void WarnAlert( String sender , String target , String reason , Long amount , String time , String ExpDate , String date , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.warn" , null ) || people.hasPermission( "staffcore.staff" ) ) {
                utils.PlaySound( people , "warn_alerts" );
                for ( String key : utils.getStringList( "warns.alerts.warn_alerts" , "alerts" ) ) {
                    key = key.replace( "%warner%" , sender + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%warned%" , target );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%amount%" , String.valueOf( amount ) );
                    key = key.replace( "%time%" , time );
                    key = key.replace( "%exp_date%" , ExpDate );
                    key = key.replace( "%date%" , date );
                    utils.tell( people , key );
                }
            }
        }
        try {
            SetStaffItems.Off( Bukkit.getPlayer( target ) );
        } catch ( NullPointerException ignored ) { }
        if ( Bukkit.getPlayer( target ) instanceof Player && utils.currentPlayerWarns( target ) >= utils.getInt( "warns.max_warns" , null ) ) {
            String ban_msg = "\n";
            for ( String msg : utils.getStringList( "ban.ban_msg" , "alerts" ) ) {
                msg = msg.replace( "%baner%" , sender );
                msg = msg.replace( "%banned%" , target );
                msg = msg.replace( "%reason%" , reason );
                msg = msg.replace( "%amount%" , String.valueOf( amount ) );
                msg = msg.replace( "%time%" , time );
                msg = msg.replace( "%exp_date%" , ExpDate );
                msg = msg.replace( "%date%" , date );
                ban_msg = ban_msg + msg + "\n";
            }
            utils.PlayParticle( Bukkit.getPlayer( target ) , "ban" );
            if ( main.plugin.getConfig( ).getBoolean( "wipe.wipe_on_ban" ) )
                wipePlayer.WipeOnBan( main.plugin , target );
            String finalBan_msg = ban_msg;
            Bukkit.getScheduler( ).scheduleSyncDelayedTask( main.plugin , ( ) -> Bukkit.getPlayer( target ).kickPlayer( utils.chat( finalBan_msg ) ) , 7L );
        }
    }
    
    
    public void WarnChangeAlert( int id , String changer , String sender , String target , String reason , String ExpDate , String date , String status , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( utils.getBoolean( "alerts.warns" , null ) || people.hasPermission( "staffcore.staff" ) ) {
                if ( status.equalsIgnoreCase( "closed" ) ) {
                    utils.PlaySound( people , "close_ban" );
                } else if ( status.equalsIgnoreCase( "unbanned" ) ) {
                    utils.PlaySound( people , "un_ban" );
                }
                for ( String key : utils.getStringList( "warn.alerts.warn_change" , "alerts" ) ) {
                    key = key.replace( "%changed_by%" , changer + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%warner%" , sender );
                    key = key.replace( "%warned%" , target );
                    key = key.replace( "%reason%" , reason );
                    key = key.replace( "%create_date%" , date );
                    key = key.replace( "%%xp_date%" , ExpDate );
                    key = key.replace( "%id%" , String.valueOf( id ) );
                    key = key.replace( "%warn_status%" , status );
                    utils.tell( people , key );
                }
            }
        }
    }
    
    public void FreezeAlert( String sender , String target , Boolean bool , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        String status = null;
        if ( bool ) {
            status = utils.getString( "freeze.freeze" , "lg" , null );
        } else {
            status = utils.getString( "freeze.unfreeze" , "lg" , null );
        }
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.freeze" ) || utils.getBoolean( "alerts.freeze" , null ) )
                for ( String key : utils.getStringList( "freeze.freeze_alerts" , "alerts" ) ) {
                    key = key.replace( "%frozen%" , target );
                    key = key.replace( "%freezer%" , sender + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%status%" , status );
                    utils.tell( people , key );
                }
        }
    }
    
    public void WipeAlert( String sender , String target , int bans , int reports , int warns , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.staff" ) || utils.getBoolean( "alerts.wipe_players" , null ) )
                for ( String key : utils.getStringList( "wipe.wipe_msg" , "alerts" ) ) {
                    key = key.replace( "%wiper%" , sender + utils.getBungeecordServerPrefix( ).replace( "%server%" , server ) );
                    key = key.replace( "%wiped%" , target );
                    key = key.replace( "%bans%" , String.valueOf( bans ) );
                    key = key.replace( "%reports%" , String.valueOf( reports ) );
                    key = key.replace( "%warns%" , String.valueOf( warns ) );
                    utils.tell( people , key );
                }
        }
        try {
            wipePlayer.WipeOnBan( this.plugin , target );
        } catch ( NullPointerException ignored ) { }
        Bukkit.getServer( ).getScheduler( ).scheduleSyncRepeatingTask( plugin , ( ) -> {
            try {
                utils.tell( Bukkit.getPlayer( target ) , "&cYour account is Wiping" );
                String ban_msg = "\n";
                for ( String msg : utils.getStringList( "wipe.wipe_kick_msg" , "alerts" ) ) {
                    msg = msg.replace( "%wiper%" , sender );
                    msg = msg.replace( "%wiped%" , target );
                    ban_msg = ban_msg + msg + "\n";
                }
                Bukkit.getPlayer( target ).kickPlayer( utils.chat( ban_msg ) );
            } catch ( NullPointerException ignored ) { }
        } , 6L , 10L );
    }
    
    public void StaffChatMSG( String sender , String msg , String server ){
        if ( utils.getServer( ).equalsIgnoreCase( server ) )
            return;
        for ( Player people : Bukkit.getOnlinePlayers( ) ) {
            if ( people.hasPermission( "staffcore.sc" ) ) {
                String message = utils.getString( "staff_chat.prefix" , "lg" , null );
                message = message.replace( "%sender%" , Objects.requireNonNull( utils.getBungeecordServerPrefix( ) ).replace( "%server%" , server ) + sender );
                message = message.replace( "%msg%" , msg );
                utils.tell( people , message );
            }
        }
    }
    
    public void openStaffChat( String sender , String server , int count , String staffMembers , String staffMembersServer , String staffMembersPing , String staffMembersGamemode ){
        main.staffMembers.addAll( Arrays.asList( staffMembers.split( ", " ) ) );
        main.playersServerMap.putAll( utils.makeHashMap( staffMembersServer ) );
        main.playersServerPingMap.putAll( utils.makeHashMap( staffMembersPing ) );
        main.playersServerGamemodesMap.putAll( utils.makeHashMap( staffMembersGamemode ) );
        this.serverCount++;
        if ( this.serverCount >= count ) {
            if ( server.equalsIgnoreCase( utils.getString( "bungeecord.server" , null , null ) ) ) {
                Player player = Bukkit.getPlayer( sender );
                new StaffListBungeeGui( new PlayerMenuUtility( player ) , this.plugin , player ).open( player );
            }
            this.serverCount = 1;
        }
    }
    
    public void HelpOp( String sender , String reason , String server ){
        if ( !server.equals( utils.getString( "bungeecord.server" , null , null ) ) ) {
            for ( Player people : Bukkit.getOnlinePlayers( ) ) {
                if ( people.hasPermission( "staffcore.helpop" ) ) {
                    String message = utils.getString( "helpop.bungee" , "lg" , null );
                    message = message.replace( "%user%" , sender );
                    message = message.replace( "%server%" , server );
                    utils.PlaySound( people , "helpop" );
                    utils.tell( people , message + reason );
                    
                }
            }
        }
    }
}
