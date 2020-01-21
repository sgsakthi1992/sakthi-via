package com.practice.sakthi_via.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    /**
     * Overridden method.
     *
     * @param target class
     * @param method method
     * @param params parameters
     * @return generated key
     */
    @Override
    public Object generate(final Object target, final Method method,
                           final Object... params) {
        return target.getClass().getSimpleName() + "_" + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
    }
}
