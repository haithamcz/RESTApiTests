package org.haitham.demoswagger;
import org.apache.commons.io.IOUtils;
import java.io.IOException;

/**
 * @author Haitham Jassim
 */
class ResourceFileManager {
    /**
     * This method is used to read json file
     * @param fileRelativeLocation
     * @return
     * @throws IOException
     */
    String readFileFromResource(String fileRelativeLocation) throws IOException {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream("files/" + fileRelativeLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
