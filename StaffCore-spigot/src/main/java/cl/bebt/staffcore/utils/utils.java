/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.Entitys.User;
import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.Queries.*;
import cl.bebt.staffcore.utils.UUID.UUIDGetter;
import dev.dbassett.skullcreator.SkullCreator;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class utils {
    
    private static final ArrayList < Player > players = new ArrayList <>( Bukkit.getServer( ).getOnlinePlayers( ).size( ) );
    
    private static main plugin;
    
    public utils( main plugin ){
        utils.plugin = plugin;
    }
    
    public static String chat( String s ){
        return ChatColor.translateAlternateColorCodes( '&' , s );
    }
    
    public static void tell( CommandSender sender , String message ){
        sender.sendMessage( chat( message ) );
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
        ComponentBuilder cb = new ComponentBuilder( chat( hover ) );
        TextComponent dis = new TextComponent( chat( msg ) );
        dis.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , cb.create( ) ) );
        dis.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL , link ) );
        p.spigot( ).sendMessage( dis );
    }
    
    /**
     * @param path   The path
     * @param type   Language = "lg" | Item = "item" | Alert = "alerts"
     * @param prefix Server Prefix = "sv" | Staff Prefix = "staff"
     *
     * @return The string asked for.
     */
    public static String getString( String path , @Nullable String type , @Nullable String prefix ){
        if ( type == null && prefix == null ) {
            return plugin.getConfig( ).getString( path );
        }
        if ( type == null && prefix.equalsIgnoreCase( "sv" ) ) {
            return plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( path );
        } else if ( type == null && prefix.equalsIgnoreCase( "staff" ) ) {
            return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.getConfig( ).getString( path );
        } else if ( type.equalsIgnoreCase( "lg" ) && prefix == null ) {
            if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                return plugin.en_na.getConfig( ).getString( path );
            } else if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                return plugin.es_cl.getConfig( ).getString( path );
            } else if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "FR" ) ) {
                return plugin.fr.getConfig( ).getString( path );
            }
        }
        if ( type.equalsIgnoreCase( "lg" ) && prefix != null ) { //Language
            if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return plugin.getConfig( ).getString( "server_prefix" ) + plugin.en_na.getConfig( ).getString( path );
                } else {
                    return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.en_na.getConfig( ).getString( path );
                }
            } else if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return plugin.getConfig( ).getString( "server_prefix" ) + plugin.es_cl.getConfig( ).getString( path );
                } else {
                    return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.es_cl.getConfig( ).getString( path );
                }
            } else {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return plugin.getConfig( ).getString( "server_prefix" ) + plugin.fr.getConfig( ).getString( path );
                } else {
                    return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.fr.getConfig( ).getString( path );
                }
            }
        } else if ( type.equalsIgnoreCase( "item" ) && prefix == null ) {
            return plugin.items.getConfig( ).getString( path );
        } else if ( type.equalsIgnoreCase( "menu" ) && prefix == null ) {
            return plugin.menus.getConfig( ).getString( path );
        } else {
            return "String Not Found";
        }
    }
    
    public static String getString( String path ){
        return plugin.getConfig( ).getString( path );
    }
    
    public static boolean getBoolean( String path ){
        return plugin.getConfig( ).getBoolean( path );
    }
    
    /**
     * @param path The String of the path
     * @param type lg | item | alerts
     *
     * @return the int that was asked for
     */
    public static int getInt( String path , @Nullable String type ){
        if ( type == null ) return plugin.getConfig( ).getInt( path );
        if ( type.equalsIgnoreCase( "lg" ) ) { //Language
            if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                return plugin.en_na.getConfig( ).getInt( path );
            } else if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "ES_CL" ) ) {
                return plugin.es_cl.getConfig( ).getInt( path );
            } else {
                return plugin.fr.getConfig( ).getInt( path );
            }
        } else if ( type.equalsIgnoreCase( "item" ) ) {
            return plugin.items.getConfig( ).getInt( path );
        } else {
            return plugin.alerts.getConfig( ).getInt( path );
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
            return plugin.items.getConfig( ).getStringList( path );
        } else if ( type.equalsIgnoreCase( "menu" ) ) {
            return plugin.menus.getConfig( ).getStringList( path );
        } else {
            return plugin.alerts.getConfig( ).getStringList( path );
        }
    }
    
    public static void reloadConfigs( ){
        plugin.reloadConfig( );
        plugin.bans.reloadConfig( );
        plugin.alts.reloadConfig( );
        plugin.warns.reloadConfig( );
        plugin.en_na.reloadConfig( );
        plugin.es_cl.reloadConfig( );
        plugin.fr.reloadConfig( );
        plugin.items.reloadConfig( );
        plugin.alerts.reloadConfig( );
        plugin.menus.reloadConfig( );
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
    
    public static ItemStack getPlayerHead( String p ){
        UUID uuid = getUUIDFromName( p );
        try {
            if ( UserUtils.isSaved( uuid ) ) {
                return SkullCreator.itemFromBase64( UserUtils.getSkin( uuid ) );
            } else {
                return SkullCreator.itemFromBase64( getSkin( p ) );
            }
        } catch ( PlayerNotFundException ignored ) {
            return SkullCreator.itemFromBase64( getSkin( p ) );
        }
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
    
    public static String getSkin( String name ){
        return Http.getHead( "http://localhost:82/api/head/" + name , name );
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
    
    public static UUID getUUIDFromName( String player ){
        try {
            if ( mysqlEnabled( ) ) {
                return AltsQuery.getUUIDByName( player );
            } else {
                return UserUtils.getUUIDFromName( player );
            }
        } catch ( NullPointerException ignored ) {
        }
        return UUIDGetter.getUUID( player );
    }
    
    public static void PlaySound( Player p , String path ){
        try {
            if ( plugin.getConfig( ).getBoolean( "sounds" ) ) {
                Sound sound = Sound.valueOf( plugin.getConfig( ).getString( "custom_sounds." + path ) );
                p.playSound( p.getLocation( ) , sound , 1 , 1 );
            }
        } catch ( IllegalArgumentException ignored ) {
        }
    }
    
    public static void PlayParticle( Player p , String path ){
        try {
            if ( plugin.getConfig( ).getBoolean( "custom_particles." + path + ".enabled" ) ) {
                Particle particle = Particle.valueOf( plugin.getConfig( ).getString( "custom_particles." + path + ".particle" ) );
                int count = plugin.getConfig( ).getInt( "custom_particles." + path + ".count" );
                int times = plugin.getConfig( ).getInt( "custom_particles." + path + ".number_of_times" );
                int offSetX = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetX" );
                int offSetY = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetY" );
                int offSetZ = plugin.getConfig( ).getInt( "custom_particles." + path + ".offSetZ" );
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
            return UserUtils.isSaved( utils.getUUIDFromName( p ) );
        }
    }
    
    public static boolean mysqlEnabled( ){
        return plugin.getConfig( ).getBoolean( "mysql.enabled" );
    }
    
    public static Boolean isPlayer( String target ){
        return Bukkit.getPlayer( target ) != null;
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
            main.plugin.warns.reloadConfig( );
            for ( int i = 1; i <= plugin.warns.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( main.plugin.warns.getConfig( ).getString( "warns." + i + ".name" ).equalsIgnoreCase( warned ) ) {
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
            main.plugin.reports.reloadConfig( );
            for ( int i = 1; i <= plugin.reports.getConfig( ).getInt( "count" ); i++ ) {
                try {
                    if ( main.plugin.reports.getConfig( ).getString( "reports." + i + ".name" ).equalsIgnoreCase( reported ) ) {
                        reports++;
                    }
                } catch ( NullPointerException ignored ) {
                }
            }
            return reports;
        }
    }
    
    public static String getServerVersion( ){
        return Bukkit.getServer( ).getClass( ).getPackage( ).getName( ).substring( 23 );
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
            ConfigurationSection inventorySection = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            try {
                for ( String key : inventorySection.getKeys( false ) ) {
                    String name = plugin.warns.getConfig( ).getString( "warns." + key + ".name" );
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
        String version = plugin.getServer( ).getBukkitVersion( );
        version = version.substring( 0 , 4 );
        if ( version.endsWith( "." ) ) {
            version = version.substring( 0 , version.length( ) - 1 );
        }
        version = version.replace( "-" , "" ).trim( );
        return switch (version) {
            case "1.20" , "1.19" , "1.18" , "1.17" , "1.16" , "1.15" , "1.14" -> false;
            default -> true;
        };
        
    }
    
    public static void sendDiscordAlertMsg( String title , ArrayList < String > msg ){
        try {
            DiscordUtils.DiscordWebHooksAlerts( msg , title );
        } catch ( IllegalArgumentException exception ) {
            if ( getBoolean( "discord.type.alerts.enabled" ) ) {
                tell( Bukkit.getConsoleSender( ) , getString( "discord.could_not_connect" , "lg" , "staff" ) );
            }
        }
    }
    
    public static void sendDiscordDebugMsg( Player p , String title , ArrayList < String > msg ){
        try {
            DiscordUtils.DiscordWebHooksDebug( p , msg , title );
        } catch ( IllegalArgumentException exception ) {
            if ( getBoolean( "discord.type.debug.enabled" ) ) {
                tell( Bukkit.getConsoleSender( ) , getString( "discord.could_not_connect" , "lg" , "staff" ) );
            }
        }
    }
    
    public static String getBungeecordServerPrefix( ){
        return plugin.getConfig( ).getString( "bungeecord.server_prefix" );
    }
    
    public static String getServer( ){
        return plugin.getConfig( ).getString( "bungeecord.server" );
    }
    
    public static String getWebServer( ){
        return plugin.getConfig( ).getString( "server_name" );
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
        
        Http.getLatestVersion( "http://staffcore.glitch.me/api/" + convertedString3 , p , server );
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
        
        Http.getLatestVersion( "http://staffcore.glitch.me/api/" + convertedString3 , p , server );
    }
    
    public static Boolean isWebServerLinked( ){
        String server = plugin.getConfig( ).getString( "server_name" );
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
    
    public static Integer count( String type ){
        int count = 0;
        switch (type) {
            case "bans" -> {
                try {
                    if ( utils.mysqlEnabled( ) ) {
                        return BansQuery.getCurrentBans( );
                    } else {
                        ConfigurationSection inventorySection = main.plugin.bans.getConfig( ).getConfigurationSection( "bans" );
                        return inventorySection.getKeys( false ).size( );
                    }
                } catch ( NullPointerException ignored ) {
                    return count;
                }
            }
            case "reports" -> {
                try {
                    if ( utils.mysqlEnabled( ) ) {
                        return ReportsQuery.getCurrentReports( );
                    } else {
                        ConfigurationSection inventorySection = main.plugin.reports.getConfig( ).getConfigurationSection( "reports" );
                        return inventorySection.getKeys( false ).size( );
                    }
                } catch ( NullPointerException ignored ) {
                    return count;
                }
            }
            case "warns" -> {
                try {
                    if ( utils.mysqlEnabled( ) ) {
                        return WarnsQuery.getCurrentWarns( );
                    } else {
                        ConfigurationSection inventorySection = main.plugin.warns.getConfig( ).getConfigurationSection( "warns" );
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
    
    public static String getTimeLeft( Date ExpDate ){
        Date now = new Date( );
        long time = now.getTime( ) - ExpDate.getTime( );
        long diffSeconds = time / 1000;
        long diffMinutes = time / (60 * 1000);
        long diffHours = time / (60 * 60 * 1000);
        long diffDays = time / (60 * 60 * 1000 * 24);
        long diffWeeks = time / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = ( long ) (time / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = time / (( long ) 60 * 60 * 1000 * 24 * 365);
        
        if ( diffMinutes < 1 ) {
            return diffSeconds + " s";
        } else if ( diffHours < 1 ) {
            return diffMinutes + " m " + diffSeconds + " s";
        } else if ( diffDays < 1 ) {
            return diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        } else if ( diffWeeks < 1 ) {
            return diffDays + " d " + diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        } else if ( diffMonths < 1 ) {
            return diffWeeks + " w " + diffDays + " d " + diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        } else if ( diffYears < 1 ) {
            return diffMonths + " m " + diffWeeks + " w " + diffDays + " d " + diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        } else {
            return diffYears + " y " + diffMonths + " m " + diffWeeks + " w " + diffDays + " d " + diffHours + " h " + diffMinutes + " m " + diffSeconds + " s";
        }
    }
    
    public static Date ConvertDate( long amount , String time ){
        Date now = new Date( );
        Calendar cal = Calendar.getInstance( );
        cal.setTime( now );
        switch (time) {
            case "s" -> cal.add( Calendar.SECOND , ( int ) amount );
            case "m" -> cal.add( Calendar.MINUTE , ( int ) amount );
            case "h" -> cal.add( Calendar.HOUR , ( int ) amount );
            case "d" -> cal.add( Calendar.DAY_OF_MONTH , ( int ) amount );
            case "w" -> cal.add( Calendar.WEEK_OF_MONTH , ( int ) amount );
            case "M" -> cal.add( Calendar.MONTH , ( int ) amount );
            case "y" -> cal.add( Calendar.YEAR , ( int ) amount );
        }
        return cal.getTime( );
    }
    
}
