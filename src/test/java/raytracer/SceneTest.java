package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Camera;
import raytracer.core.Scene;
import raytracer.geometry.shapes.Sphere;
import raytracer.imaging.Color;
import raytracer.lighting.PointLight;

import static org.junit.jupiter.api.Assertions.*;

public class SceneTest {

    @Test
    public void testDefaultValues() {
        Scene scene = new Scene();

        assertEquals(0, scene.getWidth());
        assertEquals(0, scene.getHeight());
        assertEquals("output.png", scene.getOutput());
        assertNotNull(scene.getAmbient());
        assertNotNull(scene.getLights());
        assertNotNull(scene.getShapes());
        assertTrue(scene.getLights().isEmpty());
        assertTrue(scene.getShapes().isEmpty());
    }

    @Test
    public void testSetWidthHeight() {
        Scene scene = new Scene();

        scene.setWidth(800);
        scene.setHeight(600);

        assertEquals(800, scene.getWidth());
        assertEquals(600, scene.getHeight());
    }

    @Test
    public void testSetOutput() {
        Scene scene = new Scene();
        scene.setOutput("render.png");

        assertEquals("render.png", scene.getOutput());
    }

    @Test
    public void testSetCamera() {
        Scene scene = new Scene();
        Camera cam = new Camera(
                0,0,4,
                0,0,0,
                0,1,0,
                45
        );

        scene.setCamera(cam);
        assertEquals(cam, scene.getCamera());
    }

    @Test
    public void testAddLight() {
        Scene scene = new Scene();
        PointLight light = new PointLight(1,2,3, 0.5, 0.5, 0.5);

        scene.addLight(light);

        assertEquals(1, scene.getLights().size());
        assertEquals(light, scene.getLights().get(0));
    }

    @Test
    public void testAddShape() {
        Scene scene = new Scene();
        Sphere sphere = new Sphere(0,1,2, 1.5, new Color(1,0,0), new Color(0,0,0));

        scene.addShape(sphere);

        assertEquals(1, scene.getShapes().size());
        assertEquals(sphere, scene.getShapes().get(0));
    }

}
