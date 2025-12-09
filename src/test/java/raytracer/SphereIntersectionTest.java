package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Intersection;
import raytracer.core.Ray;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SphereIntersectionTest {

    @Test
    public void testNoIntersectionWhenRayMissesSphere() {
        Sphere sphere = new Sphere(0, 0, -5, 1.0,
                new Color(1, 0, 0),
                new Color(1, 1, 1));

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)); // vers le haut

        Optional<Intersection> hit = sphere.intersect(ray);

        assertTrue(hit.isEmpty());
    }

    @Test
    public void testTwoIntersectionsKeepsNearestPositiveT() {
        Sphere sphere = new Sphere(0, 0, -5, 1.0,
                new Color(1, 0, 0),
                new Color(1, 1, 1));

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));

        Optional<Intersection> hitOpt = sphere.intersect(ray);

        assertTrue(hitOpt.isPresent());
        Intersection hit = hitOpt.get();

        // T attendu : ~4.0 (voir calculs équation du second degré)
        assertEquals(4.0, hit.getT(), 1e-6);

        Point p = hit.getPoint();
        assertEquals(0.0, p.x, 1e-6);
        assertEquals(0.0, p.y, 1e-6);
        assertEquals(-4.0, p.z, 1e-6);
    }

    @Test
    public void testRayStartsInsideSphere() {
        Sphere sphere = new Sphere(0, 0, 0, 2.0,
                new Color(0, 1, 0),
                new Color(1, 1, 1));

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        Optional<Intersection> hitOpt = sphere.intersect(ray);

        assertTrue(hitOpt.isPresent());
        Intersection hit = hitOpt.get();

        // On s'attend à sortir de la sphère à t ≈ 2
        assertEquals(2.0, hit.getT(), 1e-6);
        Point p = hit.getPoint();
        assertEquals(2.0, p.x, 1e-6);
        assertEquals(0.0, p.y, 1e-6);
        assertEquals(0.0, p.z, 1e-6);
    }

    @Test
    public void testTangentIntersection() {
        // sphère centrée sur (0,1,-5), rayon tangent à y=1
        Sphere sphere = new Sphere(0, 1, -5, 1.0,
                new Color(0, 0, 1),
                new Color(1, 1, 1));

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));

        Optional<Intersection> hitOpt = sphere.intersect(ray);

        assertTrue(hitOpt.isPresent());
        Intersection hit = hitOpt.get();

        // On vérifie juste que t > 0
        assertTrue(hit.getT() > 0.0);
    }
}