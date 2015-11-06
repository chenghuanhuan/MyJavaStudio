package com.xjj.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xjj.json.JsonResult;

/**
 * 文件上传
 * @author XuJijun
 *
 */
@RestController
@RequestMapping("/servlet/file")
public class FileUploadController {
	
	/**
	 * 保存文件的目录，放在web目录、或一个指定的绝对目录下
	 */
	 private static final String SAVE_DIR = "uploadFiles";
	
	 /**
	  * 
	  * @param request
	  * @param response
	  * @param p form表单中，type="text"的input控件，内容通过这个参数传送过来，以input控件中的name属性来区分
	  * @return JSON表示的处理结果
	  * @throws ServletException
	  * @throws IOException
	  */
	@RequestMapping("/upload")
	public JsonResult upload(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> p)
			throws ServletException, IOException {

		// 获取 web application的绝对路径
		String appPath = request.getServletContext().getRealPath("");
		
		// 构造文件存放的路径
		String savePath = appPath + File.separator + SAVE_DIR;

		// 如果文件存放路径不存在，则mkdir一个
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}

		List<String> fileNames = new ArrayList<>();
		
		//循环所有的part，把part中的文件保存到硬盘中
		for (Part part : request.getParts()) {
			String fileName = part.getSubmittedFileName();
			
			//form表单中的每个input，都在一个不同的part中，
			//所以需要判断通过fileName是否为空，过滤掉其他类型的input（比如type="text"）：
			if(!StringUtils.isEmpty(fileName)){ 
				part.write(savePath + File.separator + fileName);
				fileNames.add(fileName);
			}
		}

		Map<String, Object> resultData = new HashMap<>();
		resultData.put("savePath", savePath);
		resultData.put("files", fileNames);
		
		return new JsonResult("200", "文件上传成功！", resultData);
	}
	 
	/**
	 * 从content-disposition头中获取源文件名
	 * 
	 * content-disposition头的格式如下：
	 * form-data; name="dataFile"; filename="PHOTO.JPG"
	 * 
	 * @param part
	 * @return
	 */
	@SuppressWarnings("unused")
	private String extractFileName(Part part) {
	    String contentDisp = part.getHeader("content-disposition");
	    String[] items = contentDisp.split(";");
	    for (String s : items) {
	        if (s.trim().startsWith("filename")) {
	            return s.substring(s.indexOf("=") + 2, s.length()-1);
	        }
	    }
	    return "";
	}

}
