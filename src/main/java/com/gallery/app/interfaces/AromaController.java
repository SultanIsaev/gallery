package com.gallery.app.interfaces;

import com.gallery.app.dto.aroma.AromaDto;
import com.gallery.app.interfaces.request.AromaCreateUpdateRequest;
import com.gallery.app.service.AromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AromaController implements AromaApi {

    private final AromaService aromaService;

    @Override
    public ResponseEntity<AromaDto> findById(Integer id) {
        return ResponseEntity.ok(aromaService.findById(id));
    }

    @Override
    public ResponseEntity<List<AromaDto>> findAll() {
        return ResponseEntity.ok(aromaService.findAll());
    }

    @Override
    public ResponseEntity<AromaDto> createAroma(AromaCreateUpdateRequest request) {
        AromaDto createdAroma = aromaService.createAroma(request);
        return ResponseEntity.status(CREATED).body(createdAroma);
    }

    @Override
    public ResponseEntity<AromaDto> updateAroma(Integer id, AromaCreateUpdateRequest request) {
        return ResponseEntity.ok(aromaService.updateAroma(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteAromaById(Integer id) {
        aromaService.deleteAroma(id);
        return ResponseEntity.noContent().build();
    }
}
