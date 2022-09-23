package com.gallery.app.interfaces;

import com.gallery.app.dto.perfumer.PerfumerDto;
import com.gallery.app.interfaces.request.PerfumerCreateUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("${server.rest.endpoints.perfumers.path}")
public interface PerfumerApi {

    @GetMapping
    ResponseEntity<List<PerfumerDto>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<PerfumerDto> findById(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<PerfumerDto> createPerfumer(@RequestBody PerfumerCreateUpdateRequest request);

    @PutMapping("/{id}")
    ResponseEntity<PerfumerDto> updatePerfumer(@PathVariable Integer id,
                                               @RequestBody PerfumerCreateUpdateRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePerfumerById(@PathVariable Integer id);
}
