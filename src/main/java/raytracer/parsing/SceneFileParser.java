package raytracer.parsing;

import raytracer.core.Camera;
import raytracer.core.Scene;
import raytracer.geometry.Point;
import raytracer.geometry.shapes.Plane;
import raytracer.geometry.shapes.Sphere;
import raytracer.geometry.shapes.Triangle;
import raytracer.imaging.Color;
import raytracer.lighting.DirectionalLight;
import raytracer.lighting.PointLight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyseur de fichiers de description de scène (.scene).
 * Cette classe lit un fichier ligne par ligne, interprète chaque commande,
 * vérifie sa validité et construit un objet Scene.
 *
 * Fonctionnalités prises en charge :
 * - définition de la taille de l'image ;
 * - configuration de la caméra ;
 * - gestion des couleurs ambiante, diffuse et spéculaire ;
 * - création de lumières directionnelles et ponctuelles ;
 * - création des formes géométriques : sphère, triangle et plan ;
 * - gestion de maxverts et vertex pour la construction de triangles ;
 * - validation des valeurs imposées par la spécification.
 */
public class SceneFileParser {

    /** Scène en cours de construction. */
    private Scene scene;

    /** Couleur diffuse courante utilisée pour la prochaine forme. */
    private Color currentDiffuse = new Color(0, 0, 0);

    /** Couleur spéculaire courante utilisée pour la prochaine forme. */
    private Color currentSpecular = new Color(0, 0, 0);

    /** Exposant de brillance (Phong) courant. */
    private double currentShininess = 0.0;

    /** Liste des vertex définis via les commandes maxverts et vertex. */
    private List<Point> vertices = new ArrayList<>();

    /** Nombre maximal de vertex autorisés. */
    private int maxverts = 0;

    /** Accumulation des couleurs des lumières utilisée pour validation. */
    private Color accumulatedLightColor = new Color(0, 0, 0);

    /**
     * Analyse un fichier .scene et retourne la scène correspondante.
     *
     * @param filename chemin vers le fichier à analyser
     * @return la scène entièrement construite
     * @throws IOException en cas d'erreur de lecture
     * @throws IllegalArgumentException si une commande est invalide ou incohérente
     */
    public Scene parse(String filename) throws IOException {
        scene = new Scene();
        vertices.clear();
        maxverts = 0;
        accumulatedLightColor = new Color(0, 0, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    parseLine(line);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            String.format("Erreur à la ligne %d : %s", lineNumber, e.getMessage()),
                            e);
                }
            }
        }

        validateScene();
        return scene;
    }

    /**
     * Analyse une ligne de commande isolée du fichier .scene.
     *
     * @param line ligne nettoyée à interpréter
     */
    private void parseLine(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length == 0) {
            return;
        }

        switch (parts[0]) {
            case "size":        parseSize(parts); break;
            case "output":      parseOutput(parts); break;
            case "camera":      parseCamera(parts); break;
            case "ambient":     parseAmbient(parts); break;
            case "diffuse":     parseDiffuse(parts); break;
            case "specular":    parseSpecular(parts); break;
            case "shininess":   parseShininess(parts); break;

            case "directional": parseDirectionalLight(parts); break;
            case "point":       parsePointLight(parts); break;

            case "sphere":      parseSphere(parts); break;
            case "maxverts":    parseMaxverts(parts); break;
            case "vertex":      parseVertex(parts); break;
            case "tri":         parseTriangle(parts); break;
            case "plane":       parsePlane(parts); break;

            default:
                throw new IllegalArgumentException("Commande inconnue : " + parts[0]);
        }
    }

    /* =============================================================== */
    /* ============================ LUMIÈRES ========================= */
    /* =============================================================== */

    /**
     * Analyse une définition de lumière directionnelle.
     *
     * @param p paramètres de la commande
     */
    private void parseDirectionalLight(String[] p) {
        if (p.length != 7) {
            throw new IllegalArgumentException("directional attend 6 arguments");
        }

        double dx = Double.parseDouble(p[1]);
        double dy = Double.parseDouble(p[2]);
        double dz = Double.parseDouble(p[3]);
        double r = Double.parseDouble(p[4]);
        double g = Double.parseDouble(p[5]);
        double b = Double.parseDouble(p[6]);

        Color c = new Color(r, g, b);
        validateLightColor(c);

        accumulatedLightColor = new Color(
                accumulatedLightColor.x + c.x,
                accumulatedLightColor.y + c.y,
                accumulatedLightColor.z + c.z);

        validateAccumulated(accumulatedLightColor);

        scene.addLight(new DirectionalLight(dx, dy, dz, r, g, b));
    }

    /**
     * Analyse une lumière ponctuelle.
     *
     * @param p paramètres de la commande
     */
    private void parsePointLight(String[] p) {
        if (p.length != 7) {
            throw new IllegalArgumentException("point attend 6 arguments");
        }

        double px = Double.parseDouble(p[1]);
        double py = Double.parseDouble(p[2]);
        double pz = Double.parseDouble(p[3]);
        double r = Double.parseDouble(p[4]);
        double g = Double.parseDouble(p[5]);
        double b = Double.parseDouble(p[6]);

        Color c = new Color(r, g, b);
        validateLightColor(c);

        accumulatedLightColor = new Color(
                accumulatedLightColor.x + c.x,
                accumulatedLightColor.y + c.y,
                accumulatedLightColor.z + c.z);

        validateAccumulated(accumulatedLightColor);

        scene.addLight(new PointLight(px, py, pz, r, g, b));
    }

    /* =============================================================== */
    /* ======================= PARAMÈTRES SCÈNE ===================== */
    /* =============================================================== */

    /**
     * Analyse la commande size définissant largeur et hauteur de l'image.
     */
    private void parseSize(String[] p) {
        if (p.length != 3) {
            throw new IllegalArgumentException("size attend width height");
        }
        scene.setWidth(Integer.parseInt(p[1]));
        scene.setHeight(Integer.parseInt(p[2]));
    }

    /**
     * Analyse la commande output définissant le nom du fichier image.
     */
    private void parseOutput(String[] p) {
        if (p.length != 2) {
            throw new IllegalArgumentException("output attend un nom de fichier");
        }
        scene.setOutput(p[1]);
    }

    /**
     * Analyse les paramètres de la caméra.
     */
    private void parseCamera(String[] p) {
        if (p.length != 11) {
            throw new IllegalArgumentException("camera attend 10 valeurs");
        }
        double[] v = new double[10];
        for (int i = 0; i < 10; i++) {
            v[i] = Double.parseDouble(p[i + 1]);
        }
        scene.setCamera(new Camera(
                v[0], v[1], v[2],
                v[3], v[4], v[5],
                v[6], v[7], v[8],
                v[9]));
    }

    /* =============================================================== */
    /* ============================ MATÉRIAUX ======================== */
    /* =============================================================== */

    /**
     * Vérifie que ambient + diffuse ne dépasse pas 1 pour chaque composante.
     */
    private void validateAmbientPlusDiffuse(Color a, Color d) {
        double eps = 1e-9;
        if (a.x + d.x > 1 + eps || a.y + d.y > 1 + eps || a.z + d.z > 1 + eps) {
            throw new IllegalArgumentException("ambient + diffuse doit être <= 1");
        }
    }

    /**
     * Analyse la couleur ambiante.
     */
    private void parseAmbient(String[] p) {
        if (p.length != 4) {
            throw new IllegalArgumentException("ambient attend r g b");
        }

        Color amb = new Color(
                Double.parseDouble(p[1]),
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3]));

        validateAmbientPlusDiffuse(amb, currentDiffuse);
        scene.setAmbient(amb);
    }

    /**
     * Analyse la couleur diffuse.
     */
    private void parseDiffuse(String[] p) {
        if (p.length != 4) {
            throw new IllegalArgumentException("diffuse attend r g b");
        }

        Color diff = new Color(
                Double.parseDouble(p[1]),
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3]));

        validateAmbientPlusDiffuse(scene.getAmbient(), diff);
        currentDiffuse = diff;
    }

    /**
     * Analyse la couleur spéculaire.
     */
    private void parseSpecular(String[] p) {
        if (p.length != 4) {
            throw new IllegalArgumentException("specular attend r g b");
        }

        currentSpecular = new Color(
                Double.parseDouble(p[1]),
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3]));
    }

    /**
     * Analyse l'exposant de brillance Phong.
     */
    private void parseShininess(String[] p) {
        if (p.length != 2) {
            throw new IllegalArgumentException("shininess attend une valeur");
        }
        currentShininess = Double.parseDouble(p[1]);
    }

    /* =============================================================== */
    /* ============================ GÉOMÉTRIE ======================== */
    /* =============================================================== */

    /**
     * Analyse une sphère.
     */
    private void parseSphere(String[] p) {
        if (p.length != 5) {
            throw new IllegalArgumentException("sphere attend x y z radius");
        }

        double x = Double.parseDouble(p[1]);
        double y = Double.parseDouble(p[2]);
        double z = Double.parseDouble(p[3]);
        double r = Double.parseDouble(p[4]);

        if (r <= 0) {
            throw new IllegalArgumentException("Le rayon doit être positif");
        }

        Sphere s = new Sphere(x, y, z, r, currentDiffuse, currentSpecular);
        s.setShininess(currentShininess);
        scene.addShape(s);
    }

    /**
     * Analyse maxverts, définissant le nombre maximal de vertex.
     */
    private void parseMaxverts(String[] p) {
        maxverts = Integer.parseInt(p[1]);
        if (maxverts <= 0) {
            throw new IllegalArgumentException("maxverts doit être positif");
        }
        vertices.clear();
    }

    /**
     * Analyse un vertex individuel.
     */
    private void parseVertex(String[] p) {
        if (vertices.size() >= maxverts) {
            throw new IllegalArgumentException("Trop de vertex définis");
        }

        vertices.add(new Point(
                Double.parseDouble(p[1]),
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3])));
    }

    /**
     * Analyse un triangle défini par indices de vertex.
     */
    private void parseTriangle(String[] p) {
        int a = Integer.parseInt(p[1]);
        int b = Integer.parseInt(p[2]);
        int c = Integer.parseInt(p[3]);

        if (a < 0 || b < 0 || c < 0 ||
            a >= vertices.size() || b >= vertices.size() || c >= vertices.size()) {
            throw new IllegalArgumentException("Indice de vertex hors limite");
        }

        Triangle t = new Triangle(
                vertices.get(a), vertices.get(b), vertices.get(c),
                currentDiffuse, currentSpecular);
        t.setShininess(currentShininess);

        scene.addShape(t);
    }

    /**
     * Analyse la définition d'un plan.
     */
    private void parsePlane(String[] p) {
        double px = Double.parseDouble(p[1]);
        double py = Double.parseDouble(p[2]);
        double pz = Double.parseDouble(p[3]);
        double nx = Double.parseDouble(p[4]);
        double ny = Double.parseDouble(p[5]);
        double nz = Double.parseDouble(p[6]);

        Plane pl = new Plane(px, py, pz, nx, ny, nz,
                currentDiffuse, currentSpecular);

        pl.setShininess(currentShininess);
        scene.addShape(pl);
    }

    /* =============================================================== */
    /* ============================= VALIDATION ====================== */
    /* =============================================================== */

    /**
     * Vérifie que la scène contient les informations minimales.
     */
    private void validateScene() {
        if (scene.getWidth() == 0 || scene.getHeight() == 0) {
            throw new IllegalArgumentException("La commande size est obligatoire");
        }
        if (scene.getCamera() == null) {
            throw new IllegalArgumentException("La commande camera est obligatoire");
        }
    }

    /**
     * Vérifie que la couleur d'une lumière est dans l'intervalle [0,1].
     */
    private void validateLightColor(Color c) {
        if (c.x < 0 || c.y < 0 || c.z < 0 ||
            c.x > 1 || c.y > 1 || c.z > 1) {
            throw new IllegalArgumentException("Les couleurs de lumière doivent être dans [0,1]");
        }
    }

    /**
     * Vérifie que la somme cumulée des couleurs des lumières ne dépasse pas 1.
     */
    private void validateAccumulated(Color acc) {
        if (acc.x > 1 || acc.y > 1 || acc.z > 1) {
            throw new IllegalArgumentException("La somme des couleurs de lumière dépasse 1.0");
        }
    }
}