package com.example.rstream;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RStreamController {

    @GetMapping("/index")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    @GetMapping("/connectors")
    public String connectors(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "connectors";
    }
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(Message message) throws Exception {
    	return new Message("some","some");
    }
    
    @GetMapping("/kafka")
    
    public String kafka(Message message) throws Exception {
    	return "serverevent";
    }
    
    
   
@GetMapping("/tables")
    
    public String getTables(Message message) throws Exception {
    	return "tables";
    }


}
