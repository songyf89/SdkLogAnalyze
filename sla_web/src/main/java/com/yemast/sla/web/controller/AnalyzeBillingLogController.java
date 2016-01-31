package com.yemast.sla.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yemast.sla.common.service.AnalyzeBillingLogService;

@Controller
@RequestMapping("/")
public class AnalyzeBillingLogController {

	@Autowired
	private AnalyzeBillingLogService analyzeBillingLogService;

	@RequestMapping("/")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}

	@RequestMapping("/statisticsAPKStatus")
	@ResponseBody
	public String statisticsAPKStatus() {
		Long count = analyzeBillingLogService.statisticsAPKStatus(4);
		return count.toString();
	}
}
