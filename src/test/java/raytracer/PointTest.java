package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    public void testSubtractPointsGivesVector() {
        Point p1 = new Point(3,4,5);
        Point p2 = new Point(1,1,1);

        Vector v = p1.subtract(p2);

        assertEquals("(2.0,3.0,4.0)", v.toString());
    }

    @Test
    public void testAddVectorToPointGivesPoint() {
        Point p = new Point(1,1,1);
        Vector v = new Vector(2,3,4);

        Point result = p.add(v);

        assertEquals("(3.0,4.0,5.0)", result.toString());
    }

    @Test
    public void testSubtractVectorFromPoint() {
        Point p = new Point(5,5,5);
        Vector v = new Vector(1,2,3);

        Vector result = p.subtract(v);

        assertEquals("(4.0,3.0,2.0)", result.toString());
    }
}
