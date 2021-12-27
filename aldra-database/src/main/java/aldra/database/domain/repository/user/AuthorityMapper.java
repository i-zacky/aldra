package aldra.database.domain.repository.user;

import aldra.database.domain.repository.user.gen.AuthorityMapperDefault;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityMapper extends AuthorityMapperDefault {

  List<String> findPermissionByStaffName(@Param("name") String name);
}
