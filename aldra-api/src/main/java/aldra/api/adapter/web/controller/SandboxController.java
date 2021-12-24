package aldra.api.adapter.web.controller;

import aldra.api.ServerConfiguration;
import aldra.database.domain.repository.system.SandboxMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ServerConfiguration.VERSION)
public class SandboxController {

  private final SandboxMapper sandboxMapper;

  @GetMapping("/sandbox")
  public void getSandbox() {
    val list = sandboxMapper.findAll();
    log.info("sandbox: {}rows", list.size());
  }
}
