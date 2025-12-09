package raytracer.lighting;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;

/**
 * Représente une lumière directionnelle dans la scène.
 *
 * Une lumière directionnelle est considérée comme située à l'infini
 * et émettant des rayons parallèles entre eux. Elle est définie par :
 *  - une direction d'éclairage ;
 *  - une couleur.
 *
 * Comme la source est infiniment éloignée, sa distance effective est
 * infinie et son intensité ne décroît pas avec la distance.
 */
public class DirectionalLight extends AbstractLight {

    /** Direction dans laquelle la lumière se propage. */
    private Vector direction;

    /**
     * Construit une lumière directionnelle avec une direction et une couleur.
     *
     * @param dirX composante X du vecteur direction
     * @param dirY composante Y du vecteur direction
     * @param dirZ composante Z du vecteur direction
     * @param r composante rouge de la couleur
     * @param g composante verte de la couleur
     * @param b composante bleue de la couleur
     */
    public DirectionalLight(double dirX, double dirY, double dirZ,
                            double r, double g, double b) {
        super(new Color(r, g, b));
        this.direction = new Vector(dirX, dirY, dirZ);
    }

    /**
     * Retourne la direction normalisée de la lumière.
     *
     * @return vecteur unitaire représentant la direction d'éclairage
     */
    public Vector getDirection() {
        return (Vector) direction.normalize();
    }

    /**
     * Retourne la direction de la lumière pour un point donné.
     * Pour une lumière directionnelle, la direction est constante
     * et est opposée au vecteur direction stocké.
     *
     * @param point point éclairé
     * @return vecteur unitaire allant du point vers la lumière
     */
    @Override
    public Vector getLightDirection(Point point) {
        return (Vector) direction.multiply(-1.0).normalize();
    }

    /**
     * Retourne la distance maximale jusqu’à la lumière.
     * Pour une lumière directionnelle, la distance est infinie.
     *
     * @param p point testé
     * @return Double.POSITIVE_INFINITY
     */
    @Override
    public double getMaxDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Retourne une représentation textuelle de cette lumière directionnelle.
     *
     * @return chaîne de caractères contenant direction et couleur
     */
    @Override
    public String toString() {
        return String.format(
            "DirectionalLight{dir=(%.2f,%.2f,%.2f), color=%s}",
            direction.x, direction.y, direction.z, color
        );
    }
}
