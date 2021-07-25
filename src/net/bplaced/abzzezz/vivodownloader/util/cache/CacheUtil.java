package net.bplaced.abzzezz.vivodownloader.util.cache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CacheUtil extends JSONObject {

    public final File cacheConfig;
    private final File cache;
    private final Map<String, Cache> expirationKeyMap = new HashMap<>();

    private final JSONObject cacheConfigMap;

    private final Class<?>[] c;

    public CacheUtil(final File cacheDirectory, Class<?>... c) {
        this.c = c;
        if (!cacheDirectory.exists())
            cacheDirectory.mkdirs();
        this.cache = new File(cacheDirectory, "cache.json");
        this.cacheConfig = new File(cacheDirectory, "cache_config.json");
        this.cacheConfigMap = Util.readFile(cacheConfig).map(json -> {
            try {
                return new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }).orElse(new JSONObject());
    }

    /**
     * Initialises the cache.
     *
     * @return if cache has been initialised
     */
    private boolean initialiseCache() {
        if (!cache.exists()) {
            try {
                cache.createNewFile();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else return true;
    }

    /**
     * Load the cache from disk
     */
    public void loadCache() throws JSONException {
        if (!this.initialiseCache())
            return;

        final JSONArray fileArray = Util.readFile(cache).map(s -> {
            try {
                return new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONArray();
        }).orElse(new JSONArray());
        for (int i = 0; i < fileArray.length(); i++) {
            final JSONObject jsonAt = fileArray.getJSONObject(i);
            cacheObject(
                    jsonAt.getString("key"),
                    jsonAt.get("object"),
                    new Cache(
                            jsonAt.getLong("insert"),
                            jsonAt.getLong("exp")
                    )
            );
        }
        this.reflect();
    }

    /**
     * Writes the cache to disk
     * Creates a new json array with all cached objects, their key and expiration time
     */
    public void flushCache() {
        if (!this.initialiseCache())
            return;

        final JSONArray jsonArray = new JSONArray();
        keys().forEachRemaining(s -> {
            try {
                final Cache cache = expirationKeyMap.get(s);
                final JSONObject cacheObject = new JSONObject()
                        .put("key", s)
                        .put("object", this.get(s))
                        .put("insert", cache.getInsertTime())
                        .put("exp", cache.getExpiration());
                jsonArray.put(cacheObject);
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        });
        Util.writeFile(cache, jsonArray.toString());
    }

    /**
     * Searches for all Cachable reflections
     */
    private void reflect() {
        for (Class<?> aClass : c) {
            for (final Field field : aClass.getFields()) {
                if (field.isAnnotationPresent(Cacheable.class) && field.isAccessible()) {
                    final Cacheable cacheable = field.getAnnotation(Cacheable.class);
                    try {
                        cacheObject(cacheable.key().isEmpty() ? field.getName() : cacheable.key(), field.get(field.getName()), cacheable.expiration());
                    } catch (IllegalAccessException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Goes through all keys and removes every invalid / expired cache
     */
    public void checkCache() {
        this.keys().forEachRemaining(key -> {
            final Optional<Cache> optional = Optional.ofNullable(expirationKeyMap.get(key));
            if (optional.isPresent()) {
                Cache cache = optional.get();
                if (System.currentTimeMillis() - cache.getInsertTime() >= cache.getExpiration()) {
                    this.remove(key);
                    expirationKeyMap.remove(key);
                }
            } else this.remove(key);
        });
    }

    /**
     * Loops through all keys and checks their expiration
     * if the key is expired, the corresponding cache and map entry is deleted
     */
    public Optional<Object> getOptional(final String key) {
        final Optional<Cache> optional = Optional.ofNullable(expirationKeyMap.get(key));
        if (optional.isPresent()) {
            final Cache cache = optional.get();
            if (System.currentTimeMillis() - cache.getInsertTime() >= cache.getExpiration() && cache.getExpiration() != -1) {
                this.remove(key);
                expirationKeyMap.remove(key);
                return Optional.empty();
            }
            return Optional.ofNullable(opt(key));
        }
        return Optional.empty();
    }

    /**
     * Checks if a key is cached
     *
     * @param key key to check
     * @return if objects with given key has already been cached
     */
    public boolean cached(final String key) {
        return this.has(key);
    }

    /**
     * Caches an object with a key and infinite expiration date
     *
     * @param key key
     * @param o   objects to cache
     */
    public void cacheObject(final String key, final Object o) throws JSONException {
        this.put(key, o);
        //No expiration
        this.expirationKeyMap.put(key, new Cache(System.currentTimeMillis(), -1L));
    }

    /**
     * Caches an object with a key and given expiration date
     *
     * @param key        key
     * @param o          objects to cache
     * @param expiration expiration date
     */
    public void cacheObject(final String key, final Object o, final long expiration) throws JSONException {
        this.put(key, o);
        this.expirationKeyMap.put(key, new Cache(System.currentTimeMillis(), expiration));
    }

    /**
     * Caches an object with a key and given expiration date
     *
     * @param key   key
     * @param o     objects to cache
     * @param cache expiration cache
     */
    public void cacheObject(final String key, final Object o, final Cache cache) throws JSONException {
        this.put(key, o);
        this.expirationKeyMap.put(key, cache);
    }

    /**
     * Returns the given object from cache. Warped with an optional for ease of use
     *
     * @param key key to get cached object from
     * @return the corresponding object or an empty optional
     */
    public Optional<Object> getFromCache(final String key) {
        return getOptional(key);
    }

    /**
     * Returns the object or caches the default value and returns it
     *
     * @param key           key to get cache from and store it if necessary
     * @param defaultReturn the default return value if no cache is given
     * @param expiration    expiration deadline
     * @return the object or caches the default value and returns it
     */
    public Object getFromCacheOrCache(final String key, final Object defaultReturn, final long expiration) {
        return getOptional(key).orElseGet(() -> {
            try {
                this.cacheObject(key, defaultReturn, expiration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return defaultReturn;
        });
    }

    /**
     * Returns the object or caches the default value and returns it
     *
     * @param key           key to get cache from and store it if necessary
     * @param defaultReturn the default return value if no cache is given
     * @return the object or caches the default value and returns it
     */
    public Object getFromCacheOrCache(final String key, final Object defaultReturn) {
        return getOptional(key).orElseGet(() -> {
            try {
                this.cacheObject(key, defaultReturn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return defaultReturn;
        });
    }

    /**
     * Returns the whole cache.
     *
     * @return this
     */
    public JSONObject get() {
        return this;
    }

    public JSONObject getCacheConfigMap() {
        return cacheConfigMap;
    }

    public long getFromConfig(final String key) throws JSONException {
        return cacheConfigMap.getLong("key");
    }

    private static class Util {

        public static Optional<String> readFile(final File file) {
            if (!file.exists()) return Optional.empty();
            final StringBuilder builder = new StringBuilder();
            try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String s = builder.toString();

            if (s.isEmpty()) return Optional.empty();
            else
                return Optional.of(s);
        }

        public static void writeFile(final File file, final String string) {
            try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
