package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.geometry.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

    @Test
    public void testDotAndCross() {
        Vector v1 = new Vector(1,0,0);
        Vector v2 = new Vector(0,1,0);

        assertEquals(0, v1.dot(v2), 1e-6);
        Vector cross = v1.cross(v2);
        double dot = v1.dot(v2);
        assertEquals(new Vector(0,0,1).toString(), cross.toString());
    }

    @Test
    public void testLengthAndNormalize() {
        Vector v = new Vector(3,4,0);
        assertEquals(5.0, v.length(), 1e-6);
        Vector n = (Vector) v.normalize();
        assertEquals(1.0, n.length(), 1e-6);
    }
}