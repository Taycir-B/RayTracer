package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Intersection;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;
import raytracer.lighting.PointLight;

import static org.junit.jupiter.api.Assertions.*;

public class PhongTest {

    @Test
    public void testStrongSpecularHighlight() {

        Sphere sphere = new Sphere(0, 0, -5, 1,
                new Color(0.0, 0.0, 0.0),      // diffuse nul
                new Color(1.0, 1.0, 1.0));     // spec blanc
        sphere.setShininess(100);             // matériau très brillant

        // Point sur le devant de la sphère
        Point p = new Point(0,0,-4);
        Intersection inter = new Intersection(1.0, p, sphere);

        // Lumière alignée avec la normale → pic spéculaire
        PointLight light = new PointLight(0, 0, 0,
                1, 1, 1);

        // Eye aligné aussi
        Vector eyeDir = new Vector(0, 0, 1);

        Color spec = inter.computePhong(light, eyeDir);

        // Le pic spéculaire doit être très élevé (mais clampé par les couleurs)
        assertTrue(spec.x > 0.5);
        assertTrue(spec.y > 0.5);
        assertTrue(spec.z > 0.5);
    }
}