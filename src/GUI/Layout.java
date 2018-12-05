package GUI;

import javax.swing.*;
import java.awt.*;

public class Layout extends JPanel {

    private JLabel modelLbl;
    private JPanel panelColor;
    private JSlider sliderColR, sliderColG, sliderColB;
    private JButton btnClear, btnClp, btnScanLine, btnRedraw;
    private JCheckBox polyVisible, clipperVisible;

    public Layout(){
        init();
    }

    private void init(){
        setPreferredSize(new Dimension(300, 600));
        setBackground(new Color(0xffffff));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        modelLbl = new JLabel("Mode: ");
        modelLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        JLabel colorLbl = new JLabel("Color Settings: ");
        colorLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        colorLbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        sliderColR = new JSlider(JSlider.HORIZONTAL, 0, 255, 150);
        sliderColG = new JSlider(JSlider.HORIZONTAL, 0, 255, 150);
        sliderColB = new JSlider(JSlider.HORIZONTAL, 0, 255, 150);
        sliderColB.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        btnClp = new JButton("Clipper");
        btnClp.setToolTipText("Use to clip polygon 1.");
        btnScanLine = new JButton("Scanline");
        btnScanLine.setToolTipText("Use scanline algorithm on poly 1.");
        btnRedraw = new JButton("Redraw");
        btnRedraw.setToolTipText("Use to redraw screen.");
        btnClear = new JButton("Clear");
        btnClear.setToolTipText("Use to clean screen.");
        panelColor = new JPanel();
        JPanel panelOptions = new JPanel();
        polyVisible = new JCheckBox("Show Poly 1", true);
        polyVisible.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        clipperVisible = new JCheckBox("Show clipper poly", true);

        panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.Y_AXIS));


        panelOptions.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        sliderColR.addChangeListener(e -> panelColor.setBackground(new Color(sliderColR.getValue(), sliderColG.getValue(), sliderColB.getValue())));
        sliderColG.addChangeListener(e -> panelColor.setBackground(new Color(sliderColR.getValue(), sliderColG.getValue(), sliderColB.getValue())));
        sliderColB.addChangeListener(e -> panelColor.setBackground(new Color(sliderColR.getValue(), sliderColG.getValue(), sliderColB.getValue())));
        panelColor.setBackground(new Color(sliderColR.getValue(), sliderColG.getValue(), sliderColB.getValue()));

        panelOptions.add(modelLbl);
        panelOptions.add(colorLbl);
        panelOptions.add(sliderColR);
        panelOptions.add(sliderColG);
        panelOptions.add(sliderColB);
        panelOptions.add(btnClp);
        panelOptions.add(btnScanLine);
        panelOptions.add(btnClear);
        panelOptions.add(btnRedraw);
        panelOptions.add(polyVisible);
        panelOptions.add(clipperVisible);
        panelOptions.add(panelColor);

        add(panelOptions);
    }

    public JLabel getModelLbl() {
        return modelLbl;
    }

    public JButton getBtnClp() {
        return btnClp;
    }

    public JButton getBtnClear() {
        return btnClear;
    }


    public JButton getBtnRedraw() {
        return btnRedraw;
    }

    public JButton getBtnScanLine() {
        return btnScanLine;
    }

    public JCheckBox getPolyVisible() {
        return polyVisible;
    }


    public JCheckBox getClipperVisible() {
        return clipperVisible;
    }

    public Color getColor(){
        return new Color(sliderColR.getValue(), sliderColG.getValue(), sliderColB.getValue());
    }

    public void setRngColor(){
        sliderColR.setValue((int)(Math.random()*sliderColR.getMaximum()));
        sliderColG.setValue((int)(Math.random()*sliderColG.getMaximum()));
        sliderColB.setValue((int)(Math.random()*sliderColB.getMaximum()));
    }
}