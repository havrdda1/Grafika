package imagedata;

import org.jetbrains.annotations.NotNull;


public interface ObjectData<T> {
    /**
     * Represents data of object to be rendered
     *
     * @param image image to render into
     * @return new image
     */

    @NotNull Image<T> renderElement(
            @NotNull Image<T> image);
}
