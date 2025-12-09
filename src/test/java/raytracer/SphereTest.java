package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;

import static org.junit.jupiter.api.Assertions.*;

public class SphereTest {

    @Test
    public void testSphereConstructionAndGetters() {
        Color diffuse = new Color(0.2, 0.3, 0.4);
        Color specular = new Color(0.9, 0.9, 0.9);

        Sphere s = new Sphere(
                1.0, 2.0, 3.0,
                4.5,
                diffuse,
                specular
        );

        // Test center coordinates
        assertEquals(1.0, s.getCenter().x, 1e-6);
        assertEquals(2.0, s.getCenter().y, 1e-6);
        assertEquals(3.0, s.getCenter().z, 1e-6);

        // Test radius
        assertEquals(4.5, s.getRadius(), 1e-6);

        // Test diffuse and specular
        assertEquals(diffuse.x, s.getDiffuse().x, 1e-6);
        assertEquals(specular.x, s.getSpecular().x, 1e-6);
    }

    @Test
    public void testSphereToString() {
        Sphere s = new Sphere(
                0, 1, 2,
                3.0,
                new Color(0.1, 0.2, 0.3),
                new Color(0.5, 0.6, 0.7)
        );

        String repr = s.toString();

        assertTrue(repr.startsWith("Sphere{"));
        assertTrue(repr.contains("center="));
        assertTrue(repr.contains("radius="));
    }
}
