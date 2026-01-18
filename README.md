# Better F3 Reborn

[![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-green.svg)](https://minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-14.23.5.2859-orange.svg)](https://files.minecraftforge.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java CI with Gradle](https://github.com/hypopheria2k/better-f3-reborn/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/hypopheria2k/better-f3-reborn/actions)

**⚠️ Note:** The old codebase has been moved to the `main_legacy` branch. This branch contains the latest rewritten and optimized code (v2.1.0+).

A modern, feature-rich debug HUD overhaul for Minecraft 1.12.2 – bringing Better F3's clean design to the golden age of modding.

<img width="640" height="480" alt="2026-01-18_19 31 05" src="https://github.com/user-attachments/assets/f27791bc-6ebb-4eb5-a50b-dfc4ebb93ba4" />

## 💬 Community & Support

Need help? Have a suggestion for a new module? Join our official Discord server!

[![Discord](https://img.shields.io/discord/1330248883653152799?color=7289da&label=Discord&logo=discord&logoColor=white)](https://discord.gg/gVHefyjsRg)

**[Join the Discord Server](https://discord.gg/gVHefyjsRg)**

## ✨ New in Version 2.1.0+

- **Optimized System Info:** Drastically cleaned up GPU strings, especially for Linux/Mesa users (e.g., Ryzen APUs), to prevent HUD clutter.
- **Global Scaling:** Added a `userScale` option (0.1 to 2.0) in the config to perfectly fit any screen resolution.
- **Improved Spacing:** New default **Offset of 8** for X and Y positions to give the text more "room to breathe".
- **Full Localization:** Complete translation support for **German (de_de)** and **English (en_us)**, covering all config menus and keybindings.
- **Refined Layout:** The Compass has been repositioned above the hotbar for better visibility.

## 🏗️ Architectural Overhaul: From Monolith to Framework
The codebase has been radically refactored to prioritize maintainability and extensibility:
- **Decoupled Rendering:** Moved away from a single, monolithic `DebugRenderer`. The HUD is now powered by a modular framework where every component is its own independent class.
- **Registry-Based System:** New modules can now be easily registered within the `ModuleRegistry`. This allows for a much cleaner integration of third-party mod data without cluttering the core rendering logic.
- **Object-Oriented Design:** Each HUD element follows a strict OO approach, making the code highly efficient and developer-friendly.

## 🛠️ Features

### 🔌 Advanced Mod Compatibility
Deep integration for popular 1.12.2 magic and tech mods:
- **Thaumcraft:** Real-time Aura Vis/Flux levels and Warp tracking.
- **Botania:** Precise Mana tracking for Tablets and Buffers.
- **Blood Magic:** LP Network monitoring and Altar capacity.
- **Astral Sorcery:** Starlight concentration and Celestial data.
- **Performance Engine:** Real-time **TPS** and **MSPT** (Milliseconds Per Tick) tracking with dynamic color-coding.

### 🎨 Clean Two-Column Layout
- **Left Side:** Game information (coordinates, world data, entities).
- **Right Side:** System information (Java, CPU, GPU, memory).
- Smooth slide-in animations from both sides for a modern feel.

### 📊 Performance Graph
- Real-time FPS tracking with a visual curve and Min/Avg/Max statistics.
- **Toggle with F3 + X or F3 + G** (Intelligently locks the HUD open while active).

## ⌨️ Keybinds & Customization

Take full control over your debug interface. All F3-shortcuts are now **fully rebindable** in the standard Minecraft **Controls** menu!

| Shortcut   | Function |
|:-----------| :--- |
| **F3 + X** | Toggle Performance Graph (Locks HUD open) |
| **F3 + C** | Toggle Coordinates |
| **F3 + S** | Toggle System Info |
| **F3 + F** | Toggle FPS Display |
| **F3 + K** | Toggle Compass |
| **F3 + R** | Toggle Rotation |
| **F3 + W** | Toggle World Info |
| **F3 + E** | Toggle Entities |
| **F3 + T** | Toggle Targeted Block |
| **F3 + D** | Toggle Dimension |
| **F3 + M** | Toggle Magic/Mod Modules |
| **F3 + Q** | Show Help List in Chat |

## ⚙️ Configuration
The mod generates a detailed config file at `config/betterf3reborn.cfg`.
You can customize:
- **Module Visibility:** Enable or disable every single line of info.
- **Colors:** Full RGB Hex format support for all elements.
- **Positioning:** Fine-tune `userScale`, `offsets`, and `backgroundAlpha`.
- **Animations:** Adjust or disable the sliding effects.

## 📥 Installation
1. Download the mod from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/better-f3-reborn) or [Modrinth](https://modrinth.com/mod/better-f3-reborn).
2. Install [Minecraft Forge 1.12.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html).
3. Place the `.jar` file in your `mods` folder.
4. **Recommendation:** Delete your old `betterf3reborn.cfg` to apply the new optimized default offsets and scale.

## 🛠️ Building from Source
Requires **Java 8 JDK**.
```bash
git clone [https://github.com/hypopheria2k/better-f3-reborn.git](https://github.com/hypopheria2k/better-f3-reborn.git)
cd better-f3-reborn
./gradlew clean build
```

📜 License: MIT - Feel free to use it in any modpack!

Made with ❤️ for the Minecraft 1.12.2 community.

<img width="640" height="480" alt="2026-01-18_19 30 49" src="https://github.com/user-attachments/assets/cac7f361-6777-4552-9f40-6b1858f902f2" />

