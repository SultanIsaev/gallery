package com.gallery.app.interfaces.request;

import com.gallery.app.model.Country;
import com.gallery.app.model.Perfumer;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class PerfumerCreateUpdateRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String description;

    private Short countryId;

    /**
     * defines if update request has changes for existing perfumer
     *
     * @param request existing perfumer
     * @return false if no changes provided, otherwise return true
     */
    public boolean hasChangesForUpdate(Perfumer request) {
        boolean isEqual = Objects.equals(firstName, request.getFirstName()) &&
                Objects.equals(lastName, request.getLastName()) &&
                Objects.equals(email, request.getEmail()) &&
                Objects.equals(description, request.getDescription());

        Country requestCountry = request.getCountry();
        if (requestCountry != null) {
            isEqual = isEqual && Objects.equals(countryId, requestCountry.getId());
        }

        return !isEqual;
    }
}
