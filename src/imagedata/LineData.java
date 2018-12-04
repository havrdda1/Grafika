package imagedata;

import org.jetbrains.annotations.NotNull;
import rasterops.LineRenderer;

public class LineData<T> implements ObjectData<T> {
    private final double fromX, fromY, toX, toY;
    private final LineRenderer<T> lineRenderer;
    private final T color;

    public LineData(LineRenderer<T> lineRenderer, double fromX, double fromY, double toX, double toY, T color) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.lineRenderer = lineRenderer;
        this.color = color;
    }

    @NotNull
    @Override
    public Image<T> renderElement(@NotNull Image<T> image) {
        image = lineRenderer.render(image, fromX, fromY, toX, toY, color);
        return image;
    }
}
