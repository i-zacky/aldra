package aldra.batch;

import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EntryPoint {

  public static void main(String[] args) {
    try (val context =
        new SpringApplicationBuilder(ServerConfiguration.class) //
            .bannerMode(Banner.Mode.OFF) //
            .main(EntryPoint.class) //
            .run(args) //
    ) {
      SpringApplication.exit(context);
    }
  }
}
