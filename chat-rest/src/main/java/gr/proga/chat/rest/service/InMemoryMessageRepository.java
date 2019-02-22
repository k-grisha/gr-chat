package gr.proga.chat.rest.service;

import gr.proga.grchat.dto.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InMemoryMessageRepository implements MessageRepository {

	private final List<Message> messages = new CopyOnWriteArrayList<>();

	@Override
	public List<Message> getMessages(int messageIndex) {
		if (this.messages.isEmpty()) {
			return Collections.emptyList();
		}
		Assert.isTrue((messageIndex >= 0) && (messageIndex <= this.messages.size()), "Invalid message index");
		return messages.subList(messageIndex, this.messages.size());
	}

	@Override
	public void addMessage(Message message) {
		messages.add(message);
	}
}
