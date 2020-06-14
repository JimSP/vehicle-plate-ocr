package br.com.cafebinario.ocr;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class ImageObjectClassifier {
	
	@Autowired
	@Qualifier("haarcascadeCar")
	public CascadeClassifier haarcascadeCar;
	
	@SneakyThrows
	public List<String> classifier(final InputStream inputStream, final String identifier) {
		final File file = CreateFile.createFile(inputStream, identifier);

		try {
			//final Mat cvt = ToCvtTransform.toCvtMat(file, identifier);
			final Mat origin = Imgcodecs.imread(file.getAbsolutePath());
	        
	        final String cfgFile = "data/yolov3.cfg";
	        final String darknetModel = "data/yolov3.weights";
			final Net net = Dnn.readNetFromDarknet(cfgFile, darknetModel);

			final Size sz = new Size(416, 416);

			final Mat blob = Dnn.blobFromImage(origin, 0.00392, sz, new Scalar(0), true, false);

			net.setInput(blob);
			
			final List<String> outNames = getOutputNames(net);
			final List<Mat> outputBlobs = new ArrayList<>();

			net.forward(outputBlobs, outNames);
			
			final float confThreshold = 0.5f; 
			final List<Integer> clsIds = new ArrayList<>();
			final List<Float> confs = new ArrayList<>();
			final List<Rect> rects = new ArrayList<>();
	        for (int i = 0; i < outputBlobs.size(); ++i) {
	            Mat level = outputBlobs.get(i);
	            for (int j = 0; j < level.rows(); ++j) {
	            	final Mat row = level.row(j);
	            	final Mat scores = row.colRange(5, level.cols());
	                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
	                final float confidence = (float) mm.maxVal;
	                final Point classIdPoint = mm.maxLoc;
	                if (confidence > confThreshold) {
	                	final int centerX = (int) (row.get(0, 0)[0] * origin.cols());
	                	final int centerY = (int) (row.get(0, 1)[0] * origin.rows());
	                	final int width = (int) (row.get(0, 2)[0] * origin.cols());
	                	final int height = (int) (row.get(0, 3)[0] * origin.rows());
	                	final int left = centerX - width / 2;
	                	final int top = centerY - height / 2;

	                    clsIds.add((int) classIdPoint.x);
	                    confs.add((float) confidence);
	                    rects.add(new Rect(left, top, width, height));
	                }
	            }
	        }
	        final float nmsThresh = 0.5f;
	        final MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
	
	        final Rect2d[] boxesArray = new Rect2d[rects.size()];
	        
	        int it = 0;
	        for (Rect rect : rects) {
	        	boxesArray[it++] = new Rect2d(rect.x, rect.y, rect.width, rect.height);
			}
	        
	        final MatOfRect2d boxes = new MatOfRect2d(boxesArray);
	        final MatOfInt indices = new MatOfInt();

	        Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);

	        final int[] ind = indices.toArray();

	        final List<String> names = new ArrayList<>();
	        for (int i = 0; i < ind.length; ++i) {
	        	final int idx = ind[i];
	            System.out.println(clsIds.get(i) + " | " + confs.get(i) + "%" + " | " + idx);
	            names.add(CocoNames.get(idx) + " [" + confs.get(i) + "%]");
	        }
	        
	        return names;
			
		}finally {
			file.delete();
		}
	}
	
	private static List<String> getOutputNames(final Net net) {
        final List<String> names = new ArrayList<>();

        final List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        
        final List<String> layersNames = net.getLayerNames();

        outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));
        
        return names;
    }
}
