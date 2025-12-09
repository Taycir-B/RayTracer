package imgcompare;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage : java -jar imgcompare.jar <image1> <image2>");
            System.exit(1);
        }

        File f1 = new File(args[0]);
        File f2 = new File(args[1]);

        try {
            BufferedImage img1 = ImageIO.read(f1);
            BufferedImage img2 = ImageIO.read(f2);

            if (img1 == null || img2 == null) {
                System.err.println("Impossible de lire au moins une des images.");
                System.exit(2);
            }

            if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
                System.err.println("Les images n'ont pas la meme taille.");
                System.exit(3);
            }

            ImageComparator comparator = new ImageComparator();
            int diffCount = comparator.countDifferentPixels(img1, img2);

            // Affichage conforme aux consignes
            if (diffCount == 0) {
                System.out.println("OK");
            } else {
                System.out.println("KO");
            }
            System.out.println("Les deux images different de " + diffCount + " pixels.");

            // Generation de l'image differentielle
            BufferedImage diffImage = comparator.createDiffImage(img1, img2);
            String outName = "diff_" + f1.getName().replaceAll("\\.[^.]+$", "") +
                             "_" + f2.getName().replaceAll("\\.[^.]+$", "") + ".png";
            ImageIO.write(diffImage, "PNG", new File(outName));

        } catch (IOException e) {
            System.err.println("Erreur IO : " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }
}
