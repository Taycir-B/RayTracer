package raytracer.geometry.shapes;

import raytracer.core.Intersection;
import raytracer.core.Ray;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.imaging.Color;

import java.util.Optional;

/**
 * Représente un plan infini défini par un point et une normale.
 * Implémentation conforme au Jalon 6.
 */
public class Plane extends Shape {

    private final Point q;      // Un point appartenant au plan
    private final Vector normal; // Normale normalisée

    /**
     * Constructeur utilisé par SceneFileParser.
     *
     * @param px point du plan
     * @param py point du plan
     * @param pz point du plan
     * @param nx normale
     * @param ny normale
     * @param nz normale
     */
    public Plane(double px, double py, double pz,
                 double nx, double ny, double nz,
                 Color diffuse, Color specular) {

        super(diffuse, specular);
        this.q = new Point(px, py, pz);
        this.normal = (Vector)new Vector(nx, ny, nz).normalize();
    }

    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    @Override
    public Optional<Intersection> intersect(Ray ray) {

        Point o = ray.getOrigin();
        Vector d = ray.getDirection();

        double denom = d.dot(normal);

        // Rayon parallèle au plan
        if (Math.abs(denom) < 1e-8) {
            return Optional.empty();
        }

        double t = q.subtract(o).dot(normal) / denom;

        // Intersection derrière la caméra
        if (t < 1e-6) {
            return Optional.empty();
        }

        Point p = ray.at(t);
        return Optional.of(new Intersection(t, p, this));
    }

    @Override
    public String toString() {
        return "Plane{point=" + q + ", normal=" + normal + "}";
    }
}