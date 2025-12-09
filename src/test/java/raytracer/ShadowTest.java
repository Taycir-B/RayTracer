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

public class ShadowTest {

    @Test
    public void testPointIsInShadow() {

        // Petite sphère éclairée
        Sphere small = new Sphere(0, 0, -5, 1,
                new Color(0.4, 0.4, 0.4),
                new Color(0.5, 0.5, 0.5));
        small.setShininess(50);

        // Grande sphère qui bloque la lumière
        Sphere blocker = new Sphere(0, 0, -3, 1.5,
                new Color(0.2, 0.2, 0.2),
                new Color(0.0, 0.0, 0.0));

        // Lumière derrière le blocker
        PointLight light = new PointLight(0, 0, 0,
                1, 1, 1);

        Scene scene = new Scene();
        scene.addShape(small);
        scene.addShape(blocker);
        scene.addLight(light);

        scene.setAmbient(new Color(0.1, 0.1, 0.1));
        scene.setCamera(new Camera(
                0,0,0,
                0,0,-1,
                0,1,0,
                60));

        // Intersection sur la petite sphère
        Point p = new Point(0,0,-4); // sur la face avant
        Intersection inter = new Intersection(1.0, p, small);

        Color result = scene.computeColor(inter);

        // Puisque le blocker masque la lumière, Lambert et Phong doivent être nuls
        assertEquals(0.1, result.x, 1e-6);
        assertEquals(0.1, result.y, 1e-6);
        assertEquals(0.1, result.z, 1e-6);
    }
}