package aldra.api;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EntryPoint {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServerConfiguration.class) //
        .bannerMode(Banner.Mode.OFF) //
        .main(EntryPoint.class) //
        .run(args);
  }
}
