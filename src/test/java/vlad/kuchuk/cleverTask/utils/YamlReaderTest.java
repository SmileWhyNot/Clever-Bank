package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class YamlReaderTest {

    @BeforeAll
    public static void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field propertiesField = YamlReader.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        Map<String, String> properties = (Map<String, String>) propertiesField.get(null);

        properties.put("propertyKey", "someValue");
        properties.put("doublePropertyKey", "42.0");
    }

    @Test
    public void testGetString() {
        String expectedValue = "someValue";
        String actualValue = YamlReader.getString("propertyKey");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetStringKeyNotFound() {
        String actualValue = YamlReader.getString("nonExistentKey");
        assertNull(actualValue);
    }

    @Test
    public void testGetDouble() {
        String key = "doublePropertyKey";
        double expectedValue = 42.0;
        double actualValue = YamlReader.getDouble(key);
        assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test
    public void testGetDoubleKeyNotFound() {
        Double actualValue = YamlReader.getDouble("nonExistentKey");
        assertNull(actualValue);
    }
}

