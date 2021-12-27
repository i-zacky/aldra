package aldra.database.domain.repository.system;

import aldra.database.domain.entity.system.gen.FixedClock;
import aldra.database.domain.repository.system.gen.FixedClockMapperDefault;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FixedClockMapper extends FixedClockMapperDefault {

  Optional<FixedClock> loadBaseTime();
}
