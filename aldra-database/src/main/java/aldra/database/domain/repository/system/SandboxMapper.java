package aldra.database.domain.repository.system;

import aldra.database.domain.entity.system.gen.Sandbox;
import aldra.database.domain.repository.system.gen.SandboxMapperDefault;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SandboxMapper extends SandboxMapperDefault {

  List<Sandbox> findAll();
}
