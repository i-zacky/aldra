package aldra.api.adapter.web.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ExceptionResponseBase {

  ErrorCode error;

  public ExceptionResponseBase(ErrorCode error) {
    this.error = error;
  }
}
