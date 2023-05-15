import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        final String filePath = "src/resources/config.properties";
        try (Reader reader = new FileReader(filePath)) {
            Properties properties = new Properties();
            properties.load(reader);
            Map<Class<?>, Class<?>> hashMap = buildMapFromProperties(properties, HashMap::new);

            Injector injector = new Injector((HashMap<Class<?>, Class<?>>) hashMap);
            SomeBean someBean = new SomeBean();

            injector.inject(someBean);
            someBean.foo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static Map<Class<?>, Class<?>> buildMapFromProperties(Properties properties, Supplier<Map<Class<?>, Class<?>>> mapSupplier) {
        Map<Class<?>, Class<?>> result = mapSupplier.get();

        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            String value = properties.getProperty(key);
            try {
                result.put(Class.forName(key), Class.forName(value));
            } catch (ClassNotFoundException ex) {
            }
        }
        return result;
    }
}