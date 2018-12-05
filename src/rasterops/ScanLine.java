package rasterops;

import imagedata.Image;
import imagedata.PolygonData;
import org.jetbrains.annotations.NotNull;


public interface ScanLine<C, P> {

    @NotNull Image<C> fill(
            @NotNull Image<C> image,
            @NotNull PolygonData<C> poly,
            @NotNull C value
    );
}