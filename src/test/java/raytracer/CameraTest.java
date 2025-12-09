package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.core.Camera;

import static org.junit.jupiter.api.Assertions.*;

public class CameraTest {

    @Test
    public void testCameraConstructionAndGetters() {
        Camera cam = new Camera(
                0, 0, 5,
                0, 0, 0,
                0, 1, 0,
                45
        );

        assertEquals(0, cam.getLookFrom().x, 1e-6);
        assertEquals(0, cam.getLookFrom().y, 1e-6);
        assertEquals(5, cam.getLookFrom().z, 1e-6);

        assertEquals(0, cam.getLookAt().x, 1e-6);
        assertEquals(0, cam.getLookAt().y, 1e-6);
        assertEquals(0, cam.getLookAt().z, 1e-6);

        assertEquals(0, cam.getUp().x, 1e-6);
        assertEquals(1, cam.getUp().y, 1e-6);
        assertEquals(0, cam.getUp().z, 1e-6);

        assertEquals(45, cam.getFieldOfView(), 1e-6);
    }

    @Test
    public void testCameraToString() {
        Camera cam = new Camera(
                0, 0, 5,
                0, 0, 0,
                0, 1, 0,
                45
        );

        String repr = cam.toString();

        assertTrue(repr.startsWith("Camera{"));
        assertTrue(repr.contains("from="));
        assertTrue(repr.contains("at="));
        assertTrue(repr.contains("fov="));
    }
}
