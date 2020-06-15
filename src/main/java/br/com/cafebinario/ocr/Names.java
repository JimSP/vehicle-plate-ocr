package br.com.cafebinario.ocr;

import java.util.Arrays;

public final class Names {
	
	private Names() {
		
	}

	private static final String[] vehiclesTypes = {"bicycle", "car", "motorbike", "bus", "truck"};
	
	private static final String[] objects = {
			"person",
			"bicycle",
			"car",
			"motorbike",
			"aeroplane",
			"bus",
			"train",
			"truck",
			"boat",
			"traffic light",
			"fire hydrant",
			"stop sign",
			"parking meter",
			"bench",
			"bird",
			"cat",
			"dog",
			"horse",
			"sheep",
			"cow",
			"elephant",
			"bear",
			"zebra",
			"giraffe",
			"backpack",
			"umbrella",
			"handbag",
			"tie",
			"suitcase",
			"frisbee",
			"skis",
			"snowboard",
			"sports ball",
			"kite",
			"baseball bat",
			"baseball glove",
			"skateboard",
			"surfboard",
			"tennis racket",
			"bottle",
			"wine glass",
			"cup",
			"fork",
			"knife",
			"spoon",
			"bowl",
			"banana",
			"apple",
			"sandwich",
			"orange",
			"broccoli",
			"carrot",
			"hot dog",
			"pizza",
			"donut",
			"cake",
			"chair",
			"sofa",
			"pottedplant",
			"bed",
			"diningtable",
			"toilet",
			"tvmonitor",
			"laptop",
			"mouse",
			"remote",
			"keyboard",
			"cell phone",
			"microwave",
			"oven",
			"toaster",
			"sink",
			"refrigerator",
			"book",
			"clock",
			"vase",
			"scissors",
			"teddy bear",
			"hair drier",
			"toothbrush"};

	public static String get(final int idx) {
		
		if(idx >= objects.length) {
			return "not found";
		}
		
		return objects[idx];
	}
	
	public static boolean isVehicle(final String name) {
		
		return Arrays
					.asList(vehiclesTypes)
					.stream()
					.filter(predicate->predicate.equals(name))
					.findFirst()
					.map(mapper-> (OcrType) VehicleType.valueOf(mapper))
					.isPresent();
	}
}
