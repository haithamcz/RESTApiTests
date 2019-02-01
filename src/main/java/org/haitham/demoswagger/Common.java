package org.haitham.demoswagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Common {

    private static final String REST_SERVICE_HOST = System.getProperty(
            "REST_SERVICE_HOST", "http://localhost:12345"
    );
    private static final String REST_ENDPOINT_VALIDATE = System.getProperty(
            "REST_ENDPOINT_VALIDATE", "/validate"
    );
    private static final String REST_ENDPOINT_VALIDATE_BATCH = System.getProperty(
            "REST_ENDPOINT_VALIDATE_BATCH", "/validateBatch"
    );

    //Cucumber
    private static final String CUCUMBER_PLUGINS = "pretty:STDOUT," + "html:output/test-cucumber.html,"
            + "com.cucumber.listener.ExtentCucumberFormatter:output/report.html,"
            + "json:output/output.json,junit:output/junit_cucumber.xml";
    private static final String CUCUMBER_GLUE = "org/haitham/demoswagger";
    private static final String CUCUMBER_FEATURE_PATH = "features/";


    public static String getRestServiceHost() {
        return REST_SERVICE_HOST;
    }

    public static String getRestEndpointValidate() {
        return REST_SERVICE_HOST + REST_ENDPOINT_VALIDATE;
    }

    public static String getRestEndpointValidateBatch() {
        return REST_SERVICE_HOST + REST_ENDPOINT_VALIDATE_BATCH;
    }

    public static List<String> getCucumberPlugins() {
        List<String> pluginWithSwitch = new ArrayList<String>();
        String cucumberPluginsProperty = CUCUMBER_PLUGINS;
        for (String plugin : Arrays.asList(cucumberPluginsProperty.split("\\s*,\\s*"))) {
            pluginWithSwitch.add("--plugin");
            pluginWithSwitch.add(plugin);
        }
        return pluginWithSwitch;
    }

    public static List<String> getCucumberGlue() {
        return Arrays.asList(CUCUMBER_GLUE.split("\\s*,\\s*"));
    }

    public static List<String> getCucumberFeaturePath() {
        return Arrays.asList(CUCUMBER_FEATURE_PATH.split("\\s*,\\s*"));
    }
}
