package com.boogie.member.service;

import org.springframework.web.servlet.ModelAndView;

public interface MemberService {
	public void memberRegisterOk(ModelAndView mav);
	public void idDuplChk(ModelAndView mav);
}
