package mr.demonid.service.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Переадресация запросов на ресурсы, в данном случае на картинки.
 * Путь к ресурсу в SecurityConfig должен быть выставлен только для чтения!!!
 * Например: .requestMatchers(HttpMethod.GET, "/pk8000/api/catalog/**").permitAll()
 */
@Configuration
public class ImageWebConfig implements WebMvcConfigurer {

    @Value("${app.images-path}")
    private String imagesPath;

    @Value("${app.images-url}")
    private String imagesUrl;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(imagesUrl + "/**")
                .addResourceLocations("file:" + imagesPath + "/");
    }

}