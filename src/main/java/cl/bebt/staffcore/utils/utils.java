package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

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
            } else {
                return plugin.es_cl.getConfig( ).getString( path );
            }
        }
        if ( type.equalsIgnoreCase( "lg" ) && prefix != null ) { //Language
            if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return plugin.getConfig( ).getString( "server_prefix" ) + plugin.en_na.getConfig( ).getString( path );
                } else {
                    return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.en_na.getConfig( ).getString( path );
                }
            } else {
                if ( prefix.equalsIgnoreCase( "sv" ) ) {
                    return plugin.getConfig( ).getString( "server_prefix" ) + plugin.es_cl.getConfig( ).getString( path );
                } else {
                    return plugin.getConfig( ).getString( "staff_prefix" ) + plugin.es_cl.getConfig( ).getString( path );
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
    /*
    public static boolean getBoolean( String path , @Nullable String type ){
        if ( type == null ) return plugin.getConfig( ).getBoolean( path );
        if ( type.equalsIgnoreCase( "lg" ) ) { //Language
            if ( plugin.getConfig( ).getString( "language" ).equalsIgnoreCase( "EN_NA" ) ) {
                return plugin.en_na.getConfig( ).getBoolean( path );
            } else {
                return plugin.es_cl.getConfig( ).getBoolean( path );
            }
        } else if ( type.equalsIgnoreCase( "item" ) ) {
            return plugin.items.getConfig( ).getBoolean( path );
        } else {
            return plugin.alerts.getConfig( ).getBoolean( path );
        }
    }
     */
    
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
            } else {
                return plugin.es_cl.getConfig( ).getInt( path );
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
        boolean isNewVersion = Arrays.stream( Material.values( ) )
                .map( Material::name ).collect( Collectors.toList( ) ).contains( "PLAYER_HEAD" );
        Material type = Material.matchMaterial( isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM" );
        ItemStack item = new ItemStack( type , 1 );
        if ( !(isNewVersion) ) {
            item.setDurability( ( short ) 3 );
        }
        SkullMeta meta = ( SkullMeta ) item.getItemMeta( );
        meta.setOwner( p );
        item.setItemMeta( meta );
        return item;
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
            return SQLGetter.getPlayersNames( ).contains( p );
        } else {
            return Objects.requireNonNull( plugin.alts.getConfig( ).getConfigurationSection( "alts" ) ).contains( p );
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
            Object entityPlayer = p.getClass( ).getMethod( "getHandle" ).invoke( p );
            int ping = ( int ) entityPlayer.getClass( ).getField( "ping" ).get( entityPlayer );
            return ping;
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
            e.printStackTrace( );
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
    
    public static int currentWarns( ){
        int current = 0;
        try {
            ConfigurationSection inventorySection = main.plugin.warns.getConfig( ).getConfigurationSection( "warns" );
            for ( String key : inventorySection.getKeys( false ) ) {
                current++;
            }
        } catch ( NullPointerException ignored ) {
        }
        return current;
    }
    
    public static int currentPlayerWarns( String warned ){
        int warnings = 0;
        if ( mysqlEnabled( ) ) {
            return SQLGetter.getCurrentWarns( warned );
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
            return SQLGetter.getCurrentReports( reported );
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
            Users.addAll( SQLGetter.getPlayersNames( ) );
        } else {
            try {
                ConfigurationSection inventorySection = plugin.alts.getConfig( ).getConfigurationSection( "alts" );
                Users.addAll( inventorySection.getKeys( false ) );
            } catch ( NullPointerException ignored ) {
            }
        }
        return Users;
    }
    
    public static ArrayList < String > getWarnedPlayers( ){
        ArrayList < String > Users = new ArrayList <>( );
        if ( mysqlEnabled( ) ) {
            Users.addAll( SQLGetter.getWarnedPlayers( ) );
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
        version = version.replace( "-" , "" );
        version = version.trim( );
        if ( version.equalsIgnoreCase( "1.17" ) ) {
            return false;
        } else if ( version.equalsIgnoreCase( "1.16" ) ) {
            return false;
        } else if ( version.equalsIgnoreCase( "1.15" ) ) {
            return false;
        } else if ( version.equalsIgnoreCase( "1.14" ) ) {
            return false;
        } else if ( version.equalsIgnoreCase( "1.13" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.12" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.11" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.10" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.9" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.8" ) ) {
            return true;
        } else if ( version.equalsIgnoreCase( "1.7" ) ) {
            return true;
        } else {
            return true;
        }
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
        
        Http.get( "http://staffcore.glitch.me/api/" + convertedString3 , p );
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
        
        Http.get( "http://localhost:82/api/" + convertedString3 , p );
    }
    
}
