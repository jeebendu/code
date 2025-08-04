@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = SanitizingDeserializer.class)
public @interface SanitizeField {
    boolean trim() default false;
    boolean unescape() default false;
}

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

public class SanitizingDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {

    private boolean trim;
    private boolean unescape;

    public SanitizingDeserializer() {
        // default constructor needed for Jackson
    }

    public SanitizingDeserializer(boolean trim, boolean unescape) {
        this.trim = trim;
        this.unescape = unescape;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();

        if (value == null) {
            return null;
        }

        if (unescape) {
            // unescape HTML entities, e.g. &lt; -> <, &amp; -> &
            value = StringEscapeUtils.unescapeHtml4(value);
        }

        if (trim) {
            value = value.trim();
        }

        return value;
    }

    /**
     * This method is called to create a contextual deserializer with access to the field's annotation.
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, Annotated annotated) throws IOException {
        if (annotated != null) {
            SanitizeField ann = null;

            if (annotated instanceof AnnotatedMember) {
                ann = ((AnnotatedMember) annotated).getAnnotation(SanitizeField.class);
            } else {
                ann = annotated.getAnnotation(SanitizeField.class);
            }

            if (ann != null) {
                return new SanitizingDeserializer(ann.trim(), ann.unescape());
            }
        }

        // fallback to default (no trimming or unescaping)
        return new SanitizingDeserializer(false, false);
    }
}


public class User {

    @SanitizeField(trim = true, unescape = true)
    private String email;

    // getters and setters
}
