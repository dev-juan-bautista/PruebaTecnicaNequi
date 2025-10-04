package co.com.nequi.validator.engine;

import co.com.nequi.validator.error.ValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidatorEngine {

    private final Validator validator;

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            List<Map<String, String>> errors = violations.stream()
                    .map(v -> {
                        String fieldName = v.getPropertyPath().toString();
                        String jsonProperty = getJsonPropertyName(object.getClass(), fieldName);
                        return Map.of(
                                "campo", jsonProperty,
                                "mensaje", v.getMessage()
                        );
                    })
                    .collect(Collectors.toList());
            throw new ValidationException("Errores de validación en los campos", errors);
        }
    }

    private String getJsonPropertyName(Class<?> clazz, String fieldName) {
        try {
            Field field = ReflectionUtils.findField(clazz, fieldName);
            if (field != null) {
                JsonProperty annotation = field.getAnnotation(JsonProperty.class);
                if (annotation != null && !annotation.value().isEmpty()) {
                    return annotation.value();
                }
            }
        } catch (Exception e) {
            // log o ignorar si no se encuentra
        }
        return fieldName; // fallback al nombre del campo Java
    }

    public void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("El ID es obligatorio",
                    List.of(Map.of("campo", "id", "mensaje", "El campo id es obligatorio")));
        }
    }

}
