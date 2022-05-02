package aldra.database.domain.repository.system;

import aldra.database.domain.entity.system.gen.FixedClock;
import aldra.database.domain.repository.system.gen.FixedClockMapperDefault;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedClockMapper extends FixedClockMapperDefault {

  Optional<FixedClock> loadBaseTime();
}
