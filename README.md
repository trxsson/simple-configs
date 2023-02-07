# Simple-Configs
A simple config API for Java that creates, loads and updates JSON configs using Jackson by FasterXML.
## Usage + Examples
**1.** Create a config class which is going to store the default values and load the configured values as well, if provided. Make sure to extend the Config class from `dev.trxsson.simpleconfigs`.
```java
public class SystemConfig extends Config {
    public String test_ip = "127.0.0.1";
    public int test_port = 22;
}
```
**2.** Create a variable for the `dev.trxsson.simpleconfigs.init.ConfigLoader`, providing a Logger of your choice.
```java
ConfigLoader configLoader = new ConfigLoader(System.getLogger("test"));
```
**3.** Create a variable or field for the config class and initialize it using the `configLoader.loadConfig()` method. Provide the file which should be automatically read / created, the class type of the config which should be returned by the method and your project's version as a String. Whenever the version changes, the plugin will automatically back up the config and add / remove changed config parameters.
```java
SystemConfig systemConfig = configLoader.loadConfig(new File("config.json"), SystemConfig.class, "1.0.0")
```
