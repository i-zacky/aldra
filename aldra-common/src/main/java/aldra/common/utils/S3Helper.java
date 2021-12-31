package aldra.common.utils;

import aldra.common.framework.exception.ApplicationException;
import aldra.common.settings.AWSSettings;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Helper {

  private final AWSSettings awsSettings;

  private AmazonS3 client() {
    return AmazonS3ClientBuilder.standard() //
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsSettings.getS3().getEndpointUrl(), awsSettings.getS3().getRegion())) //
            .withCredentials(new AWSStaticCredentialsProvider(awsSettings.getCredentials())) //
            .build();
  }

  public String generatePreSignedURL(@NonNull String objectName) {
    return generatePreSignedURL(HttpMethod.GET, awsSettings.getS3().getExpirationSeconds(), awsSettings.getS3().getDataBucket(), objectName);
  }

  public String generatePreSignedURL(@NonNull String bucketName, @NonNull String objectName) {
    return generatePreSignedURL(HttpMethod.GET, awsSettings.getS3().getExpirationSeconds(), bucketName, objectName);
  }

  public String generatePreSignedURL(@NonNull HttpMethod method, @NonNull String objectName) {
    return generatePreSignedURL(method, awsSettings.getS3().getExpirationSeconds(), awsSettings.getS3().getDataBucket(), objectName);
  }

  public String generatePreSignedURL(@NonNull HttpMethod method, @NonNull String bucketName, @NonNull String objectName) {
    return generatePreSignedURL(method, awsSettings.getS3().getExpirationSeconds(), bucketName, objectName);
  }

  public String generatePreSignedURL(@NonNull HttpMethod method, long expiry, @NonNull String bucketName, @NonNull String objectName) {
    val key = Optional.of(objectName) //
            .map(o -> StringUtils.removeStart(o, "/")) //
            .map(o -> StringUtils.removeEnd(o, "/")) //
            .orElseThrow(ApplicationException::newInstance);
    val request = new GeneratePresignedUrlRequest(bucketName, key, method) //
            .withExpiration(Date.from(ZonedDateTime.now().plusSeconds(expiry).toInstant()));
    return client().generatePresignedUrl(request).toString();
  }

  public List<String> listObjectName(String prefix) {
    return listObjectName(awsSettings.getS3().getDataBucket(), prefix);
  }

  public List<String> listObjectName(@NonNull String bucketName, String prefix) {
    return client().listObjects(bucketName, prefix) //
            .getObjectSummaries() //
            .stream() //
            .map(S3ObjectSummary::getKey) //
            .collect(Collectors.toList());
  }

  public byte[] getObject(@NonNull String objectName) {
    return getObject(awsSettings.getS3().getDataBucket(), objectName);
  }

  public byte[] getObject(@NonNull String bucketName, @NonNull String objectName) {
    try {
      return IOUtils.toByteArray(getObjectStream(bucketName, objectName));
    } catch (IOException e) {
      log.error("failed to download from S3.", e);
      throw ApplicationException.withCause(e);
    }
  }

  public InputStream getObjectStream(@NonNull String bucketName, @NonNull String objectName) {
    val key = Optional.of(objectName) //
            .map(o -> StringUtils.removeStart(o, "/")) //
            .map(o -> StringUtils.removeEnd(o, "/")) //
            .orElseThrow(ApplicationException::newInstance);
    return client().getObject(bucketName, key).getObjectContent();
  }

  public void putObject(File file) {
    putObject(file, awsSettings.getS3().getDataBucket(), file.getName());
  }

  public void putObject(File file, @NonNull String objectName) {
    putObject(file, awsSettings.getS3().getDataBucket(), objectName);
  }

  public void putObject(File file, @NonNull String bucketName, @NonNull String objectName) {
    val key = Optional.of(objectName) //
            .map(p -> StringUtils.removeStart(p, "/")) //
            .map(p -> StringUtils.removeEnd(p, "/")) //
            .orElseThrow(ApplicationException::newInstance);
    client().putObject(bucketName, key, file);
  }

  public void deleteObjects(@NonNull List<String> objectNames) {
    deleteObjects(awsSettings.getS3().getDataBucket(), objectNames);
  }

  public void deleteObjects(@NonNull String bucketName, @NonNull List<String> objectNames) {
    val keys = objectNames.stream() //
            .map(o -> StringUtils.removeStart(o, "/")) //
            .map(o -> StringUtils.removeEnd(o, "/")) //
            .map(DeleteObjectsRequest.KeyVersion::new) //
            .collect(Collectors.toList());
    val request = new DeleteObjectsRequest(bucketName).withKeys(keys);
    client().deleteObjects(request);
  }

  public void sync(String sourceBucketName, String sourcePrefix, String destinationBucketName, String destinationPrefix) {
    deleteObjects(destinationBucketName, List.of(destinationPrefix));
    listObjectName(sourceBucketName, sourcePrefix).forEach(sourceKey -> {
      val array = sourceKey.split("/");
      val objectName = array[array.length - 1];
      client().copyObject(sourceBucketName, sourceKey, destinationBucketName, Joiner.on("/").join(destinationPrefix, objectName));
    });
  }
}
