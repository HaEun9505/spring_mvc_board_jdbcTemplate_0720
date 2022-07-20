package com.haeun.jdbcTemp.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.haeun.jdbcTemp.dao.BDao;

public class BReplyCommand implements BCommand {

	@Override
	public void execute(Model model) {
		//map 안에 든 값을 뽑아냄(arrayList와 비슷)
		Map<String, Object> map = model.asMap();
		//model객체로 전달받은 데이터를 request객체로 받기
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String bid = request.getParameter("bid");		
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		String bgroup= request.getParameter("bgroup");
		String bstep= request.getParameter("bstep");
		String bindent= request.getParameter("bindent");
		
		BDao dao = new BDao();		
		//dao 메소드
		dao.reply(bid, bname, btitle, bcontent, bgroup, bstep, bindent);

	}

}
