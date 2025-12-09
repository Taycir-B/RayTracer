package raytracer.lighting;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;

/**
 * Classe abstraite représentant une source de lumière.
 * Une lumière possède une couleur et doit fournir la direction de la lumière
 * ainsi que la distance maximale utile pour les tests d'ombrage.
 */
public abstract class AbstractLight {

    /** Couleur de la source lumineuse. */
    protected Color color;

    /**
     * Construit une source lumineuse avec la couleur indiquée.
     *
     * @param color couleur de la lumière
     */
    public AbstractLight(Color color) {
        this.color = color;
    }

    /**
     * Renvoie la couleur de la lumière.
     *
     * @return couleur de la lumière
     */
    public Color getColor() {
        return color;
    }

    /**
     * Renvoie le vecteur directionnel normalisé allant du point donné vers la lumière.
     *
     * @param p point sur un objet de la scène
     * @return direction normalisée de la lumière
     */
    public abstract Vector getLightDirection(Point p);

    /**
     * Renvoie la distance maximale entre le point et la lumière,
     * utilisée pour les calculs d’ombres.
     * Pour une lumière directionnelle, cette distance est infinie.
     *
     * @param p point de la scène
     * @return distance maximale jusqu'à la lumière
     */
    public abstract double getMaxDistance(Point p);
}
