package nitinka.hazelcast.recipes.activemq;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by nitinka on 30/10/14.
 */
public class ObjectMapperUtil {
    private static ObjectMapper instance = new ObjectMapper();

    public static ObjectMapper instance() {
        return instance;
    }

    public static void prettyPrint(Object object) throws IOException {
        System.out.println(instance.defaultPrettyPrintingWriter().writeValueAsString(object));
    }
}
