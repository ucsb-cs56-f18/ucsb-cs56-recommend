package recommend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;


//This class is flagged as a @RestController
//This means the class "HelloController" can be used by Spring MVC to handle web requests
@Controller
public class HelloController {

	//@RequestMapping will map "/" to the index method??
	@RequestMapping("/")
    public String index(Model model) {
    	String message = "hello";

    	model.addAttribute("name", message);
        return "index";
    }

	//HelloController is able to return text thanks to @RestController
	//IF it is invoked by a broswer or through curl in cmd
	//@RestController is actually @Controller and @ResponseBody combined
	//They allow web requests to return data (instead of view??)

}
