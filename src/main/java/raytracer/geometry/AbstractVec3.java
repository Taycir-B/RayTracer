package raytracer.geometry;

/**
 * Classe abstraite représentant un triplet (x, y, z) utilisé pour
 * les vecteurs, les points et les couleurs du moteur de raytracing.
 *
 * Cette classe fournit les opérations communes pour manipuler
 * des valeurs 3D, notamment :
 *  - addition
 *  - soustraction
 *  - multiplication par un scalaire
 *  - produit de Schur
 *  - normalisation
 *  - calcul de la longueur
 *
 * Les classes Point, Vector et Color héritent de cette classe.
 */
public abstract class AbstractVec3 {

    /** Composante x. */
    public double x;

    /** Composante y. */
    public double y;

    /** Composante z. */
    public double z;

    /**
     * Retourne la composante x.
     *
     * @return valeur x
     */
    public double getX(){
        return x;
    }

    /**
     * Retourne la composante y.
     *
     * @return valeur y
     */
    public double getY(){
        return y;
    }

    /**
     * Retourne la composante z.
     *
     * @return valeur z
     */
    public double getZ(){
        return z;
    }

    /**
     * Constructeur permettant d'initialiser les trois composantes.
     *
     * @param x valeur de x
     * @param y valeur de y
     * @param z valeur de z
     */
    public AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructeur par défaut créant un triplet (0, 0, 0).
     */
    public AbstractVec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Ajoute un autre triplet à celui-ci.
     *
     * @param other triplet à ajouter
     * @return un nouveau vecteur contenant la somme
     */
    public AbstractVec3 add(AbstractVec3 other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Soustrait un autre triplet de celui-ci.
     *
     * @param other triplet à soustraire
     * @return un nouveau vecteur correspondant à la différence
     */
    public AbstractVec3 subtract(AbstractVec3 other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Multiplie chaque composante par un scalaire.
     *
     * @param scalar valeur multiplicative
     * @return un nouveau vecteur mis à l'échelle
     */
    public AbstractVec3 multiply(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Produit de Schur : multiplication composante par composante.
     *
     * @param other autre triplet
     * @return un nouveau vecteur contenant le produit terme à terme
     */
    public AbstractVec3 schurMultiply(AbstractVec3 other) {
        return new Vector(x * other.x, y * other.y, z * other.z);
    }

    /**
     * Calcule la longueur euclidienne du vecteur.
     *
     * @return norme du vecteur
     */
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Retourne un vecteur normalisé.
     * Si le vecteur est nul, renvoie (0, 0, 0).
     *
     * @return vecteur unitaire
     */
    public AbstractVec3 normalize() {
        double len = length();
        if (len == 0) return new Vector(0,0,0);
        return new Vector(x/len, y/len, z/len);
    }

    /**
     * Retourne une représentation textuelle du triplet.
     *
     * @return chaîne de caractères représentant (x, y, z)
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    /**
     * Compare deux valeurs flottantes avec une petite tolérance.
     *
     * @param a première valeur
     * @param b seconde valeur
     * @return vrai si la différence absolue est inférieure à un seuil
     */
    protected boolean equalsDouble(double a, double b) {
        double epsilon = 1e-6;
        return Math.abs(a - b) < epsilon;
    }
}