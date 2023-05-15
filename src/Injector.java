import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public final class Injector {
    private final HashMap<Class<?>, Class<?>> _injectorRules;
    public Injector(HashMap<Class<?>, Class<?>> injectorRules) throws IllegalArgumentException {
        _injectorRules = injectorRules;
    }

    public <T> T inject(T obj) throws IllegalArgumentException, InstantiationException, IllegalAccessException {

        List<Field> fieldsWithAnnotation = getFieldsWithAnnotation(obj, AutoInjectable.class);

        for (Field field : fieldsWithAnnotation) {
            Class<?> mappedValue = _injectorRules.get(field.getGenericType());

            if (mappedValue == null)
                continue;

            Object initedInjectable = mappedValue.newInstance();

            field.setAccessible(true);
            field.set(obj, initedInjectable);
        }

        return obj;
    }

    private static List<Field> getFieldsWithAnnotation(Object obj, Class<? extends Annotation> annotation) {
        List<Field> result = new ArrayList<Field>();
        Class<?> clas = obj.getClass();

        while (clas != null)
        {
            for (Field field : clas.getDeclaredFields())
                if (field.isAnnotationPresent(annotation))
                    result.add(field);

            clas = clas.getSuperclass();
        }

        return result;
    }
}
