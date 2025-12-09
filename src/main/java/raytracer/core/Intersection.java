package raytracer.core;

import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Shape;
import raytracer.imaging.Color;
import raytracer.lighting.AbstractLight;

/**
 * Représente le résultat de l'intersection entre un rayon et une forme.
 *
 * Une intersection contient :
 *  - la valeur t du paramètre du rayon correspondant au point d'impact ;
 *  - le point exact d'intersection ;
 *  - la forme concernée ;
 *  - la normale à la surface en ce point ;
 *  - les propriétés de matériau : couleurs diffuse et spéculaire, shininess.
 *
 * Toutes les valeurs nécessaires au calcul de l'éclairage sont
 * prélevées immédiatement dans la forme pour simplifier l'utilisation ultérieure.
 */
public class Intersection {

    /** Paramètre t du rayon pour cette intersection. */
    private final double t;

    /** Point exact où le rayon touche la forme. */
    private final Point point;

    /** Forme touchée par le rayon. */
    private final Shape shape;

    /** Normale à la surface au point d'intersection. */
    private final Vector normal;

    /** Couleur diffuse du matériau de la forme. */
    private final Color diffuse;

    /** Couleur spéculaire du matériau de la forme. */
    private final Color specular;

    /** Exposant de brillance utilisé pour la réflexion spéculaire. */
    private final double shininess;

    /**
     * Construit une intersection à partir de sa distance t,
     * du point touché et de la forme correspondante.
     *
     * @param t valeur du paramètre du rayon
     * @param point point d'intersection
     * @param shape forme touchée
     */
    public Intersection(double t, Point point, Shape shape) {
        this.t = t;
        this.point = point;
        this.shape = shape;

        this.normal = shape.getNormal(point);
        this.diffuse = shape.getDiffuse();
        this.specular = shape.getSpecular();
        this.shininess = shape.getShininess();
    }

    /** @return la valeur t de l'intersection */
    public double getT() { return t; }

    /** @return le point d'intersection */
    public Point getPoint() { return point; }

    /** @return la forme touchée */
    public Shape getShape() { return shape; }

    /** @return la normale à la surface au point d'intersection */
    public Vector getNormal() { return normal; }

    /** @return la couleur diffuse du matériau au point */
    public Color getDiffuse() { return diffuse; }

    /** @return la couleur spéculaire du matériau au point */
    public Color getSpecular() { return specular; }

    /** @return l'exposant de brillance pour le modèle de Phong */
    public double getShininess() { return shininess; }

    /**
     * Calcule la contribution diffuse selon le modèle de Lambert.
     * La valeur retournée est :
     *   max(n · l, 0) multiplié par la couleur de la lumière et par la couleur diffuse.
     *
     * @param light source lumineuse
     * @return couleur diffuse résultante
     */
    public Color computeLambert(AbstractLight light) {
        Vector L = light.getLightDirection(point);
        double ndotl = normal.dot(L);

        if (ndotl <= 0.0) {
            return new Color(0, 0, 0);
        }

        return light.getColor().schurMultiply(diffuse).multiply(ndotl);
    }

    /**
     * Calcule la contribution spéculaire selon le modèle de Blinn-Phong.
     * La couleur vaut zéro si la brillance vaut zéro ou si le point n'est pas orienté
     * de manière à produire une réflexion spéculaire.
     *
     * @param light source lumineuse
     * @param eyeDir direction du regard depuis le point
     * @return contribution spéculaire
     */
    public Color computePhong(AbstractLight light, Vector eyeDir) {
        if (shininess <= 0.0) {
            return new Color(0, 0, 0);
        }

        Vector L = light.getLightDirection(point);
        Vector H = (Vector) L.add(eyeDir).normalize();

        double ndoth = normal.dot(H);
        if (ndoth <= 0.0) {
            return new Color(0, 0, 0);
        }

        double factor = Math.pow(ndoth, shininess);
        return light.getColor().schurMultiply(specular).multiply(factor);
    }

    /**
     * Retourne une représentation textuelle de l'intersection.
     *
     * @return chaîne contenant t, le point et la forme
     */
    @Override
    public String toString() {
        return String.format(
                "Intersection{t=%.4f, point=%s, shape=%s}",
                t, point, shape
        );
    }
}
