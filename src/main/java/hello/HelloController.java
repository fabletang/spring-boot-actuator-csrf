package hello;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping(value="hello")
public class HelloController {
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String sayHello(@RequestBody String helloRequest, Authentication authentication) {
		return "what's up " + authentication.getName() + "?";
	}
	
	@RequestMapping(method = RequestMethod.GET)
    public String getHello(Authentication authentication) {
		System.out.println("what's up " + authentication.getName() + "?");
		return "hello";
	}
}
