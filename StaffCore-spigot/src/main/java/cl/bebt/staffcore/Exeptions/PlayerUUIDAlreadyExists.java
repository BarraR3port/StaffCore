/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Exeptions;

import cl.bebt.staffcore.utils.utils;

import java.util.UUID;

public class PlayerUUIDAlreadyExists extends Exception {
    
    public PlayerUUIDAlreadyExists( UUID uuid ){
        super( utils.chat( "&4The UUID: &9" + uuid.toString( ) + " &4Already Exists" ) );
    }
    
}
