package raytracer.geometry.shapes;

import raytracer.imaging.Color;
import raytracer.geometry.Point;
import raytracer.geometry.Vector;
import raytracer.core.Intersection;
import raytracer.core.Ray;

import java.util.Optional;

/**
 * Représente un plan infini dans la scène.
 *
 * Un plan est défini par :
 *  - un point situé sur le plan ;
 *  - une normale au plan ;
 *  - des propriétés de matériau héritées de la classe Shape.
 *
 * L'intersection avec un rayon est déterminée à l'aide de la formule :
 *    t = ((p0 - o) · n) / (d · n)
 * où :
 *  - p0 est un point du plan,
 *  - o est l'origine du rayon,
 *  - d est la direction du rayon,
 *  - n est la normale du plan.
 *
 * Si d · n est nul, le rayon est parallèle au plan et ne l'intersecte pas.
 * Si t est négatif ou très proche de zéro, l'intersection est ignorée.
 */
public class Plane extends Shape {

    /** Un point appartenant au plan. */
    private Point point;

    /** La normale du plan. */
    private Vector normal;

    /**
     * Construit un plan en fournissant un point du plan et une normale.
     *
     * @param pointX coordonnée X du point du plan
     * @param pointY coordonnée Y du point du plan
     * @param pointZ coordonnée Z du point du plan
     * @param normalX composante X de la normale
     * @param normalY composante Y de la normale
     * @param normalZ composante Z de la normale
     * @param diffuse couleur diffuse du matériau
     * @param specular couleur spéculaire du matériau
     */
    public Plane(double pointX, double pointY, double pointZ,
                 double normalX, double normalY, double normalZ,
                 Color diffuse, Color specular) {

        super(diffuse, specular);
        this.point = new Point(pointX, pointY, pointZ);
        this.normal = new Vector(normalX, normalY, normalZ);
    }

    /**
     * Retourne un point appartenant au plan.
     *
     * @return point de référence du plan
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Retourne la normale telle que stockée dans la forme
     * sans normalisation.
     *
     * @return vecteur normal non normalisé
     */
    public Vector getStoredNormal() {
        return normal;
    }

    /**
     * Retourne la normale normalisée au point donné.
     * Pour un plan, elle est constante.
     *
     * @param p point où la normale est demandée
     * @return vecteur normalisé
     */
    @Override
    public Vector getNormal(Point p) {
        return (Vector) normal.normalize();
    }

    /**
     * Calcule l'intersection entre ce plan et un rayon.
     *
     * @param ray rayon testé
     * @return un Optional contenant l'intersection si elle existe
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        Vector n = getNormal(null);
        Point o = ray.getOrigin();
        Vector d = ray.getDirection();

        double denom = n.dot(d);
        double eps = 1e-6;

        if (Math.abs(denom) < eps) {
            return Optional.empty();
        }

        double t = (point.subtract(o)).dot(n) / denom;

        if (t <= eps) {
            return Optional.empty();
        }

        Point hitPoint = ray.at(t);
        return Optional.of(new Intersection(t, hitPoint, this));
    }

    /**
     * Retourne une description textuelle du plan.
     *
     * @return chaîne contenant point et normale
     */
    @Override
    public String toString() {
        return String.format(
            "Plane{point=(%.2f,%.2f,%.2f), normal=(%.2f,%.2f,%.2f)}",
            point.x, point.y, point.z,
            normal.x, normal.y, normal.z
        );
    }
}
