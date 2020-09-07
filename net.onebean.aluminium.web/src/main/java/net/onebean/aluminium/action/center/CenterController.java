package net.onebean.aluminium.action.center;

import net.onebean.aluminium.security.SpringSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CenterController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());


	@RequestMapping({"/index","/center",""})
	public String view(Model model) {
		model.addAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
		return "center/view";
	}
}
