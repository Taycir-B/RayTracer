package raytracer.lighting;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;

/**
 * Représente une lumière ponctuelle dans la scène.
 *
 * Une lumière ponctuelle est définie par :
 *  - une position dans l'espace ;
 *  - une couleur ;
 *
 * Contrairement à une lumière directionnelle, la direction de la lumière
 * dépend du point éclairé. L'intensité décroît également en fonction
 * de la distance, ce qui est pris en compte par les tests d'ombre.
 */
public class PointLight extends AbstractLight {

    /** Position de la source lumineuse. */
    private Point position;

    /**
     * Construit une lumière ponctuelle avec position et couleur.
     *
     * @param x coordonnée X de la source
     * @param y coordonnée Y de la source
     * @param z coordonnée Z de la source
     * @param r composante rouge de la couleur
     * @param g composante verte de la couleur
     * @param b composante bleue de la couleur
     */
    public PointLight(double x, double y, double z,
                      double r, double g, double b) {

        super(new Color(r, g, b));
        this.position = new Point(x, y, z);
    }

    /**
     * Retourne la position de la lumière.
     *
     * @return point représentant la position de la source
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Retourne la direction de la lumière pour un point donné.
     * Cette direction va du point éclairé vers la lumière.
     *
     * @param point point éclairé
     * @return vecteur directionnel normalisé point -> lumière
     */
    @Override
    public Vector getLightDirection(Point point) {
        return (Vector) position.subtract(point).normalize();
    }

    /**
     * Retourne la distance maximale entre un point et la lumière.
     * Cette valeur est utilisée pour les tests d'ombre :
     * une intersection est valide uniquement si elle se trouve avant la lumière.
     *
     * @param p point testé
     * @return distance entre p et la lumière
     */
    @Override
    public double getMaxDistance(Point p) {
        return position.subtract(p).length();
    }

    /**
     * Retourne une description textuelle de la lumière ponctuelle.
     *
     * @return chaîne contenant position et couleur
     */
    @Override
    public String toString() {
        return String.format(
            "PointLight{pos=(%.2f,%.2f,%.2f), color=%s}",
            position.x, position.y, position.z, color
        );
    }
}