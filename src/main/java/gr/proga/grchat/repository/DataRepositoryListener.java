package gr.proga.grchat.repository;

import gr.proga.grchat.dto.TransferData;

public interface DataRepositoryListener {
	void onPost(TransferData transferData);
}
