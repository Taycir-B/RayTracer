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

public class LightingTest {

    @Test
    public void testLambertFullIntensityWithPointLight() {

        Color diffuse = new Color(0.4, 0.2, 0.1);
        Color specular = new Color(0.0, 0.0, 0.0);
        Sphere sphere = new Sphere(0, 0, -5, 1.0, diffuse, specular);

        Point p = new Point(0, 0, -4);
        Intersection inter = new Intersection(1.0, p, sphere);

        PointLight light = new PointLight(
                0.0, 0.0, 0.0,
                1.0, 1.0, 1.0
        );

        Color lambert = inter.computeLambert(light);

        assertEquals(diffuse.x, lambert.x, 1e-6);
        assertEquals(diffuse.y, lambert.y, 1e-6);
        assertEquals(diffuse.z, lambert.z, 1e-6);
    }

    @Test
    public void testLambertZeroWhenLightBehindSurface() {

        Color diffuse = new Color(0.4, 0.2, 0.1);
        Sphere sphere = new Sphere(0, 0, -5, 1.0, diffuse, new Color(0,0,0));

        Point pBack = new Point(0, 0, -6);
        Intersection interBack = new Intersection(1.0, pBack, sphere);

        PointLight light = new PointLight(
                0.0, 0.0, 0.0,
                1.0, 1.0, 1.0
        );

        Color lambertBack = interBack.computeLambert(light);

        assertEquals(0.0, lambertBack.x, 1e-6);
        assertEquals(0.0, lambertBack.y, 1e-6);
        assertEquals(0.0, lambertBack.z, 1e-6);
    }

    @Test
    public void testSceneComputeColorAmbientPlusLambert() {

        Color diffuse = new Color(0.4, 0.4, 0.4);
        Sphere sphere = new Sphere(0, 0, -5, 1.0, diffuse, new Color(0,0,0));
        Point p = new Point(0, 0, -4);
        Intersection inter = new Intersection(1.0, p, sphere);

        Scene scene = new Scene();
        scene.setAmbient(new Color(0.1, 0.1, 0.1));

        // ❗ JALON 5 : computeColor a besoin d'une caméra (Phong)
        scene.setCamera(new Camera(
                0, 0, 0,     // lookFrom
                0, 0, -5,    // lookAt
                0, 1, 0,     // up
                60           // fov
        ));

        PointLight light = new PointLight(
                0.0, 0.0, 0.0,
                0.5, 0.5, 0.5
        );
        scene.addLight(light);

        Color result = scene.computeColor(inter);

        assertEquals(0.3, result.x, 1e-6);
        assertEquals(0.3, result.y, 1e-6);
        assertEquals(0.3, result.z, 1e-6);
    }
}