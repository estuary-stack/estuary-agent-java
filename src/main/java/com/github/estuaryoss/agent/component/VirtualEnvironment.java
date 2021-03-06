package com.github.estuaryoss.agent.component;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class VirtualEnvironment {
    private static final Logger log = LoggerFactory.getLogger(VirtualEnvironment.class);
    private static final String EXT_ENV_VAR_PATH = "environment.properties";
    private final ImmutableMap<String, String> environment = ImmutableMap.copyOf(System.getenv());
    private final Map<String, String> virtualEnvironment = new LinkedHashMap<>();

    public static final int VIRTUAL_ENVIRONMENT_MAX_SIZE = 100;

    public VirtualEnvironment() {
        this.setExtraEnvVarsFromFile();
    }

    private void setExtraEnvVarsFromFile() {

        try (InputStream fileInputStream = new FileInputStream(Paths.get(".", EXT_ENV_VAR_PATH).toFile())) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            virtualEnvironment.putAll(properties.entrySet()
                    .stream()
                    .filter(elem -> !environment.containsKey(elem.getKey()))
                    .collect(Collectors.toMap(elem -> elem.getKey().toString(),
                            elem -> elem.getValue().toString())));
        } catch (Exception e) {
            log.debug(ExceptionUtils.getStackTrace(e));
        }

        log.debug("External env vars read from file '" + EXT_ENV_VAR_PATH + "' are: " + new JSONObject(virtualEnvironment).toString());
    }

    public boolean setExternalEnvVar(String key, String value) {
        if (environment.containsKey(key)) return false;

        if (virtualEnvironment.containsKey(key)) {
            virtualEnvironment.put(key, value);
            return true;
        }
        if (virtualEnvironment.size() <= VIRTUAL_ENVIRONMENT_MAX_SIZE) {
            virtualEnvironment.put(key, value);
            return true;
        }

        return false;
    }

    public Map<String, String> setExternalEnvVars(Map<String, String> envVars) {
        Map<String, String> addedEnvVars = new LinkedHashMap<>();

        envVars.forEach((key, value) -> {
            if (this.setExternalEnvVar(key, value)) addedEnvVars.put(key, value);
        });

        return addedEnvVars;
    }

    /**
     * Gets the immutable environment variables from the System
     *
     * @return Map containing initial immutable env vars plus virtual env vars set by the user
     */
    public Map<String, String> getEnvAndVirtualEnv() {
        Map<String, String> systemAndExternalEnvVars = new LinkedHashMap<>();
        systemAndExternalEnvVars.putAll(environment);

        virtualEnvironment.forEach((key, value) -> {
            if (!systemAndExternalEnvVars.containsKey(key)) systemAndExternalEnvVars.put(key, value);
        });

        return systemAndExternalEnvVars;
    }

    /**
     * Gets the immutable environment variables from the System
     *
     * @return Map containing initial immutable env vars
     */
    public Map<String, String> getEnv() {
        return environment;
    }

    /**
     * Gets the virtual environment variables
     *
     * @return Map containing mutable env vars set by the user
     */
    public Map<String, String> getVirtualEnv() {
        return virtualEnvironment;
    }

    /**
     * Deletes all the custom env vars contained in the virtual environment
     */
    public void cleanVirtualEnv() {
        virtualEnvironment.clear();
    }
}
