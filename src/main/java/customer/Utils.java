package customer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class Utils {

    public static Image mat2Image(Mat frame) {

        try {

            org.opencv.core.MatOfByte buffer = new org.opencv.core.MatOfByte();

            Imgcodecs.imencode(".png", frame, buffer);

            BufferedImage img = ImageIO.read(
                    new ByteArrayInputStream(buffer.toArray()));

            return SwingFXUtils.toFXImage(img, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}