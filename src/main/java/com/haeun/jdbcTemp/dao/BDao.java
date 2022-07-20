package com.haeun.jdbcTemp.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.haeun.jdbcTemp.dto.BDto;
import com.haeun.jdbcTemp.util.Constant;

//DB랑 연동해주는 클래스(데이터 접속 객체)

public class BDao {
	
	//DataSource dataSource;	// Server의 context.xml 불러서 사용
	
	JdbcTemplate template;

	public BDao() {	//생성한 Constant의 Template 초기화 시켜줌	
		this.template = Constant.template;
	}
	
	public void write(final String bname, final String btitle, final String bcontent) {	 // 사용자가 입력한 3개의 값을 받음
		
		this.template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				
				String sql = "INSERT INTO mvc_board(bid,bname,btitle,bcontent,bhit,bgroup,bstep,bindent) VALUES(mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, bname);
				pstmt.setString(2, btitle);
				pstmt.setString(3, bcontent);
				
				return pstmt;
			}
		});
	
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		//PreparedStatement - SQL구문을 실행시키는 기능
//		try {
//			conn = dataSource.getConnection();
//			String sql = "INSERT INTO mvc_board(bid,bname,btitle,bcontent,bhit,bgroup,bstep,bindent) "
//					+ "VALUES(mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
//			//물음표의 n번째 값 지정(1부터 시작)
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, bname);
//			pstmt.setString(2, btitle);
//			pstmt.setString(3, bcontent);
//			pstmt.executeUpdate();	// integer 반환 가능
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
	
	//배열 - BDto로 만든 데이터 객체(자료구조)
	public ArrayList<BDto> list() {
		
		String sql = "SELECT * FROM mvc_board ORDER BY bgroup DESC, bstep ASC";
		
		//Dto로 만든 Dto리스트에서 객체 하나씩 반환
		//RowMapper : 테이블 행마다 resultSet 객체를 통해 매핑
		return (ArrayList<BDto>) template.query(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		//빈 리스트(배열)
//		ArrayList<BDto> dtos = new ArrayList<BDto>();
//		
//		try {
//			conn = dataSource.getConnection();
//			//bgroup 내림차순, bstep 오름차순 (원글이 맨 위로, 최신 댓글이 밑으로 내려가게)
//			String sql = "SELECT * FROM mvc_board ORDER BY bgroup DESC, bstep ASC";
//			pstmt = conn.prepareStatement(sql);		
//			rs = pstmt.executeQuery();	//sql 실행문
//			
//			while(rs.next()) {//글의 개수만큼 반복
//				int bid = rs.getInt("bid");
//				String bname = rs.getString("bname");
//				String btitle = rs.getString("btitle");
//				String bcontent = rs.getString("bcontent");
//				Timestamp bdate = rs.getTimestamp("bdate");
//				int bhit = rs.getInt("bhit");
//				int bgroup = rs.getInt("bgroup");
//				int bstep = rs.getInt("bstep");
//				int bindent = rs.getInt("bindent");
//				
//				//필드값을 가지고 초기화
//				BDto dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
//				dtos.add(dto);				
//			}
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
//		return dtos;	// 리스트 반환
	}
	
	//한개만 반환하므로 배열 x
	public BDto contentView(String strbid) {
		
		//이 클래스 안에 있는 upHit 메소드 호출
		this.upHit(strbid);	//메소드 호출할때마다 bhit 값 1씩 증가
		
		String sql = "SELECT * FROM mvc_board WHERE bid=" + strbid;
		
		//Dto객체 하나만 반환
		//queryForObject : 한줄의 행만 처리
		return template.queryForObject(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		BDto dto = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			String sql = "SELECT * FROM mvc_board WHERE bid=?";
//			pstmt = conn.prepareStatement(sql);	
//			pstmt.setInt(1, Integer.parseInt(strbid));
//			//문자열로 들어온 strbid를 int형으로 형변환
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {	//글의 개수만큼 반복
//				int bid = rs.getInt("bid");
//				String bname = rs.getString("bname");
//				String btitle = rs.getString("btitle");
//				String bcontent = rs.getString("bcontent");
//				Timestamp bdate = rs.getTimestamp("bdate");
//				int bhit = rs.getInt("bhit");
//				int bgroup = rs.getInt("bgroup");
//				int bstep = rs.getInt("bstep");
//				int bindent = rs.getInt("bindent");
//				
//				dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);	
//			}
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
//		return dto;
	}
	
	//반환값 x
	public void delete(final String strbid) {
		
		String sql = "DELETE FROM mvc_board WHERE bid=?";
		
		this.template.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, strbid);				
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			//id 값을 찾아서 해당 라인 삭제
//			String sql = "DELETE FROM mvc_board WHERE bid=?";
//			pstmt = conn.prepareStatement(sql);	
//			pstmt.setInt(1, Integer.parseInt(strbid));
//			//문자열로 들어온 strbid를 int형으로 형변환
//			pstmt.executeUpdate();
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
	
	public void modify(final String bid, final String bname, final String btitle, final String bcontent) {	 // 사용자가 입력한 3개의 값을 받음
		
		String sql = "UPDATE mvc_board SET bname=?, btitle=?, bcontent=? WHERE bid=?";
		
		this.template.update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				
				pstmt.setString(1, bname);
				pstmt.setString(2, btitle);
				pstmt.setString(3, bcontent);
				pstmt.setInt(4, Integer.parseInt(bid));	//문자열이므로 숫자로 변환
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		//PreparedStatement - SQL구문을 실행시키는 기능
//		try {
//			conn = dataSource.getConnection();
//			String sql = "UPDATE mvc_board SET bname=?, btitle=?, bcontent=? WHERE bid=?";
//			//물음표의 n번째 값 지정(1부터 시작)
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, bname);
//			pstmt.setString(2, btitle);
//			pstmt.setString(3, bcontent);
//			pstmt.setInt(4, Integer.parseInt(bid));	//문자열이므로 숫자로 변환
//			pstmt.executeUpdate();	// integer 반환 가능(실행 성공하면 1을 반환)
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
	
	//다른 클래스에서 접근 못하게 private으로 접근지정자, 반환 x
	//메소드 호출할때마다 bhit 값 1씩 증가
	private void upHit(final String bid) {	//조회수
		
		String sql = "UPDATE mvc_board SET bhit=bhit+1 WHERE bid=?";
		
		this.template.update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				
				pstmt.setInt(1, Integer.parseInt(bid));	//문자열이므로 int형으로 변환
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		//PreparedStatement - SQL구문을 실행시키는 기능
//		try {
//			conn = dataSource.getConnection();
//			
//			String sql = "UPDATE mvc_board SET bhit=bhit+1 WHERE bid=?";
//			//물음표의 n번째 값 지정(1부터 시작)
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(bid));	//문자열이므로 숫자로 변환
//			pstmt.executeUpdate();	// integer 반환 가능(실행 성공하면 1을 반환)
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
	
	public BDto replyView(String strbid) {
		
		String sql = "SELECT * FROM mvc_board WHERE bid=" + strbid;
		
		//Dto객체 하나만 반환(한줄의 열을 처리함)
		return template.queryForObject(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
		
		//bid 찾아서 글의 내용 반환	
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		BDto dto = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			String sql = "SELECT * FROM mvc_board WHERE bid=?";
//			pstmt = conn.prepareStatement(sql);	
//			pstmt.setInt(1, Integer.parseInt(strbid));
//			//문자열로 들어온 strbid를 int형으로 형변환
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {	//글의 개수만큼 반복
//				int bid = rs.getInt("bid");
//				String bname = rs.getString("bname");
//				String btitle = rs.getString("btitle");
//				String bcontent = rs.getString("bcontent");
//				Timestamp bdate = rs.getTimestamp("bdate");
//				int bhit = rs.getInt("bhit");
//				int bgroup = rs.getInt("bgroup");
//				int bstep = rs.getInt("bstep");
//				int bindent = rs.getInt("bindent");
//				
//				dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);	
//			}
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
//		return dto;
	}
	
	public void reply(final String bid, final String bname, final String btitle, final String bcontent, final String bgroup, final String bstep, final String bindent) {
		
		this.replyShape(bgroup, bstep);	// 댓글 정렬기능 메소드 호출
		
		String sql = "INSERT INTO mvc_board(bid,bname,btitle,bcontent,bgroup,bstep,bindent) "
				+ "VALUES(mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?)";
				//mvc_board_seq.nextval(현재 시퀀스의 다음 값을 불러옴) - 원본의 글 번호
		
		this.template.update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				
				pstmt.setString(1, bname);
				pstmt.setString(2, btitle);
				pstmt.setString(3, bcontent);
				//bgroup - 원글과 댓글을 하나로 모아주는 역할
				pstmt.setInt(4, Integer.parseInt(bgroup));
				//bstep - 댓글을 달면 원글보다 1씩 증가해야 함
				pstmt.setInt(5, Integer.parseInt(bstep)+1);
				pstmt.setInt(6, Integer.parseInt(bindent)+1);
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		//PreparedStatement - SQL구문을 실행시키는 기능
//		try {
//			conn = dataSource.getConnection();
//			String sql = "INSERT INTO mvc_board(bid,bname,btitle,bcontent,bgroup,bstep,bindent) "
//					+ "VALUES(mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?)";
//					//mvc_board_seq.nextval(현재 시퀀스의 다음 값을 불러옴) - 원본의 글 번호
//			//물음표의 n번째 값 지정(1부터 시작)
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, bname);
//			pstmt.setString(2, btitle);
//			pstmt.setString(3, bcontent);
//			//bgroup - 원글과 댓글을 하나로 모아주는 역할
//			pstmt.setInt(4, Integer.parseInt(bgroup));
//			//댓글을 달면 원글보다 1씩 증가해야 함
//			pstmt.setInt(5, Integer.parseInt(bstep)+1);
//			pstmt.setInt(6, Integer.parseInt(bindent)+1);
//			pstmt.executeUpdate();	// integer 반환 가능
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
	
	// 댓글 정렬, 반환값 X, 매개변수 O(특정 글만 해당되므로)
	private void replyShape(final String strGroup, final String strStep) {	
		
		String sql = "UPDATE mvc_board SET bstep=bstep+1 WHERE bgroup=? and bstep > ?";
		
		this.template.update(sql, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				
				pstmt.setInt(1, Integer.parseInt(strGroup));	//문자열이므로 숫자로 변환
				pstmt.setInt(2, Integer.parseInt(strStep));
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		//PreparedStatement - SQL구문을 실행시키는 기능
//		try {
//			conn = dataSource.getConnection();
//			//bgroup이 같은 것을 찾고 bstep 값이 더 큰 것을 찾아서 bstep값을 1씩 증가시킴
//			//원글을 제외한 나머지 step값 1씩 증가
//			String sql = "UPDATE mvc_board SET bstep=bstep+1 WHERE bgroup=? and bstep > ?"; 
//			//물음표의 n번째 값 지정(1부터 시작)
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(strGroup));	//문자열이므로 숫자로 변환
//			pstmt.setInt(2, Integer.parseInt(strStep));
//			pstmt.executeUpdate();	// integer 반환 가능(실행 성공하면 1을 반환)
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn !=null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}			
//		}
	}
}