package com.akhil.ghbli_sb.service;

import com.akhil.ghbli_sb.request.TextToImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GhibliArtService {

    private final StabilityAIWebClient stabilityAIClient;

    @Value("${stability.api.key}")
    private String apiKey;

    public byte[] createGhibliArt(MultipartFile image, String prompt) throws IOException {
        String finalPrompt = prompt + ", in the beautiful, detailed anime style of studio ghibli.";
        String engineId = "stable-diffusion-v1-6";
        String stylePreset = "anime";

        return stabilityAIClient.generateImageFromImage(
                "Bearer " + apiKey,
                engineId,
                image,
                finalPrompt,
                stylePreset
        );
    }

    public byte[] createGhibliArtFromText(String prompt, String style) {
        String finalPrompt = prompt + ", in the beautiful, detailed anime style of studio ghibli.";
        String engineId = "stable-diffusion-v1-6";
        String stylePreset = style.equals("general") ? "anime" : style.replace("_", "-");

        // Using your existing TextToImageRequest structure
        TextToImageRequest request = new TextToImageRequest();
        request.setText_prompts(List.of(new TextToImageRequest.TextPrompt(finalPrompt)));
        request.setStyle_preset(stylePreset);
        // Your default values from the class will be used for other parameters

        return stabilityAIClient.generateImageFromText(
                "Bearer " + apiKey,
                engineId,
                request
        );
    }
}