package aldra.api.adapter.web.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ValidationExceptionResponse {

  List<String> messages;
}
