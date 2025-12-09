package raytracer.geometry;

/**
 * Représente un vecteur en trois dimensions.
 *
 * Un vecteur exprime une direction ou un déplacement dans l'espace.
 * Cette classe fournit les opérations vectorielles essentielles :
 *  - produit scalaire ;
 *  - produit vectoriel ;
 *  - addition ;
 *  - soustraction ;
 *  - multiplication par un scalaire.
 *
 * Elle hérite de la structure de données d'AbstractVec3.
 */
public class Vector extends AbstractVec3 {

    /**
     * Construit un vecteur avec les composantes données.
     *
     * @param x composante X
     * @param y composante Y
     * @param z composante Z
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Construit le vecteur nul (0, 0, 0).
     */
    public Vector() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Calcule le produit scalaire entre ce vecteur et un autre.
     * La valeur retournée est :
     *   x1*x2 + y1*y2 + z1*z2
     *
     * @param other autre vecteur
     * @return produit scalaire
     */
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Calcule le produit vectoriel entre ce vecteur et un autre.
     * Le résultat est un vecteur perpendiculaire aux deux vecteurs d'entrée.
     *
     * @param other autre vecteur
     * @return vecteur résultant du produit vectoriel
     */
    public Vector cross(Vector other) {
        return new Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    /**
     * Ajoute un autre triplet à ce vecteur.
     *
     * @param other vecteur ou point représenté sous forme d'AbstractVec3
     * @return un nouveau vecteur résultant de l'addition
     */
    @Override
    public Vector add(AbstractVec3 other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Soustrait un autre triplet de ce vecteur.
     *
     * @param other vecteur ou point représenté sous forme d'AbstractVec3
     * @return un nouveau vecteur résultant de la soustraction
     */
    @Override
    public Vector subtract(AbstractVec3 other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Multiplie ce vecteur par un scalaire.
     *
     * @param scalar coefficient multiplicatif
     * @return un nouveau vecteur mis à l'échelle
     */
    @Override
    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }
}
