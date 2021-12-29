package aldra.api.usecase;

import aldra.api.adapter.web.controller.UpdateStaffApi;
import aldra.api.adapter.web.dto.UpdateStaffRequest;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.CognitoHelper;
import aldra.database.domain.repository.user.StaffMapper;
import aldra.database.utils.DateTimeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
public class UpdateStaff implements UpdateStaffApi {

  private final StaffMapper staffMapper;

  private final CognitoHelper cognitoHelper;

  @PreAuthorize("hasAuthority('UpdateStaff')")
  @Override
  public ResponseEntity<Void> execute(@RequestBody UpdateStaffRequest request) {
    val staff = staffMapper.findById(request.getId()) //
            .orElseThrow(() -> ValidationException.withMessage("not found staff"));

    // update cognito user pool
    try {
      if (request.getEnable()) {
        cognitoHelper.enableUser(staff.getName());
      } else {
        cognitoHelper.disableUser(staff.getName());
      }
    } catch (Exception e) {
      log.error("failed to update cognito user pool.", e);
      throw ApplicationException.withMessage("failed to update cognito user pool");
    }

    // update staff table
    staff.setRoleId(request.getRoleId());
    staff.setUpdatedAt(DateTimeHelper.now());
    staffMapper.update(staff);

    return ResponseEntity.ok().build();
  }
}
