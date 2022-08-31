package com.gallery.app.interfaces;

import com.gallery.app.dto.aroma.AromaDto;
import com.gallery.app.interfaces.request.AromaCreateUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("${server.rest.endpoints.aromas.path}")
public interface AromaApi {

    @GetMapping("/{id}")
    ResponseEntity<AromaDto> findById(@PathVariable Integer id);

    @GetMapping
    ResponseEntity<List<AromaDto>> findAll();

    @PostMapping
    ResponseEntity<AromaDto> createAroma(@RequestBody AromaCreateUpdateRequest request);

    @PutMapping("/{id}")
    ResponseEntity<AromaDto> updateAroma(@PathVariable Integer id,
                                         @RequestBody AromaCreateUpdateRequest aromaDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAromaById(@PathVariable Integer id);
}
