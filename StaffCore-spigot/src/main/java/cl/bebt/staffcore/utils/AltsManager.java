/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcore.utils;

import cl.bebt.staffcore.EntitysUtils.UserUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AltsManager {
    
    public static HashMap < String, String > getAlts( UUID uuid ){
        return Objects.requireNonNull( UserUtils.findUser( uuid ) ).getAlts( );
    }
    
    
}
