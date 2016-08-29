package com.cdjdgm.dip.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class JdbcUtil {
	static Connection conn;
	
	public static void connect(String driverClass, String jdbcUrl, String user, String pwd){
		try {
			conn = DriverManager.getConnection(jdbcUrl, user, pwd);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public static void execute(String... sqls){
	    try (Statement stmt = conn.createStatement();){
	    	for (String sql : sqls){
	    		if (sql!=null && !"".equals(sql.trim())){
	    			stmt.execute(sql);
	    		}
	    	}
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	public static void close() {
		try{
			if (conn!=null && !conn.isClosed())
			conn.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

}
