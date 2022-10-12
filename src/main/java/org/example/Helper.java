package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Helper {
    int blockWidth;
    int blockHeight;

    Helper(int blockWidth, int blockHeight) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
    }

    public String imageToASCII(BufferedImage image) {
        BufferedImage grayscale = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscale.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        char[] luminance = "  .,:;ox#%@".toCharArray();

        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < grayscale.getHeight(); y+=blockHeight) {
            for (int x = 0; x < grayscale.getWidth(); x+=blockWidth) {
                int width = Math.min(blockWidth, grayscale.getWidth()-x);
                int height = Math.min(blockHeight, grayscale.getHeight()-y);
                BufferedImage block = grayscale.getSubimage(x, y, width, height);
                int blockGrayValue = 0;
                for (int blockY = 0; blockY < height; blockY++) {
                    for (int blockX = 0; blockX < width; blockX++) {
                        int pixel = block.getRGB(blockX, blockY);
                        String hexRgb = Integer.toHexString(pixel);
                        String hexGray = hexRgb.substring(hexRgb.length() - 2);
                        int gray = Integer.parseInt(hexGray, 16);
                        blockGrayValue += gray;
                    }
                }
                blockGrayValue /= width*height;
                char c = luminance[(int) ((blockGrayValue/255.0)*(luminance.length))];
                sb.append(c);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public int getBlockHeight() {
        return blockHeight;
    }
}
