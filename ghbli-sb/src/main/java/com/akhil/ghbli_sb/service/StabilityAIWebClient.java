package com.akhil.ghbli_sb.service;

import com.akhil.ghbli_sb.request.TextToImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StabilityAIWebClient {

    private final WebClient stabilityWebClient;

    public byte[] generateImageFromText(String authorizationHeader, String engineId, TextToImageRequest request) {
        return stabilityWebClient.post()
                .uri("/v1/generation/{engine_id}/text-to-image", engineId)
                .header("Authorization", authorizationHeader)
                .accept(MediaType.IMAGE_PNG)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Stability AI API Error - Status: " + response.statusCode() +
                                                ", Body: " + body))))
                .onStatus(status -> status.is5xxServerError(), response ->
                        Mono.error(new RuntimeException(
                                "Stability AI Server Error - Status: " + response.statusCode())))
                .bodyToMono(byte[].class)
                .block();
    }

    public byte[] generateImageFromImage(
            String authorizationHeader,
            String engineId,
            MultipartFile initImage,
            String textPrompt,
            String stylePreset
    ) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("init_image", initImage.getResource())
                .header("Content-Disposition",
                        "form-data; name=init_image; filename=" + initImage.getOriginalFilename());

        builder.part("text_prompts[0][text]", textPrompt);
        builder.part("text_prompts[0][weight]", "1.0");
        if (stylePreset != null && !stylePreset.isEmpty()) {
            builder.part("style_preset", stylePreset);
        }

        return stabilityWebClient.post()
                .uri("/v1/generation/{engine_id}/image-to-image", engineId)
                .header("Authorization", authorizationHeader)
                .accept(MediaType.IMAGE_PNG)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Stability AI API Error - Status: " + response.statusCode() +
                                                ", Body: " + body))))
                .bodyToMono(byte[].class)
                .block();
    }
}