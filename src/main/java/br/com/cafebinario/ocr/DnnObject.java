package br.com.cafebinario.ocr;

import org.opencv.core.Point;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class DnnObject {
 
    private final int objectClassId;
    private final String objectName;
    private final Point leftBottom;
    private final Point rightTop;
    private final Point centerCoordinate;

}