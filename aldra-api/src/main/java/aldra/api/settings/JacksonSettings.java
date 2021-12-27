package aldra.api.settings;

import aldra.api.framework.jackson.IgnoreLoggingIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class JacksonSettings {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.JAPAN);

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);

  @Primary
  @Bean
  public ObjectMapper objectMapper() {
    return configureCommonSettings();
  }

  @Bean(name = "loggingObjectMapper")
  public ObjectMapper loggingObjectMapper() {
    val mapper = configureCommonSettings();
    mapper.setAnnotationIntrospector(new IgnoreLoggingIntrospector());
    return mapper;
  }

  private ObjectMapper configureCommonSettings() {
    return new Jackson2ObjectMapperBuilder() //
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE) //
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) //
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //
            .featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL) //
            .modules(new JavaTimeModule(), new Jdk8Module()) //
            .timeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Tokyo"))) //
            .serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMAT)) //
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMAT)) //
            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT)) //
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMAT)) //
            .locale(Locale.JAPAN) //
            .build();
  }
}
