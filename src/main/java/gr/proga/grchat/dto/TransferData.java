package gr.proga.grchat.dto;

import lombok.AllArgsConstructor;

/**
 * Обертка для всех пересылаемых данных
 * Любые уведомления/приглашения/сообщения и т.д.
 *
 * @param <T> тип сообщения
 */
@AllArgsConstructor
public class TransferData<T> {
	/** id адресата */
	public final String recipientUid;
	public final TransferDataType dataType;
	public final T body;
}
