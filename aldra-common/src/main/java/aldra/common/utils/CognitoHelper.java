package aldra.common.utils;

import aldra.common.settings.AWSSettings;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CognitoHelper {

  private final AWSSettings awsSettings;

  private AWSCognitoIdentityProvider client() {
    return AWSCognitoIdentityProviderClientBuilder.standard() //
            .withRegion(awsSettings.getCognito().getRegion()) //
            .withCredentials(new AWSStaticCredentialsProvider(awsSettings.getCredentials())) //
            .build();
  }

  public AdminInitiateAuthResult login(@NonNull String userName, @NonNull String password) {
    return client().adminInitiateAuth(new AdminInitiateAuthRequest() //
            .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH) //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withClientId(awsSettings.getCognito().getClientId()) //
            .withAuthParameters(Map.of("USERNAME", userName, "PASSWORD", password)));
  }

  public AdminUserGlobalSignOutResult logout(@NonNull String userName) {
    return client().adminUserGlobalSignOut(new AdminUserGlobalSignOutRequest() //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withUsername(userName));
  }

  public AdminInitiateAuthResult refreshToken(@NonNull String token) {
    return client().adminInitiateAuth(new AdminInitiateAuthRequest() //
            .withAuthFlow(AuthFlowType.REFRESH_TOKEN) //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withClientId(awsSettings.getCognito().getClientId()) //
            .withAuthParameters(Map.of("REFRESH_TOKEN", token)));
  }

  public ListUsersResult getUserByEmail(@NonNull String email) {
    val request = new ListUsersRequest() //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withFilter(String.format("email=\"%s\"", email)) //
            .withLimit(5);
    return client().listUsers(request);
  }

  public AdminGetUserResult getUserByUserName(@NonNull String userName) {
    val request = new AdminGetUserRequest() //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withUsername(userName);
    return client().adminGetUser(request);
  }

  public AdminCreateUserResult createUser(@NonNull String email) {
    val emailAttribute = new AttributeType() //
            .withName("email") //
            .withValue(email);
    val emailVerifiedAttribute = new AttributeType() //
            .withName("email_verified") //
            .withValue("true");
    val createUserRequest = new AdminCreateUserRequest() //
            .withUserPoolId(awsSettings.getCognito().getPoolId()) //
            .withUsername(email) //
            .withTemporaryPassword(RandomStringUtils.randomAlphanumeric(16)) //
            .withUserAttributes(emailAttribute, emailVerifiedAttribute);
    return client().adminCreateUser(createUserRequest);
  }
}
