package com.gallery.app.problem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalizableProblem {

    private final Integer status;
    private final String message;
    @JsonProperty("title-i18n")
    private final String titleI18n;

    public LocalizableProblem(LocalizableException exception) {
        super();
        this.status = exception.getStatus();
        this.message = exception.getMessage();
        this.titleI18n = exception.getTitleI18n();
    }
}
