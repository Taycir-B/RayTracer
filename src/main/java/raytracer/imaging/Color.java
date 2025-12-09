package raytracer.imaging;

import raytracer.geometry.AbstractVec3;

/**
 * Représente une couleur RGB avec des composantes flottantes comprises entre 0 et 1.
 *
 * Une couleur est définie par trois valeurs :
 *  - rouge
 *  - vert
 *  - bleu
 *
 * Cette classe permet également :
 *  - l'addition de couleurs
 *  - la multiplication par un scalaire
 *  - le produit de Schur (multiplication composante par composante)
 *  - la conversion en entier RGB utilisable pour l'écriture d'images
 */
public class Color extends AbstractVec3 {

    /**
     * Construit une couleur à partir des trois composantes.
     *
     * @param x composante rouge
     * @param y composante verte
     * @param z composante bleue
     */
    public Color(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construit la couleur noire (0, 0, 0).
     */
    public Color() {
        super(0, 0, 0);
    }

    /**
     * Assure que chaque composante reste dans l'intervalle [0, 1].
     * Cette méthode n'est pas utilisée automatiquement mais reste disponible
     * si une clamping explicite est souhaitée.
     */
    private void clamp() {
        x = Math.min(1, Math.max(0, x));
        y = Math.min(1, Math.max(0, y));
        z = Math.min(1, Math.max(0, z));
    }

    /**
     * Convertit la couleur en un entier RGB 24 bits utilisable par ImageIO.
     * Les composantes sont d'abord bornées dans l'intervalle [0, 1].
     *
     * @return valeur entière au format 0xRRGGBB
     */
    public int toRGB() {
        double cx = Math.min(1.0, Math.max(0.0, x));
        double cy = Math.min(1.0, Math.max(0.0, y));
        double cz = Math.min(1.0, Math.max(0.0, z));

        int red = (int) Math.round(cx * 255.0);
        int green = (int) Math.round(cy * 255.0);
        int blue = (int) Math.round(cz * 255.0);

        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Ajoute une autre couleur ou un triplet à cette couleur.
     *
     * @param other autre couleur ou triplet
     * @return une nouvelle couleur résultant de l'addition
     */
    @Override
    public Color add(AbstractVec3 other) {
        return new Color(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Multiplie cette couleur par un scalaire.
     *
     * @param scalar valeur multiplicative
     * @return une nouvelle couleur mise à l'échelle
     */
    @Override
    public Color multiply(double scalar) {
        return new Color(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Produit de Schur entre deux couleurs ou triplets.
     * Chaque composante est multipliée indépendamment.
     *
     * @param other autre couleur ou triplet
     * @return une nouvelle couleur représentant le produit composante par composante
     */
    @Override
    public Color schurMultiply(AbstractVec3 other) {
        return new Color(x * other.x, y * other.y, z * other.z);
    }
}
