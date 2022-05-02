package aldra.database.domain.repository.user;

import aldra.database.domain.repository.user.gen.AuthorityMapperDefault;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityMapper extends AuthorityMapperDefault {

  List<String> findPermissionByStaffName(@Param("name") String name);
}
