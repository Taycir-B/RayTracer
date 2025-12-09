package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Intersection;
import raytracer.core.Ray;
import raytracer.core.Scene;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SceneIntersectionTest {

    @Test
    public void testNoShapesReturnsEmptyIntersection() {
        Scene scene = new Scene();

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));

        Optional<Intersection> hit = scene.findClosestIntersection(ray);

        assertTrue(hit.isEmpty());
    }

    @Test
    public void testFindClosestIntersectionBetweenTwoSpheres() {
        Scene scene = new Scene();

        // sphère proche
        Sphere near = new Sphere(0, 0, -3, 1.0,
                new Color(1, 0, 0),
                new Color(1, 1, 1));
        // sphère plus loin
        Sphere far = new Sphere(0, 0, -6, 1.0,
                new Color(0, 1, 0),
                new Color(1, 1, 1));

        scene.addShape(near);
        scene.addShape(far);

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));

        Optional<Intersection> hitOpt = scene.findClosestIntersection(ray);

        assertTrue(hitOpt.isPresent());
        Intersection hit = hitOpt.get();

        // On doit toucher la sphère "near" en premier
        assertSame(near, hit.getShape());

        // t doit être ≈ 2 pour la sphère proche
        assertEquals(2.0, hit.getT(), 1e-6);
    }
}