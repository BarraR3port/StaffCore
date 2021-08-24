/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffcoreapi.Entities;

import java.util.UUID;

public class PlayerUUID {
    
    private String name;
    
    private UUID id;
    
    public String getName( ){
        return name;
    }
    
    public UUID getId( ){
        return id;
    }
}
