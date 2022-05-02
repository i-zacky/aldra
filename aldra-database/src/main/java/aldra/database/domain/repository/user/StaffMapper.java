package aldra.database.domain.repository.user;

import aldra.database.domain.entity.user.gen.Staff;
import aldra.database.domain.repository.user.gen.StaffMapperDefault;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffMapper extends StaffMapperDefault {

  Optional<Staff> findById(@Param("id") Integer id);

  int insertOne(@Param("entity") Staff staff);

  int update(@Param("entity") Staff staff);
}
