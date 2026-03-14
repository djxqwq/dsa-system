package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.util.CaptchaStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @GetMapping("/image")
    public ResultDTO<?> image() throws Exception {
        String code = randomCode(4);
        String captchaId = CaptchaStore.put(code);

        BufferedImage image = render(code);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        Map<String, Object> data = new HashMap<>();
        data.put("captchaId", captchaId);
        data.put("imageBase64", "data:image/png;base64," + base64);
        return ResultDTO.success("获取成功", data);
    }

    private static String randomCode(int len) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static BufferedImage render(String code) {
        int w = 120;
        int h = 40;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(new Color(245, 246, 250));
        g.fillRect(0, 0, w, h);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(rand(50, 160), rand(50, 160), rand(50, 160)));
            int x1 = rand(0, w);
            int y1 = rand(0, h);
            int x2 = rand(0, w);
            int y2 = rand(0, h);
            g.drawLine(x1, y1, x2, y2);
        }

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(rand(20, 120), rand(20, 120), rand(20, 120)));
            int x = 18 + i * 24 + rand(-2, 2);
            int y = 30 + rand(-2, 2);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }

        g.dispose();
        return image;
    }

    private static int rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
