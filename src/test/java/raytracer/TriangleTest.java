package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Triangle;
import raytracer.imaging.Color;

import static org.junit.jupiter.api.Assertions.*;

public class TriangleTest {

    @Test
    public void testTriangleConstructionAndGetters() {
        Point v0 = new Point(0, 0, 0);
        Point v1 = new Point(1, 0, 0);
        Point v2 = new Point(0, 1, 0);

        Color diffuse = new Color(0.2, 0.3, 0.4);
        Color specular = new Color(0.8, 0.8, 0.8);

        Triangle tri = new Triangle(v0, v1, v2, diffuse, specular);

        // Check vertices
        assertEquals(v0, tri.getV0());
        assertEquals(v1, tri.getV1());
        assertEquals(v2, tri.getV2());

        // Check shape colors
        assertEquals(diffuse.x, tri.getDiffuse().x, 1e-6);
        assertEquals(specular.x, tri.getSpecular().x, 1e-6);
    }

    @Test
    public void testComputeNormal() {
        Point v0 = new Point(0, 0, 0);
        Point v1 = new Point(1, 0, 0);
        Point v2 = new Point(0, 1, 0);

        Triangle tri = new Triangle(
                v0, v1, v2,
                new Color(0.2, 0.3, 0.4),
                new Color(0.8, 0.8, 0.8)
        );

        Vector normal = tri.computeNormal();

        // Expected normal = (0,0,1)
        assertEquals(0.0, normal.x, 1e-6);
        assertEquals(0.0, normal.y, 1e-6);
        assertEquals(1.0, normal.z, 1e-6);
    }

    @Test
    public void testTriangleToString() {
        Triangle tri = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0),
                new Color(0.1, 0.2, 0.3),
                new Color(0.4, 0.5, 0.6)
        );

        String repr = tri.toString();

        assertTrue(repr.startsWith("Triangle{"));
        assertTrue(repr.contains("v0="));
        assertTrue(repr.contains("v1="));
        assertTrue(repr.contains("v2="));
    }
}
