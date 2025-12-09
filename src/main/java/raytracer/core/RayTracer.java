package raytracer.core;

import raytracer.imaging.Color;
import raytracer.geometry.Orthonormal;
import raytracer.geometry.Vector;

import java.util.Optional;

/**
 * Traceur de rayons responsable du calcul de la couleur d'un pixel.
 *
 * Le RayTracer génère des rayons primaires à partir de la caméra, détermine
 * la première forme intersectée par le rayon, puis demande à la scène de
 * calculer la couleur finale au point d'intersection.
 *
 * Fonctionnement selon les jalons :
 *  - Jalon 3 : renvoie simplement la couleur diffuse de l'objet touché
 *              ou du noir si rien n'est touché.
 *  - Jalon 4 : ajoute la diffusion Lambert.
 *  - Jalon 5 : ajoute l'éclairage spéculaire de Phong et la gestion des ombres.
 */
public class RayTracer {

    /** Scène utilisée pour le rendu. */
    private final Scene scene;

    /** Repère orthonormé construit à partir de la caméra. */
    private final Orthonormal basis;

    /** Largeur d'un pixel en coordonnées caméra. */
    private final double pixelWidth;

    /** Hauteur d'un pixel en coordonnées caméra. */
    private final double pixelHeight;

    /**
     * Construit le RayTracer et pré-calcule les informations nécessaires
     * pour convertir les coordonnées de pixel en directions de rayons.
     *
     * @param scene scène à tracer
     */
    public RayTracer(Scene scene) {
        this.scene = scene;
        this.basis = new Orthonormal(scene.getCamera());

        int imgWidth = scene.getWidth();
        int imgHeight = scene.getHeight();
        double fov = scene.getCamera().getFieldOfView();

        double fovRad = Math.toRadians(fov);
        double halfHeight = Math.tan(fovRad / 2.0);
        double halfWidth = halfHeight * imgWidth / imgHeight;

        this.pixelHeight = (2.0 * halfHeight) / imgHeight;
        this.pixelWidth = (2.0 * halfWidth) / imgWidth;
    }

    /**
     * Calcule la couleur d'un pixel situé aux coordonnées (i, j).
     *
     * Étapes du calcul :
     *  1. Conversion des coordonnées de pixel en coordonnées caméra.
     *  2. Construction d'une direction de rayon dans le repère (u, v, w).
     *  3. Lancement du rayon depuis l'œil.
     *  4. Recherche de l'intersection la plus proche.
     *  5. Appel à scene.computeColor si un objet est touché.
     *  6. Retourne noir si aucun objet n'est intersecté.
     *
     * @param i coordonnée X du pixel
     * @param j coordonnée Y du pixel
     * @return couleur calculée pour le pixel
     */
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
            return scene.computeColor(hit.get());
        }

        return new Color(0, 0, 0);
    }
}
