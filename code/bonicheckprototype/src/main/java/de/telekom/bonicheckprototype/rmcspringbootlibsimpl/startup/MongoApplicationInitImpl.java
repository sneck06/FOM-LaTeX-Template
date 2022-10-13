package de.telekom.bonicheckprototype.rmcspringbootlibsimpl.startup;

import de.telekom.propertyloader.AppInitInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class MongoApplicationInitImpl implements AppInitInterface {

    String mongoPW = "mongoPW";

    @Override
    public void loadLocalConf(Properties prop) {

        String stage = System.getenv().getOrDefault("CI_JOB_STAGE","EMPTY");

        String mongoDbLocalUser = null;
        String MongoPWLocal = null;

        // The MongoDbUri will be read from configmap-<environment>.properties. For Pipeline: in case the CI_JOB_STAGE is set to "Test" the connection string for the MongoDB in the Pipeline will be used.
        if (stage.equalsIgnoreCase("test")) {
            System.setProperty("spring.data.mongodb.uri","mongodb://mongo:27017");
            log.info("Stage is: {}", stage);
        } else {
            setMongoDBUri(mongoDbLocalUser,MongoPWLocal,prop.getProperty("MongoDbUri"));;
            log.info("Stage is: local");
        }

        System.setProperty("spring.data.mongodb.database",prop.getProperty("MongoDatabase","SP"));
        System.setProperty("MongoHousekeeping",prop.getProperty("MongoHousekeeping","1"));
        System.setProperty("MongoCertsActive",prop.getProperty("MongoCertsActive","false"));
        System.setProperty("MongoDevController",prop.getProperty("MongoDevController","true"));

        System.setProperty("MongoDbUser", prop.getProperty("MongoDbUser","notSet"));
    }

    @Override
    public void loadCloudConf(Properties prop) {

        setMongoDBUri(prop);
        System.setProperty("MongoDbUser", prop.getProperty("MongoDbUser"));
        System.setProperty("spring.data.mongodb.database",prop.getProperty("MongoDatabase"));
        System.setProperty("MongoHousekeeping", prop.getProperty("MongoHousekeeping"));
        System.setProperty("MongoCertsActive",prop.getProperty("MongoCertsActive","true"));
        System.setProperty("MongoDevController",prop.getProperty("MongoDevController", "false"));

    }

    @Override
    public void writeLogApplicationInitValues() {

        log.info("MONGO_URI= {}", System.getProperty("spring.data.mongodb.uri").replace(mongoPW, "******"));
        log.info("spring.data.mongodb.database: {}", System.getProperty("spring.data.mongodb.database"));
        log.info("MongoDbUser: {}", System.getProperty("MongoDbUser"));
        log.info("MongoHousekeeping: {}", System.getProperty("MongoHousekeeping"));
        log.info("MongoCertsActive: {}", System.getProperty("MongoCertsActive"));
        log.info("MongoDevController: {}", System.getProperty("MongoDevController"));

        if(System.getProperty("MongoDevController").equals("true"))
        {
            log.warn("!!! TestController is ACTIVE, NON Prod Property is set testcontoller: {} !!!", System.getProperty("MongoDevController"));
        }
    }

    // Method to set up Uri with Username and Password
    private void setMongoDBUri(Properties prop) {
        if (prop.getProperty("MongoDbUser") != null)
        {
            System.setProperty("spring.data.mongodb.uri", "mongodb://"+ prop.getProperty("MongoDbUser") +":"+System.getenv("MongoDbPw")+"@"+ prop.getProperty("MongoDbUri"));
            mongoPW = System.getenv("MongoDbPw");
            System.out.println(mongoPW);
        }else
        {
            System.setProperty("spring.data.mongodb.uri", "mongodb://" + prop.getProperty("MongoDbUri"));
        }
    }

    private void setMongoDBUri(String user, String password, String uri) {
        if (user != null)
        {
            System.setProperty("spring.data.mongodb.uri", "mongodb://"+ user +":"+ password +"@"+ uri);
            mongoPW = password;
        }else
        {
            System.setProperty("spring.data.mongodb.uri", "mongodb://" + uri );
        }
    }

}
