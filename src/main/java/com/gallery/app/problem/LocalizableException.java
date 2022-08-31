package com.gallery.app.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LocalizableException extends RuntimeException {

    private final Integer status;
    private final String message;
    @JsonProperty("title-i18n")
    private final String titleI18n;

    @JsonCreator
    public LocalizableException(@JsonProperty("status") Integer status,
                                @JsonProperty("message") String message,
                                @JsonProperty("title-i18n") String titleI18n) {
        super();
        this.status = status;
        this.message = message;
        this.titleI18n = titleI18n;
    }

    public static LocalizableException of(Integer status,
                                          String message,
                                          String titleI18n) {
        return new LocalizableException(status, message, titleI18n);
    }

    public static LocalizableException of(Integer status,
                                          List<String> messages,
                                          String titleI18n) {
        return new LocalizableException(status, String.join(",", messages), titleI18n);
    }
}
