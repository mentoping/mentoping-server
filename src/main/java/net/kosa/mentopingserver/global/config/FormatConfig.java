package net.kosa.mentopingserver.global.config;

import net.kosa.mentopingserver.global.common.converter.StringToCategoryConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FormatConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToCategoryConverter());
    }
}