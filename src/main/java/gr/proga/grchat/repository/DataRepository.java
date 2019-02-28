package gr.proga.grchat.repository;

import gr.proga.grchat.dto.TransferData;

import java.util.List;


public interface DataRepository {
	List<TransferData<?>> getTransferData(String recipientUid, int messageIndex);

	void addTransferData(TransferData<?> transferData);

	void registerListener(DataRepositoryListener listener);
}
