package dev.trxsson.simpleconfigs.init;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.trxsson.simpleconfigs.Config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Logger;

public class ConfigLoader {

    private final ObjectMapper objectMapper;
    private final Logger logger;

    public ConfigLoader(Logger logger) {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.logger = logger;
    }

    public <A> Config loadConfig(File file, Class<? extends Config> aClass, String configVersion) {
        try {
            if (file.exists()) {
                if (!Objects.equals(objectMapper.readTree(file).get("version").asText(), configVersion)) {
                    File backupFile = new File(new File(file.getParentFile(), "backups"), "backup_" + file.getName());
                    if (backupFile.getParentFile().mkdir() || backupFile.delete()) {
                        logger.info("CLEANED UP PATH FOR " + backupFile.getName());
                    }
                    Files.copy(file.toPath(), backupFile.toPath());
                    logger.info("BACKED UP " + file.getName() + " TO \"" + backupFile.toPath() + "\"");
                    JsonNode oldNode = objectMapper.readTree(file);
                    ObjectNode newNode = objectMapper.createObjectNode();
                    Config instance = aClass.getDeclaredConstructor().newInstance();
                    objectMapper.valueToTree(instance).fields().forEachRemaining(entry -> {
                        newNode.set(entry.getKey(), entry.getValue());
                    });
                    oldNode.fields().forEachRemaining(entry -> {
                        if (newNode.has(entry.getKey())) {
                            newNode.set(entry.getKey(), entry.getValue());
                        }
                    });
                    newNode.put("version", configVersion);
                    logger.info("SUCCESSFULLY UPDATED " + file.getName());
                    Config finalInstance = objectMapper.treeToValue(newNode, aClass);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, finalInstance);
                }
            } else {
                if (file.getParentFile().mkdirs() && file.createNewFile()) {
                    logger.info("Succeeded in creating " + file);
                }
                Config instance = aClass.getDeclaredConstructor().newInstance();
                instance.setConfigVersion(configVersion);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, instance);
            }
            return objectMapper.treeToValue(objectMapper.readTree(file), aClass);
        } catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}