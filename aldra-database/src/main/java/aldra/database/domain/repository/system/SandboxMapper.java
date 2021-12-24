package aldra.database.domain.repository.system;

import aldra.database.domain.entity.system.gen.Sandbox;
import aldra.database.domain.repository.system.gen.SandboxMapperDefault;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SandboxMapper extends SandboxMapperDefault {

  List<Sandbox> findAll();
}
