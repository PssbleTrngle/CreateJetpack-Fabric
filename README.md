[KOTLIN_FORGE]: https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge
[KOTLIN_FABRIC]: https://www.curseforge.com/minecraft/mc-mods/fabric-language-kotlin
[CREATE_FORGE]: https://www.curseforge.com/minecraft/mc-mods/create
[CREATE_FABRIC]: https://www.curseforge.com/minecraft/mc-mods/create-fabric
[ISSUES]: https://github.com/PssbleTrngle/CreateJetpack/issues
[DOWNLOAD]: https://www.curseforge.com/minecraft/mc-mods/create-jetpack/files
[CURSEFORGE]: https://www.curseforge.com/minecraft/mc-mods/create-jetpack
[MODRINTH]: https://modrinth.com/mod/create-jetpack

# Create Jetpack <!-- modrinth_exclude.start --> <img src="https://raw.githubusercontent.com/PssbleTrngle/CreateJetpack/1.19.x/src/main/resources/assets/create_jetpack/icon.png" align="right" height="128" />

[Looking for the Forge version?](https://github.com/PssbleTrngle/CreateJetpack)

[![Release](https://img.shields.io/github/v/release/PssbleTrngle/CreateJetpack-Fabric?label=Version&sort=semver)][DOWNLOAD]
[![Downloads](https://cf.way2muchnoise.eu/full_655608_downloads.svg)][CURSEFORGE]
[![Version](https://cf.way2muchnoise.eu/versions/655608.svg)][DOWNLOAD]
[![Issues](https://img.shields.io/github/issues/PssbleTrngle/CreateJetpack?label=Issues)][ISSUES]
[![Modrinth](https://img.shields.io/modrinth/dt/UbFnAd4l?color=green&logo=modrinth&logoColor=green)][MODRINTH]
<!-- modrinth_exclude.end -->

[![](https://img.shields.io/badge/FORGE%20%20REQUIRES-1e2a41?labelColor=gray&style=for-the-badge)][KOTLIN_FORGE]
[![](https://img.shields.io/badge/KOTLIN%20FOR%20FORGE-blue?logo=kotlin&labelColor=gray&style=for-the-badge)][KOTLIN_FORGE]
[![](https://img.shields.io/badge/CREATE-ae7c38?logo=curseforge&labelColor=gray&style=for-the-badge)][CREATE_FORGE]

[![](https://img.shields.io/badge/FABRIC%20REQUIRES-c6bca5?labelColor=gray&style=for-the-badge)][KOTLIN_FABRIC]
[![](https://img.shields.io/badge/FABRIC%20LANGUAGE%20KOTLIN-blue?logo=kotlin&labelColor=gray&style=for-the-badge)][KOTLIN_FABRIC]
[![](https://img.shields.io/badge/CREATE%20FABRIC-ae7c38?logo=curseforge&labelColor=gray&style=for-the-badge)][CREATE_FABRIC]

Using brass you are able to upgrade your copper backtank to a jetpack, 
using the pressurized air inside to propel yourself through the air.

Inspired by [Simply Jetpacks](https://www.curseforge.com/minecraft/mc-mods/simply-jetpacks-2),
this jetpack also has a hover mode.

Like the copper backtank, the jetpack does also go in the chest slot, 
feeds air to items like the extendo-grip and can be enchanted with _Capacity_.
Just like the backtank it is charged by placing it down and supplying it with rotational force.

![Usage](https://raw.githubusercontent.com/PssbleTrngle/CreateJetpack/1.19.x/screenshots/usage.png)

When underwater while sprint-swimming, the jetpack boosts your swimming speed.
Additionally, the hover-mode prevents you from floating downwards.

![Underwater Usage](https://raw.githubusercontent.com/PssbleTrngle/CreateJetpack/1.19.x/screenshots/underwater.png)

Trinkets Support is possible by adding it via a datapack. Save to following to `data/trinkets/tags/items/chest/back.json`:

```json
{
  "replace": false,
  "values": [
    "create_jetpack:jetpack"
  ]
}
```

If the back slot is not already enabled by another mod, save the following to `data/trinkets/entities/flightlib.json`:

```json
{
  "entities": [
    "player"
  ],
  "slots": [
    "chest/back"
  ]
}
```
