package aldra.api.adapter.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApplicationExceptionResponse {

  ErrorCode error;

  String message;

  Object data;
}
