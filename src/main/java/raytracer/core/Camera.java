package raytracer.core;

import raytracer.geometry.Point;
import raytracer.geometry.Vector;

/**
 * Représente la caméra utilisée pour générer l'image dans le raytracer.
 * La caméra définit :
 *  - la position de l'observateur ;
 *  - le point visé dans la scène ;
 *  - la direction verticale (up) qui définit l'orientation ;
 *  - le champ de vision en degrés.
 *
 * La caméra est utilisée par le RayTracer pour construire un repère
 * orthonormé permettant de calculer la direction de chaque rayon.
 */
public class Camera {

    /** Position de la caméra dans la scène. */
    private Point lookFrom;

    /** Point ciblé par le regard de la caméra. */
    private Point lookAt;

    /** Direction considérée comme verticale pour la caméra. */
    private Vector up;

    /** Champ de vision vertical en degrés. */
    private double fieldOfView;

    /**
     * Construit une caméra définie par sa position, son orientation
     * et son champ de vision.
     *
     * @param lookFromX composante X de la position de l'observateur
     * @param lookFromY composante Y de la position de l'observateur
     * @param lookFromZ composante Z de la position de l'observateur
     * @param lookAtX composante X du point visé
     * @param lookAtY composante Y du point visé
     * @param lookAtZ composante Z du point visé
     * @param upX composante X du vecteur vertical
     * @param upY composante Y du vecteur vertical
     * @param upZ composante Z du vecteur vertical
     * @param fov champ de vision vertical en degrés
     */
    public Camera(double lookFromX, double lookFromY, double lookFromZ,
                  double lookAtX, double lookAtY, double lookAtZ,
                  double upX, double upY, double upZ, double fov) {

        this.lookFrom = new Point(lookFromX, lookFromY, lookFromZ);
        this.lookAt = new Point(lookAtX, lookAtY, lookAtZ);
        this.up = new Vector(upX, upY, upZ);
        this.fieldOfView = fov;
    }

    /**
     * Retourne la position de la caméra.
     *
     * @return point représentant la position de l'observateur
     */
    public Point getLookFrom() {
        return lookFrom;
    }

    /**
     * Retourne le point visé par la caméra.
     *
     * @return point que la caméra regarde
     */
    public Point getLookAt() {
        return lookAt;
    }

    /**
     * Retourne la direction verticale utilisée pour définir l'orientation.
     *
     * @return vecteur vertical
     */
    public Vector getUp() {
        return up;
    }

    /**
     * Retourne le champ de vision vertical de la caméra.
     *
     * @return champ de vision en degrés
     */
    public double getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Retourne une description textuelle de la caméra.
     *
     * @return chaîne décrivant position, cible et fov
     */
    @Override
    public String toString() {
        return String.format(
            "Camera{from=%.2f,%.2f,%.2f, at=%.2f,%.2f,%.2f, fov=%.1f°}",
            lookFrom.x, lookFrom.y, lookFrom.z,
            lookAt.x, lookAt.y, lookAt.z,
            fieldOfView
        );
    }
}
