package com.haeun.jdbcTemp.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.haeun.jdbcTemp.dao.BDao;

public class BDeleteCommand implements BCommand {

	@Override
	public void execute(Model model) {
		
		//안에 든 값을 뽑아냄(arrayList와 비슷)
		Map<String, Object> map = model.asMap();
		//model객체로 전달받은 데이터를 request객체로 받기
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String bid = request.getParameter("bid");
		BDao dao = new BDao();
		
		//dao에 있는 delete 메소드 호출
		dao.delete(bid);
	}

}
