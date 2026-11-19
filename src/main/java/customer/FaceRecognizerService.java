package customer;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.objdetect.FaceDetectorYN;
import org.opencv.objdetect.FaceRecognizerSF;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.opencv.core.Core.*;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.ArrayList;
import java.util.List;
public class FaceRecognizerService {

    static {
        OpenCV.loadLocally();
    }

    private FaceDetectorYN detector;
    private FaceRecognizerSF recognizer;

    public FaceRecognizerService() throws URISyntaxException {

        URL detectURL = getClass().getResource("/models/face_detection_yunet_2023mar.onnx");
        URL recognizeURL = getClass().getResource("/models/face_recognition_sface_2021dec.onnx");

        File detectFile = new File(detectURL.toURI());
        File recognizeFile = new File(recognizeURL.toURI());

        System.out.println(detectFile.getAbsolutePath());
        System.out.println(recognizeFile.getAbsolutePath());


        System.out.println(getClass().getResource("/models/face_detection_yunet_2023mar.onnx"));
        System.out.println(getClass().getResource("/models/face_recognition_sface_2021dec.onnx"));
        detector = FaceDetectorYN.create(
                detectFile.getAbsolutePath(),
                "",
                new Size(320,320)
        );

        recognizer = FaceRecognizerSF.create(
                recognizeFile.getAbsolutePath(),
                ""
        );

        System.out.println("AI Face Recognition Models Loaded Successfully");
    }

    public FaceDetectorYN getDetector() {
        return detector;
    }

    public FaceRecognizerSF getRecognizer() {
        return recognizer;
    }
    public Mat getFaceEmbedding(Mat frame, Mat face) {

        Mat alignedFace = new Mat();

        recognizer.alignCrop(frame, face, alignedFace);

        Mat feature = new Mat();

        recognizer.feature(alignedFace, feature);

        return feature;
    }
    public double compareFaces(Mat feature1, Mat feature2) {

        return recognizer.match(
                feature1,
                feature2,
                FaceRecognizerSF.FR_COSINE
        );
    }public List<Mat> loadAdminEmbeddings() {

        List<Mat> embeddings = new ArrayList<>();

        File folder = new File("FaceDataset/admin");

        File[] files = folder.listFiles();

        if (files == null)
            return embeddings;

        for (File file : files) {

            Mat img = Imgcodecs.imread(file.getAbsolutePath());

            if (img.empty())
                continue;

            detector.setInputSize(img.size());

            Mat faces = new Mat();

            detector.detect(img, faces);

            if (faces.rows() > 0) {

                Mat face = new Mat(1, 15, CvType.CV_32F);

                faces.row(0).copyTo(face);

                Mat feature = getFaceEmbedding(img, face);

                embeddings.add(feature);
            }
        }

        return embeddings;
    }
}