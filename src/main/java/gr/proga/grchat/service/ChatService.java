package gr.proga.grchat.service;

import gr.proga.grchat.dto.TransferData;
import gr.proga.grchat.model.DataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

	// Все ожидающие запросы
//	private final Set<DataRequest> requests = new HashSet<>();
	private final Map<String, DataRequest> requests = new HashMap<>();


	private final DataRepository dataRepository;

	@Autowired
	public ChatService(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}


	public DeferredResult<List<TransferData<?>>> getData(String recipientUid, int messageIndex) {
		final DeferredResult<List<TransferData<?>>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
		deferredResult.onCompletion(() -> requests.remove(recipientUid));            // выполниться когда запрос будет обработан
		DataRequest dataRequest = new DataRequest(messageIndex, deferredResult);
		requests.put(recipientUid, dataRequest);
		sendDataTo(recipientUid);
		return deferredResult;
	}


	public void postData(TransferData<?> transferData) {
		dataRepository.addTransferData(transferData);
		sendDataTo(transferData.recipientUid);
	}


	private void sendDataTo(String recipientUid) {
		DataRequest dataRequest = requests.get(recipientUid);
		if (dataRequest != null) {
			List<TransferData<?>> transferDataList = dataRepository.getTransferData(
					recipientUid,
					dataRequest.getMessageIndex());
			dataRequest.getDeferredResult().setResult(transferDataList);
		}
	}


}
