package rasterops;

import imagedata.PolygonData;
import org.jetbrains.annotations.NotNull;

public interface Clipper<T> {
    @NotNull PolygonData<T> clip(
            @NotNull PolygonData<T> poly,
            @NotNull PolygonData<T> clipPoly
    );

    @NotNull T intersect(
            @NotNull T v1, @NotNull T v2,
            @NotNull T u1, @NotNull T u2
    );
}
