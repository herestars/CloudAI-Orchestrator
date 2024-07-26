package org.stars.ai.service.impl;

import com.alibaba.cloud.ai.tongyi.audio.api.SpeechClient;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stars.ai.service.TongYiService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * CloudAI-Orchestrator.TongYiServiceImpl
 *
 * @author HanZiXin
 * @version 2024/7/19 14:29
 * @apiNote 通义模型服务
 **/
@Service
@Log4j2
public class TongYiServiceImpl implements TongYiService {

    /**
     * 自动注入ChatClient，屏蔽模型调用细节
     */
    private final ChatClient chatClient;

    private final ImageClient imageClient;

    private final SpeechClient speechClient;

    @Autowired
    public TongYiServiceImpl(ChatClient chatClient, ImageClient imageClient, SpeechClient speechClient) {
        this.chatClient = chatClient;
        this.imageClient = imageClient;
        this.speechClient = speechClient;
    }


    /**
     * 基本问答
     *
     * @param message 消息
     */
    @Override
    public String completion(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

    /**
     * 文生图
     *
     * @param imgPrompt 图片描述
     */
    @Override
    public ImageResponse genImg(String imgPrompt) {
        var prompt = new ImagePrompt(imgPrompt);
        return imageClient.call(prompt);

    }

    /**
     * 语音合成
     *
     * @param text 文字
     */
    @Override
    public String genAudio(String text) {
        var resWAV = speechClient.call(text);
        // save的代码省略，就是将音频保存到本地而已
        return save(resWAV, SpeechSynthesisAudioFormat.WAV.getValue());
    }

    private String save(ByteBuffer resWAV, String value) {
        // 设置要保存的文件路径和名称，例如："output.wav"
        String fileName = "/audio/output." + value; // 你可能需要根据具体情况更改这个
        Path filePath = Paths.get(fileName); // 假设当前目录是可写的

        try (FileChannel fileChannel = FileChannel.open(filePath,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            // 确保ByteBuffer是可写的
            resWAV.flip();

            // 将ByteBuffer中的数据写入文件
            while (resWAV.hasRemaining()) {
                int total = fileChannel.write(resWAV);
                log.debug("成功写入{}字节数据", total);
            }
            // 返回保存的文件路径
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            // 处理可能的异常，例如文件无法写入等
            log.error("保存失败：{}", e.getMessage());
            return null; // 或者抛出一个自定义异常
        }
    }
}
