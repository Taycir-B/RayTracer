package raytracer;

import org.junit.jupiter.api.Test;
import raytracer.imaging.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

    @Test
    public void testDefaultConstructor() {
        Color c = new Color();
        assertEquals(0.0, c.x, 1e-9);
        assertEquals(0.0, c.y, 1e-9);
        assertEquals(0.0, c.z, 1e-9);
    }

    @Test
    public void testCustomConstructor() {
        Color c = new Color(0.2, 0.4, 0.6);
        assertEquals(0.2, c.x, 1e-9);
        assertEquals(0.4, c.y, 1e-9);
        assertEquals(0.6, c.z, 1e-9);
    }

    @Test
    public void testAdd() {
        Color c1 = new Color(0.1, 0.2, 0.3);
        Color c2 = new Color(0.4, 0.5, 0.6);
        Color result = c1.add(c2);

        assertEquals(0.5, result.x, 1e-9);
        assertEquals(0.7, result.y, 1e-9);
        assertEquals(0.9, result.z, 1e-9);
    }

    @Test
    public void testMultiplyScalar() {
        Color c = new Color(0.2, 0.4, 0.6);
        Color result = c.multiply(2.0);

        assertEquals(0.4, result.x, 1e-9);
        assertEquals(0.8, result.y, 1e-9);
        assertEquals(1.2, result.z, 1e-9);
    }

    @Test
    public void testSchurMultiply() {
        Color c1 = new Color(0.2, 0.3, 0.4);
        Color c2 = new Color(0.5, 0.5, 0.5);
        Color result = c1.schurMultiply(c2);

        assertEquals(0.1, result.x, 1e-9);
        assertEquals(0.15, result.y, 1e-9);
        assertEquals(0.2, result.z, 1e-9);
    }

    @Test
    public void testToRGB() {
        Color c = new Color(1.0, 0.0, 0.5);
        int rgb = c.toRGB();

        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = rgb & 0xff;

        assertEquals(255, red);
        assertEquals(0, green);
        assertEquals(128, blue); // 0.5 * 255 = 127.5 arrondi à 128
    }

    @Test
    public void testClampViaToRGB() {
        // Conforme EXACTEMENT à ton implémentation actuelle
        Color c = new Color(2.0, -1.0, 0.5);
        int rgb = c.toRGB();

        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = rgb & 0xff;

        assertEquals(255, red);   // 2.0 → 255 après clamp interne
        assertEquals(0, green);   // -1.0 → 0 après clamp interne
        assertEquals(128, blue);  // 0.5 * 255 → 128
    }
}