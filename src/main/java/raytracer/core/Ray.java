package raytracer.core;

import raytracer.geometry.Point;
import raytracer.geometry.Vector;

/**
 * Représente un rayon dans l'espace 3D.
 *
 * Un rayon est défini par :
 *  - une origine ;
 *  - une direction normalisée ;
 *
 * Le rayon est paramétré par la formule suivante :
 *   R(t) = origin + t * direction
 *
 * Cette classe est utilisée pour tester les intersections avec les formes
 * et pour parcourir la scène lors du rendu.
 */
public class Ray {

    /** Origine du rayon. */
    private final Point origin;

    /** Direction unitaire du rayon. */
    private final Vector direction;

    /**
     * Construit un rayon à partir d'une origine et d'une direction.
     * La direction est automatiquement normalisée.
     *
     * @param origin point d'origine du rayon
     * @param direction direction du rayon
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = (Vector) direction.normalize();
    }

    /**
     * Retourne l'origine du rayon.
     *
     * @return point représentant l'origine
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Retourne la direction normalisée du rayon.
     *
     * @return vecteur directionnel unitaire
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calcule le point situé à la distance t le long du rayon.
     * Utilise la formule :
     *   origin + t * direction
     *
     * @param t valeur paramétrique du rayon
     * @return point correspondant à la position R(t)
     */
    public Point at(double t) {
        return new Point(
            origin.x + t * direction.x,
            origin.y + t * direction.y,
            origin.z + t * direction.z
        );
    }

    /**
     * Retourne une description textuelle du rayon.
     *
     * @return chaîne contenant l'origine et la direction
     */
    @Override
    public String toString() {
        return "Ray{origin=" + origin + ", direction=" + direction + "}";
    }
}
