package edu.urfu.project_workshop.model.service;

import lombok.val;
import org.apache.commons.io.IOUtils;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.datavec.image.loader.Java2DNativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.RECEIVED_DIR;

@Service
public class ImagePersistService {

    public File persistBase64ToFile(String data, String name) {

        val filePath = RECEIVED_DIR + name + ".png";
        createDirIfNeed(RECEIVED_DIR);
        String base64Image = data.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {

            IOUtils.copy(bis, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Получен файл: " + filePath);
        return new File(filePath);
    }

    public File persistFrameToFile(Frame data, String name, String path) {
        val bufferedImage = new Java2DFrameConverter().getBufferedImage(data);
        return writeBufferedImage(bufferedImage, name, path);
    }

    public void persistNDArrayToFile(INDArray indArray, String name, String path) {
        val bufferedImage = new Java2DNativeImageLoader().asBufferedImage(indArray);
        writeBufferedImage(bufferedImage, name, path);
    }

    private void createDirIfNeed(String dir) {
        File dirFile = new File(dir);

        if (!dirFile.exists() && !dirFile.mkdirs()) {
            throw new RuntimeException("Can't create directory" + dir);
        }

    }

    private File writeBufferedImage(BufferedImage bufferedImage, String name, String path) {
        createDirIfNeed(path);
        val resultFile = new File(path + name);
        try {
            ImageIO.write(bufferedImage, "png", resultFile);
        } catch (IOException e) {
            throw new RuntimeException("Невозможно записать файл" + resultFile.getPath(), e);
        }
        return resultFile;
    }
}
