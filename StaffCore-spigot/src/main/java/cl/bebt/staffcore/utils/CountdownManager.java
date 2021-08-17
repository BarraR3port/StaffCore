/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.EntitysUtils.UserUtils;
import cl.bebt.staffcore.Exeptions.PlayerNotFundException;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class CountdownManager {
    
    public static void setMuteCountdown( UUID uuid , long seconds ){
        try {
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            UserUtils.setMutedTime( uuid , delay );
        } catch ( PlayerNotFundException ignored ) {
        }
    }
    
    public static void removeMuteCountdown( UUID uuid ){
        try {
            UserUtils.setMutedTime( uuid , 0D );
        } catch ( PlayerNotFundException ignored ) {
        }
    }
    
    public static long getMuteCountDown( UUID uuid ){
        try {
            double delay = UserUtils.getMutedTime( uuid );
            return Math.toIntExact( Math.round( (delay - System.currentTimeMillis( )) / 1000 ) );
        } catch ( PlayerNotFundException error ) {
            return 0;
        }
    }
    
    public static boolean checkMuteCountdown( UUID uuid ){
        try {
            return UserUtils.getMutedTime( uuid ) <= System.currentTimeMillis( );
        } catch ( PlayerNotFundException error ) {
            return false;
        }
    }
    
    public static void setCountDown( UUID uuid , Double seconds ){
        try {
            double delay = System.currentTimeMillis( ) + (seconds * 1000);
            UserUtils.setMutedTime( uuid , delay );
        } catch ( PlayerNotFundException ignored ) {
        }
    }
    
}
