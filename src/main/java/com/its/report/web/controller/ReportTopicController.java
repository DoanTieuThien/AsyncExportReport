package com.its.report.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReportTopicController extends AbstractController {

	@Override
	@RequestMapping("/report-topic")
	public String loadPage() throws Exception {
		return "report-topic";
	}

}
