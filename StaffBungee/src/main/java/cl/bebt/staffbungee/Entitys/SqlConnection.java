/*
 * Copyright (c) 2021. StaffCore Use of this source is governed by the MIT License that can be found int the LICENSE file
 */

package cl.bebt.staffbungee.Entitys;

public class SqlConnection {
    
    private String Host;
    
    private String Port;
    
    private String UserName;
    
    private String DataBase;
    
    private String Password;
    
    private Boolean Enabled;
    
    public SqlConnection( String host , String port , String userName , String dataBase , String password , Boolean enabled ){
        Host = host;
        Port = port;
        UserName = userName;
        DataBase = dataBase;
        Password = password;
        Enabled = enabled;
    }
    
    public String getHost( ){
        return Host;
    }
    
    public void setHost( String address ){
        Host = address;
    }
    
    public String getPort( ){
        return Port;
    }
    
    public void setPort( String port ){
        Port = port;
    }
    
    public String getUserName( ){
        return UserName;
    }
    
    public void setUserName( String userName ){
        UserName = userName;
    }
    
    public String getDataBase( ){
        return DataBase;
    }
    
    public void setDataBase( String dataBase ){
        DataBase = dataBase;
    }
    
    public String getPassword( ){
        return Password;
    }
    
    public void setPassword( String password ){
        Password = password;
    }
    
    public Boolean getEnabled( ){
        return Enabled;
    }
    
    public void setEnabled( Boolean enabled ){
        Enabled = enabled;
    }
}
