package aldra.api.adapter.web.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
@Builder
public class ExceptionResponseBase {

  ErrorCode error;
}
