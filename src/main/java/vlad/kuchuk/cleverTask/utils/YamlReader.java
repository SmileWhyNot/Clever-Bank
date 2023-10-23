package vlad.kuchuk.cleverTask.utils;

import java.io.InputStream;
import java.util.Map;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;

/**
 * Класс YamlReader используется для получения значений из YAML-файла конфигурации.
 * Он загружает значения из файла "application.yml" и предоставляет методы для доступа к значениям по ключам.
 */
@UtilityClass
public class YamlReader {

    /** Имя файла конфигурации. */
    private static final String CONFIG_FILE = "application.yml";

    /** Хранилище свойств из конфигурационного файла. */
    private static final Map<String, String> properties;

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = YamlReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.yml", e);
        }
    }

    /**
     * Получает значение из конфигурации по указанному ключу.
     *
     * @param key Ключ, по которому нужно получить значение.
     * @return Значение из конфигурации или null, если ключ не найден.
     */
    public static String getString(String key) {
        return properties.get(key);
    }

    /**
     * Получает значение из конфигурации по указанному ключу и преобразует его в тип {@code Double}.
     *
     * @param key Ключ, по которому нужно получить значение.
     * @return Значение из конфигурации преобразованное в тип {@code Double}, или null, если ключ не найден или значение не может быть преобразовано в {@code Double}.
     * @throws NumberFormatException Если значение в конфигурации не может быть корректно преобразовано в тип {@code Double}.
     */
    public static Double getDouble(String key) {
        String value = properties.get(key);
        return value != null ? Double.parseDouble(value) : null;
    }
}
