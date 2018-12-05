package rasterops;

import imagedata.PolygonData;
import org.jetbrains.annotations.NotNull;
import transforms.Point2D;

import java.util.ArrayList;

public class ClipperAWT implements Clipper<Point2D> {

    @Override
    public @NotNull PolygonData<Point2D> clip(@NotNull PolygonData<Point2D> poly, @NotNull PolygonData<Point2D> clipPoly) {
        ArrayList<Point2D> out = poly.getList();

        for (int i = 0; i < clipPoly.getSize(); i++) {

            Point2D c1 = new Point2D(clipPoly.getPoint(i));
            Point2D c2 = new Point2D(clipPoly.getPoint((i + 1) % clipPoly.getSize()));
            Point2D f = new Point2D(clipPoly.getPoint((i + 2) % clipPoly.getSize()));
            double clipInsideEdge = calcHalf(c1, c2, f);

            out = cut(c1, c2, out, clipInsideEdge);
        }
        System.out.println("______________");
        return new PolygonData(out, poly.getColor(), poly.getLineRenderer());
    }


    @NotNull
    @Override
    public Point2D intersect(@NotNull Point2D v1, @NotNull Point2D v2, @NotNull Point2D u1, @NotNull Point2D u2) {
        double c1 = (v1.getX() * v2.getY()) - (v2.getX() * v1.getY());
        double c2 = u1.getX() - u2.getX();
        double c3 = u1.getY() - u2.getY();
        double c4 = ((u1.getX() * u2.getY()) - (u2.getX() * u1.getY()));
        double c5 = v1.getX() - v2.getX();
        double c6 = v1.getY() - v2.getY();

        double x = ((c1 * c2) - (c4 * c5)) / ((c5 * c3) - (c6 * c2));
        double y = ((c1 * c3) - (c4 * c6)) / ((c5 * c3) - (c6 * c2));

        return new Point2D(x, y);
    }


    @NotNull
    private ArrayList<Point2D> cut(@NotNull Point2D c1, @NotNull Point2D c2, @NotNull ArrayList<Point2D> poly, double clipInsideEdge) {
        ArrayList<Point2D> out = new ArrayList<>();
        Point2D last = poly.get(poly.size() - 1);
        for (Point2D v2 : poly) {
            if (!((clipInsideEdge < 0 && calcHalf(c1, c2, v2) > 0) || (clipInsideEdge > 0 && calcHalf(c1, c2, v2) < 0))) {

                if ((clipInsideEdge < 0 && calcHalf(c1, c2, last) > 0) || (clipInsideEdge > 0 && calcHalf(c1, c2, last) < 0)) {
                    out.add(intersect(c1, c2, last, v2));
                }
                out.add(v2);
            } else {
                if (!((clipInsideEdge < 0 && calcHalf(c1, c2, last) > 0) || (clipInsideEdge > 0 && calcHalf(c1, c2, last) < 0))) {
                    out.add(intersect(c1, c2, last, v2));

                }
            }
            last = v2;
        }
        return out;
    }

    private double calcHalf(@NotNull Point2D c1, @NotNull Point2D c2, @NotNull Point2D p) {
        return ((c2.getY() - c1.getY()) * p.getX()) - ((c2.getX() - c1.getX()) * p.getY()) + (c2.getX() * c1.getY()) - (c2.getY() * c1.getX());
    }

}
