package raytracer.geometry.shapes;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.core.Intersection;
import raytracer.core.Ray;

/**
 * Classe abstraite représentant une forme géométrique dans la scène.
 *
 * Une forme possède des propriétés de matériau :
 *  - une couleur diffuse ;
 *  - une couleur spéculaire ;
 *  - un coefficient de brillance (shininess) utilisé par le modèle de Phong.
 *
 * Les classes concrètes héritant de Shape doivent obligatoirement définir :
 *  - la normale au point de la surface ;
 *  - le test d'intersection avec un rayon.
 */
public abstract class Shape {

    /** Couleur diffuse du matériau. */
    protected Color diffuse;

    /** Couleur spéculaire du matériau. */
    protected Color specular;

    /** Exposant de brillance pour l'éclairage spéculaire (Phong). */
    protected double shininess = 0.0;

    /**
     * Construit une forme avec une couleur diffuse et une couleur spéculaire.
     *
     * @param diffuse couleur diffuse
     * @param specular couleur spéculaire
     */
    public Shape(Color diffuse, Color specular) {
        this.diffuse = diffuse;
        this.specular = specular;
    }

    /**
     * Retourne la couleur diffuse du matériau.
     *
     * @return couleur diffuse
     */
    public Color getDiffuse() {
        return diffuse;
    }

    /**
     * Retourne la couleur spéculaire du matériau.
     *
     * @return couleur spéculaire
     */
    public Color getSpecular() {
        return specular;
    }

    /**
     * Définit la couleur diffuse.
     *
     * @param diffuse couleur diffuse
     */
    public void setDiffuse(Color diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * Définit la couleur spéculaire.
     *
     * @param specular couleur spéculaire
     */
    public void setSpecular(Color specular) {
        this.specular = specular;
    }

    /**
     * Retourne l'exposant de brillance de Phong.
     *
     * @return valeur de shininess
     */
    public double getShininess() {
        return shininess;
    }

    /**
     * Définit l'exposant de brillance utilisé par le modèle de Phong.
     *
     * @param shininess coefficient de brillance
     */
    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    /**
     * Retourne la normale de la surface au point donné.
     * Chaque forme calcule la normale selon sa propre géométrie.
     *
     * @param p point concerné
     * @return vecteur normalisé représentant la normale
     */
    public abstract Vector getNormal(Point p);

    /**
     * Teste l'intersection entre cette forme et un rayon.
     *
     * @param r rayon testé
     * @return un Optional contenant une intersection si elle existe
     */
    public abstract java.util.Optional<Intersection> intersect(Ray r);
}
