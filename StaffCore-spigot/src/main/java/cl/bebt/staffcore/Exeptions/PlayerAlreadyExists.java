/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.Exeptions;

import cl.bebt.staffcore.utils.utils;

public class PlayerAlreadyExists extends Exception {
    
    public PlayerAlreadyExists( String player ){
        super( utils.chat( "&4The Player: &9" + player + " &4Already Exists" ) );
    }
    
}
