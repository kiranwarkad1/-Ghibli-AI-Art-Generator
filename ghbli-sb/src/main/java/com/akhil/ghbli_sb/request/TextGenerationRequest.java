package com.akhil.ghbli_sb.request;

import lombok.Data;

@Data
public class TextGenerationRequest {
    private String prompt;
    private String style;
}
