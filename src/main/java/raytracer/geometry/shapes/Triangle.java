package raytracer.geometry.shapes;

import raytracer.core.Intersection;
import raytracer.core.Ray;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.imaging.Color;

import java.util.Optional;

/**
 * Représente un triangle défini par trois points (ordre anti-horaire).
 * Implémentation conforme au Jalon 6.
 */
public class Triangle extends Shape {

    private final Point a, b, c;
    private final Vector normal;

    /**
     * Constructeur utilisé par SceneFileParser.
     */
    public Triangle(Point a, Point b, Point c,
                    Color diffuse, Color specular) {

        super(diffuse, specular);

        this.a = a;
        this.b = b;
        this.c = c;

        // Normale orientée (ordre anti-horaire)
        this.normal = (Vector)b.subtract(a).cross(c.subtract(a)).normalize();
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

        // Rayon parallèle au triangle
        if (Math.abs(denom) < 1e-8) {
            return Optional.empty();
        }

        double t = a.subtract(o).dot(normal) / denom;

        // Intersection derrière la caméra
        if (t < 1e-6) {
            return Optional.empty();
        }

        Point p = ray.at(t);

        // ==== Tests barycentriques (inside test) ====

        if (b.subtract(a).cross(p.subtract(a)).dot(normal) < 0) return Optional.empty();
        if (c.subtract(b).cross(p.subtract(b)).dot(normal) < 0) return Optional.empty();
        if (a.subtract(c).cross(p.subtract(c)).dot(normal) < 0) return Optional.empty();

        return Optional.of(new Intersection(t, p, this));
    }

    @Override
    public String toString() {
        return "Triangle{" + a + "," + b + "," + c + "}";
    }
}