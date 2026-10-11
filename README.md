# GastinelWeatherMod

Mod NeoForge pour **Minecraft 1.21.1** : un système météo réaliste et déterministe, avec prédictions, vent 3D, thermiques, adret/ubac et microclimats. Il ajoute aussi les blocs et objets d'une station météo à construire : capteurs, antennes, réseau et alimentation.

## Commandes

Accessibles à tous les joueurs. `/gastinelweather` accepte aussi `forecast` et `daily`.

| Commande | Effet |
|---|---|
| `/gweather` (ou `now`, `current`) | Météo actuelle |
| `/gweather forecast [heures]` | Prévision horaire, de 1 à 72 heures (12 par défaut) |
| `/gweather daily [jours]` | Prévision journalière, de 1 à 14 jours (7 par défaut) |

## Blocs de la station

- **Capteurs :** station Vantage Pro 2, anémomètre à coupelles, anémomètre ultrasonique 3D, pluviomètre à augets, pluviomètre à neige laser.
- **Transmission :** carte ESP32, antenne directionnelle (Yagi), antenne omnidirectionnelle, console VP2, boîtier réseau/routeur, câbles réseau (intérieur, extérieur, souterrain).
- **Affichage et serveur :** boîtier PC (serveur météo) et écran de moniteur.
- **Énergie :** éolienne, turbine hydraulique immergée, générateur à charbon avec cheminée d'évacuation, câbles électriques (aérien et souterrain).
- **Objets :** enregistreur Elitech, carte mère, processeur et ventirad, carte graphique, alimentation, câble d'alimentation, transistor.

## Compiler

Prérequis : JDK 21.

```bash
./gradlew build
```

Le fichier `.jar` est produit dans `build/libs/`. Les textures sont générées par `TextureGenerator` (dans `src/main/java/com/gastinelweather/tools`).

## Licence

[MIT](LICENSE)
