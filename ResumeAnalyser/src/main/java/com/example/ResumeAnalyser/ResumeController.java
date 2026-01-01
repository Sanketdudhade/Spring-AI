package com.example.ResumeAnalyser;

import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("*")
public class ResumeController {
    private final ChatClient chatClient;

    private  final Tika tika= new Tika();

    public ResumeController(OpenAiChatModel openAiChatModel){
        this.chatClient=ChatClient.create(openAiChatModel);
    }

    @PostMapping("/analyze")
    public Map<String ,Object> analyser(@RequestParam("file") MultipartFile file) throws Exception{
        //extract text
        String content=tika.parseToString(file.getInputStream());

        String prompt= """
                Analyze this resume text :
                %s
                1.Extract key skills
                2.Rate overall Resume quality(0-10)
                3.Suggest 3 improvements
                Reply in structured JSON format.
                """.formatted(content);


        String aiResponse=chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        return Map.of("Analysis",aiResponse);

        
    }
}
