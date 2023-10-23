package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YamlReaderTest {

    private static MockedStatic<YamlReader> mockedYaml;

    @BeforeEach
    public void setUpStaticMock() {
        mockedYaml = mockStatic(YamlReader.class);
    }

    @AfterEach
    public void closeStaticMock() {
        if (!mockedYaml.isClosed())
            mockedYaml.close();
    }

    @Test
    @DisplayName("getString")
    void testGetString() {
        when(YamlReader.getString("propertyKey")).thenReturn("someValue");

        String expectedValue = "someValue";
        String actualValue = YamlReader.getString("propertyKey");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("getStringNotFound")
    void testGetStringKeyNotFound() {
        when(YamlReader.getString("nonExistentKey")).thenReturn(null);

        String actualValue = YamlReader.getString("nonExistentKey");
        assertNull(actualValue);
    }

    @Test
    @DisplayName("getDouble")
    void testGetDouble() {
        when(YamlReader.getDouble("doublePropertyKey")).thenReturn(42.0D);

        String key = "doublePropertyKey";
        double expectedValue = 42.0;
        double actualValue = YamlReader.getDouble(key);
        assertEquals(expectedValue, actualValue, 0.001);
        mockedYaml.close();
        assertEquals(0.01, YamlReader.getDouble("interestRate"), 0.001);
        assertNull(YamlReader.getDouble("noValue"));

    }

    @Test
    @DisplayName("getDoubleNotFound")
    void testGetDoubleKeyNotFound() {
        when(YamlReader.getDouble("nonExistentKey")).thenReturn(null);

        Double actualValue = YamlReader.getDouble("nonExistentKey");
        assertNull(actualValue);
    }
}

