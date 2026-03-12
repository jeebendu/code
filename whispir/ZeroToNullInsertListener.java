package com.config.hibernate;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;

import jakarta.persistence.Id;
import java.lang.reflect.Field;

public class ZeroToNullInsertListener implements PreInsertEventListener {

    @Override
    public boolean onPreInsert(PreInsertEvent event) {

        Object entity = event.getEntity();

        for (Field field : entity.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Id.class)) {

                try {
                    field.setAccessible(true);

                    Object value = field.get(entity);

                    if (value instanceof Number && ((Number) value).longValue() == 0) {
                        field.set(entity, null);
                    }

                } catch (Exception ignored) {
                }
            }
        }

        return false;
    }
}
