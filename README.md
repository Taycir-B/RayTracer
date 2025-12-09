# RayTracer Java

> Moteur de raytracing éducatif en Java 11 : parse un fichier `.scene`, construit la scène (caméra, lumières, matériaux, géométrie) puis calcule chaque pixel (Lambert + Blinn-Phong + ombres) avant d'écrire un PNG.

- [Aperçu rapide](#aperçu-rapide)
- [Démo express](#démo-express)
- [Workflow de développement](#workflow-de-développement)
- [Créer votre scène](#créer-votre-scène)
- [Format `.scene`](#format-scene)
- [Architecture](#architecture)
- [Qualité et tests](#qualité-et-tests)
- [Débogage rapide / FAQ](#débogage-rapide--faq)
- [Pistes d'amélioration](#pistes-damélioration)

## Aperçu rapide
- Caméra perspective paramétrable (position, cible, up, champ de vision).
- Formes supportées : sphères, triangles (avec `maxverts`/`vertex`), plans infinis.
- Éclairage : ambiant global, diffusion Lambert, spéculaire Blinn-Phong, ombres par rayon d'ombre.
- Parsing strict des scènes `.scene` (bornes de couleur, tailles, indices de sommets) et génération de PNG dans `images_gen/`.

## Démo express
1. **Installer les prérequis** : Java 11+ et Maven 3.8+ disponibles dans votre `$PATH`.
2. **Construire** :
   ```bash
   mvn clean package
   ```
3. **Rendre une scène fournie** :
   ```bash
   java -jar target/imgcompare-1.0-SNAPSHOT-raytracer.jar scenes/final.scene
   ```
4. **Résultat** : l'image est écrite dans `images_gen/<nom_output>` où `<nom_output>` provient de la commande `output` du fichier `.scene`.

> Astuce : vous pouvez modifier `scenes/final.scene` (par ex. intensité lumineuse) puis relancer la commande pour comparer vos rendus.

## Workflow de développement
- **Compiler** : `mvn clean package`
- **Tests unitaires** : `mvn test`
- **Générer la javadoc** : `mvn javadoc:javadoc` (sortie dans `doc/index.html`)
- **Exécuter en local** : `java -jar target/imgcompare-1.0-SNAPSHOT-raytracer.jar <chemin/scene>`

<details>
<summary>Paths utiles</summary>

- `target/*-raytracer.jar` : exécutable principal
- `target/*-imgcompare.jar` : utilitaire de comparaison d'images
- `target/*-jalon-test.jar` : harness de tests de jalon
- `images_gen/` : rendu PNG généré après exécution
</details>

## Créer votre scène
1. Copiez un exemple depuis `scenes/` et renommez-le.
2. Renseignez obligatoirement :
   - `size <width> <height>`
   - `camera <lookFrom> <lookAt> <up> <fov>`
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

| Commande | Description |
| --- | --- |
| `size <width> <height>` | dimensions de l'image (obligatoire) |
| `output <filename>` | nom du fichier PNG de sortie |
| `camera <lookFrom> <lookAt> <up> <fov>` | configuration caméra |
| `ambient r g b` | couleur ambiante globale |
| `diffuse r g b` | couleur diffuse courante |
| `specular r g b` | couleur spéculaire courante |
| `shininess s` | exposant de brillance |
| `directional dx dy dz r g b` | lumière directionnelle |
| `point px py pz r g b` | lumière ponctuelle |
| `sphere x y z r` | sphère centrée en (x, y, z) de rayon r |
| `maxverts N` + `vertex x y z` + `tri i j k` | définition de triangles par indices |
| `plane px py pz nx ny nz` | plan infini (point + normale) |

## Architecture
- `raytracer.app.RaytracerMain` : point d'entrée CLI, parse la scène et déclenche le rendu.
- `raytracer.parsing.SceneFileParser` : lecture ligne à ligne du `.scene`, validations, instanciation des lumières et formes.
- `raytracer.core` : moteur (caméra, rayons, intersections, scène, traceur).
- `raytracer.geometry` : points, vecteurs, repères et formes dans `geometry.shapes`.
- `raytracer.lighting` : lumières directionnelles/ponctuelles (direction, intensité, distance max).
- `raytracer.imaging` : couleurs flottantes et renderer PNG.

## Qualité et tests
- **Tests** : JUnit 5 couvre géométrie, intersections, éclairage et parsing.
- **Lancer** :
  ```bash
  mvn test
  ```
- **Objectif** : `mvn test` doit réussir ; en CI, `mvn clean package` est recommandé pour vérifier la compilation et les tests.

## Débogage rapide / FAQ
- **Image noire** : vérifier `size`, `camera` et qu'au moins une lumière est définie.
- **Auto-intersections / artefacts brillants** : ajouter un léger décalage du rayon d'ombre le long de la normale (non implémenté par défaut).
- **Fichier non trouvé** : lancer la commande depuis la racine du projet ou utiliser un chemin absolu vers le `.scene`.
- **Résolution déformée** : adapter le champ de vision ou le ratio `width/height` dans `size`.

## Pistes d'amélioration
- Anti-crénelage (multi-échantillonnage) pour lisser les contours.
- Décalage systématique des rayons d'ombre depuis le point d'intersection pour limiter l'auto-ombre.
- Matériaux avancés (transparence, réfraction) et accélérateur spatial (BVH) pour les grandes scènes.
