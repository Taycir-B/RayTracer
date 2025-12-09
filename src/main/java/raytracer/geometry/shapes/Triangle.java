package raytracer.geometry.shapes;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.core.Intersection;
import raytracer.core.Ray;

import java.util.Optional;

/**
 * Représente un triangle dans la scène de raytracing.
 *
 * Un triangle est défini par trois sommets.
 * Il s'agit d'une primitive plane, utilisée pour modéliser
 * des surfaces polygonales ou des maillages.
 *
 * L'intersection avec un rayon est calculée en utilisant
 * l'algorithme de Möller–Trumbore, connu pour sa rapidité
 * et son efficacité en raytracing.
 */
public class Triangle extends Shape {

    /** Premier sommet du triangle. */
    private Point v0;

    /** Deuxième sommet du triangle. */
    private Point v1;

    /** Troisième sommet du triangle. */
    private Point v2;

    /**
     * Construit un triangle à partir de trois sommets et de propriétés de matériau.
     *
     * @param v0 premier sommet
     * @param v1 deuxième sommet
     * @param v2 troisième sommet
     * @param diffuse couleur diffuse
     * @param specular couleur spéculaire
     */
    public Triangle(Point v0, Point v1, Point v2,
                    Color diffuse, Color specular) {

        super(diffuse, specular);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    /** @return le sommet v0 */
    public Point getV0() { return v0; }

    /** @return le sommet v1 */
    public Point getV1() { return v1; }

    /** @return le sommet v2 */
    public Point getV2() { return v2; }

    /**
     * Calcule une normale non normalisée du triangle.
     * La normale est donnée par :
     *   (v1 - v0) × (v2 - v0)
     *
     * @return un vecteur normal non normalisé
     */
    public Vector computeNormal() {
        Vector edge1 = (Vector) v1.subtract(v0);
        Vector edge2 = (Vector) v2.subtract(v0);
        return edge1.cross(edge2);
    }

    /**
     * Retourne la normale normalisée du triangle.
     *
     * @param p point concerné (ignoré car la normale est uniforme)
     * @return vecteur normalisé
     */
    @Override
    public Vector getNormal(Point p) {
        return (Vector) computeNormal().normalize();
    }

    /**
     * Teste l'intersection entre un rayon et ce triangle
     * en utilisant l'algorithme de Möller–Trumbore.
     *
     * L'algorithme calcule :
     *  - la position de l'intersection potentielle ;
     *  - les coordonnées barycentriques u et v ;
     *  - la valeur t du paramètre du rayon.
     *
     * Une intersection valide doit respecter :
     *  - t > 0 ;
     *  - 0 <= u <= 1 ;
     *  - 0 <= v <= 1 ;
     *  - u + v <= 1.
     *
     * @param ray rayon testé
     * @return un Optional contenant l'intersection si elle existe
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        Vector dir = ray.getDirection();
        Point o = ray.getOrigin();

        Vector edge1 = (Vector) v1.subtract(v0);
        Vector edge2 = (Vector) v2.subtract(v0);

        Vector h = dir.cross(edge2);
        double a = edge1.dot(h);
        double eps = 1e-6;

        // Rayon parallèle au plan du triangle
        if (Math.abs(a) < eps) {
            return Optional.empty();
        }

        double f = 1.0 / a;
        Vector s = (Vector) o.subtract(v0);
        double u = f * s.dot(h);

        if (u < 0.0 || u > 1.0) {
            return Optional.empty();
        }

        Vector q = s.cross(edge1);
        double v = f * dir.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return Optional.empty();
        }

        double t = f * edge2.dot(q);

        if (t <= eps) {
            return Optional.empty();
        }

        Point hitPoint = ray.at(t);
        return Optional.of(new Intersection(t, hitPoint, this));
    }

    /**
     * Retourne une description textuelle du triangle.
     *
     * @return chaîne contenant les trois sommets
     */
    @Override
    public String toString() {
        return String.format(
            "Triangle{v0=(%.2f,%.2f,%.2f), v1=(%.2f,%.2f,%.2f), v2=(%.2f,%.2f,%.2f)}",
            v0.x, v0.y, v0.z,
            v1.x, v1.y, v1.z,
            v2.x, v2.y, v2.z
        );
    }
}
