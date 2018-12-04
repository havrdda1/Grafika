package imagedata;

import org.jetbrains.annotations.NotNull;
import rasterops.LineRenderer;
import transforms.Point2D;

import java.util.ArrayList;

public class PolygonData<T> implements ObjectData<T> {

    private ArrayList<Point2D> list;
    private T color;
    private LineRenderer<T> lineRenderer;

    public PolygonData() {
        list = new ArrayList<>();
    }

    public PolygonData(ArrayList<Point2D> list, T color, LineRenderer<T> lineRenderer) {
        this.list = list;
        this.color = color;
        this.lineRenderer = lineRenderer;
    }

    public void addPoint(Point2D point) {
        list.add(point);
    }

    public Point2D getPoint(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }

    public void clearList() {
        list.clear();
    }

    public ArrayList<Point2D> getList() {
        return list;
    }

    public T getColor() {
        return color;
    }

    public void setColor(T color) {
        this.color = color;
    }

    public LineRenderer<T> getLineRenderer() {
        return lineRenderer;
    }

    public void setLineRenderer(LineRenderer<T> lineRenderer) {
        this.lineRenderer = lineRenderer;
    }


    @NotNull
    @Override
    public Image<T> renderElement(@NotNull Image<T> image) {

        for (int i = 0; i < list.size(); i++) {
            Point2D from = list.get(i);
            Point2D to = list.get((i + 1) % list.size());
            image = lineRenderer.render(image, from.getX(), from.getY(), to.getX(), to.getY(), color);
        }
        return image;
    }

    public void removeLastPoint() {
        if (list.size() > 0) {
            list.remove(list.size() - 1);
        }
    }

    public Point2D findYmax() {
        Point2D max = list.get(0);
        for (Point2D p : list) {
            if (max.getY() < p.getY())
                max = p;
        }
        return max;
    }

    public Point2D findYmin() {
        Point2D min = list.get(0);
        for (Point2D p : list) {
            if (min.getY() > p.getY())
                min = p;
        }
        return min;
    }
}
