package com.haeun.jdbcTemp.command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.haeun.jdbcTemp.dao.BDao;
import com.haeun.jdbcTemp.dto.BDto;

public class BListCommand implements BCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		BDao dao = new BDao();
		ArrayList<BDto> dtos = dao.list();	//반환되는 리스트 불러오기
		
		//model 객체에 전달
		model.addAttribute("list", dtos);
		
	}
	
}