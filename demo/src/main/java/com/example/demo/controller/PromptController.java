//package com.example.demo.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//public class PromptController {
//
//    @Value("${gemini.api.key}")
//    private String apiKey;
//
//    private final WebClient webClient = WebClient.builder()
//            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent")
//            .build();
//
//    @PostMapping("/rewrite")
//    public Mono<Map<String, String>> rewritePrompt(@RequestBody Map<String, String> body) {
//        String input = body.get("prompt");
//        String mode = body.getOrDefault("mode", "rewrite");
//
//        // 🧠 Smarter mode-based system prompts
//        String lowerInput = input.toLowerCase();
//        boolean looksLikeImagePrompt = lowerInput.matches(".*\\b(image|imagine|generate|render|draw|portrait|photo|scene)\\b.*");
//
//        String systemPrompt = switch (mode) {
//            case "shorten" ->
//                    "Shorten the following text while keeping its original meaning intact. Output only the rewritten text.";
//            case "lengthen" -> {
//                if (looksLikeImagePrompt)
//                    yield "Expand this text by adding realistic, meaningful visual details suitable for image generation prompts. Avoid storytelling or long paragraphs.";
//                else
//                    yield "Expand this text naturally by adding informative, relevant, and coherent details. Maintain the same context and tone.";
//            }
//            case "casual" ->
//                    "Rewrite the following text in a simple, natural, and conversational tone. Keep it short and clear.";
//            case "formal" ->
//                    "Rewrite the following text in a professional and formal tone, keeping it concise and precise.";
//            case "imagine" ->
//                    "Refine the following text into a concise, detailed image-generation prompt (for models like DALL·E, Midjourney, or Leonardo). Describe the visual details, lighting, and style — but do not narrate a story.";
//            default ->
//                    "Rewrite this text for better clarity, keeping the tone neutral and concise. Output only the rewritten text.";
//        };
//
//
//        // 🧩 Build request body
//        String requestBody = String.format("""
//        {
//          "contents": [{
//            "parts": [{"text": "%s %s"}]
//          }]
//        }
//        """, systemPrompt, input.replace("\"", "\\\""));
//
//        // 🚀 Send request to Gemini API
//        return webClient.post()
//                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
//                .header("Content-Type", "application/json")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(response -> {
//                    try {
//                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
//                        if (candidates != null && !candidates.isEmpty()) {
//                            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
//                            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
//                            if (parts != null && !parts.isEmpty()) {
//                                String text = (String) parts.get(0).get("text");
//
//                                // 🧹 Clean up unwanted storytelling or headers
//                                String cleanedOutput = text
//                                        .replaceAll("(?s)(?i).*?(?=\\b[A-Z])", "") // remove intro text before first capitalized block
//                                        .replaceAll("(?i)The (Scenario|Image|Composition|Role|Personality).*", "") // remove sections
//                                        .replaceAll("\n{2,}", "\n") // collapse multiple newlines
//                                        .trim();
//
//                                return Map.of("output", cleanedOutput);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return Map.of("output", "No output generated.");
//                })
//                .onErrorResume(e -> Mono.just(Map.of("output", "Error: " + e.getMessage())));
//    }
//}


package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PromptController {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent")
            .build();

    @PostMapping("/rewrite")
    public Mono<Map<String, String>> rewritePrompt(@RequestBody Map<String, String> body) {
        String input = body.get("prompt");
        String mode = body.getOrDefault("mode", "rewrite");

        // Mode-based system prompt
        String systemPrompt = switch (mode) {
            case "shorten" ->
                    "Shorten the following text while keeping its original meaning intact. Output only the rewritten text.";
            case "lengthen" ->
                    "Expand the following prompt naturally by adding meaningful, realistic details that make it suitable for image generation. Avoid storytelling or long paragraphs. Output a single, well-structured image prompt.";
            case "casual" ->
                    "Rewrite the following text in a simple, natural, and conversational tone. Keep it short and clear.";
            case "formal" ->
                    "Rewrite the following text in a professional and formal tone, keeping it concise and precise.";
            case "imagine" ->
                    "Refine the following text into a concise, detailed image-generation prompt (for models like DALL·E, Midjourney, or Leonardo). Describe the visual details, lighting, and style — but do not narrate a story.";
            default ->
                    "Rewrite this text for better clarity, keeping the tone neutral and concise. Output only the rewritten text.";
//            case "shorten" -> "Shorten this text while keeping its meaning:";
//            case "lengthen" -> "Expand this text with more details:";
//            case "casual" -> "Rewrite this text in a casual tone:";
//            case "formal" -> "Rewrite this text in a formal tone:";
//            default -> "Rewrite this text for better clarity:";
        };

        // Build request body
        String requestBody = String.format("""
        {
          "contents": [{
            "parts": [{"text": "%s %s"}]
          }]
        }
        """, systemPrompt, input.replace("\"", "\\\""));

        // Send request to Gemini API
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                        if (candidates != null && !candidates.isEmpty()) {
                            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                            if (parts != null && !parts.isEmpty()) {
                                String text = (String) parts.get(0).get("text");
                                return Map.of("output", text);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Map.of("output", "No output generated.");
                })
                .onErrorResume(e -> Mono.just(Map.of("output", "Error: " + e.getMessage())));
    }
}
