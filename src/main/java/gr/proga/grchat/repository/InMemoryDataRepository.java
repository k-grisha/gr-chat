package gr.proga.grchat.repository;

import gr.proga.grchat.dto.TransferData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class InMemoryDataRepository implements DataRepository {

	//	private final List<TransferData<?>> transferData = new CopyOnWriteArrayList<>();
	private final Map<String, List<TransferData<?>>> dataStore = new ConcurrentHashMap<>();
	private final List<DataRepositoryListener> listeners = new ArrayList<>();

	@Override
	public List<TransferData<?>> getTransferData(String recipientUid, int messageIndex) {
		if (dataStore.isEmpty() || dataStore.get(recipientUid) == null || dataStore.get(recipientUid).isEmpty()) {
			return Collections.emptyList();
		}
		List<TransferData<?>> transferData = dataStore.get(recipientUid);
		Assert.isTrue((messageIndex >= 0) && (messageIndex <= transferData.size()), "Invalid message index");
		return transferData.subList(messageIndex, transferData.size());
	}

	@Async
	@Override
	public void addTransferData(TransferData<?> transferData) {
		dataStore.putIfAbsent(transferData.recipientUid, new CopyOnWriteArrayList<>());
		dataStore.get(transferData.recipientUid).add(transferData);
		listeners.forEach(listener -> listener.onPost(transferData));
	}

	@Override
	public void registerListener(DataRepositoryListener listener) {
		listeners.add(listener);
	}


}
