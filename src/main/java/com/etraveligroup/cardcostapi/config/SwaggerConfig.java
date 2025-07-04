import com.etraveligroup.cardcostapi.util.AppConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Card Cost API")
                .version(AppConstants.DEFAULT_API_VERSION)
                .description("API for calculating costs associated with payment cards.")
                .contact(new Contact().name("Dimitrios Milios")));
  }
}
