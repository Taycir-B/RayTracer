package raytracer.core;

import raytracer.lighting.AbstractLight;
import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.shapes.Shape;
import raytracer.geometry.Vector;

import java.util.*;

/**
 * Représente une scène complète de raytracing.
 *
 * Une scène contient :
 *  - les dimensions de l'image ;
 *  - une caméra ;
 *  - une couleur ambiante ;
 *  - une liste de lumières ;
 *  - une liste de formes géométriques.
 *
 * La scène fournit également des services essentiels :
 *  - la recherche de l'intersection la plus proche pour un rayon ;
 *  - la détection d'ombres ;
 *  - le calcul complet de l'éclairage en un point d'intersection
 *    incluant ambiant, Lambert et Phong.
 */
public class Scene {

    /** Largeur de l'image. */
    private int width;

    /** Hauteur de l'image. */
    private int height;

    /** Nom du fichier de sortie. */
    private String output = "output.png";

    /** Caméra de la scène. */
    private Camera camera;

    /** Couleur ambiante de la scène. */
    private Color ambient = new Color();

    /** Liste des lumières. */
    private List<AbstractLight> lights = new ArrayList<>();

    /** Liste des formes géométriques. */
    private List<Shape> shapes = new ArrayList<>();

    /** @return largeur de la scène */
    public int getWidth() { return width; }

    /** Définit la largeur de la scène. */
    public void setWidth(int w) { width = w; }

    /** @return hauteur de la scène */
    public int getHeight() { return height; }

    /** Définit la hauteur de la scène. */
    public void setHeight(int h) { height = h; }

    /** Définit le chemin du fichier de sortie. */
    public void setOutput(String o) { output = o; }

    /** @return fichier de sortie */
    public String getOutput() { return output; }

    /** Déclare la caméra de la scène. */
    public void setCamera(Camera cam) { camera = cam; }

    /** @return la caméra active */
    public Camera getCamera() { return camera; }

    /** Définit la couleur ambiante de la scène. */
    public void setAmbient(Color a) { ambient = a; }

    /** @return la couleur ambiante */
    public Color getAmbient() { return ambient; }

    /** Ajoute une lumière à la scène. */
    public void addLight(AbstractLight l) { lights.add(l); }

    /** @return la liste des lumières */
    public List<AbstractLight> getLights() { return lights; }

    /** Ajoute une forme géométrique à la scène. */
    public void addShape(Shape s) { shapes.add(s); }

    /** @return la liste des formes */
    public List<Shape> getShapes() { return shapes; }

    /**
     * Recherche l'intersection la plus proche entre un rayon
     * et n'importe quelle forme de la scène.
     *
     * @param ray rayon lancé depuis la caméra ou un point de la scène
     * @return intersection la plus proche, si elle existe
     */
    public Optional<Intersection> findClosestIntersection(Ray ray) {
        Optional<Intersection> best = Optional.empty();
        double minT = Double.POSITIVE_INFINITY;

        for (Shape s : shapes) {
            Optional<Intersection> hit = s.intersect(ray);
            if (hit.isPresent() &&
                hit.get().getT() > 0 &&
                hit.get().getT() < minT) {

                minT = hit.get().getT();
                best = hit;
            }
        }
        return best;
    }

    /**
     * Indique si un point d'intersection est dans l'ombre
     * pour une lumière donnée.
     *
     * Un rayon d'ombre est lancé depuis le point vers la lumière.
     * Si une forme est touchée avant la lumière, le point est dans l'ombre.
     *
     * @param inter intersection courante
     * @param light lumière testée
     * @return vrai si un obstacle bloque la lumière
     */
    private boolean isInShadow(Intersection inter, AbstractLight light) {

        Point p = inter.getPoint();
        Vector L = light.getLightDirection(p);
        Ray shadowRay = new Ray(p, L);

        double maxDist = light.getMaxDistance(p);
        double eps = 1e-4;

        Optional<Intersection> hit = findClosestIntersection(shadowRay);
        if (hit.isPresent()) {
            double t = hit.get().getT();
            if (t > eps && t < maxDist - eps) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcule la couleur finale en un point d'intersection.
     *
     * La couleur résultante combine :
     *  - la contribution ambiante globale ;
     *  - la diffusion Lambert pour chaque lumière visible ;
     *  - la composante spéculaire de Phong si applicable ;
     *  - l'occlusion par ombres.
     *
     * @param inter intersection pour laquelle calculer la couleur
     * @return couleur finale en ce point
     */
    public Color computeColor(Intersection inter) {

        Color result = new Color(ambient.x, ambient.y, ambient.z);

        Point eye = camera.getLookFrom();
        Point p = inter.getPoint();
        Vector eyeDir = (Vector) eye.subtract(p).normalize();

        for (AbstractLight light : lights) {

            if (isInShadow(inter, light)) {
                continue;
            }

            Color lambert = inter.computeLambert(light);
            Color phong = inter.computePhong(light, eyeDir);

            result = result.add(lambert).add(phong);
        }

        return result;
    }
}
