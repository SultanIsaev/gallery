package com.gallery.app.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.util.ObjectUtils.isEmpty;

@UtilityClass
public class PathUtil {

    public static final Consumer<HttpHeaders> NO_HEADERS = headers -> {
    };

    public static String build(String path, Object... variables) {
        return UriComponentsBuilder.fromPath(path)
                .build(variables)
                .toString();
    }

    public static LinkedMultiValueMap<String, String> noParams() {
        return new LinkedMultiValueMap<>();
    }

    public static LinkedMultiValueMap<String, String> toParams(Map<String, String> params) {
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        params.forEach((k, v) -> addParam(multiValueMap, k, v));
        return multiValueMap;
    }

    public static void addParam(MultiValueMap<String, String> params, String key, String value) {
        if (!isEmpty(value)) {
            params.put(key, List.of(value));
        }
    }

    public static void addParamAsString(MultiValueMap<String, String> params, String key, Object value) {
        if (!isEmpty(value)) {
            params.put(key, List.of(value.toString()));
        }
    }
}
