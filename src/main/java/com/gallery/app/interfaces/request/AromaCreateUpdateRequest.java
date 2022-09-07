package com.gallery.app.interfaces.request;

import com.gallery.app.model.Aroma;
import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class AromaCreateUpdateRequest {

    private Integer perfumerId;

    private Short countryId;

    private String name;

    private String description;

    public boolean hasChangesForUpdate(Aroma request) {
        boolean isEqual = Objects.equals(name, request.getName()) &&
                Objects.equals(description, request.getDescription());

        Country actualCountry = request.getCountry();
        if (actualCountry != null) {
            isEqual = isEqual && Objects.equals(countryId, actualCountry.getId());
        }
        Perfumer actualPerfumer = request.getPerfumer();
        if (actualPerfumer != null) {
            isEqual = isEqual && Objects.equals(perfumerId, actualPerfumer.getId());
        }

        return !isEqual;
    }
}
