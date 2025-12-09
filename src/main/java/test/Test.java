package test;

import imgcompare.ImageComparator;
import raytracer.core.Scene;
import raytracer.imaging.ImageRenderer;
import raytracer.parsing.SceneFileParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Lance tous les tests *.test sous src/main/java/test/jalon/...
 * Génère images_gen/<nom>.png et compare avec le PNG de référence
 * situé dans le même dossier que le .test.
 */
public class Test {

    private static final int PIXEL_THRESHOLD = 1000;

    // ----------------------------------------
    // Résultat d'un test individuel
    // ----------------------------------------
    private static class TestResult {
        final String name;
        final String jalon;  
        final boolean skipped;
        final boolean ok;
        final int diffPixels;   // -1 si erreur parsing / exécution
        final String message;

        TestResult(String name, String jalon,
                   boolean skipped, boolean ok,
                   int diffPixels, String message) {
            this.name = name;
            this.jalon = jalon;
            this.skipped = skipped;
            this.ok = ok;
            this.diffPixels = diffPixels;
            this.message = message;
        }
    }

    // ----------------------------------------
    // main
    // ----------------------------------------
    public static void main(String[] args) throws IOException {
        Path testsRoot = (args.length > 0)
                ? Paths.get(args[0])
                : Paths.get("src/main/java/test");

        if (!Files.isDirectory(testsRoot)) {
            System.err.println("Dossier de tests introuvable : " + testsRoot.toAbsolutePath());
            System.exit(1);
        }

        System.out.println("Recherche des .test dans : " + testsRoot.toAbsolutePath());

        List<TestResult> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(testsRoot)) {
            paths.filter(p -> p.getFileName().toString().endsWith(".test"))
                    .sorted()
                    .forEach(p -> results.add(runSingleTest(p)));
        }

        printSummary(results);
    }

    // ----------------------------------------
    // Exécution d'un test
    // ----------------------------------------
    private static TestResult runSingleTest(Path testFile) {
        String fileName = testFile.getFileName().toString(); // p.ex. tp61.test
        String baseName = fileName.substring(0, fileName.lastIndexOf('.')); // tp61

        // jalon = parent direct (tp61.test est dans .../jalon/jalon6/)
        String jalon = testFile.getParent().getFileName().toString(); // jalon6

        Path refImagePath = testFile.getParent().resolve(baseName + ".png");
        if (!Files.exists(refImagePath)) {
            String msg = "[SKIP] Pas d'image de référence pour "
                    + testFile + " (attendu : " + refImagePath + ")";
            System.err.println(msg);
            return new TestResult(baseName, jalon, true, false, 0, msg);
        }

        System.out.println("=== Test " + jalon + " / " + baseName + " ===");
        System.out.println("Scene : " + testFile);
        System.out.println("Ref   : " + refImagePath);

        try {
            // 1) parsing de la scène
            SceneFileParser parser = new SceneFileParser();
            Scene scene = parser.parse(testFile.toString());

            // impose le nom de sortie = baseName.png
            scene.setOutput(baseName + ".png");

            // 2) rendu -> images_gen/baseName.png
            ImageRenderer renderer = new ImageRenderer(scene);
            renderer.render();

            Path generatedImagePath = Paths.get("images_gen").resolve(scene.getOutput());
            System.out.println("Gen   : " + generatedImagePath);

            // 3) chargement des images
            BufferedImage refImg = ImageIO.read(refImagePath.toFile());
            BufferedImage genImg = ImageIO.read(generatedImagePath.toFile());

            if (refImg.getWidth() != genImg.getWidth()
                    || refImg.getHeight() != genImg.getHeight()) {
                String msg = String.format(
                        "[ERREUR] Dimensions différentes : ref=%dx%d, gen=%dx%d",
                        refImg.getWidth(), refImg.getHeight(),
                        genImg.getWidth(), genImg.getHeight());
                System.err.println(msg);
                System.out.println();
                return new TestResult(baseName, jalon, false, false, -1, msg);
            }

            // 4) comparaison
            ImageComparator comparator = new ImageComparator();
            int diffPixels = comparator.countDifferentPixels(refImg, genImg);

            // image de diff (optionnelle mais utile)
            BufferedImage diffImg = comparator.createDiffImage(refImg, genImg);
            Path diffPath = Paths.get("images_gen").resolve(baseName + "_diff.png");
            ImageIO.write(diffImg, "png", diffPath.toFile());

            System.out.println("Différence : " + diffPixels + " pixels");
            System.out.println("Diff   : " + diffPath);

            boolean ok = diffPixels < PIXEL_THRESHOLD;
            System.out.println("=> " + (ok ? "OK" : "KO"));
            System.out.println();

            return new TestResult(baseName, jalon, false, ok, diffPixels,
                    "diff=" + diffPixels + " pixels");

        } catch (Exception e) {
            String msg = "[ERREUR] pendant le test " + testFile + " : " + e.getMessage();
            System.err.println(msg);
            e.printStackTrace();
            System.out.println();
            return new TestResult(baseName, jalon, false, false, -1, msg);
        }
    }

    // ----------------------------------------
    // Résumé global + par jalon
    // ----------------------------------------
    private static void printSummary(List<TestResult> results) {
        int total = results.size();
        int skipped = (int) results.stream().filter(r -> r.skipped).count();
        int executed = total - skipped;
        int ok = (int) results.stream().filter(r -> !r.skipped && r.ok).count();
        int ko = executed - ok;

        int maxDiff = results.stream()
                .filter(r -> !r.skipped && r.diffPixels >= 0)
                .mapToInt(r -> r.diffPixels)
                .max()
                .orElse(0);

        System.out.println("======================================");
        System.out.println("Résumé global");
        System.out.println("Total    : " + total);
        System.out.println("Exécutés : " + executed);
        System.out.println("OK       : " + ok);
        System.out.println("KO       : " + ko);
        System.out.println("SKIP     : " + skipped);
        System.out.println("Diff max : " + maxDiff + " pixels");
        System.out.println("======================================");

        // Résumé par jalon (jalon2, jalon3, jalon4, ...)
        Map<String, List<TestResult>> byJalon = new TreeMap<>();
        for (TestResult r : results) {
            byJalon.computeIfAbsent(r.jalon, k -> new ArrayList<>()).add(r);
        }

        for (Map.Entry<String, List<TestResult>> e : byJalon.entrySet()) {
            String jalon = e.getKey();
            List<TestResult> list = e.getValue();

            int t = list.size();
            int s = (int) list.stream().filter(r -> r.skipped).count();
            int ex = t - s;
            int okJ = (int) list.stream().filter(r -> !r.skipped && r.ok).count();
            int koJ = ex - okJ;
            int maxDiffJ = list.stream()
                    .filter(r -> !r.skipped && r.diffPixels >= 0)
                    .mapToInt(r -> r.diffPixels)
                    .max()
                    .orElse(0);

            System.out.println("---- " + jalon + " ----");
            System.out.println("Total    : " + t);
            System.out.println("Exécutés : " + ex);
            System.out.println("OK       : " + okJ);
            System.out.println("KO       : " + koJ);
            System.out.println("SKIP     : " + s);
            System.out.println("Diff max : " + maxDiffJ + " pixels");
        }
        System.out.println("======================================");
    }
}
