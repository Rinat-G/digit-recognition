package edu.urfu.project_workshop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    MultipartConfigElement multipartConfigElement(){
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize("1MB");
//        factory.setMaxRequestSize("1MB");
//        return factory.createMultipartConfig();
//    }


}
