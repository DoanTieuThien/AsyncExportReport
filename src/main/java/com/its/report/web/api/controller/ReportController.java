package com.its.report.web.api.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.report.web.api.model.ParameterModel;
import com.its.report.web.api.model.RegisterCommandModel;
import com.its.report.web.api.model.ResponseModel;

@RestController
@RequestMapping("/report")
public class ReportController {
	@Autowired
	@Qualifier("queueRegisterCommand")
	public LinkedBlockingDeque<RegisterCommandModel> queueRegisterCommand = null;
	@Autowired
	@Qualifier("commandManagerMap")
	public ConcurrentHashMap<String, RegisterCommandModel> commandManagerMap = null;

	@PostMapping("/register-export")
	public ResponseModel registerExportReport(@RequestBody RegisterCommandModel registerModel) {
		ResponseModel res = new ResponseModel();

		try {
			String commandName = Optional.ofNullable(registerModel.getCommandName()).orElse("").trim();
			String channelId = Optional.ofNullable(registerModel.getChannelId()).orElse("").trim();
			List<ParameterModel> parameters = registerModel.getParameters();

			if (!"EXPORT-REPORT".equals(commandName)) {
				throw new Exception("commandName Không được hỗ trợ, xin hãy thử lại");
			}
			if ("".equals(channelId)) {
				throw new Exception("channelId không tồn tại, xin hãy thử lại");
			}
			if (parameters == null || parameters.size() == 0) {
				throw new Exception("parameters không được để trống, xin hãy thử lại");
			}

			String reportNameValue = "";
			String reportFromDate = "";
			String reportToDate = "";

			for (ParameterModel param : parameters) {
				String key = Optional.ofNullable(param.getKey()).orElse("").trim();

				if (key.equals("reportName")) {
					reportNameValue = Optional.ofNullable(param.getValue()).orElse("").trim();
				} else if (key.equals("fromDate")) {
					reportFromDate = Optional.ofNullable(param.getValue()).orElse("").trim();
				} else if (key.equals("toDate")) {
					reportToDate = Optional.ofNullable(param.getValue()).orElse("").trim();
				}
			}
			if ("".equals(reportNameValue) || "".equals(reportFromDate) || "".equals(reportToDate)) {
				throw new Exception("Tham số báo cáo Không được để trống, xin hãy thử lại");
			}
			String transactionId = UUID.randomUUID().toString();
			res.setCode("API-00000");
			res.setDes("Bạn đã đăng ký xuất báo cáo " + reportNameValue + " với mã báo cáo " + transactionId
					+ " thành công.");
			registerModel.setStatus("ENQUEUED");
			registerModel.setTransactionId(transactionId);
			commandManagerMap.put(transactionId, registerModel);
			this.queueRegisterCommand.push(registerModel);
		} catch (Exception exp) {
			res.setCode("API-99999");
			res.setDes("Lỗi xử lý: " + exp.getMessage());
		}
		return res;
	}

	@GetMapping("/load-all-report")
	public ResponseModel loadAllReport() {
		ResponseModel res = new ResponseModel();

		try {
			res.setPayload(commandManagerMap.values());
			res.setCode("API-00000");
			res.setDes("SUCCESSED");
		} catch (Exception exp) {
			res.setCode("API-99999");
			res.setDes("Lỗi xử lý: " + exp.getMessage());
		}
		return res;
	}

	@GetMapping(value = "/download-report")
	public void downloadFile(HttpServletResponse response) {
		InputStream inputStream = null;

		try {
			File file = ResourceUtils.getFile("classpath:download/demo.xlsx");
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
			response.setContentLength(data.length);
			inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
