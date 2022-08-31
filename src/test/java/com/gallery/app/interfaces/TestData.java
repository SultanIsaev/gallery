package com.gallery.app.interfaces;

import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TestData {

    public static final String DEFAULT_FIRST_NAME = "Alberto";
    public static final String DEFAULT_LAST_NAME = "Morillas";
    public static final String DEFAULT_EMAIL = "Alberto_Morillas@gmail.com";
    public static final String DEFAULT_DESCRIPTION = "Good description";

    public static final String DEFAULT_PERFUMER_COUNTRY_CODE = "ITA";
    public static final String DEFAULT_PERFUMER_COUNTRY_NAME = "Italy";

    public static final String DEFAULT_AROMA_NAME = "Montale";
    public static final String DEFAULT_AROMA_COUNTRY_CODE = "FRA";
    public static final String DEFAULT_AROMA_COUNTRY_NAME = "France";


    public static Perfumer.PerfumerBuilder getDefaultPerfumer() {
        return Perfumer.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .description(DEFAULT_DESCRIPTION);
    }

    public static Aroma.AromaBuilder getDefaultAroma() {
        return Aroma.builder()
                .name(DEFAULT_AROMA_NAME)
                .description(DEFAULT_DESCRIPTION);
    }

    public static Country.CountryBuilder getAromaDefaultCountry() {
        return Country.builder()
                .code(DEFAULT_AROMA_COUNTRY_CODE)
                .name(DEFAULT_AROMA_COUNTRY_NAME);
    }

    public static Country.CountryBuilder getPerfumerDefaultCountry() {
        return Country.builder()
                .code(DEFAULT_PERFUMER_COUNTRY_CODE)
                .name(DEFAULT_PERFUMER_COUNTRY_NAME);
    }
}
