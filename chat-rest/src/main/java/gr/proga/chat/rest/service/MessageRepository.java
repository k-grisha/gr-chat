package gr.proga.chat.rest.service;

import gr.proga.grchat.dto.Message;

import java.util.List;


public interface MessageRepository {
	List<Message> getMessages(int messageIndex);

	void addMessage(Message message);

}
