# Better F3 Reborn

[![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-green.svg)](https://minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-14.23.5.2859-orange.svg)](https://files.minecraftforge.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java CI with Gradle](https://github.com/hypopheria2k/better-f3-reborn/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/hypopheria2k/better-f3-reborn/actions)

**Better F3 Reborn** is a comprehensive overhaul of the Minecraft debug HUD for version 1.12.2. It combines the modern, highly customizable design of "Better F3" with extreme performance optimizations specifically tailored for the 1.12.2 modding era.

> **New in v2.3.0 ("Silent Fan Update"):** Massive CPU load reduction of up to 90% via intelligent Tick-Caching. Specifically optimized for integrated GPUs (AMD Ryzen Vega 7 / Intel Xe).

---

## 💬 Community & Support

Have questions? Want to suggest a feature? Join our community!

**[![Join our Discord](https://img.shields.io/badge/Discord-Join%20our%20Server-7289DA?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/gVHefyjsRg)**

---

## ✨ Key Features

### 🖥️ Ingame GUI Configurator
No more manual editing of config files! Press **CTRL + C** to open the visual editor:
* **Live Preview:** Toggle modules ON/OFF with a single click.
* **Dual-Column Layout:** Freely move modules between the left and right sides of your screen.
* **X-Offset Sliders:** Fine-tune the positioning of each side to match your UI scale perfectly.

### ⚡ Performance Engine (v2.3.0)
Engineered for low-end systems, APUs, and heavy modpacks:
* **Smart-Tick-Caching:** Expensive calculations (Weather, Biomes, System Data) are updated once per second (20 ticks) instead of 60+ times per second.
* **Strict Gating:** Disabled modules consume **zero** CPU cycles (Zero Overhead).
* **Responsive Updates:** Movement data (Speedometer/Compass) remains fluid using adaptive high-frequency update rates.

### 🐎 Entity & Mob Analysis
* **Horse Breeding Tool:** Displays exact speed (m/s) and jump height (blocks) for your mount.
* **Targeted Health:** Instantly see the exact HP, name, and type of the entity you are looking at.
* **Mob Aggro Radar:** Real-time warnings for hostile mobs targeting you, including "Explosion Alerts" for igniting Creepers.

### 🔌 Out-of-the-Box Mod Compatibility
Deep integration for popular 1.12.2 magic and tech mods:
* **Magic:** Thaumcraft (Vis/Flux), Botania (Mana), Blood Magic (LP), Astral Sorcery (Starlight), and Stellar API.
* **Performance:** Real-time **TPS** and **MSPT** tracking for server monitoring.
* **Environment:** Detailed weather forecasts (timers for rain/thunder) and a Slime Chunk detector.

---

## ⌨️ Controls & Keybinds

| Shortcut | Function |
| :--- | :--- |
| **CTRL + C** | **Open Visual HUD Editor (GUI)** |
| **F3 + X** | Toggle Performance Graph (Lock HUD) |
| **F3 + K** | Toggle Graphical Compass |
| **F3 + C** | Toggle Coordinates & Nether Link |
| **F3 + J** | Toggle Text Background Boxes |
| **F3 + S** | Toggle System Info (CPU/GPU) |

---

## 🏗️ Technical Architecture

The project has been radically refactored for modern standards:
- **Registry System:** Modules are decoupled and independent. Adding new data sources is seamless.
- **RenderUtils:** A centralized rendering pipeline ensures a consistent look and full Hex-Color support.
- **ScaledResolution:** The GUI editor automatically detects your Minecraft GUI scale to remain perfectly usable on any resolution.

---

## 📥 Installation

1. Download the mod from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/better-f3-reborn) or [Modrinth](https://modrinth.com/mod/better-f3-reborn).
2. Requires **Minecraft Forge 1.12.2** (Recommended: 14.23.5.2859).
3. Place the `.jar` file into your `mods` folder.
4. **Important:** When upgrading to v2.3.0, delete your old `betterf3reborn.cfg` to enable all new caching and temperature features.

---

📜 **License:** MIT - Feel free to use this in any modpack!

*Made with ❤️ for the 1.12.2 Modding Community.*
