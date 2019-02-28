package gr.proga.grchat.controller;

import gr.proga.grchat.dto.TransferData;
import gr.proga.grchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {
	private final ChatService chatService;

	@Autowired
	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping
	public DeferredResult<List<TransferData<?>>> getMessage(@RequestParam String recipientUid, @RequestParam int messageIndex) {
		// todo мапить в ДТО
		return chatService.getData(recipientUid, messageIndex);
	}


	@PostMapping
	public void postMessage(@RequestBody TransferData transferData) {
		// todo мапить из ДТО
		chatService.postData(transferData);
	}

	@GetMapping(path = "size")
	public int getRequestSize() {

		return chatService.getRequestSize();
	}

}
