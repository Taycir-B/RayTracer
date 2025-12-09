package raytracer.geometry.shapes;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.core.Intersection;
import raytracer.core.Ray;

import java.util.Optional;

/**
 * Représente une sphère dans la scène de raytracing.
 *
 * Une sphère est définie par :
 *  - un centre ;
 *  - un rayon ;
 *  - des propriétés de matériau héritées de Shape.
 *
 * L'intersection avec un rayon est déterminée en résolvant une équation
 * du second degré. La sphère est l'une des formes les plus simples
 * et les plus courantes en raytracing.
 */
public class Sphere extends Shape {

    /** Centre de la sphère. */
    private Point center;

    /** Rayon de la sphère. */
    private double radius;

    /**
     * Construit une sphère avec centre, rayon et propriétés de matériau.
     *
     * @param centerX coordonnée X du centre
     * @param centerY coordonnée Y du centre
     * @param centerZ coordonnée Z du centre
     * @param radius rayon de la sphère
     * @param diffuse couleur diffuse
     * @param specular couleur spéculaire
     */
    public Sphere(double centerX, double centerY, double centerZ,
                  double radius, Color diffuse, Color specular) {

        super(diffuse, specular);
        this.center = new Point(centerX, centerY, centerZ);
        this.radius = radius;
    }

    /**
     * Retourne le centre de la sphère.
     *
     * @return point représentant le centre
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Retourne le rayon de la sphère.
     *
     * @return rayon
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Calcule la normale au point donné sur la sphère.
     * La normale est simplement la direction du point vers le centre,
     * normalisée.
     *
     * @param point point d'intersection
     * @return vecteur normalisé
     */
    @Override
    public Vector getNormal(Point point) {
        return (Vector) point.subtract(center).normalize();
    }

    /**
     * Teste l'intersection entre la sphère et un rayon.
     *
     * La formule résulte de la résolution de :
     *   (o + t*d - c)² = r²
     *
     * où :
     *  - o est l'origine du rayon,
     *  - d est sa direction,
     *  - c est le centre de la sphère,
     *  - r est le rayon.
     *
     * @param ray rayon à tester
     * @return un Optional contenant l'intersection la plus proche si elle existe
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        Point o = ray.getOrigin();
        Vector d = ray.getDirection();

        Vector oc = (Vector) o.subtract(center);

        double a = d.dot(d);
        double b = 2.0 * oc.dot(d);
        double cTerm = oc.dot(oc) - radius * radius;

        double delta = b * b - 4.0 * a * cTerm;
        if (delta < 0.0) {
            return Optional.empty();
        }

        double sqrtDelta = Math.sqrt(delta);
        double inv2a = 1.0 / (2.0 * a);

        double t1 = (-b - sqrtDelta) * inv2a;
        double t2 = (-b + sqrtDelta) * inv2a;

        double t = Double.POSITIVE_INFINITY;
        double eps = 1e-6;

        if (t1 > eps && t1 < t) {
            t = t1;
        }
        if (t2 > eps && t2 < t) {
            t = t2;
        }

        if (Double.isInfinite(t)) {
            return Optional.empty();
        }

        Point p = ray.at(t);
        return Optional.of(new Intersection(t, p, this));
    }

    /**
     * Retourne une description textuelle de la sphère.
     *
     * @return chaîne contenant centre et rayon
     */
    @Override
    public String toString() {
        return String.format(
            "Sphere{center=(%.2f,%.2f,%.2f), radius=%.2f}",
            center.x, center.y, center.z, radius
        );
    }
}
