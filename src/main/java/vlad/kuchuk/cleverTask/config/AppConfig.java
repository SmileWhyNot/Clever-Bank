package vlad.kuchuk.cleverTask.config;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Класс AppConfig используется для получения значений из YAML-файла конфигурации.
 * Он загружает конфигурацию из файла "application.yml" и предоставляет методы для доступа к значениям по ключам.
 */
public class AppConfig {

    /** Имя файла конфигурации. */
    private static final String CONFIG_FILE = "application.yml";

    /** Хранилище свойств из конфигурационного файла. */
    private static final Map<String, String> properties;

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
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
    public static String get(String key) {
        return properties.get(key);
    }
}
