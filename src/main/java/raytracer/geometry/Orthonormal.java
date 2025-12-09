package raytracer.geometry;

import raytracer.core.Camera;

/**
 * Représente un repère orthonormé construit à partir de la caméra.
 *
 * Ce repère contient trois vecteurs unitaires :
 *  - u : axe horizontal de la caméra
 *  - v : axe vertical de la caméra
 *  - w : axe orienté vers l'arrière de la caméra
 *
 * Le RayTracer utilise ce repère pour construire les directions
 * des rayons primaires à partir des coordonnées de pixels.
 */
public class Orthonormal {

    /** Axe horizontal de la caméra. */
    private final Vector u;

    /** Axe vertical de la caméra. */
    private final Vector v;

    /** Axe arrière de la caméra, opposé à la direction de vue. */
    private final Vector w;

    /**
     * Construit le repère orthonormé associé à une caméra.
     *
     * Méthode utilisée :
     *  - w est la direction normalisée de lookFrom vers lookAt, mais inversée
     *  - u est la normalisation du produit vectoriel up x w
     *  - v est le produit vectoriel w x u
     *
     * @param camera caméra servant de base au repère
     */
    public Orthonormal(Camera camera) {
        Point lookFrom = camera.getLookFrom();
        Point lookAt = camera.getLookAt();
        Vector up = camera.getUp();

        Vector wVec = (Vector) lookFrom.subtract(lookAt).normalize();
        Vector uVec = up.cross(wVec);
        uVec = (Vector) uVec.normalize();
        Vector vVec = wVec.cross(uVec);

        this.u = uVec;
        this.v = vVec;
        this.w = wVec;
    }

    /**
     * Retourne le vecteur u du repère.
     *
     * @return vecteur horizontal
     */
    public Vector getU() {
        return u;
    }

    /**
     * Retourne le vecteur v du repère.
     *
     * @return vecteur vertical
     */
    public Vector getV() {
        return v;
    }

    /**
     * Retourne le vecteur w du repère.
     *
     * @return vecteur arrière
     */
    public Vector getW() {
        return w;
    }
}
