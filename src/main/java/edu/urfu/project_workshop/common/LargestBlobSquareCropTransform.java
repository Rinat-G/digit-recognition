package edu.urfu.project_workshop.common;

import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.data.ImageWritable;
import org.datavec.image.transform.BaseImageTransform;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Random;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class LargestBlobSquareCropTransform extends BaseImageTransform<Mat> {
    protected org.nd4j.linalg.api.rng.Random rng;


    protected int mode, method, blurWidth, blurHeight, upperThresh, lowerThresh;
    protected boolean isCanny;

    private int x;
    private int y;

    public LargestBlobSquareCropTransform() {
        this(null);
    }

    public LargestBlobSquareCropTransform(Random random) {
        this(random, CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, 3, 3, 100, 200, true);
    }

    public LargestBlobSquareCropTransform(Random random, int mode, int method, int blurWidth, int blurHeight, int lowerThresh,
                                          int upperThresh, boolean isCanny) {
        super(random);
        this.rng = Nd4j.getRandom();
        this.mode = mode;
        this.method = method;
        this.blurWidth = blurWidth;
        this.blurHeight = blurHeight;
        this.lowerThresh = lowerThresh;
        this.upperThresh = upperThresh;
        this.isCanny = isCanny;
        this.converter = new OpenCVFrameConverter.ToMat();
    }

    @Override
    protected ImageWritable doTransform(ImageWritable image, Random random) {
        if (image == null) {
            return null;
        }

        //Convert image to gray and blur
        Mat original = converter.convert(image.getFrame());
        Mat grayed = new Mat();
        cvtColor(original, grayed, CV_BGR2GRAY);
        if (blurWidth > 0 && blurHeight > 0)
            blur(grayed, grayed, new Size(blurWidth, blurHeight));

        //Get edges from Canny edge detector
        Mat edgeOut = new Mat();
        if (isCanny)
            Canny(grayed, edgeOut, lowerThresh, upperThresh);
        else
            threshold(grayed, edgeOut, lowerThresh, upperThresh, 0);

        double largestArea = 0;
        Rect boundingRect = new Rect();
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();

        findContours(edgeOut, contours, hierarchy, this.mode, this.method);

        for (int i = 0; i < contours.size(); i++) {
            //  Find the area of contour
            double area = contourArea(contours.get(i), false);

            if (area > largestArea) {
                // Find the bounding rectangle for biggest contour
                boundingRect = boundingRect(contours.get(i));
            }
        }

        //Apply crop and return result
        x = boundingRect.x();
        y = boundingRect.y();
        Mat result = new Mat(original, boundingRect);

        return new ImageWritable(converter.convert(result));
    }
}
