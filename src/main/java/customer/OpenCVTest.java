package customer;

import nu.pattern.OpenCV;
import org.opencv.core.Core;

public class OpenCVTest {

    public static void main(String[] args) {

        OpenCV.loadLocally();

        System.out.println("OpenCV Version : " + Core.VERSION);
        System.out.println("OpenCV Loaded Successfully");
    }
}