package com.its.report.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TableController extends AbstractController {

	@Override
	@RequestMapping("/tables")
	public String loadPage() throws Exception {
		return "tables";
	}

}
