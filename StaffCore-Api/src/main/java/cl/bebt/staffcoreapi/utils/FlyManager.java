/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FlyManager {
    
    public static void enable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            p.setAllowFlight( true );
            p.setFlying( true );
            UserUtils.setFly( uuid , true );
        } catch ( NullPointerException ignored ) {
            ignored.printStackTrace( );
        }
    }
    
    public static void disable( UUID uuid ){
        try {
            Player p = Bukkit.getPlayer( uuid );
            if ( !UserUtils.getVanish( uuid ) || !UserUtils.getStaff( uuid ) ) {
                if ( p.getGameMode( ) == GameMode.ADVENTURE || p.getGameMode( ) == GameMode.SURVIVAL ) {
                    p.setAllowFlight( false );
                    p.setFlying( false );
                    if ( Utils.getBoolean( "staff.fly_invincible" ) ) {
                        p.setInvulnerable( false );
                    }
                }
            }
        } catch ( NullPointerException ignored ) {
        }
    }
    
    
}

