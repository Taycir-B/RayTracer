package raytracer.geometry;

/**
 * Représente un point dans l'espace 3D.
 *
 * Un point décrit une position absolue, contrairement à un vecteur
 * qui décrit une direction ou une translation. Cette classe hérite
 * des opérations génériques d'AbstractVec3 mais redéfinit certaines
 * méthodes pour respecter la sémantique géométrique.
 */
public class Point extends AbstractVec3 {

    /**
     * Construit un point aux coordonnées données.
     *
     * @param x coordonnée X
     * @param y coordonnée Y
     * @param z coordonnée Z
     */
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Construit le point (0, 0, 0).
     */
    public Point() {
        super(0, 0, 0);
    }

    /**
     * Ajoute un vecteur à ce point, ce qui correspond à une translation.
     *
     * @param other vecteur ajouté au point
     * @return un nouveau point résultant de la translation
     */
    @Override
    public Point add(AbstractVec3 other) {
        return new Point(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Soustrait un autre point de ce point.
     * La différence entre deux points est un vecteur.
     *
     * @param other autre point
     * @return un vecteur représentant la direction et la distance entre les deux points
     */
    @Override
    public Vector subtract(AbstractVec3 other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }
}
