package raytracer;

import raytracer.core.Intersection;
import raytracer.core.Ray;
import raytracer.core.Scene;
import raytracer.geometry.Orthonormal;
import raytracer.geometry.Vector;
import raytracer.imaging.Color;

import java.util.Optional;

/**
 * Traceur de rayons pour calculer la couleur d'un pixel.
 * Jalon 4 : ambient + Lambert sur toutes les lumières.
 */
public class RayTracer {

    private final Scene scene;
    private final Orthonormal basis;
    private final double pixelWidth;
    private final double pixelHeight;

    public RayTracer(Scene scene) {
        this.scene = scene;
        this.basis = new Orthonormal(scene.getCamera());

        int imgWidth = scene.getWidth();
        int imgHeight = scene.getHeight();
        double fov = scene.getCamera().getFieldOfView();

        double fovr = Math.toRadians(fov);
        double halfHeight = Math.tan(fovr / 2.0);
        double halfWidth = halfHeight * imgWidth / imgHeight;

        this.pixelHeight = (2.0 * halfHeight) / imgHeight;
        this.pixelWidth  = (2.0 * halfWidth)  / imgWidth;
    }

    public Color getPixelColor(int i, int j) {
        int imgWidth = scene.getWidth();
        int imgHeight = scene.getHeight();

        double x = ((i + 0.5) - imgWidth / 2.0) * pixelWidth;
        double y = (imgHeight / 2.0 - (j + 0.5)) * pixelHeight;

        Vector u = basis.getU();
        Vector v = basis.getV();
        Vector w = basis.getW();

        Vector dir = u.multiply(x)
                      .add(v.multiply(y))
                      .subtract(w);

        Ray ray = new Ray(scene.getCamera().getLookFrom(), dir);

        Optional<Intersection> hit = scene.findClosestIntersection(ray);

        if (hit.isPresent()) {
            // Jalon 4 : on calcule la couleur du point (ambient + Lambert)
            return scene.computeColor(hit.get());
        } else {
            // Fond noir
            return new Color(0, 0, 0);
        }
    }
}