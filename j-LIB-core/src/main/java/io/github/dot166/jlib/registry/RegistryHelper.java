package io.github.dot166.jlib.registry;

import static io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class RegistryHelper {

    public static List<Object> getFromRegistry(Context context) {
        return readXmlFromFile(context, "Registry.xml");
    }

    public static class Object {
        private String stationName;
        private String stationUrl;
        private String stationLogoUrl;

        public Object(Map<String, String> attributes) {
            stationName = attributes.get("objectName");
            stationUrl = attributes.get("objectUrl");
            stationLogoUrl = attributes.get("objectLogoUrl");
        }

        public String getLogoUrl() {
            return stationLogoUrl;
        }

        public String getName() {
            return stationName;
        }

        public String getUrl() {
            return stationUrl;
        }

        public void updateAttributes(Map<String, String> attributes) {
            stationName = attributes.get("objectName");
            stationUrl = attributes.get("objectUrl");
            stationLogoUrl = attributes.get("objectLogoUrl");
        }
    }
}
