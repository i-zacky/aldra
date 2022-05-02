package aldra.api.framework.jackson;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Please add this annotation to the fields of the request/response to the WebAPI that are not
 * output to the log.<br>
 * Use it for fields that contain personal information.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLogging {}
