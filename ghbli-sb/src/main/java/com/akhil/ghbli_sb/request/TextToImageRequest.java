package com.akhil.ghbli_sb.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextToImageRequest {

    // List of text prompts (descriptions of the image to generate)
    private List<TextPrompt> text_prompts;

    // Controls how closely the image should match the text (higher = more accurate)
    private double cfg_scale = 7;

    // Height of the generated image (in pixels)
    private int height = 512;

    // Width of the generated image (in pixels)
    private int width=768;

    // Number of images to generate (1 = single image, 2 or more = multiple versions)
    private int samples = 1;

    // How many steps the model should take to make the image (more = better quality but slower)
    private int steps = 30;

    // Style of the image like "realistic", "anime", "fantasy-art", etc.
    private String style_preset;

    @Getter
    @Setter
    public static class TextPrompt {
        // The actual text describing what image to generate
        private String text;

        public TextPrompt(String text) {
            this.text = text;
        }
    }

    // Constructor to quickly create a request with just text and style
    public TextToImageRequest(String text, String style) {
        this.text_prompts = List.of(new TextPrompt(text)); // Creates a list with one prompt
        this.style_preset = style;
    }
}
