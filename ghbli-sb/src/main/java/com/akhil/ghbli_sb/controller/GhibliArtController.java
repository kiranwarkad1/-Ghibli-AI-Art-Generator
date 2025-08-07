package com.akhil.ghbli_sb.controller;

import com.akhil.ghbli_sb.request.TextGenerationRequest;
import com.akhil.ghbli_sb.service.GhibliArtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GhibliArtController {

    private final GhibliArtService ghibliArtService;

    @PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateGhibliArt(
            @RequestParam("image") MultipartFile image,
            @RequestParam("prompt") String prompt) {
        try {
            byte[] imageBytes = ghibliArtService.createGhibliArt(image, prompt);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Image generation failed: " + e.getMessage(), e);
        }
    }

    @PostMapping(value = "/generate-from-text", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateGhibliArtFromText(
            @RequestBody TextGenerationRequest requestDTO) {
        try {
            byte[] imageBytes = ghibliArtService.createGhibliArtFromText(
                    requestDTO.getPrompt(),
                    requestDTO.getStyle());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Text-to-image generation failed: " + e.getMessage(), e);
        }
    }
}