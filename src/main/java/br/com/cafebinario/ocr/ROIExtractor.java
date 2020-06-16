package br.com.cafebinario.ocr;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

@Component
public class ROIExtractor {

	public Mat[] getROI(Mat raw) {
		ArrayList<Rect> pureBoundRectList = getPureBoundRectList(raw);
		Mat view = raw.clone();
		for (Rect pureBoundRect : pureBoundRectList) {
			Imgproc.rectangle(view, pureBoundRect, new Scalar(0, 255, 0), 1, 8, 0);
		}

		Imgcodecs.imwrite("images-temp/roi.png", view);
		
		Imgproc.cvtColor(raw, raw, Imgproc.COLOR_BGR2GRAY);
		Rect[] cutPointRects = getCutPointRects(pureBoundRectList);
		Rect roiRect = getRoiRect(cutPointRects);
		Mat roi = raw.submat(roiRect);
		view = view.submat(roiRect);
		return new Mat[] { roi, view };
	}

	private ArrayList<Rect> getPureBoundRectList(Mat raw) {
		Mat processed = new Mat();
		Imgproc.blur(raw, processed, new Size(2, 2));
		Imgproc.cvtColor(processed, processed, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(processed, processed, 200, 300);
		ArrayList<MatOfPoint> contourList = new ArrayList<>();
		Imgproc.findContours(processed, contourList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		int contourListSize = contourList.size();

		ArrayList<Rect> pureBoundRectList = new ArrayList<>();
		MatOfPoint2f curContourPoly = new MatOfPoint2f();

		double ratio = -1.00;
		for (int i = 0; i < contourListSize; ++i) {
			Imgproc.approxPolyDP(new MatOfPoint2f(contourList.get(i).toArray()), curContourPoly, 1, true);
			Rect rect = Imgproc.boundingRect(curContourPoly);
			
			ratio = (double) rect.height / rect.width;
			if (0.29 <= ratio && ratio <= 3.35) {
				if (1500 <= rect.area() && rect.area() <= 300_000) {
					pureBoundRectList.add(rect);
				}
			}
		}

		return pureBoundRectList;
	}

	private Rect[] getCutPointRects(ArrayList<Rect> pureBoundRectList) {
		pureBoundRectList.sort((prev, next) -> {
			return Double.compare(prev.tl().x, next.tl().x);
		});

		int maxCount = 0;
		double deltaX = 0;
		double deltaY = 0;
		double gradient = 0;
		final double toleranceAreaRatio = 0.25;
		Rect maxCountRect = null;
		Rect endRectOfMaxCountRect = null;
		for (int i = 0; i < pureBoundRectList.size(); ++i) {
			int curCount = 0;
			double curStdArea = pureBoundRectList.get(i).area();
			double tolerance = curStdArea * toleranceAreaRatio;
			double minArea = curStdArea - tolerance;
			double maxArea = curStdArea + tolerance;

			for (int j = i + 1; j < pureBoundRectList.size(); ++j) {
				deltaX = Math.abs(pureBoundRectList.get(j).tl().x - pureBoundRectList.get(i).tl().x);
				deltaY = Math.abs(pureBoundRectList.get(j).tl().y - pureBoundRectList.get(i).tl().y);

				if (deltaX > 150) {
					break;
				}
				
				if (deltaX == 0) {
					deltaX = 1;
				}

				if (deltaY == 0) {
					deltaY = 1;
				}
				
				endRectOfMaxCountRect = pureBoundRectList.get(j);

				gradient = deltaY / deltaX;
				double curArea = pureBoundRectList.get(j).area();
				if (gradient < 0.12 && minArea <= curArea && curArea <= maxArea) {
					++curCount;
					if (maxCount <= curCount) {
						endRectOfMaxCountRect = pureBoundRectList.get(j);
					}
				}
			}

			if (maxCount < curCount) {
				maxCount = curCount;
				maxCountRect = pureBoundRectList.get(i);
			}
		}

		return new Rect[] { maxCountRect, endRectOfMaxCountRect };
	}

	private Rect getRoiRect(Rect[] cutPointRects) {
		Rect startRect = cutPointRects[0];
		Rect endRect = cutPointRects[1];
		double ltlx = startRect.tl().x;
		double ltly = startRect.tl().y;
		double lbry = startRect.br().y;
		double rtly = endRect.tl().y;
		double rbrx = endRect.br().x;
		double rbry = endRect.br().y;
		double widthVariation = 1;
		double heightVariation = 1;

		Point roiStartPoint = null;
		Point roiEndPoint = null;
		if (rtly <= ltly) {
			roiStartPoint = new Point(ltlx - widthVariation, rtly - heightVariation);
			roiEndPoint = new Point(rbrx + widthVariation, lbry + heightVariation);
		} else {
			roiStartPoint = new Point(ltlx - widthVariation, ltly - heightVariation);
			roiEndPoint = new Point(rbrx + widthVariation, rbry + heightVariation);
		}

		return new Rect(roiStartPoint, roiEndPoint);
	}
}