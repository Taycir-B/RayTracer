package raytracer.app;

import raytracer.parsing.SceneFileParser;
import raytracer.imaging.ImageRenderer;
import raytracer.core.Scene;

import java.io.IOException;
import java.nio.file.*;

public class RaytracerMain {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage : java RaytracerMain <fichier.scene>");
            System.exit(1);
        }

        String sceneFile = args[0];

        try {
            // 1) Parse scène
            SceneFileParser parser = new SceneFileParser();
            Scene scene = parser.parse(sceneFile);

            // 1) dossier des images générées, relatif au répertoire de lancement
            Path imagesDir = Paths.get("images_gen");

            // 2) création du dossier s'il n'existe pas
            Files.createDirectories(imagesDir);

            // 3) chemin complet de sortie : "images générés/<nom_scene_output>"
            Path outputPath = imagesDir.resolve(scene.getOutput());

            // 2) Rendu
            ImageRenderer renderer = new ImageRenderer(scene);
            renderer.render();

            System.out.println("Image générée : " + scene.getOutput());

        } catch (IOException e) {
            System.err.println("Erreur I/O : " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur dans le fichier .scene : " + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }
    }
}