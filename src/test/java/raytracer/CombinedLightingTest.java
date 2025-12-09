package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Camera;
import raytracer.core.Intersection;
import raytracer.core.Scene;
import raytracer.geometry.Point;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;
import raytracer.lighting.PointLight;

import static org.junit.jupiter.api.Assertions.*;

public class CombinedLightingTest {

    @Test
    public void testAmbientPlusLambertPlusPhong() {

        // Matériau brillant
        Sphere sphere = new Sphere(0, 0, -5, 1,
                new Color(0.4, 0.4, 0.4),    // diffuse
                new Color(0.6, 0.6, 0.6));   // specular
        sphere.setShininess(50);

        Point p = new Point(0,0,-4);
        Intersection inter = new Intersection(1.0, p, sphere);

        Scene scene = new Scene();
        scene.addShape(sphere);
        scene.setAmbient(new Color(0.1,0.1,0.1));

        // Eye
        scene.setCamera(new Camera(
                0,0,0,
                0,0,-1,
                0,1,0,
                60));

        // Lumière devant
        PointLight light = new PointLight(0,0,0,
                1,1,1);
        scene.addLight(light);

        Color result = scene.computeColor(inter);

        // Tous les termes doivent augmenter la luminosité totale
        assertTrue(result.x > 0.4);
        assertTrue(result.y > 0.4);
        assertTrue(result.z > 0.4);
    }
}