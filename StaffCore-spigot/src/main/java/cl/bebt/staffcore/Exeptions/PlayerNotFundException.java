/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Exeptions;

import cl.bebt.staffcore.utils.utils;

public class PlayerNotFundException extends Exception {
    
    public PlayerNotFundException( ){
        super( utils.chat( "&4Player not found." ) );
    }
    
    public PlayerNotFundException( String message ){
        super( message );
    }
}
