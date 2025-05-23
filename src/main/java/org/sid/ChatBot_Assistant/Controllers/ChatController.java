package org.sid.ChatBot_Assistant.Controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        // build a windowed memory that retains only the last 30 messages
        ChatMemory memory = MessageWindowChatMemory.builder()
                .maxMessages(30)
                .build();  // :contentReference[oaicite:0]{index=0}

        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .build();
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

//    @GetMapping("/")
//    public String home(@RequestParam String message) {
//        return chatClient.prompt()
//                .user(message)
//                .call()
//                .content();
//    }
}
