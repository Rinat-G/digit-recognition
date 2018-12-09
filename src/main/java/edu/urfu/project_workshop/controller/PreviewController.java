package edu.urfu.project_workshop.controller;

import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static edu.urfu.project_workshop.common.Constants.LOADED_DIR;

@Controller
public class PreviewController {
    @CrossOrigin
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    public String previewPost(@RequestParam("name") String name) throws IOException, InterruptedException {
        System.out.println("/preview controller got data: " + name);
        File file = new File(LOADED_DIR + name + ".png");
        int i = 0;
        while (!file.exists()) {
            if (i > 50) {
                throw new RuntimeException("Preview file is not available");
            }
            Thread.sleep(100);
            i++;
        }
        val bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
