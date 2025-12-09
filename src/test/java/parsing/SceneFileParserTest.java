package parsing;

import org.junit.jupiter.api.Test;
import raytracer.core.Scene;
import raytracer.parsing.SceneFileParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SceneFileParserTest {

    // Utilitaire : crée un fichier temporaire contenant du texte
    private Path makeTempScene(String content) throws IOException {
        Path file = Files.createTempFile("scene_test_", ".scene");
        try (FileWriter fw = new FileWriter(file.toFile())) {
            fw.write(content);
        }
        return file;
    }

    // =====================================================================
    // ====================== TESTS DE BASE =================================
    // =====================================================================

    @Test
    public void testMinimalValidScene() throws Exception {
        String txt =
                "size 100 200\n" +
                "camera 0 0 5  0 0 0  0 1 0  45\n" +
                "ambient 0.2 0.2 0.2\n";

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();
        Scene s = p.parse(f.toString());

        assertEquals(100, s.getWidth());
        assertEquals(200, s.getHeight());
        assertNotNull(s.getCamera());
        assertEquals(0.2, s.getAmbient().x, 1e-9);
    }

    // =====================================================================
    // ========== TESTS ambient + diffuse <= 1 EXACTEMENT ENONCÉ ===========
    // =====================================================================

    @Test
    public void testAmbientPlusDiffuseAllowed() throws Exception {
        // 0.3 + 0.7 = 1.0 OK
        String txt =
                "size 50 50\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.3 0.3 0.3\n" +
                "diffuse 0.7 0.7 0.7\n";

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();
        Scene s = p.parse(f.toString());

        assertEquals(0.3, s.getAmbient().x, 1e-9);
    }

    @Test
    public void testAmbientPlusDiffuseTooHighThrows() throws Exception {
        // 0.6 + 0.5 = 1.1 → interdit → exception
        String txt =
                "size 50 50\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.6 0.6 0.6\n" +
                "diffuse 0.5 0.5 0.5\n";  // dépasse

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();

        assertThrows(IllegalArgumentException.class,
                () -> p.parse(f.toString()));
    }

    @Test
    public void testDiffuseDeclaredFirstStillChecked() throws Exception {
        // Diffuse mis avant ambient, énoncé : validation doit fonctionner dans les deux sens
        String txt =
                "size 80 60\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "diffuse 0.9 0.9 0.9\n" +
                "ambient 0.2 0.2 0.2\n"; // 0.9 + 0.2 > 1 → doit throw

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();

        assertThrows(IllegalArgumentException.class,
                () -> p.parse(f.toString()));
    }

    @Test
    public void testAmbientPlusDiffuseDifferentComponents() throws Exception {
        // Une seule composante dépasse → interdit
        String txt =
                "size 80 60\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.2 0.9 0.2\n" +
                "diffuse 0.3 0.2 0.9\n"; // Y = 0.9 + 0.2 = 1.1 → interdit

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();

        assertThrows(IllegalArgumentException.class,
                () -> p.parse(f.toString()));
    }

    // =====================================================================
    // ====================== TEST SUM LIGHTS ===============================
    // =====================================================================

    @Test
    public void testLightColorSumThrows() throws Exception {
        // Une seule lumière ok : (0.6)
        // Deuxième lumière → (0.6 + 0.6 = 1.2) → interdit
        String txt =
                "size 50 50\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.2 0.2 0.2\n" +
                "diffuse 0.2 0.2 0.2\n" +
                "directional 1 0 0  0.6 0.6 0.6\n" +
                "point 0 0 0  0.6 0.6 0.6\n"; // dépassement somme

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();
        assertThrows(IllegalArgumentException.class,
                () -> p.parse(f.toString()));
    }

    @Test
    public void testLightColorComponentRange() throws Exception {
        // Lumière avec composante > 1
        String txt =
                "size 50 50\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.1 0.1 0.1\n" +
                "diffuse 0.1 0.1 0.1\n" +
                "directional 1 0 0  1.2 0 0\n";

        Path f = makeTempScene(txt);

        SceneFileParser p = new SceneFileParser();
        assertThrows(IllegalArgumentException.class,
                () -> p.parse(f.toString()));
    }
}