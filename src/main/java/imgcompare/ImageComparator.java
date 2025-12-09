package imgcompare;

import java.awt.image.BufferedImage;

public class ImageComparator {

    /**
     * Compte le nombre de pixels differents entre imgA et imgB.
     */
    public int countDifferentPixels(BufferedImage imgA, BufferedImage imgB) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        int count = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    count++;
                }
            }
        }
        return count;
    }


    public BufferedImage createDiffImage(BufferedImage imgA, BufferedImage imgB) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        final int DIFFER_COLOR = 0xFFFF00FF;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbA = imgA.getRGB(x, y);
                int rgbB = imgB.getRGB(x, y);
                if (rgbA == rgbB) {
                    out.setRGB(x, y, rgbA);
                } else {
                    out.setRGB(x, y, DIFFER_COLOR);
                }
            }
        }

        return out;
    }
}
