package org.stars.ai.controller;

import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stars.ai.service.TongYiService;

/**
 * CloudAI-Orchestrator.TongYiController
 *
 * @author HanZiXin
 * @version 2024/7/19 14:23
 * @apiNote 通义大模型
 **/
@RestController
@RequestMapping("/tongyi")
@CrossOrigin
public class TongYiController {

    private final TongYiService tongYiService;

    @Autowired
    public TongYiController(TongYiService tongYiService) {
        this.tongYiService = tongYiService;
    }

    @GetMapping("/completion")
    public String completion(String message) {
        return tongYiService.completion(message);
    }

    @GetMapping("/genImg")
    public ImageResponse genImg(String message) {
        return tongYiService.genImg(message);
    }

    @GetMapping("/genAudio")
    public String genAudio(String message) {
        return tongYiService.genAudio(message);
    }

}
