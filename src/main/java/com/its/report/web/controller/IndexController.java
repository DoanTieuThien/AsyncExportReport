package com.its.report.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends AbstractController {

	@Override
	@RequestMapping("/")
	public String loadPage() throws Exception {
		return "report-dashboard";
	}

}
