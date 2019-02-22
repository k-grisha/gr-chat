package gr.proga.grchat.service;

import gr.proga.grchat.dto.TransferData;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InMemoryDataRepository implements DataRepository {

	private final List<TransferData<?>> transferData = new CopyOnWriteArrayList<>();

	@Override
	public List<TransferData<?>> getTransferData(String recipientUid, int messageIndex) {
		if (transferData.isEmpty()) {
			return Collections.emptyList();
		}
		Assert.isTrue((messageIndex >= 0) && (messageIndex <= this.transferData.size()), "Invalid message index");
		return transferData.subList(messageIndex, this.transferData.size());
	}

	@Override
	public void addTransferData(TransferData<?> transferData) {
		this.transferData.add(transferData);
	}
}
