package aldra.api.usecase;

import aldra.api.adapter.web.controller.CreateStaffApi;
import aldra.api.adapter.web.dto.CreateStaffRequest;
import aldra.api.adapter.web.dto.CreateStaffResponse;
import aldra.common.utils.CognitoHelper;
import aldra.database.domain.entity.user.gen.Staff;
import aldra.database.domain.repository.user.StaffMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
public class CreateStaff implements CreateStaffApi {

  private final StaffMapper staffMapper;

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<CreateStaffResponse> execute(@RequestBody CreateStaffRequest request) {
    val result = cognitoHelper.createUser(request.getEmail());

    val staff = new Staff();
    staff.setName(result.getUser().getUsername());
    staff.setRoleId(request.getRoleId());
    staffMapper.insertOne(staff);

    val response = new CreateStaffResponse();
    response.setId(staff.getId());
    response.setName(result.getUser().getUsername());
    return ResponseEntity.ok(response);
  }
}
