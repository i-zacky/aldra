package aldra.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SystemPropertyUtils {

  public static String getTempDirPath() {
    var path = System.getProperty("java.io.tmpdir");
    return StringUtils.removeEnd(path, File.separator);
  }
}
