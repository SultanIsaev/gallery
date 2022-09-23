package com.gallery.app.interfaces;

import com.gallery.app.dto.perfumer.PerfumerDto;
import com.gallery.app.interfaces.request.PerfumerCreateUpdateRequest;
import com.gallery.app.service.PerfumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PerfumerController implements PerfumerApi {

    private final PerfumerService perfumerService;

    @Override
    public ResponseEntity<List<PerfumerDto>> findAll() {
        return ResponseEntity.ok(perfumerService.findAll());
    }

    public ResponseEntity<PerfumerDto> findById(Integer id) {
        return ResponseEntity.ok(perfumerService.findById(id));
    }

    @Override
    public ResponseEntity<PerfumerDto> createPerfumer(PerfumerCreateUpdateRequest request) {
        PerfumerDto createdPerfumer = perfumerService.createPerfumer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerfumer);
    }

    @Override
    public ResponseEntity<PerfumerDto> updatePerfumer(Integer id, PerfumerCreateUpdateRequest request) {
        return ResponseEntity.ok(perfumerService.updatePerfumer(id, request));
    }

    @Override
    public ResponseEntity<Void> deletePerfumerById(Integer id) {
        perfumerService.deletePerfumer(id);
        return ResponseEntity.noContent().build();
    }
}
