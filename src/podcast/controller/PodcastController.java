package podcast.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;


import podcast.model.dao.UploadPodcastDAO;
import podcast.model.javabean.uploadPodcastBean;



@Controller
public class PodcastController {
	//管理頻道
	@RequestMapping(path = "/managePodcast", method = RequestMethod.GET)
	public String showManagePodcast(Model m) {
		return "/PodcastManage/PodcastManage";
	}
	
	//新增頻道
	@RequestMapping(path = "/addPodcast", method = RequestMethod.GET)
	public String showAddForm(Model m) {
		uploadPodcastBean apodcast = new uploadPodcastBean();
		m.addAttribute("uploadPodcastBean", apodcast);
		return "/PodcastManage/AddPodcast";
	}

	//修改頻道
	@RequestMapping(path = "/modifyPodcast", method = RequestMethod.GET)
	public String showmodifyForm(Model m) {
		uploadPodcastBean mpodcast = new uploadPodcastBean();
		m.addAttribute("uploadPodcastBean", mpodcast);
		return "/PodcastManage/ModifyPodcast";
	}
	//接收新增頻道
	@RequestMapping(path = "/addPodcastProcess", method = RequestMethod.POST)
    	public String processPodcast(@RequestParam("podcastpic") MultipartFile multipartFile,@RequestParam("podcastfile") MultipartFile multipartFile2,
    			@RequestParam("category") Integer categoryId,@RequestParam("openComment") Integer openComment,@RequestParam("openPayment") Integer openPayment,
    			HttpServletRequest request,@ModelAttribute("uploadPodcastBean") uploadPodcastBean upload,
    			BindingResult result, Model m) throws Exception {
    		if(result.hasErrors()) {
    			return "/PodcastManage/AddPodcast";
    		}
    	
    	m.addAttribute("podcastId", upload.getPodcastId());
    	m.addAttribute("podcastTitle", upload.getTitle());
    	
    	m.addAttribute("podcastInfo", upload.getPodcastInfo());
    	m.addAttribute("podcastPaymentSetting", upload.getOpenPayment());
    	m.addAttribute("podcastCommentSetting", upload.getOpenComment());

    	String filename= upload.getTitle();
    	String saveaudioPath="C:\\SpringSource\\springworkspace\\PodcastProject2\\WebContent\\audio\\"+filename+".mp3";
    	String savePicPath = "C:\\SpringSource\\springworkspace\\PodcastProject2\\WebContent\\audio\\" + filename+".jpg";
//    	String saveaudioPath = request.getSession().getServletContext().getRealPath("/") + "audio\\" + filename+".mp3";
//    	String savePicPath = request.getSession().getServletContext().getRealPath("/") + "audiopic\\" + filename+".jpg";
    	File saveAudioFile = new File(saveaudioPath);
    	multipartFile2.transferTo(saveAudioFile);
    	File savePicFile = new File(savePicPath);
    	multipartFile.transferTo(savePicFile);
    	
    	
    	
    	upload.setAudioimg(savePicPath);
    	upload.setAudioPath(saveaudioPath);
    	upload.setOpenComment(openComment);
    	upload.setOpenPayment(openPayment);
    	upload.setCategoryId(categoryId);
    	
    	ServletContext app = request.getServletContext();
    	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(app);
    	
    	UploadPodcastDAO upDao = (UploadPodcastDAO)context.getBean("UploadPodcastDAO");

    	upDao.insert(upload);
    	return "/PodcastManage/PodcastManage";
    }
	
	@RequestMapping(path = "/allPodcast", method = RequestMethod.GET)
	public String showActivities(HttpServletRequest request,Model m) throws Exception {
		
		ServletContext app = request.getServletContext();
    	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(app);
    	
    	UploadPodcastDAO upDao = (UploadPodcastDAO)context.getBean("UploadPodcastDAO");
    	List<uploadPodcastBean> list = new LinkedList<uploadPodcastBean>();
    	
    	list = upDao.selectAll();

		m.addAttribute("list", list);
		return "../../index";
		
	}

}