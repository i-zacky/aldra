package aldra.api.usecase;

import aldra.api.adapter.web.controller.GetSandboxApi;
import aldra.api.adapter.web.dto.CreateSandboxResponse;
import aldra.api.adapter.web.dto.CustomerStatus;
import aldra.database.domain.repository.system.SandboxMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetSandbox implements GetSandboxApi {

  private final SandboxMapper sandboxMapper;

  @Override
  public ResponseEntity<List<CreateSandboxResponse>> execute() {
    val result = sandboxMapper.findAll() //
            .stream() //
            .map(e -> new CreateSandboxResponse() //
                    .id(e.getId()) //
                    .firstName(e.getFirstName()) //
                    .lastName(e.getLastName()) //
                    .email(e.getEmail()) //
                    .birthday(e.getBirthday()) //
                    .customerStatus(CustomerStatus.valueOf(e.getCustomerStatus())) //
            ) //
            .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }
}
