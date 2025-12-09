package raytracer.imaging;

import raytracer.core.RayTracer;
import raytracer.core.Scene;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Classe chargée de générer l'image finale à partir d'une scène.
 *
 * L'image est produite en parcourant chaque pixel de l'image et en
 * demandant au RayTracer la couleur correspondante. Une fois le rendu
 * terminé, l'image est écrite dans un fichier PNG.
 */
public class ImageRenderer {

    /** Scène à rendre. */
    private final Scene scene;

    /** Traceur de rayons utilisé pour calculer la couleur de chaque pixel. */
    private final RayTracer rayTracer;

    /**
     * Construit un renderer associé à une scène donnée.
     *
     * @param scene scène à rendre
     */
    public ImageRenderer(Scene scene) {
        this.scene = scene;
        this.rayTracer = new RayTracer(scene);
    }

    /**
     * Effectue le rendu de la scène et écrit l'image dans un fichier PNG
     * dans le répertoire "images_gen".
     *
     * @throws IOException si l'écriture du fichier échoue
     * @throws IllegalArgumentException si la largeur ou la hauteur est invalide
     */
    public void render() throws IOException {
        int width = scene.getWidth();
        int height = scene.getHeight();

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Scene width and height must be positive");
        }

        BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Color c = rayTracer.getPixelColor(i, j);
                int rgb = c.toRGB();
                image.setRGB(i, j, rgb);
            }
        }

        // nom de fichier venant du .scene (ex: "output.png")
        String filename = scene.getOutput();

        // dossier où stocker les rendus
        File outputDir = new File("images_gen");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IOException("Unable to create output directory: " + outputDir);
        }

        // chemin complet : images_gen/<filename>
        File outFile = new File(outputDir, filename);

        // écriture de l'image
        ImageIO.write(image, "png", outFile);
    }
}
