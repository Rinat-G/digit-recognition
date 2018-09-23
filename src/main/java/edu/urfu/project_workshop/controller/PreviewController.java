package edu.urfu.project_workshop.controller;

import org.datavec.image.loader.Java2DNativeImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.LargestBlobCropTransform;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class PreviewController {
    @CrossOrigin
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    public String previewPost(@RequestParam("data") String data) throws IOException {

        System.out.println("На /preview контроллер получены данные: " + data);
        String base64Image = data.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform();

        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, largestBlobCropTransform);

        INDArray imageArray = loader.asMatrix(bais);

        Java2DNativeImageLoader java2DNativeImageLoader = new Java2DNativeImageLoader(28, 28, 1);
        BufferedImage bufferedImage = java2DNativeImageLoader.asBufferedImage(imageArray);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(bufferedImage, "png", baos);


        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
