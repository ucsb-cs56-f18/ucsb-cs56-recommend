package hello;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.ui.Model;

import hello.User;
import hello.UserRepository;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	           // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;	//inject an repository instance

	@GetMapping(path="/add") // Map ONLY GET Requests
	public @ResponseBody String addNewUser (@RequestParam String song
			, @RequestParam String artist, @RequestParam String genre) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		User n = new User();
		n.setSongName(song);
		n.setArtist(artist);
		n.setGenre(genre);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}

	@GetMapping(path="/find") 
	public @ResponseBody String findGenre(@RequestParam String song) {
		//userRepository.findSongExist(song);
		return userRepository.findUserBySong(song).getSongName();
	}

	@RequestMapping(path="/webpage")
    public String webpage(Model model) {
        
        String message;
        JSONObject json = new JSONObject();
        json.put("name", "student");

        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", 3);
        item.put("name", "course1");
        array.add(item);

        json.put("course", array);
        message = json.toString();
        
        model.addAttribute("name", message);
        return "index";
    }

    
    @RequestMapping(path="/request")
    public String request(Model model) {
    	/*
    	//getARandomItemFromDatabase
    	int n = (int) (Math.random() * userRepository.count()); //Might be problem when size of userRepository exceeds size of int
    	User u = userRepository.findById(n);
    	String userInfo = u.toString();
    	model.addAttribute("user", userInfo);
    	//runApiFunctionUsingItem^^AsParameter 			(jennifer)
    	//3SongsGetsReturned.NowRunDisplayFTLFile		(shirlyn)
    	return "display";
    	*/
    	model.addAttribute("user", "ryan");
    	return "display";
    }
    
}