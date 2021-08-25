/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.utils;

import cl.bebt.staffcoreapi.EntitiesUtils.UserUtils;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class CountdownManager {
    
    public static void setMuteCountdown( UUID uuid , long seconds ){
        double delay = System.currentTimeMillis( ) + (seconds * 1000);
        UserUtils.setMutedTime( uuid , delay );
        
    }
    
    public static void removeMuteCountdown( UUID uuid ){
        UserUtils.setMutedTime( uuid , 0D );
        
    }
    
    public static long getMuteCountDown( UUID uuid ){
        double delay = UserUtils.getMutedTime( uuid );
        return Math.toIntExact( Math.round( (delay - System.currentTimeMillis( )) / 1000 ) );
        
    }
    
    public static boolean checkMuteCountdown( UUID uuid ){
        return UserUtils.getMutedTime( uuid ) <= System.currentTimeMillis( );
        
    }
    
    public static void setCountDown( UUID uuid , Double seconds ){
        double delay = System.currentTimeMillis( ) + (seconds * 1000);
        UserUtils.setMutedTime( uuid , delay );
        
    }
    
}
