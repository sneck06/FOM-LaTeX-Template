package de.telekom.bonicheckprototype.configuration;


import de.telekom.bonicheckprototype.rmcspringbootlibsimpl.startup.ApmInitImpl;
import de.telekom.bonicheckprototype.rmcspringbootlibsimpl.startup.ApplicationInitImpl;
import de.telekom.bonicheckprototype.rmcspringbootlibsimpl.startup.MongoApplicationInitImpl;
import de.telekom.propertyloader.PropertyLoader;
import lombok.extern.slf4j.Slf4j;

/*
For loading System Environment variables before Spring or Test is started
Register Interface implementaions here. You will find and define this in de/telekom/springwebfluxstarter/rmcspringbootlibsimpl
The destination for the loacle config file (configmap.local.properties) and the configfile in the cloud are autoconfigured. 
In case the standard location need to be adjusted, the change is over setter for the PropertyLoader possible.
Example: propertyLoader.setLocalConfigFileLocation("charts/<project>/resources/configmap-local.properties");
 Availeable:
    setApplicationName() // For the Application name read in from the application.yml. Needed for Autoconfiguration config file path.
    setCloudConfigFileLocation() // For the location of the config file in the cloud.    
    setLocalConfigFileLocation() // for the location of the config file local.  
    setApplicationYmlLocation() // for the location of the application.yml. Needed for Autoconfiguration config file path.
 */


@Slf4j
public class ApplicationInit {

    private static final PropertyLoader propertyLoader = new PropertyLoader();

    public static void init() {

        propertyLoader.registerInterfaceImpl(new ApplicationInitImpl());
        propertyLoader.registerInterfaceImpl(new MongoApplicationInitImpl());
        propertyLoader.registerInterfaceImpl(new ApmInitImpl());

        propertyLoader.loadProperties();

    }

    public static PropertyLoader getPropertyLoader() {
        return propertyLoader;
    }
}
