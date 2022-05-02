package aldra.api.usecase;

import aldra.api.adapter.web.controller.GetPreSignedUrlApi;
import aldra.api.adapter.web.dto.GetPreSignedURLResponse;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.S3Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetPreSignedURL implements GetPreSignedUrlApi {

  private final S3Helper s3Helper;

  @Override
  public ResponseEntity<GetPreSignedURLResponse> execute(
      @RequestParam("fileName") String fileName) {
    if (!s3Helper.exist(fileName)) {
      throw ValidationException.withMessage("does not exist file");
    }

    val response =
        new GetPreSignedURLResponse() //
            .url(s3Helper.generatePreSignedURL(fileName));
    return ResponseEntity.ok(response);
  }
}
