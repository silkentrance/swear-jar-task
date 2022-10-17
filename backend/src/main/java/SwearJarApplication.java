import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "silkentrance.swearjar")
@EnableJpaRepositories
@EntityScan("silkentrance.swearjar")
public class SwearJarApplication {
    public static void main(String... args) {
        SpringApplication.run(SwearJarApplication.class, args);
    }
}
