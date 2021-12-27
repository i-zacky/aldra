package aldra.api.framework.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class IgnoreLoggingIntrospector extends JacksonAnnotationIntrospector {

  @Override
  public boolean hasIgnoreMarker(AnnotatedMember m) {
    return super.hasIgnoreMarker(m) || m.hasAnnotation(IgnoreLogging.class);
  }
}
