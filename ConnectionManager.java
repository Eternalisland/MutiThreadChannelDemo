package com.ergs.data;

import java.sql.Connection;

public class ConnectionManager {
	
     private static ThreadLocal<Connection> Local = new ThreadLocal<>();
     
     public static Connection getConnection(){
    	 Connection cnn = Local.get();
    	 if(null != cnn){
    		 return cnn;
    	 }
    	 else{
    		 cnn = GetConnection.getConnection();  		 
    		 Local.set(cnn);
    	 }
    	 return cnn;
     }
      
     public static void release(){
    	 Connection cnn = Local.get();
    	 if(cnn != null){
    		 DBUtil.release(cnn);
    		 Local.remove();
    	 }
    	 
     }
     
}
    

