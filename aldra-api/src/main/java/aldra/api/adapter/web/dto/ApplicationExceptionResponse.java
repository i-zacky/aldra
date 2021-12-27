package aldra.api.adapter.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApplicationExceptionResponse {

  String message;

  Object data;
}
