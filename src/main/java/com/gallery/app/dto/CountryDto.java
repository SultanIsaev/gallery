package com.gallery.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDto {

    private String code;

    private String name;
}
