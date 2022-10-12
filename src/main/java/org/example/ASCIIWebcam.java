package org.example;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ASCIIWebcam {
    private static JFrame textFrame;
    private static JTextArea textArea;

    public static void main(String[] args) {
        long lastTime = System.currentTimeMillis();
        int blockWidth = 3;
        int blockHeight = 7;
        double width = WebcamResolution.QVGA.getWidth();
        double height = WebcamResolution.QVGA.getHeight();
        Helper helper = new Helper(blockWidth, blockHeight);

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.QVGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        JFrame webcamFrame = new JFrame("Webcam");
        webcamFrame.add(panel);
        webcamFrame.setResizable(true);
        webcamFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        webcamFrame.pack();
        webcamFrame.setVisible(true);

        buildTextFrame(false);

        JFrame settingsFrame = new JFrame("Settings");
        JLabel blockWidthLabel = new JLabel("Block width");
        JTextField blockWidthField = new JTextField();
        JLabel blockHeightLabel = new JLabel("Block height");
        JTextField blockHeightField = new JTextField();
        JLabel fontSizeLabel = new JLabel("Font size");
        JTextField fontSizeField = new JTextField();
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(event -> {
            try {
                int _blockWidth = Integer.parseInt(blockWidthField.getText());
                int _blockHeight = Integer.parseInt(blockHeightField.getText());
                int _fontSize = Integer.parseInt(fontSizeField.getText());

                helper.setBlockWidth(_blockWidth);
                helper.setBlockHeight(_blockHeight);
                Font _font = new Font("Consolas", Font.PLAIN, _fontSize);
                textArea.setFont(_font);
            } catch (Exception e) {
                System.out.println("Wrong input");
            }
        });
        JButton decorationsButton = new JButton("Toggle decorations");
        decorationsButton.addActionListener(event -> {
            textFrame.dispose();
            buildTextFrame(!textFrame.isUndecorated());
        });
        JButton showCamButton = new JButton("Toggle camera visibility");
        showCamButton.addActionListener(event -> webcamFrame.setVisible(!webcamFrame.isVisible()));
        JPanel settingsPanel = new JPanel();
        BoxLayout box = new BoxLayout(settingsPanel, BoxLayout.Y_AXIS);
        settingsPanel.setLayout(box);
        settingsPanel.add(blockWidthLabel);
        settingsPanel.add(blockWidthField);
        settingsPanel.add(blockHeightLabel);
        settingsPanel.add(blockHeightField);
        settingsPanel.add(fontSizeLabel);
        settingsPanel.add(fontSizeField);
        settingsPanel.add(applyButton);
        settingsPanel.add(decorationsButton);
        settingsFrame.getContentPane().add(settingsPanel);
        settingsFrame.setResizable(true);
        settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsFrame.setSize(new Dimension(300, 200));
        settingsFrame.setVisible(true);

        while (true) {
            int asciiWidth = (int) Math.ceil(width / helper.getBlockWidth());
            int asciiHeight = (int) Math.ceil(height / helper.getBlockHeight());
            BufferedImage image = webcam.getImage();
            double fps = 1.0 / ((System.currentTimeMillis() - lastTime) / 1000.0);
            lastTime = System.currentTimeMillis();
            String ascii = helper.imageToASCII(image) +
                    "\nBlock size: " + helper.getBlockWidth() + "x" + helper.getBlockHeight() +
                    "\nASCII resolution: " + asciiWidth + "x" + asciiHeight +
                    "\nFont size: " + textArea.getFont().getSize() +
                    "\nFPS: " + fps;
            textArea.setText(ascii);
        }
    }

    private static void buildTextFrame(boolean undecorated) {
        textFrame = new JFrame("ASCII");
        textFrame.setUndecorated(undecorated);
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        Font font = new Font("Consolas", Font.PLAIN, 12);
        textArea.setFont(font);
        textArea.setLineWrap(false);
        textFrame.add(textArea);
        textFrame.setResizable(true);
        textFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textFrame.setSize(new Dimension(860, 653));
        textFrame.setVisible(true);
    }

}
