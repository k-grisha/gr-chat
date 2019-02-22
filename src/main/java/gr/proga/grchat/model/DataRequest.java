package gr.proga.grchat.model;

import gr.proga.grchat.dto.TransferData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataRequest {

	/** Индекс первого запрашиваемого сообщения */
	private final int messageIndex;
	/** Куда сообещния положить */
	private final DeferredResult<List<TransferData<?>>> deferredResult;

}
