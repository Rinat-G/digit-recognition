package edu.urfu.project_workshop.controller;

import lombok.val;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY_INV;

public class TestBoxing {

    @Test
    public void testBoxing() throws IOException {
        File file = new File(TEMP_DIR + "drawn-2018-9-27-19.9.48.png");
        BufferedImage bufferedImage = ImageIO.read(file);
        Frame frame = new Java2DFrameConverter().getFrame(bufferedImage);
        val converter = new OpenCVFrameConverter.ToMat();
        val matImage = converter.convert(frame);
        val monochromeMat = monochrome(matImage);

        val threshold = new opencv_core.Mat();
        opencv_imgproc.threshold(monochromeMat, threshold, 254, 255, THRESH_BINARY_INV);

        val points = new opencv_core.Mat();
        opencv_core.findNonZero(threshold, points);
        val rect = opencv_imgproc.boundingRect(points);

        val resultMat = new opencv_core.Mat(matImage, rect);

        val resultFrame = converter.convert(resultMat);

        val resultImage = new Java2DFrameConverter().getBufferedImage(resultFrame);

        val resultFile = new File(TEMP_DIR + "Cropped1.png");
        ImageIO.write(resultImage, "png", resultFile);

    }

    private opencv_core.Mat monochrome(opencv_core.Mat input) {
        val monoMat = new opencv_core.Mat();
        opencv_imgproc.cvtColor(input, monoMat, CV_BGR2GRAY);
        return monoMat;
    }


}
