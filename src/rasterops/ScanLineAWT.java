package rasterops;

import imagedata.Image;
import imagedata.PolygonData;
import org.jetbrains.annotations.NotNull;
import transforms.Point2D;

import java.util.ArrayList;

import java.util.Comparator;

public class ScanLineAWT<C, P> implements ScanLine<C, P> {

    private ClipperAWT clipper = new ClipperAWT();

    @NotNull
    @Override
    public Image fill(@NotNull Image image, @NotNull PolygonData poly, @NotNull Object value) {
        int minY = 0, maxY = 0;
        //Find out the Ymin and Ymax from the given polygon
        if (poly.getSize() > 0) {
            minY = (int) pointToDisplay(image, poly.findYmin()).getY();
            maxY = (int) pointToDisplay(image, poly.findYmax()).getY();
        }

        final int rowCount = minY - maxY;

        ArrayList<Point2D> intersectLines = new ArrayList<>();

        for (int i = maxY + 1; i < maxY + rowCount - 1; i++) {
            for (int j = 0; j < poly.getSize(); j++) {
                Point2D p1 = pointToDisplay(image, poly.getPoint(j));
                Point2D p2 = pointToDisplay(image, poly.getPoint((j + 1) % poly.getSize()));
                if (isIntersection(i, p1, p2) && !isHorizontal(p1, p2)) {
                    intersectLines.add(clipper.intersect(p1, p2, new Point2D(-1, i), new Point2D(1, i)));
                }
            }
            //sort the intersection points in the increasing order of X coordinate
            intersectLines.sort(Comparator.comparingDouble(Point2D::getX));

            //Fill all pairs of coordinates inside polygon
            for (int j = 0; j < intersectLines.size(); j += 2) {
                if (intersectLines.size() % 2 != 1)
                    image = poly.getLineRenderer()
                                .renderRaster(image,
                                        intersectLines.get(j).getX() + 1,
                                        intersectLines.get(j).getY(),
                                        intersectLines.get((j + 1) % poly.getSize()).getX(),
                                        intersectLines.get((j + 1) % poly.getSize()).getY(), value);
                else
                    System.err.println("Zadejte spravny pocet bodu!!");
            }
            intersectLines.clear();
        }
        return image;
    }


    private Point2D pointToDisplay(@NotNull Image<C> image, @NotNull Point2D point) {
        int x = (int) Math.round((point.getX() + 1) * image.getWidth() / 2);
        int y = (int) Math.round((-point.getY() + 1) * image.getHeight() / 2);
        point = new Point2D(x, y);
        return point;
    }

    private boolean isIntersection(int y, @NotNull Point2D p1, @NotNull Point2D p2) {
        return (y >= p1.getY() && y < p2.getY()) || (y < p1.getY() && y >= p2.getY());
    }

    private boolean isHorizontal(@NotNull Point2D p1, @NotNull Point2D p2) {
        return p1.getY() == p2.getY();
    }
}

