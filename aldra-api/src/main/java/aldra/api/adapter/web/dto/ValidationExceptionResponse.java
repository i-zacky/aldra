package aldra.api.adapter.web.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ValidationExceptionResponse {

  ErrorCode error;

  List<String> messages;
}
