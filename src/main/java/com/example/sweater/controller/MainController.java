package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filterText,
                       @RequestParam(required = false, defaultValue = "") String filterTag,
                       Model model) {
        model.addAttribute("messages", getMessages(filterText, filterTag));
        model.addAttribute("filterText", filterText);
        model.addAttribute("filterTag", filterTag);
        return "main";
    }

    private Iterable<Message> getMessages(String filterText, String filterTag) {
        Iterable<Message> messages;
        boolean isEmptyFilterText = isEmpty(filterText),
                isEmptyFilterTag = isEmpty(filterTag);
        if (isEmptyFilterText && isEmptyFilterTag) {
            messages = messageRepo.findAll();
        } else if (!isEmptyFilterText && isEmptyFilterTag) {
            messages = messageRepo.findByTextOrderById(filterText);
        } else if (isEmptyFilterText) {
            messages = messageRepo.findByTagOrderById(filterTag);
        } else {
            messages = messageRepo.findByTextAndTagOrderById(filterText, filterTag);
        }
        return messages;
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam("file") MultipartFile file) throws IOException {

        Message message = new Message(text, tag, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }

        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }
}