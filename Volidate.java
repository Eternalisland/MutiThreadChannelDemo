package com.ergs.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ergs.data.JdbcTemplate.PreparedStatementSetter;

public class Volidate {

	public static String VLOIDATE_SUCCESS = "VLOIDATE_SUCCESS";
	public static String VLOIDATE_FAILED = "VLOIDATE_FAILED";
	public static String volidate(String pid){
		String sql = "select * from tempemp where pidsn = ? ";
		ResultSet rs = null;
		try {
			rs = JdbcTemplate.query(sql, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement pstmt) throws SQLException {
					pstmt.setString(1, pid);
				}
			});
			if(rs.next()){
				return VLOIDATE_SUCCESS;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return VLOIDATE_FAILED;
	}
}
