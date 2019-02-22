package gr.proga.grchat.controller;

import gr.proga.grchat.dto.TransferData;
import gr.proga.grchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ChatController {
	private final ChatService chatService;

	@Autowired
	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@RequestMapping(method= RequestMethod.GET)
	@ResponseBody
	public DeferredResult<List<TransferData<?>>> getMessage(@RequestParam String recipientUid, @RequestParam int messageIndex){
		// todo мапить в ДТО
		return chatService.getData(recipientUid, messageIndex);
	}


	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public void postMessage(@RequestParam TransferData transferData) {
		// todo мапить из ДТО
		chatService.postData(transferData);
	}

}
