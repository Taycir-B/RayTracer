package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Ray;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class RayTest {

    @Test
    public void testRayStoresOriginAndNormalizedDirection() {
        Point origin = new Point(1, 2, 3);
        Vector dir = new Vector(0, 3, 4); // norme 5

        Ray ray = new Ray(origin, dir);

        assertEquals(origin.x, ray.getOrigin().x, 1e-9);
        assertEquals(origin.y, ray.getOrigin().y, 1e-9);
        assertEquals(origin.z, ray.getOrigin().z, 1e-9);

        // direction doit être normalisée
        assertEquals(1.0, ray.getDirection().length(), 1e-9);
    }

    @Test
    public void testAtComputesPointAlongRay() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        Point p = ray.at(2.5);

        assertEquals(2.5, p.x, 1e-9);
        assertEquals(0.0, p.y, 1e-9);
        assertEquals(0.0, p.z, 1e-9);
    }
}