import GUI.Layout;
import imagedata.*;
import imagedata.Image;
import org.jetbrains.annotations.NotNull;
import rasterops.*;
import transforms.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *  class for rasterizing of the canvas
 *
 *  @version 2018
 *  @author Havrda Daniel
 *  author of transforms library Jan Vanek
 */

public class Canvas2 {
    private final JPanel panel;
    private final BufferedImage img;
    private final @NotNull Presenter<Color, Graphics> presenter;
    private final @NotNull LineRenderer<Color> lineRenderer;
    private final @NotNull Clipper clipper;
    private final @NotNull ScanLine<Color, Point2D> scanLine;
    private Layout panelMenu;
    private int drawModeIndex = 0;
    private Canvas2.DrawMode dm;
    private @NotNull Image<Color> image;
    private int startC, startR;
    private PolygonData<Color> poly1, poly2;
    private boolean polygonVisible, clipperVisible;

    private Canvas2(final int width, final int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass()
                                               .getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dm = DrawMode.values()[drawModeIndex];


        img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

        image = new ImageAWT<>(img,Color::getRGB,
                Color::new
        );

        presenter = new PresenterAWT<>(
        );

        lineRenderer = new LineRendererDDA<>();
        clipper = new ClipperAWT();
        scanLine = new ScanLineAWT();
        panelMenu = new Layout();
        panelMenu.getModelLbl()
                 .setText("Mode: " + dm.name());
        polygonVisible = panelMenu.getPolyVisible().isSelected();
        clipperVisible = panelMenu.getClipperVisible().isSelected();

        poly1 = new PolygonData<>(new ArrayList<>(), panelMenu.getColor(), lineRenderer);
        poly2 = new PolygonData<>(new ArrayList<>(), panelMenu.getColor(), lineRenderer);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    startC = e.getX();
                    startR = e.getY();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    changeDrawMode();
                    panelMenu.setRngColor();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    final int endC = e.getX();
                    final int endR = e.getY();

                    final double x2 =
                            2 * (double) endC / image.getWidth() - 1;
                    final double y2 =
                            -(2 * (double) endR / image.getHeight() - 1);

                    clearWithAxes();
                    switch (dm) {
                        case Poly1:
                            poly1.addPoint(new Point2D(x2, y2));
                            clearWithAxes();
                            redraw();
                            break;

                        case Poly2:
                            poly2.addPoint(new Point2D(x2, y2));
                            poly2.setLineRenderer(lineRenderer);
                            clearWithAxes();
                            redraw();
                            break;

                        case FloodFill:
                            clearWithAxes();
                            redraw();
                            image = new FloodFill4<Color>().fill(image,
                                    startC, startR,
                                    panelMenu.getColor(),
                                    pixel -> pixel.equals(new Color(0x333333)) || pixel.equals(new Color(0x444444)));
                            break;
                    }

                    panel.repaint();

                }

            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {
                    final int endC = e.getX();
                    final int endR = e.getY();
                    final double x1 =
                            2 * (double) startC / image.getWidth() - 1;
                    final double y1 =
                            -(2 * (double) startR / image.getHeight() - 1);
                    final double x2 =
                            2 * (double) endC / image.getWidth() - 1;
                    final double y2 =
                            -(2 * (double) endR / image.getHeight() - 1);


                    clearWithAxes();
                    redraw();
                    switch (dm) {
                        case Poly1:
                            if (poly1.getSize() == 0) {
                                poly1.addPoint(new Point2D(x1, y1));
                            }
                            poly1.setColor(panelMenu.getColor());
                            poly1.addPoint(new Point2D(x2, y2));
                            poly1.renderElement(image);
                            poly1.removeLastPoint();
                            break;

                        case Poly2:
                            if (poly2.getSize() == 0) {
                                poly2.addPoint(new Point2D(x1, y1));
                            }
                            poly2.setColor(panelMenu.getColor());
                            poly2.addPoint(new Point2D(x2, y2));
                            poly2.renderElement(image);
                            poly2.removeLastPoint();
                            break;
                    }
                }
                panel.repaint();
            }

        });

        panelMenu.getBtnClp()
                 .addActionListener(e -> {
                     if (poly2.getSize() >= 3)
                         poly1 = clipper.clip(poly1, poly2);
                     clearWithAxes();
                     redraw();
                     panel.repaint();
                 });
        panelMenu.getBtnClear()
                 .addActionListener(e -> {
                     poly1.clearList();
                     poly2.clearList();
                     clearWithAxes();
                     redraw();
                     panel.repaint();
                 });
        panelMenu.getBtnRedraw()
                 .addActionListener(e -> {
                     redraw();
                     panel.repaint();
                 });
        panelMenu.getBtnScanLine()
                 .addActionListener(e -> {
                     image = scanLine.fill(image, poly1, panelMenu.getColor());
                     redraw();
                     panel.repaint();
                 });

        panelMenu.getPolyVisible()
                 .addItemListener(e -> {
                     this.polygonVisible = !this.polygonVisible;
                     clearWithAxes();
                     redraw();
                     panel.repaint();
                 });

        panelMenu.getClipperVisible()
                 .addItemListener(e -> {
                     this.clipperVisible = !this.clipperVisible;
                     clearWithAxes();
                     redraw();
                     panel.repaint();
                 });

        panelMenu.getBtnRedraw()
                 .addActionListener(e -> {
                     redraw();
                     panel.repaint();
                 });

        frame.add(panel, BorderLayout.WEST);
        frame.add(panelMenu, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Canvas2(800, 600)::start);
    }

    private void changeDrawMode() {
        if (drawModeIndex < DrawMode.values().length - 1)
            drawModeIndex++;
        else
            drawModeIndex = 0;

        dm = DrawMode.values()[drawModeIndex];
        panelMenu.getModelLbl()
                 .setText("Mode: " + dm.name());
    }

    private void redraw() {

        if (poly1 != null && polygonVisible) {
            poly1.renderElement(image);
            panel.repaint();
        }
        if (poly2 != null && clipperVisible) {
            poly2.renderElement(image);
            panel.repaint();
        }
    }

    private void clearWithAxes() {
        clear();
        for (int i = 0; i < image.getWidth(); i++) {
            image = image.withValue(i, image.getHeight() / 2,
                    new Color(0x333333));
        }
        for (int j = 0; j < image.getHeight(); j++) {
            image = image.withValue(image.getWidth() / 2, j,
                    new Color(0x333333));
        }
    }

    private void clear() {
        image = new ImageAWT<>(img,
                Color::getRGB,
                Color::new).cleared(new Color(0x444444));

    }
    private void present(final Graphics graphics) {
        presenter.present(image, graphics);
    }

    private void draw() {
        clearWithAxes();
    }

    private void start() {
        draw();
        panel.repaint();
    }

    private enum DrawMode {Poly1, Poly2, FloodFill}


}
