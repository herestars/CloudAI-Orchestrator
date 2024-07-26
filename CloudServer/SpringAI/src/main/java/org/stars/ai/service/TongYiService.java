package org.stars.ai.service;

import org.springframework.ai.image.ImageResponse;

/**
 * CloudAI-Orchestrator.TongYiService
 *
 * @author HanZiXin
 * @version 2024/7/18 15:37
 * @apiNote
 **/
public interface TongYiService {

    /**
     * 基本问答
     */
    String completion(String message);

    /**
     * 文生图
     */
    ImageResponse genImg(String imgPrompt);

    /**
     * 语音合成
     */
    String genAudio(String text);

}
