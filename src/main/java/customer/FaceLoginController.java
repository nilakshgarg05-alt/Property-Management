package customer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.objdetect.FaceDetectorYN;
import org.opencv.videoio.VideoCapture;
import java.util.List;
import java.net.URISyntaxException;

public class FaceLoginController {
    private FaceDetectorYN detector;
    private Mat faces = new Mat();
    private boolean loginDone = false;
    private List<Mat> adminEmbeddings;
    @FXML
    private ImageView cameraView;

    private VideoCapture camera;

    private FaceRecognizerService ai;

    static {
        OpenCV.loadLocally();
    }

    @FXML
    public void initialize() throws URISyntaxException {

        ai = new FaceRecognizerService();
        detector = ai.getDetector();

        camera = new VideoCapture(0);

        if(!camera.isOpened()){
            System.out.println("Camera Not Found");
            return;
        }

        startCamera();
        adminEmbeddings = ai.loadAdminEmbeddings();

        System.out.println("Registered Faces : " + adminEmbeddings.size());
    }

    private void startCamera() {

        Thread thread = new Thread(() -> {

            Mat frame = new Mat();

            while (camera.isOpened() && !loginDone) {

                if (!camera.read(frame) || frame.empty())
                    continue;

                detector.setInputSize(frame.size());

                faces.release();
                faces = new Mat();
                System.out.println("Frame Size : " + frame.size());
                System.out.println("Frame Type : " + frame.type());
                System.out.println("Frame Empty : " + frame.empty());
                detector.detect(frame, faces);

                if (!faces.empty() && !loginDone) {

                    Mat face = new Mat(1, 15, CvType.CV_32F);

                    faces.row(0).copyTo(face);

                    Mat feature = ai.getFaceEmbedding(frame, face);

                    boolean matched = false;

                    for (Mat admin : adminEmbeddings) {

                        double score = ai.compareFaces(feature, admin);

                        System.out.println("Similarity : " + score);

                        if (score > 0.36) {

                            matched = true;
                            break;
                        }
                    }

                    if (matched && !loginDone) {

                        loginDone = true;

                        Platform.runLater(() -> {

                            try {

                                camera.release();

                                FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource("/customerview/dashboardview.fxml"));

                                Parent root = loader.load();

                                Stage stage = new Stage();

                                stage.setScene(new Scene(root));

                                stage.show();

                                Stage current = (Stage) cameraView.getScene().getWindow();

                                current.close();

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        });

                        break;

                    } else {

                        System.out.println("FACE NOT MATCHED");

                    }
                    System.out.println("Feature Generated : " + feature.cols());

                    loginDone = true;
                }

                Image image = Utils.mat2Image(frame);

                Platform.runLater(() ->
                        cameraView.setImage(image));
            }

            frame.release();

        });

        thread.setDaemon(true);
        thread.start();
    }

}