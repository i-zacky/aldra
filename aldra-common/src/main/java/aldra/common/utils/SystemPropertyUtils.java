package aldra.common.utils;

import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SystemPropertyUtils {

  public static String getTempDirPath() {
    var path = System.getProperty("java.io.tmpdir");
    return StringUtils.removeEnd(path, File.separator);
  }
}
