package gr.proga.grchat.service;

import gr.proga.grchat.dto.TransferData;
import gr.proga.grchat.model.DataRequest;
import gr.proga.grchat.repository.DataRepository;
import gr.proga.grchat.repository.DataRepositoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService implements DataRepositoryListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

	// Все ожидающие запросы
	private final Map<String, DataRequest> requests = new ConcurrentHashMap<>();

	private final DataRepository dataRepository;

	@Autowired
	public ChatService(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataRepository.registerListener(this);
	}


	/**
	 * Получить данные.
	 * поиск сообщений для recipientUid начиная с messageIndex,
	 * если сообщения найдутся, тут же будут отправлены,
	 * иначе запрос ляжет в requests и будет ждать появления данных или отвалится по таймауту.
	 */
	public synchronized DeferredResult<List<TransferData<?>>> getData(String recipientUid, int messageIndex) {
		final DeferredResult<List<TransferData<?>>> deferredResult = buildDeferredResult(recipientUid);
		List<TransferData<?>> transferDataList = dataRepository.getTransferData(recipientUid, messageIndex);
		if (!transferDataList.isEmpty()) {
			deferredResult.setResult(transferDataList);
		} else {
			DataRequest dataRequest = new DataRequest(recipientUid, messageIndex, deferredResult);
			requests.put(recipientUid, dataRequest);
		}
		return deferredResult;
	}


	/**
	 * Сохранить новое сообщение
	 */
	public void postData(TransferData<?> transferData) {
		dataRepository.addTransferData(transferData);
	}


	/**
	 * Метод будет выполнен при каждом добавлении данных в dataRepository
	 * Еслили получатель нового сообщения есть в ожидающих, ему будут отправлены все что есть
	 */
	@Override
	public void onPost(TransferData transferData) {
		DataRequest dataRequest = requests.get(transferData.recipientUid);
		if (dataRequest == null) {
			return;
		}

		List<TransferData<?>> transferDataList = dataRepository.getTransferData(
				dataRequest.getRecipientUid(),
				dataRequest.getMessageIndex());
		if (!transferDataList.isEmpty()) {
			dataRequest.getDeferredResult().setResult(transferDataList);
		}
	}




	public int getRequestSize() {
		return requests.size();
	}


	private DeferredResult<List<TransferData<?>>> buildDeferredResult(String recipientUid) {
		DeferredResult<List<TransferData<?>>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
		deferredResult.onCompletion(() -> {
			LOGGER.info("onCompletion");
			requests.remove(recipientUid);
		});
		deferredResult.onError(throwable -> {
//			requests.remove(recipientUid);
			LOGGER.warn("Connection onError", throwable);
		});
		deferredResult.onTimeout(() -> {
//			LOGGER.info("onTimeout");
//			requests.remove(recipientUid);
		});
		return deferredResult;
	}


}
