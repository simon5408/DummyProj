/**
 * 
 */
package com.simonsw.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simonsw.base.entity.Users;
import com.simonsw.common.ViewName;
import com.simonsw.common.bean.Page;
import com.simonsw.common.controller.CommonController;
import com.simonsw.common.controller.Module;
import com.simonsw.common.controller.path.ResultPath;

/**
 * @author Simon Lv
 * @since Oct 29, 2013
 */
@Controller
@RequestMapping(ResultPath.user)
public class UsersController extends CommonController {

	@RequestMapping
	public String list(Page page, HttpServletRequest request,
			HttpServletResponse response) {
		userService.findPage(page);
		return forward(ViewName.list);
	}
	
	@RequestMapping(value="/register-new", method=RequestMethod.GET)
	public String register(Model model){
		model.addAttribute("errorMessages", "");
		return forward(ViewName.insert);
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String createUser(Model model, Users user){
		logger.debug("create: user[{}]", user);
		if(userService.getUserByName(user.getUsername()) != null){
			model.addAttribute("errorMessages", "用户名重复");
			return forward(ViewName.insert);
		}
		
		userService.save(user);
		return redirect(ResultPath.user);
	}
	
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public String login(HttpServletRequest request, Model model, Users user) {
		String username = user.getUsername();
		String password = user.getPassword();
		logger.debug("login: username[{}]", username);
		logger.debug("login: password[{}]", password);
		
		if(!userService.isExistUser(user)){
			model.addAttribute("errorMessages", "填写有错");
			return ResultPath.login;
		}
		
		return redirect(ResultPath.user);
	}
	
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable long id, Model model) {
		logger.debug("edit: id[{}]", id);
		model.addAttribute("user", userService.get(id));
		return forward(ViewName.edit);
	}
	
	@RequestMapping(value = "/update/{user.userid}", method = RequestMethod.POST)
	public String update(Users user) {
		userService.saveOrUpdate(user);
		return redirect(ResultPath.user);
	}
	
	@RequestMapping("/destroy/{id}")
	public String destroy(@PathVariable long id) {
		logger.debug("destroy: id[{}]", id);
		userService.delete(id);
		return redirect(ResultPath.user);
	}
	
	/* (non-Javadoc)
	 * @see com.simonsw.common.controller.CommonController#getModule()
	 */
	@Override
	protected String getModule() {
		return Module.example.getName();
	}

}
