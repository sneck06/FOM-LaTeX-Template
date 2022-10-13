package de.telekom.bonienrichmentprototype.datatypes.api.rmcspringbootlibsimpl.startup;

import de.telekom.propertyloader.AppInitInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ApmInitImpl implements AppInitInterface {


    @Override
    public void loadLocalConf(Properties prop) {

        System.setProperty("elastic.apm.enabled", prop.getProperty("elastic.apm.enabled", "true"));
        System.setProperty("elastic.apm.server_url", prop.getProperty("elastic.apm.server-url", "http://localhost:8200"));
        System.setProperty("elastic.apm.service_name", prop.getProperty("elastic.apm.service-name", "elastic-apm-spring-boot-integration"));
        System.setProperty("elastic.apm.secret_token", prop.getProperty("elastic.apm.secret-token", "xxVpmQB2HMzCL9PgBHVrnxjNXXw5J7bd79DFm6sjBJR5HPXDhcF8MSb3vv4bpg44"));
        System.setProperty("elastic.apm.environment", prop.getProperty("elastic.apm.environment", "dev"));
        System.setProperty("elastic.apm.application_packages", prop.getProperty("elastic.apm.application-packages", "de.telekom.bonienrichmentprototype"));
        System.setProperty("elastic.apm.log_level", prop.getProperty("elastic.apm.log-level", "DEBUG"));
        System.setProperty("elastic.apm.cloud_provider",prop.getProperty("elastic.apm.cloud_provider","NONE"));

        System.setProperty("elastic.apm.use_elastic_traceparent_header", prop.getProperty("elastic.apm.use_elastic_traceparent_header", "true"));
        System.setProperty("elastic.apm.hostname",  "local");
        System.setProperty("elastic.apm.service_node_name", "Locale_Node_Docker");
        System.setProperty("elastic.apm.recording",prop.getProperty("elastic.apm.recording","true"));
        System.setProperty("elastic.apm.instrument",prop.getProperty("elastic.apm.instrument","true"));

    }

    @Override
    public void loadCloudConf(Properties prop) {

        System.setProperty("elastic.apm.enabled", prop.getProperty("elastic.apm.enabled", "true"));
        System.setProperty("elastic.apm.server_url", prop.getProperty("elastic.apm.server-url"));
        System.setProperty("elastic.apm.service_name", prop.getProperty("elastic.apm.service-name"));
        System.setProperty("elastic.apm.secret_token", prop.getProperty("elastic.apm.secret-token"));
        System.setProperty("elastic.apm.environment", prop.getProperty("elastic.apm.environment"));
        System.setProperty("elastic.apm.application_packages", prop.getProperty("elastic.apm.application-packages"));
        System.setProperty("elastic.apm.log_level", prop.getProperty("elastic.apm.log-level"));
        System.setProperty("elastic.apm.cloud_provider",prop.getProperty("elastic.apm.cloud_provider","AUTO"));

        System.setProperty("elastic.apm.use_elastic_traceparent_header", prop.getProperty("elastic.apm.use_elastic_traceparent_header", "true"));
        System.setProperty("elastic.apm.hostname", System.getenv().get("HOSTNAME"));
        System.setProperty("elastic.apm.service_node_name", System.getenv().get("HOSTNAME"));
        System.setProperty("elastic.apm.recording",prop.getProperty("elastic.apm.recording","true"));
        System.setProperty("elastic.apm.instrument",prop.getProperty("elastic.apm.instrument","true"));
    }


        @Override
        public void writeLogApplicationInitValues() {

            log.info("elastic.apm.enabled: {}", System.getProperty("elastic.apm.enabled"));
            log.info("elastic.apm.recording: {}", System.getProperty("elastic.apm.recording"));
            log.info("elastic.apm.server_url= {}", System.getProperty("elastic.apm.server_url"));
            log.info("elastic.apm.service_name: {}", System.getProperty("elastic.apm.service_name"));
            log.info("elastic.apm.secret_token: {}", System.getProperty("elastic.apm.secret_token"));
            log.info("elastic.apm.environment: {}", System.getProperty("elastic.apm.environment"));
            log.info("elastic.apm.application_packages: {}", System.getProperty("elastic.apm.application_packages"));
            log.info("elastic.apm.log_level: {}", System.getProperty("elastic.apm.log_level"));

            log.info("elastic.apm.use_elastic_traceparent_header: {}", System.getProperty("elastic.apm.use_elastic_traceparent_header"));
            log.info("elastic.apm.hostname: {}", System.getProperty("elastic.apm.hostname"));
            log.info("elastic.apm.service_node_name: {}", System.getProperty("elastic.apm.service_node_name"));
            log.info("elastic.apm.recording: {}", System.getProperty("elastic.apm.recording"));
            log.info("elastic.apm.instrument: {}", System.getProperty("elastic.apm.instrument"));



        }

}

