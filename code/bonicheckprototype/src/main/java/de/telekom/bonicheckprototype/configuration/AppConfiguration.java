package de.telekom.bonicheckprototype.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/*
For loading System Environment variables after Spring or Test is started
And Bean Configuration used by the Application.
Reading the variables from the CONFIG_LOCATION (set in ApplicationInit)
 */

@Slf4j
@Configuration
@Data
@PropertySource("file:${CONFIG_LOCATION}")
public class AppConfiguration {

    private final Environment environment;

    public AppConfiguration(Environment environment) {
        this.environment = environment;
    }

    public String oAuth_Issuer_Uri;


    @PostConstruct
    public void appConfigInit() {
        log.info("---------Set Configuration after Spring Boot Start----------");
        oAuth_Issuer_Uri = environment.getProperty("OAuth_Issuer_Uri");

        log.info(this.toString());
        ApplicationInit.getPropertyLoader().printValues();
        log.info("---------Set Configuration after Spring Boot is set----------");

    }

}
