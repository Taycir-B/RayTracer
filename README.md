# RayTracer Java

Petit moteur de raytracing en Java 11 qui lit un fichier `.scene`, construit la scène (caméra, lumières, matériaux, géométrie) puis calcule chaque pixel (Lambert + Blinn-Phong + ombres) avant d'écrire un PNG.

- [Vue d'ensemble](#vue-densemble)
- [Prérequis et dépendances](#prérequis-et-dépendances)
- [Commandes utiles](#commandes-utiles)
- [Créer votre scène](#créer-votre-scène)
- [Format `.scene`](#format-scene)
- [Architecture](#architecture)
- [Qualité et tests](#qualité-et-tests)
- [Débogage rapide / FAQ](#débogage-rapide--faq)
- [Pistes d'amélioration](#pistes-damélioration)

## Vue d'ensemble
- Caméra perspective paramétrable (position, cible, up, champ de vision).
- Formes : sphères, triangles (avec `maxverts`/`vertex`), planes infinis.
- Éclairage : ambiant global, diffusion Lambert, spéculaire Blinn-Phong, ombres par rayon d'ombre.
- Parsing strict des scènes `.scene` (bornes de couleur, tailles, indices de sommets) et génération de PNG dans `images_gen/`.

## Prérequis et dépendances
- Java 11+
- Maven 3.8+
- Dépendances Maven principales : `junit-jupiter` (tests), `commons-cli` (CLI), `commons-imaging` (écriture PNG). Les versions sont déclarées dans `pom.xml`.

## Commandes utiles
- Compiler :
  ```bash
  mvn clean package

* Lancer un rendu :

  ```bash
  java -jar target/imgcompare-1.0-SNAPSHOT-raytracer.jar scenes/final.scene
  ```
* Générer la javadoc :

  ```bash
  mvn javadoc:javadoc
  ```
* L'export PNG se trouve dans `images_gen/<nom_output>` où `<nom_output>` vient de la commande `output` du fichier `.scene`.

## Créer votre scène

1. Copiez un exemple depuis `scenes/` et renommez-le.
2. Définissez au minimum :

   * `size <width> <height>`
   * `camera <lookFrom> <lookAt> <up> <fov>`
3. Ajoutez matériaux et lumières (`ambient`, `diffuse`, `specular`, `shininess`, `directional`, `point`).
4. Placez vos géométries (`sphere`, `maxverts`/`vertex`/`tri`, `plane`).
5. Choisissez le nom de sortie avec `output <filename>`.
6. Lancez le rendu : `java -jar target/imgcompare-1.0-SNAPSHOT-raytracer.jar votre.scene`.

### Exemple minimal commenté

```text
size 400 300           # largeur/hauteur de l'image
output demo.png        # nom du fichier écrit dans images_gen/
camera 0 0 3  0 0 0  0 1 0  60   # position, cible, up, FOV
ambient 0.1 0.1 0.1    # lumière ambiante de la scène
diffuse 0.7 0.2 0.2    # couleur diffuse courante
specular 0.8 0.8 0.8   # couleur spéculaire courante
shininess 50           # exposant de brillance
point 3 3 3  1 1 1     # lumière ponctuelle
sphere 0 0 0 1         # géométrie : une sphère de rayon 1 au centre
```

## Format `.scene`

Les commandes disponibles sont listées ci-dessous. Les valeurs sont validées (couleurs dans `[0,1]`, rayons positifs, indices de sommets cohérents, `ambient+diffuse<=1`).

| Commande                                    | Description                            |
| ------------------------------------------- | -------------------------------------- |
| `size <width> <height>`                     | dimensions de l'image (obligatoire)    |
| `output <filename>`                         | nom du fichier PNG de sortie           |
| `camera <lookFrom> <lookAt> <up> <fov>`     | configuration caméra                   |
| `ambient r g b`                             | couleur ambiante globale               |
| `diffuse r g b`                             | couleur diffuse courante               |
| `specular r g b`                            | couleur spéculaire courante            |
| `shininess s`                               | exposant de brillance                  |
| `directional dx dy dz r g b`                | lumière directionnelle                 |
| `point px py pz r g b`                      | lumière ponctuelle                     |
| `sphere x y z r`                            | sphère centrée en (x, y, z) de rayon r |
| `maxverts N` + `vertex x y z` + `tri i j k` | définition de triangles par indices    |
| `plane px py pz nx ny nz`                   | plan infini (point + normale)          |

## Architecture

* `raytracer.app.RaytracerMain` : point d'entrée CLI, parse la scène et déclenche le rendu.
* `raytracer.parsing.SceneFileParser` : lecture ligne à ligne du `.scene`, validations, instanciation des lumières et formes.
* `raytracer.core` : moteur (caméra, rayons, intersections, scène, traceur).
* `raytracer.geometry` : points, vecteurs, repères et formes dans `geometry.shapes`.
* `raytracer.lighting` : lumières directionnelles/ponctuelles (direction, intensité, distance max).
* `raytracer.imaging` : couleurs flottantes et renderer PNG.

## Qualité et tests

* Tests JUnit 5 sur la géométrie, les intersections, l'éclairage et le parsing.
* Lancer :

  ```bash
  mvn test
  ```
* Si Maven ne peut pas accéder au dépôt central, il faut configurer un miroir ou utiliser un cache local.

## Débogage rapide / FAQ

* Image noire : vérifier `size`, `camera` et qu'au moins une lumière est définie.
* Auto-intersections / artefacts brillants : ajouter un léger décalage du rayon d'ombre le long de la normale (non implémenté par défaut).
* Fichier non trouvé : lancer la commande depuis la racine du projet ou utiliser un chemin absolu vers le `.scene`.
* Résolution déformée : adapter le champ de vision ou le ratio `width/height` dans `size`.

## Pistes d'amélioration

* Anti-crénelage (multi-échantillonnage) pour lisser les contours.
* Décalage systématique des rayons d'ombre depuis le point d'intersection pour limiter l'auto-ombre.
* Matériaux avancés (transparence, réfraction) et accélérateur spatial (BVH) pour les grandes scènes.

```
```
