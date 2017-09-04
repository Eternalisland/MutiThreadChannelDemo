package com.ergs.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JdbcTemplate{
	public static <T> List<T>  query(String sql,PreparedStatementSetter setters,RowCallBackHandler<T> hander){
		ResultSet rs =null;
		List<T> list = null;
		try {
			rs = query(sql, setters);
			if(hander != null && rs.next()){
				list = new ArrayList<>();
				list.add(hander.processRow(rs));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	public static <T> T singleQuery(String sql,PreparedStatementSetter setters,RowCallBackHandler<T> hander){
		ResultSet rs = null;
		try {
			rs = query(sql, setters);
			if(hander !=null && rs.next()){
				return hander.processRow(rs);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new JdbcTemplateException();
		}	finally{
			DBUtil.release(rs);
		}
		return null;
		
	}
	
	public static <T> T singleQuery(String sql,RowCallBackHandler<T> hander){
		return singleQuery(sql, null, hander);
		}
	public static ResultSet query(String sql,PreparedStatementSetter setter) throws SQLException{
		PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
		if(setter != null){
			setter.setValues(pstmt);
		}
		return pstmt.executeQuery();
		}
	
	public static int[] batch(String sql,PreparedStatementSetter...setter) {
		PreparedStatement pstmt =null;
		try {
			pstmt = ConnectionManager.getConnection().prepareStatement(sql);
			if(setter != null){
				for(PreparedStatementSetter set :setter){
						set.setValues(pstmt);
						pstmt.addBatch();
				}
			}
			return pstmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JdbcTemplateException();
		}finally{
			DBUtil.release(pstmt);
			
		}
		
	}
	public static int update(String sql,PreparedStatementSetter setter){
		PreparedStatement pstmt =null;
		try {
			pstmt = ConnectionManager.getConnection().prepareStatement(sql);
			if(setter !=null){
			setter.setValues(pstmt);
			return pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
		
	}
	
	public static class JdbcTemplateException extends RuntimeException{		
		private static final long serialVersionUID = 1L;
		public JdbcTemplateException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public JdbcTemplateException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

		public JdbcTemplateException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public JdbcTemplateException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public JdbcTemplateException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	
	public static interface PreparedStatementSetter {
		void setValues(PreparedStatement pstmt) throws SQLException;
	}
	public static interface RowCallBackHandler<T> {
		T processRow(ResultSet rs) throws SQLException;
	}
	
	
}
