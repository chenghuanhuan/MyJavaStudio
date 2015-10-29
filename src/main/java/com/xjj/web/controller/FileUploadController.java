package com.xjj.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xjj.json.JsonResult;

/**
 * 文件上传
 * @author XuJijun
 *
 */
@Controller
@RequestMapping("/servlet/file")
public class FileUploadController {
	
	/**
	 * 保存文件的目录，放在web目录、或一个指定的绝对目录下
	 */
	 private static final String SAVE_DIR = "uploadFiles";
	
	@RequestMapping("/upload")
	public @ResponseBody JsonResult upload(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> p)
			throws ServletException, IOException {

		// 获取 web application的绝对路径
		String appPath = request.getServletContext().getRealPath("");
		
		// 构造文件存放的路径
		String savePath = appPath + File.separator + SAVE_DIR;

		// 如果文件存放路径不存在，则mkdir一个
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		for (Part part : request.getParts()) {
			String fileName = extractFileName(part);
			if(!StringUtils.isEmpty(fileName)){
				part.write(savePath + File.separator + fileName);
			}
		}

		return new JsonResult("200", "文件上传成功！", savePath);
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
