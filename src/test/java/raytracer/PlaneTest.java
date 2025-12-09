package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Plane;
import raytracer.imaging.Color;

import static org.junit.jupiter.api.Assertions.*;

public class PlaneTest {

    @Test
    public void testPlaneConstructionAndGetters() {
        Color diffuse = new Color(0.3, 0.3, 0.3);
        Color specular = new Color(0.8, 0.8, 0.8);

        Plane p = new Plane(
                1.0, 2.0, 3.0,     // point coordinates
                0.0, 1.0, 0.0,     // normal vector
                diffuse,
                specular
        );

        // Test stored point
        assertEquals(1.0, p.getPoint().x, 1e-6);
        assertEquals(2.0, p.getPoint().y, 1e-6);
        assertEquals(3.0, p.getPoint().z, 1e-6);

        // Test normalized normal
        Vector n = p.getNormal(new Point(0,0,0));  // argument ignored, plane normal is constant
        assertEquals(0.0, n.x, 1e-6);
        assertEquals(1.0, n.y, 1e-6);
        assertEquals(0.0, n.z, 1e-6);

        // Test diffuse and specular
        assertEquals(diffuse.x, p.getDiffuse().x, 1e-6);
        assertEquals(specular.x, p.getSpecular().x, 1e-6);
    }

    @Test
    public void testPlaneToString() {
        Plane p = new Plane(
                0, 1, 2,     // point
                3, 4, 5,     // normal
                new Color(0.1, 0.2, 0.3),
                new Color(0.4, 0.5, 0.6)
        );

        String repr = p.toString();
        assertNotNull(repr);

        assertTrue(repr.startsWith("Plane{"));
        assertTrue(repr.contains("point=("));
        assertTrue(repr.contains("normal=("));
    }
}
