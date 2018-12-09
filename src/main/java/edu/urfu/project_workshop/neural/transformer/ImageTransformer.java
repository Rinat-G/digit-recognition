package edu.urfu.project_workshop.neural.transformer;

import edu.urfu.project_workshop.model.service.ImagePersistService;
import lombok.val;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.data.ImageWritable;
import org.datavec.image.transform.BoxImageTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.BOXED_DIR;
import static edu.urfu.project_workshop.common.Constants.CROPPED_DIR;
import static java.lang.Math.round;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY_INV;

@Component
public class ImageTransformer {
    private ImagePersistService persistService;

    @Autowired
    public ImageTransformer(final ImagePersistService persistService) {
        this.persistService = persistService;
    }

    public File transform(final File inputFile) {

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Frame srcFrame = new Java2DFrameConverter().getFrame(bufferedImage);
        val croppedFrame = cropImage(srcFrame);
        val boxedFrame = boxImage(croppedFrame);
        persistService.persistFrameToFile(croppedFrame, inputFile.getName(), CROPPED_DIR);

        return persistService.persistFrameToFile(boxedFrame, inputFile.getName(), BOXED_DIR);
    }

    private Frame cropImage(final Frame input) {

        val converter = new OpenCVFrameConverter.ToMat();
        val matImage = converter.convert(input);
        val monochromeMat = monochrome(matImage);

        val threshold = threshold(monochromeMat);

        val points = new opencv_core.Mat();
        opencv_core.findNonZero(threshold, points);

        val rect = opencv_imgproc.boundingRect(points);

        val resultMat = new opencv_core.Mat(matImage, rect);

        try {
            return converter.convert(resultMat);
        } catch (NullPointerException e) {
            throw new RuntimeException("Пустое изображение!");
        }
    }

    private Frame boxImage(final Frame input) {
        val imageWritable = new ImageWritable(input);
        int maxDimension = imageWritable.getHeight() > imageWritable.getWidth()
                ? imageWritable.getHeight()
                : imageWritable.getWidth();

        int width = (int) round(maxDimension + (maxDimension * 0.5));
        int height = (int) round(maxDimension + (maxDimension * 0.5));

        val imageBoxed = new BoxImageTransform(width, height)
                .borderValue(new opencv_core.Scalar(255.0, 255.0, 255.0, 255.0))
                .transform(imageWritable);
        return imageBoxed.getFrame();
    }

    private opencv_core.Mat monochrome(final opencv_core.Mat input) {
        val monoMat = new opencv_core.Mat();
        opencv_imgproc.cvtColor(input, monoMat, CV_BGR2GRAY);
        return monoMat;
    }

    private opencv_core.Mat threshold(final opencv_core.Mat input) {
        val threshold = new opencv_core.Mat();
        opencv_imgproc.threshold(input, threshold, 254, 255, THRESH_BINARY_INV);
        return threshold;
    }
}
