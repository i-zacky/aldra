package aldra.database.domain.repository.user;

import aldra.database.domain.entity.user.gen.Staff;
import aldra.database.domain.repository.user.gen.StaffMapperDefault;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffMapper extends StaffMapperDefault {

    int insertOne(@Param("entity") Staff staff);
}
