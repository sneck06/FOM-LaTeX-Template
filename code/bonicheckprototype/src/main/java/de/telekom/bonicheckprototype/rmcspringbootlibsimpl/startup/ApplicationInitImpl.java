package de.telekom.bonicheckprototype.rmcspringbootlibsimpl.startup;

import de.telekom.propertyloader.AppInitInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Properties;

@Slf4j
public class ApplicationInitImpl implements AppInitInterface {

    /* should be called before Spring is started. Configuration for loading System Environment variables.  */
    static final String CONFIG_LOCATION = "CONFIG_LOCATION";
    static final String SERVER_PORT = "server.port";
    static final String LOGGING_DIR = "loggingDir";
    static final String LOGFILE_BASENAME = "logfileBasename";
    static final String LOGGING_CONFIG = "logging.config";

    public void basicInit() {
        System.setProperty("server.ssl.trust-store", System.getenv().getOrDefault("TRUSTSTORE_PATH", "src/main/resources/keystore/truststore.jks"));
        System.setProperty("server.ssl.trust-store-password", System.getenv().getOrDefault("TRUSTSTORE_PASSWORD", "changeit"));
        System.setProperty("server.ssl.key-store", System.getenv().getOrDefault("KEYSTORE_PATH", "src/main/resources/keystore/keystore.jks"));
        System.setProperty("server.ssl.key-store-password", System.getenv().getOrDefault("KEYSTORE_PASSWORD", "changeit"));
        Locale.setDefault(Locale.ENGLISH);

    }

    @Override
    public void loadLocalConf(Properties prop) {

        basicInit();
        System.setProperty("FILE", prop.getProperty("FILE"));
        System.setProperty(CONFIG_LOCATION, prop.getProperty(CONFIG_LOCATION));
        System.setProperty(SERVER_PORT, prop.getProperty("PORT","8080"));
        System.setProperty(LOGGING_DIR, prop.getProperty(LOGGING_DIR));
        System.setProperty(LOGFILE_BASENAME, "bonicheckprototype");
        System.setProperty(LOGGING_CONFIG, prop.getProperty("logback_location"));
        System.setProperty("OAuth_Issuer_Uri", prop.getProperty("OAuth_Issuer_Uri", ""));
    }

    @Override
    public void loadCloudConf(Properties prop) {

        basicInit();
        System.setProperty("FILE",prop.getProperty ("FILE"));
        System.setProperty(SERVER_PORT, prop.getProperty("PORT"));
        System.setProperty(CONFIG_LOCATION,prop.getProperty (CONFIG_LOCATION));
        System.setProperty(LOGGING_DIR, prop.getProperty(LOGGING_DIR));
        System.setProperty(LOGFILE_BASENAME, System.getenv().get("HOSTNAME"));
        System.setProperty(LOGGING_CONFIG, prop.getProperty("logback_location"));
        System.setProperty("OAuth_Issuer_Uri", prop.getProperty("OAuth_Issuer_Uri", ""));

    }

    @Override
    public void writeLogApplicationInitValues() {

        log.info("Truststore and Keystore are set");
        log.info("keystore-path: {}", System.getProperty("server.ssl.key-store"));
        log.info("truststore-path: {}", System.getProperty("server.ssl.trust-store"));
        log.info("default language is set: " + Locale.ENGLISH);
        log.info("CONFIG_LOCATION={}", System.getProperty(CONFIG_LOCATION));
        log.info("server.port={}", System.getProperty(SERVER_PORT));
        log.info("logging Directory={}", System.getProperty(LOGGING_DIR));
        log.info("logfile Basename={}", System.getProperty(LOGFILE_BASENAME));
        log.info("logback location={}", System.getProperty(LOGGING_CONFIG));
        log.info("OAuth_Issuer_Uri={}", System.getProperty("OAuth_Issuer_Uri"));
    }
}
