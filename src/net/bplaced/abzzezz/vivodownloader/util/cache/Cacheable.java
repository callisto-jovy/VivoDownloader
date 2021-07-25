package net.bplaced.abzzezz.vivodownloader.util.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    /**
     * the expiration value in ms
     *
     * @return -1
     */
    long expiration() default -1;

    /**
     * the key to be cached
     *
     * @return the cache key
     */
    String key() default "";
}
