package customer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class CameraController {

    @FXML
    private ImageView cameraView;

    @FXML
    private TextField txtPerson;

    private VideoCapture camera;
    private CascadeClassifier faceDetector;
    private Mat currentFrame = new Mat();
    private int imageCount = 0;
    private ScheduledExecutorService captureService;
    private final int MAX_IMAGES = 20;
    private Rect detectedFace = null;
    private volatile boolean cameraActive = false;
    static {
        OpenCV.loadLocally();
    }

    @FXML
    public void initialize() {

        // Load Haar Cascade
        String xmlPath = new File(
                getClass()
                        .getResource("/haarcascade/haarcascade_frontalface_default.xml")
                        .getFile()
        ).getAbsolutePath();

        faceDetector = new CascadeClassifier(xmlPath);

        if (faceDetector.empty()) {
            System.out.println("Could not load Haar Cascade!");
            return;
        }

        System.out.println("Haar Cascade Loaded Successfully!");

        // Open Webcam
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Cannot open webcam!");
            return;
        }

        System.out.println("Webcam Started");
        cameraActive = true;

        Thread cameraThread = new Thread(() -> {

            Mat frame = new Mat();
            int failCount = 0;

            while (cameraActive && camera.isOpened()) {

                if (!camera.read(frame) || frame.empty()) {
                    failCount++;
                    if (failCount > 50) {
                        System.out.println("Too many failed frame grabs. Stopping camera.");
                        break;
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                    continue;
                }

                failCount = 0; // reset on successful read

                frame.copyTo(currentFrame);

                Mat gray = new Mat();
                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.equalizeHist(gray, gray);

                detectedFace = null;

                MatOfRect faces = new MatOfRect();

                faceDetector.detectMultiScale(
                        gray,
                        faces,
                        1.1,
                        3,
                        0,
                        new Size(100, 100),
                        new Size()
                );

                for (Rect rect : faces.toArray()) {

                    detectedFace = rect;

                    Imgproc.rectangle(
                            frame,
                            rect.tl(),
                            rect.br(),
                            new Scalar(0, 255, 0),
                            3
                    );
                }

                Image image = Utils.mat2Image(frame);

                Platform.runLater(() -> cameraView.setImage(image));

                gray.release();

                // Throttle to ~30 FPS
                try { Thread.sleep(33); } catch (InterruptedException e) { break; }
            }

            frame.release();

        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    @FXML
    public void captureFace(ActionEvent event) {

        if (detectedFace == null) {
            System.out.println("No face detected.");
            return;
        }

        String person = txtPerson.getText().trim();

        if (person.isEmpty()) {
            System.out.println("Enter Customer Mobile.");
            return;
        }

        File folder = new File("FaceDataset" + File.separator + person);

        if (!folder.exists())
            folder.mkdirs();

        imageCount = 0;

        captureService = Executors.newSingleThreadScheduledExecutor();

        captureService.scheduleAtFixedRate(() -> {

            if (imageCount >= MAX_IMAGES) {

                captureService.shutdown();

                try {

                    Connection con = projectdatabaseconnection.doconnecttodb();

                    PreparedStatement pst = con.prepareStatement(
                            "REPLACE INTO face_registration VALUES(?,?,NOW())"
                    );

                    String username = txtPerson.getText().trim();

                    if (username.isEmpty()) {
                        System.out.println("Enter Username.");
                        return;
                    }

                    pst.setString(1, username);
                    pst.setString(2, folder.getAbsolutePath());

                    pst.executeUpdate();

                    pst.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Platform.runLater(() ->
                        System.out.println("Registration Saved Successfully"));
                stopCamera();
                Stage stage = (Stage) cameraView.getScene().getWindow();
                stage.close();
                return;
            }
            if (detectedFace == null)
                return;

            Mat face = new Mat(currentFrame, detectedFace);

            Mat resized = new Mat();

            Imgproc.resize(face, resized, new Size(200,200));

            String filename = folder.getAbsolutePath()
                    + File.separator
                    + (imageCount + 1)
                    + ".jpg";

            Imgcodecs.imwrite(filename, resized);

            imageCount++;

            Platform.runLater(() ->
                    System.out.println("Captured : "
                            + imageCount
                            + "/20"));

            face.release();
            resized.release();

        },0,300, TimeUnit.MILLISECONDS);

    }
    public void setCustomerMobile(String mobile) {
        txtPerson.setText(mobile);
    }

    public void stopCamera() {

        cameraActive = false;

        if (captureService != null && !captureService.isShutdown()) {
            captureService.shutdownNow();
        }

        if (camera != null && camera.isOpened()) {
            camera.release();
        }

        System.out.println("Camera Released");

    }
}