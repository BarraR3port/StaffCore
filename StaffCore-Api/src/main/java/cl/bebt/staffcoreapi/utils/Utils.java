/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.Api;
import cl.bebt.staffcoreapi.Entities.User;
import cl.bebt.staffcoreapi.EntitiesUtils.SqlUtils;
import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import cl.bebt.staffcoreapi.Enums.ApiType;
import cl.bebt.staffcoreapi.Enums.UpdateType;
import cl.bebt.staffcoreapi.SQL.Queries.*;
import cl.bebt.staffcoreapi.utils.UUID.UUIDGetter;
import dev.dbassett.skullcreator.SkullCreator;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {
    
    /* VARIABLE SECTION */
    
    private static ApiType apiType;
    
    private static Plugin Bungee;
    
    private static JavaPlugin Spigot;
    
    /* INITIALIZATION SECTION */
    
    public Utils( ApiType type , JavaPlugin spigot ){
        apiType = type;
        Spigot = spigot;
    }
    
    public Utils( ApiType type , Plugin bungee ){
        apiType = type;
        Bungee = bungee;
    }
    
    public static Plugin getBungee( ){
        return Bungee;
    }
    
    public static JavaPlugin getSpigot( ){
        return Spigot;
    }
    
    /* UTILS SECTION */
    
    /**
     * @param path   The path
     * @param type   Language = "lg" | Item = "item" | Alert = "alerts"
     * @param prefix Server Prefix = "sv" | Staff Prefix = "staff"
     *
     * @return The string asked for.
     */
    public static String getString( String path , @Nullable String type , @Nullable String prefix ){
        if ( type == null && prefix == null ) {
            return Spigot.getConfig( ).getString( path );
        }
        if ( type == null && prefix.equalsIgnoreCase( "sv" ) ) {
            return Spigot.getConfig( ).getString( "server_prefix" ) + Spigot.getConfig( ).getString( path );
        } else if ( type == null && prefix.equalsIgnoreCase( "staff" ) ) {
            return Spigot.getConfig( ).getString( "staff_prefix" ) + Spigot.getConfig( ).getString( path );
        } else if ( type.equalsIgnoreCase( "lg" ) && prefix == null ) {
            if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                return Api.en_na.getConfig( ).getString( path );
            } else if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                return Api.es_cl.getConfig( ).getString( path );
            } else if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "FR" ) ) {
                return Api.fr.getConfig( ).getString( path );
            }
        }
        if ( type.equalsIgnoreCase( "lg" ) && prefix != null ) { //Language
            if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return Spigot.getConfig( ).getString( "server_prefix" ) + Api.en_na.getConfig( ).getString( path );
                } else {
                    return Spigot.getConfig( ).getString( "staff_prefix" ) + Api.en_na.getConfig( ).getString( path );
                }
            } else if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return Spigot.getConfig( ).getString( "server_prefix" ) + Api.es_cl.getConfig( ).getString( path );
                } else {
                    return Spigot.getConfig( ).getString( "staff_prefix" ) + Api.es_cl.getConfig( ).getString( path );
                }
            } else {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return Spigot.getConfig( ).getString( "server_prefix" ) + Api.fr.getConfig( ).getString( path );
                } else {
                    return Spigot.getConfig( ).getString( "staff_prefix" ) + Api.fr.getConfig( ).getString( path );
                }
            }
        } else if ( type.equalsIgnoreCase( "item" ) && prefix == null ) {
            return Api.items.getConfig( ).getString( path );
        } else if ( type.equalsIgnoreCase( "menu" ) && prefix == null ) {
            return Api.menus.getConfig( ).getString( path );
        } else {
            return "String Not Found";
        }
    }
    
    public static String getString( String path ){
        return Spigot.getConfig( ).getString( path );
    }
    
    public static boolean getBoolean( String path ){
        return Spigot.getConfig( ).getBoolean( path );
    }
    
    /**
     * @param path The String of the path
     * @param type lg | item | alerts
     *
     * @return the int that was asked for
     */
    public static int getInt( String path , @Nullable String type ){
        if ( type == null ) return Spigot.getConfig( ).getInt( path );
        if ( type.equalsIgnoreCase( "lg" ) ) { //Language
            if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                return Api.en_na.getConfig( ).getInt( path );
            } else if ( Spigot.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                return Api.es_cl.getConfig( ).getInt( path );
            } else {
                return Api.fr.getConfig( ).getInt( path );
            }
        } else if ( type.equalsIgnoreCase( "item" ) ) {
            return Api.items.getConfig( ).getInt( path );
        } else {
            return Api.alerts.getConfig( ).getInt( path );
        }
    }
    
    /**
     * @param path The String of the path
     * @param type item | alerts | menu
     *
     * @return
     */
    public static List < String > getStringList( @NotNull String path , @NotNull String type ){
        if ( type.equalsIgnoreCase( "item" ) ) {
            return Api.items.getConfig( ).getStringList( path );
        } else if ( type.equalsIgnoreCase( "menu" ) ) {
            return Api.menus.getConfig( ).getStringList( path );
        } else {
            return Api.alerts.getConfig( ).getStringList( path );
        }
    }
    
    public static void tell( CommandSender sender , String message ){
        sender.sendMessage( chat( message ) );
    }
    
    public static void tell( net.md_5.bungee.api.CommandSender sender , String message ){
        if ( sender instanceof ProxiedPlayer ) {
            ProxiedPlayer player = ( ProxiedPlayer ) sender;
            player.sendMessage( tc( message ) );
        } else {
            ProxyServer.getInstance( ).getConsole( ).sendMessage( tc( message ) );
        }
    }
    
    public static void tell( Player player , String message ){
        player.sendMessage( chat( message ) );
    }
    
    public static void tell( UUID uuid , String message ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            p.sendMessage( chat( message ) );
        } catch ( NullPointerException ignored ) {
        }
    }
    
    public static void tellHover( Player p , String msg , String hover , String link ){
        ComponentBuilder cb = new ComponentBuilder( Utils.chat( hover ) );
        TextComponent dis = new TextComponent( Utils.chat( msg ) );
        dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
        dis.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , link ) );
        p.spigot( ).sendMessage( dis );
    }
    
    public static String chat( String s ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes( '&' , s );
        } else {
            return ChatColor.translateAlternateColorCodes( '&' , s );
        }
        
    }
    
    
    public static TextComponent tc( String s ){
        return new TextComponent( chat( s ) );
    }
    
    public static void tellConsole( String s ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            tell( Bungee.getProxy( ).getInstance( ).getConsole( ) , chat( s ) );
        } else {
            tell( Bukkit.getConsoleSender( ) , chat( s ) );
        }
    }
    
    public static UUID getUUID( String name ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            return UUIDGetter.getBungeeUUID( name );
        } else {
            return UUIDGetter.getSpigotUUID( name );
        }
        
    }
    
    public static String getSkin( String name ){
        return Http.getSkin( "http://localhost:82/api/head/" + name , name );
    }
    
    public static String getDataFolder( ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            return Bungee.getDataFolder( ).getAbsolutePath( );
        } else {
            return Spigot.getDataFolder( ).getAbsolutePath( );
        }
    }
    
    public static void StopServer( ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            Bungee.getProxy( ).getInstance( ).stop( );
        } else {
            Bukkit.shutdown( );
        }
    }
    
    public static String getPluginVersion( ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            return Bungee.getDescription( ).getVersion( );
        } else {
            return Bukkit.getVersion( );
        }
    }
    
    public static ApiType getApiType( ){
        return apiType;
    }
    
    public static Plugin Bungee( ){
        return Bungee;
    }
    
    
    public static void sendDiscordAlertMsg( String title , ArrayList < String > msg ){
        try {
            DiscordUtils.DiscordWebHooksAlerts( msg , title );
        } catch ( IllegalArgumentException exception ) {
            if ( getBoolean( "discord.type.alerts.enabled" ) ) {
                Utils.tell( Bukkit.getConsoleSender( ) , getString( "discord.could_not_connect" , "lg" , "staff" ) );
            }
        }
    }
    
    public static void sendDiscordDebugMsg( Player p , String title , ArrayList < String > msg ){
        try {
            DiscordUtils.DiscordWebHooksDebug( p , msg , title );
        } catch ( IllegalArgumentException exception ) {
            if ( getBoolean( "discord.type.debug.enabled" ) ) {
                Utils.tell( Bukkit.getConsoleSender( ) , getString( "discord.could_not_connect" , "lg" , "staff" ) );
            }
        }
    }
    
    public static void reloadConfigs( ){
        Spigot.reloadConfig( );
        Api.bans.reloadConfig( );
        Api.alts.reloadConfig( );
        Api.warns.reloadConfig( );
        Api.en_na.reloadConfig( );
        Api.es_cl.reloadConfig( );
        Api.fr.reloadConfig( );
        Api.items.reloadConfig( );
        Api.alerts.reloadConfig( );
        Api.menus.reloadConfig( );
    }
    
    public static void ccAll( ){
        for ( Player p : Bukkit.getOnlinePlayers( ) ) {
            for ( int i = 0; i < 99; i++ ) {
                p.sendMessage( "\n" );
                p.sendMessage( " " );
            }
        }
    }
    
    public static void ccPlayer( Player p ){
        for ( int i = 0; i < 99; i++ ) {
            p.sendMessage( "\n" );
            p.sendMessage( " " );
        }
    }
    
    public static boolean mysqlEnabled( ){
        return SqlUtils.isEnabled( );
    }
    
    public static ItemStack getPlayerHead( String p ){
        UUID uuid = getUUIDFromName( p );
        if ( UserUtils.isSaved( uuid ) ) {
            return SkullCreator.itemFromBase64( UserUtils.getSkin( uuid ) );
        } else {
            return SkullCreator.itemFromBase64( getSkin( p ) );
        }
    }
    
    public static UUID getUUIDFromName( String player ){
        try {
            if ( mysqlEnabled( ) ) {
                return AltsQuery.getUUIDByName( player );
            } else {
                return UserUtils.getUUIDFromName( player );
            }
        } catch ( NullPointerException ignored ) {
        }
        return Utils.getUUID( player );
    }
    
    public static HashMap < String, String > getSavedSkins( ){
        if ( mysqlEnabled( ) ) {
            return ServerQuery.getSavedSkins( );
        } else {
            HashMap < String, String > skins = new HashMap <>( );
            for ( User user : UserUtils.getUsers( ) ) {
                skins.put( user.getName( ) , user.getSkin( ) );
            }
            return skins;
        }
    }
    
    public static ItemStack getDecorationHead( String type ){
        return switch (type) {
            case "next" -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19=" );
            case "previous" -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ===" );
            case "check" -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMwZjQ1MzdkMjE0ZDM4NjY2ZTYzMDRlOWM4NTFjZDZmN2U0MWEwZWI3YzI1MDQ5YzlkMjJjOGM1ZjY1NDVkZiJ9fX0==" );
            case "delete" -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19=" );
            case "cancel" -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQwYTE0MjA4NDRjZTIzN2E0NWQyZTdlNTQ0ZDEzNTg0MWU5ZjgyZDA5ZTIwMzI2N2NmODg5NmM4NTE1ZTM2MCJ9fX0==" );
            default -> SkullCreator.itemFromBase64( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19" );
        };
        
    }
    
    public static void PlaySound( Player p , String path ){
        try {
            if ( getBoolean( "sounds" ) ) {
                Sound sound = Sound.valueOf( Spigot.getConfig( ).getString( "custom_sounds." + path ) );
                p.playSound( p.getLocation( ) , sound , 1 , 1 );
            }
        } catch ( IllegalArgumentException ignored ) {
        }
    }
    
    public static void PlayParticle( Player p , String path ){
        try {
            if ( getBoolean( "custom_particles." + path + ".enabled" ) ) {
                Particle particle = Particle.valueOf( Spigot.getConfig( ).getString( "custom_particles." + path + ".particle" ) );
                int count = getInt( "custom_particles." + path + ".count" , null );
                int times = getInt( "custom_particles." + path + ".number_of_times" , null );
                int offSetX = getInt( "custom_particles." + path + ".offSetX" , null );
                int offSetY = getInt( "custom_particles." + path + ".offSetY" , null );
                int offSetZ = getInt( "custom_particles." + path + ".offSetZ" , null );
                for ( int i = 0; i < times; i++ ) {
                    p.getWorld( ).spawnParticle( particle , p.getLocation( ) , count , offSetX , offSetY , offSetZ );
                }
            }
        } catch ( IllegalArgumentException ignored ) {
        }
    }
    
    public static String stringify( List < String > l , String Ip ){
        StringBuilder rs = new StringBuilder( Ip );
        for ( String marker : l ) {
            if ( !Ip.equalsIgnoreCase( marker ) && !"127.0.0.1".equalsIgnoreCase( marker ) ) {
                rs.append( ',' ).append( marker );
            }
        }
        return rs.toString( );
    }
    
    public static HashMap < String, String > makeHashMap( String s ){
        HashMap < String, String > map = new HashMap <>( );
        s = s.replace( "{" , "" ).replace( "}" , "" );
        //split the String by a comma
        String[] parts = s.split( "," );
        
        //iterate the parts and add them to a map
        
        for ( String part : parts ) {
            
            //split the employee data by : to get id and name
            String[] empdata = part.split( "=" );
            
            String strId = empdata[0].trim( );
            String strName = empdata[1].trim( );
            strId = strId.replace( " " , "" );
            strName = strName.replace( " " , "" );
            //add to map
            map.put( strId , strName );
        }
        return map;
    }
    
    public static List < String > makeList( String rs ){
        List < String > rl = new LinkedList <>( );
        String[] a = rs.split( "," );
        rl.addAll( Arrays.asList( a ) );
        return rl;
    }
    
    public static boolean isRegistered( String p ){
        if ( mysqlEnabled( ) ) {
            return AltsQuery.getPlayersNames( ).contains( p );
        } else {
            return UserUtils.isSaved( Utils.getUUIDFromName( p ) );
        }
    }
    
    public static Boolean isPlayer( String target ){
        return Spigot.getServer( ).getPlayer( target ) != null;
    }
    
    public static int getPing( Player p ){
        try {
            if ( getServerVersion( ).substring( 1 , 5 ).equalsIgnoreCase( "1_17" ) ) {
                return p.getPing( );
            }
            Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
            return ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
            
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
            return 0;
        }
    }
    
    public static Player randomPlayer( Player p ){
        int count = 0;
        ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ).size( ) );
        while (count < 10) {
            if ( !Bukkit.getOnlinePlayers( ).isEmpty( ) ) {
                players.addAll( Bukkit.getOnlinePlayers( ) );
                int random = new Random( ).nextInt( players.size( ) );
                Player selected = ( Player ) Bukkit.getServer( ).getOnlinePlayers( ).toArray( )[random];
                if ( !selected.equals( p ) ) {
                    players.clear( );
                    return selected;
                } else {
                    players.clear( );
                    count++;
                }
            } else {
                return null;
            }
        }
        return null;
    }
    
    public static int currentPlayerWarns( String warned ){
        int warnings = 0;
        if ( mysqlEnabled( ) ) {
            return WarnsQuery.getPlayerWarns( warned ).size( );
        } else {
            Api.warns.reloadConfig( );
            for ( int i = 1; i <= Api.warns.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( Api.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) ) {
                        warnings++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return warnings;
        }
    }
    
    
    public static int currentPlayerReports( String reported ){
        int reports = 0;
        if ( mysqlEnabled( ) ) {
            return ReportsQuery.getCurrentReports( reported );
        } else {
            Api.reports.reloadConfig( );
            for ( int i = 1; i <= Api.reports.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( Api.reports.getConfig( ).getString( "reports." + i + ".name" ).equalsIgnoreCase( reported ) ) {
                        reports++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return reports;
        }
    }
    
    public static String getServerVersion( ){
        if ( apiType.equals( ApiType.BUNGEECORD ) ) {
            return Bungee.getDescription( ).getVersion( );
        } else {
            return Bukkit.getServer( ).getClass( ).getPackage( ).getName( ).substring( 23 );
        }
    }
    
    public static double getTPS( ){
        return TPS.getTPS( );
    }
    
    public static ArrayList < String > getUsers( ){
        ArrayList < String > Users = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            Users.addAll( AltsQuery.getPlayersNames( ) );
        } else {
            for ( User users : UserUtils.getUsers( ) ) {
                Users.add( users.getName( ) );
            }
        }
        return Users;
    }
    
    public static ArrayList < String > getWarnedPlayers( ){
        ArrayList < String > Users = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            Users.addAll( WarnsQuery.getWarnedPlayers( ) );
        } else {
            ConfigurationSection inventorySection = Api.warns.getConfig( ).getConfigurationSection( "warns" );
            try {
                for ( String key : inventorySection.getKeys( false ) ) {
                    String name = Api.warns.getConfig( ).getString( "warns." + key + ".name" );
                    if ( !Users.contains( name ) ) {
                        Users.add( name );
                    }
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return Users;
    }
    
    public static Boolean isOlderVersion( ){
        String version = Spigot.getServer( ).getBukkitVersion( );
        version = version.substring( 0 , 4 );
        if ( version.endsWith( "." ) ) {
            version = version.substring( 0 , version.length( ) - 1 );
        }
        version = version.replace( "-" , "" ).trim( );
        return switch (version) {
            case "1.24" , "1.23" , "1.22" , "1.21" , "1.20" , "1.19" , "1.18" , "1.17" , "1.16" , "1.15" , "1.14" -> false;
            default -> true;
        };
        
    }
    
    public static String getServer( ){
        return Spigot.getConfig( ).getString( "bungeecord.server" );
    }
    
    public static String getBungeecordServerPrefix( ){
        return Spigot.getConfig( ).getString( "bungeecord.server_prefix" );
    }
    
    public static void linkWeb( Player p , String server , String webPlayerName , String webPassword ){
        JSONObject jsonMessage = new JSONObject( );
        String playerName = Base64.getEncoder( ).withoutPadding( ).encodeToString( webPlayerName.getBytes( ) );
        String serverAddress = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "server_address" ).getBytes( ) );
        String host = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "mysql.host" ).getBytes( ) );
        String port = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "mysql.port" ).getBytes( ) );
        String user = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "mysql.username" ).getBytes( ) );
        String password = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "mysql.password" ).getBytes( ) );
        String database = Base64.getEncoder( ).withoutPadding( ).encodeToString( getString( "mysql.database" ).getBytes( ) );
        String serverEncoded = Base64.getEncoder( ).withoutPadding( ).encodeToString( server.getBytes( ) );
        String webPasswordEncoded = Base64.getEncoder( ).withoutPadding( ).encodeToString( webPassword.getBytes( ) );
        String type = Base64.getEncoder( ).withoutPadding( ).encodeToString( "link".getBytes( ) );
        
        jsonMessage.put( "type" , type );
        jsonMessage.put( "address" , serverAddress );
        jsonMessage.put( "owner" , playerName );
        jsonMessage.put( "server" , serverEncoded );
        jsonMessage.put( "webPasswordEncoded" , webPasswordEncoded );
        jsonMessage.put( "host" , host );
        jsonMessage.put( "port" , port );
        jsonMessage.put( "username" , user );
        jsonMessage.put( "password" , password );
        jsonMessage.put( "db" , database );
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        
        Http.getFromApi( "http://staffcore.glitch.me/api/" + convertedString3 , p , server );
    }
    
    public static void unlinkWeb( Player p , String server , String webPlayerName , String webPassword ){
        JSONObject jsonMessage = new JSONObject( );
        String playerName = Base64.getEncoder( ).withoutPadding( ).encodeToString( webPlayerName.getBytes( ) );
        String serverEncoded = Base64.getEncoder( ).withoutPadding( ).encodeToString( server.getBytes( ) );
        String webPasswordEncoded = Base64.getEncoder( ).withoutPadding( ).encodeToString( webPassword.getBytes( ) );
        String type = Base64.getEncoder( ).withoutPadding( ).encodeToString( "unlink".getBytes( ) );
        
        jsonMessage.put( "type" , type );
        jsonMessage.put( "owner" , playerName );
        jsonMessage.put( "server" , serverEncoded );
        jsonMessage.put( "webPasswordEncoded" , webPasswordEncoded );
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        
        Http.getFromApi( "http://staffcore.glitch.me/api/" + convertedString3 , p , server );
    }
    
    public static Boolean isWebServerLinked( ){
        String server = getString( "server_name" );//TODO FIX THIS AND LINK IT WITH THE NEW SERVER SETTINGS
        JSONObject jsonMessage = new JSONObject( );
        String serverEncoded = Base64.getEncoder( ).withoutPadding( ).encodeToString( server.getBytes( ) );
        String type = Base64.getEncoder( ).withoutPadding( ).encodeToString( "islinked".getBytes( ) );
        
        jsonMessage.put( "type" , type );
        jsonMessage.put( "server" , serverEncoded );
        String convertedString1 = Base64.getEncoder( ).withoutPadding( ).encodeToString( jsonMessage.toString( ).getBytes( ) );
        String convertedString2 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString1.getBytes( ) );
        String convertedString3 = Base64.getEncoder( ).withoutPadding( ).encodeToString( convertedString2.getBytes( ) );
        
        return Http.getBoolean( "http://staffcore.glitch.me/api/" + convertedString3 , "is_Registered" );
    }
    
    public static Integer count( UpdateType type ){
        int count = 0;
        switch (type) {
            case BAN -> {
                try {
                    if ( Utils.mysqlEnabled( ) ) {
                        return BansQuery.getCurrentBans( );
                    } else {
                        ConfigurationSection inventorySection = Api.bans.getConfig( ).getConfigurationSection( "bans" );
                        return inventorySection.getKeys( false ).size( );
                    }
                } catch ( NullPointerException ignored ) {
                    return count;
                }
            }
            case REPORT -> {
                try {
                    if ( Utils.mysqlEnabled( ) ) {
                        return ReportsQuery.getCurrentReports( );
                    } else {
                        ConfigurationSection inventorySection = Api.reports.getConfig( ).getConfigurationSection( "reports" );
                        return inventorySection.getKeys( false ).size( );
                    }
                } catch ( NullPointerException ignored ) {
                    return count;
                }
            }
            case WARN -> {
                try {
                    if ( Utils.mysqlEnabled( ) ) {
                        return WarnsQuery.getCurrentWarns( );
                    } else {
                        ConfigurationSection inventorySection = Api.warns.getConfig( ).getConfigurationSection( "warns" );
                        return inventorySection.getKeys( false ).size( );
                    }
                } catch ( NullPointerException ignored ) {
                    return count;
                }
            }
        }
        return count;
    }
    
    public static String getLanguage( ){
        return getString( "language" );
    }
    
    public static String getConsoleName( ){
        return getString( "console" , "lg" , null );
    }
    
    public static String getTimeLeft( Date d1 , Date ExpDate ){
        long Time = ExpDate.getTime( ) - d1.getTime( );
        long Seconds = TimeUnit.MILLISECONDS.toSeconds( Time ) % 60;
        long Minutes = TimeUnit.MILLISECONDS.toMinutes( Time ) % 60;
        long Hours = TimeUnit.MILLISECONDS.toHours( Time ) % 24;
        long Days = TimeUnit.MILLISECONDS.toDays( Time ) % 365;
        long Years = TimeUnit.MILLISECONDS.toDays( Time ) / 365L;
        if ( Years >= 1 ) {
            return Years + "y " + Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if ( Days >= 1 ) {
            return Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if ( Hours >= 1 ) {
            return Hours + "h " + Minutes + "m " + Seconds + "s";
        } else if ( Minutes >= 1 ) {
            return Minutes + "m " + Seconds + "s";
        } else {
            return Seconds + "s";
        }
    }
    
    public static Date ConvertDate( int amount , String time ){
        long now = new Date( ).getTime( );
        switch (time) {
            case "seconds" -> {
                return new Date( now + TimeUnit.SECONDS.toMillis( amount ) );
            }
            case "minutes" -> {
                return new Date( now + TimeUnit.MINUTES.toMillis( amount ) );
            }
            case "hours" -> {
                return new Date( now + TimeUnit.HOURS.toMillis( amount ) );
            }
            case "days" -> {
                return new Date( now + TimeUnit.DAYS.toMillis( amount ) );
            }
            case "weeks" -> {
                return new Date( now + (TimeUnit.DAYS.toMillis( amount ) * 7) );
            }
            case "months" -> {
                return new Date( now + (TimeUnit.DAYS.toMillis( amount ) * 31) );
            }
            default -> {
                return new Date( now + (TimeUnit.DAYS.toMillis( amount ) * 365) );
            }
        }
        
    }
    
    public static Boolean isStillBanned( int Id ){
        if ( mysqlEnabled( ) )
            return BansQuery.isStillBanned( Id );
        try {
            Date now = new Date( );
            Date exp_date = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ).parse( Api.bans.getConfig( ).getString( "bans." + Id + ".expdate" ) );
            if ( now.after( exp_date ) ) {
                Api.bans.getConfig( ).set( "bans." + Id + ".status" , "closed" );
                Api.bans.saveConfig( );
                Api.bans.reloadConfig( );
                return false;
            }
            return true;
        } catch ( ParseException | NullPointerException error ) {
            error.printStackTrace( );
            Api.bans.getConfig( ).set( "bans." + Id + ".status" , "closed" );
            Api.bans.saveConfig( );
            Api.bans.reloadConfig( );
            return false;
        }
    }
    
    
    public static ArrayList < String > getBannedPlayers( ){
        ArrayList < String > bannedPlayers = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            bannedPlayers.addAll( BansQuery.getBannedPlayers( ) );
        } else {
            try {
                ConfigurationSection inventorySection = Api.bans.getConfig( ).getConfigurationSection( "bans" );
                for ( String key : inventorySection.getKeys( false ) ) {
                    String name = Api.bans.getConfig( ).getString( "bans." + key + ".name" );
                    if ( !bannedPlayers.contains( name ) )
                        bannedPlayers.add( name );
                }
            } catch ( NullPointerException ignored ) {
            }
        }
        return bannedPlayers;
    }
    
    public static int getCurrentBans( ){
        if ( mysqlEnabled( ) ) {
            return BansQuery.getCurrentBans( );
        } else {
            try {
                ConfigurationSection inventorySection = Api.bans.getConfig( ).getConfigurationSection( "bans" );
                return inventorySection.getKeys( false ).size( );
            } catch ( NullPointerException ignored ) {
                return 0;
            }
        }
    }
    
    public static int getCurrentWarns( ){
        if ( mysqlEnabled( ) ) {
            return WarnsQuery.getCurrentWarns( );
        } else {
            int current = 0;
            try {
                ConfigurationSection inventorySection = Api.warns.getConfig( ).getConfigurationSection( "warns" );
                for ( String key : inventorySection.getKeys( false ) )
                    current++;
            } catch ( NullPointerException ignored ) {
            }
            return current;
        }
    }
    
    public static int getCurrentReports( ){
        if ( mysqlEnabled( ) ) {
            return ReportsQuery.getCurrentReports( );
        } else {
            int current = 0;
            try {
                ConfigurationSection inventorySection = Api.reports.getConfig( ).getConfigurationSection( "reports" );
                for ( String key : inventorySection.getKeys( false ) )
                    current++;
            } catch ( NullPointerException ignored ) {
            }
            return current;
        }
    }
    
    public static List < String > getVanishedPlayers( ){
        if ( mysqlEnabled( ) ) {
            return VanishQuery.getVanishedPlayers( );
        } else {
            List < String > vanishedPlayer = new ArrayList <>( );
            for ( User user : UserUtils.getUsers( ) ) {
                if ( user.getVanish( ) ) {
                    vanishedPlayer.add( user.getName( ) );
                }
            }
            return vanishedPlayer;
        }
    }
    
    public static String getIp( Player p ){
        InetAddress address = p.getAddress( ).getAddress( );
        String ip = address.toString( );
        ip = ip.replace( "/" , "" );
        return ip;
    }
    
    public static String getIp( ProxiedPlayer p ){
        InetAddress address = p.getAddress( ).getAddress( );
        String ip = address.toString( );
        ip = ip.replace( "/" , "" );
        return ip;
    }
    
    
    public static ArrayList < String > getAlts( UUID uuid ){
        ArrayList < String > alts = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            AltsQuery.getAltsIps( uuid );
        } else {
            UserUtils.getIps( uuid );
        }
        return alts;
    }
    
    public static void runAsync( Runnable task ){
        switch (Api.currentApiType) {
            case BUNGEECORD -> Bungee.getProxy( ).getScheduler( ).runAsync( Bungee , task );
            case SPIGOT -> Spigot.getServer( ).getScheduler( ).runTaskAsynchronously( Spigot , task );
        }
    }
    
    //TODO REMOVE THIS SHIT AND CHANGE HOW THIS WORKS
    
    public static void setTrollMode( Player p , Boolean bol ){
        PersistentDataContainer PlayerData = p.getPersistentDataContainer( );
        if ( bol ) {
            PlayerData.set( new NamespacedKey( Spigot , "troll" ) , PersistentDataType.STRING , "troll" );
        } else {
            PlayerData.remove( new NamespacedKey( Spigot , "troll" ) );
        }
    }
    
    //TODO CHANGE THIS
    public static boolean getTrollStatus( String player ){
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( Spigot , "troll" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    //TODO CHANGE THIS
    public static List < String > getStaffPlayers( ){
        if ( mysqlEnabled( ) ) {
            return StaffQuery.getStaffPlayers( );
        } else {
            List < String > staffPlayer = new ArrayList <>( );
            for ( Player p : Bukkit.getOnlinePlayers( ) ) {
                if ( p.getPersistentDataContainer( ).has( new NamespacedKey( Spigot , "staff" ) , PersistentDataType.STRING ) ) {
                    staffPlayer.add( p.getName( ) );
                }
            }
            return staffPlayer;
        }
    }
    
    public static Boolean isStillWarned( int Id ){
        if ( mysqlEnabled( ) ) {
            return WarnsQuery.isStillWarned( Id );
        }
        try {
            Date now = new Date( );
            Date exp_date = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ).parse( Api.warns.getConfig( ).getString( "warns." + Id + ".expdate" ) );
            if ( now.after( exp_date ) ) {
                Api.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
                Api.warns.saveConfig( );
                Api.warns.reloadConfig( );
                return false;
            }
            return true;
        } catch ( ParseException | NullPointerException ignored ) {
            Api.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
            Api.warns.saveConfig( );
            Api.warns.reloadConfig( );
            return false;
        }
    }
    
}
