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

**[![Join our Discord](https://img.shields.io/badge/Discord-Join%20our%20Server-7289DA?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/gVHefyjsRg)**

## ✨ New in Version 2.1.1

- **Dynamic Text Backgrounds:** Added **F3 + J** to toggle the dark background boxes behind text modules on the fly.
- **Individual Compass Positioning:** The vertical position of the compass can now be fine-tuned via `compassYOffset` in the config to avoid overlapping with other HUD mods (e.g., Tough As Nails).
- **Advanced Color Customization:** Text background colors can now be fully customized using Hex-Strings (e.g., `0x000000` for black).
- **Hardened System Info:** Improved GPU string cleaning for Linux/Mesa users (Ryzen APUs) to ensure a clean, clutter-free HUD.
- **Global Scaling:** Use the `userScale` option (0.1 to 2.0) to perfectly match your screen resolution.
- **Improved Spacing:** Optimized default offsets to provide better readability.

## 🏗️ Architectural Overhaul: From Monolith to Framework
The codebase has been radically refactored to prioritize maintainability and extensibility:
- **Decoupled Rendering:** Every HUD component is now an independent class, making the system more stable and easier to update.
- **Registry-Based System:** New modules can be easily registered within the `ModuleRegistry`, allowing for clean integration of third-party mod data.
- **Centralized Utilities:** Design elements like backgrounds and colors are now handled by a central `RenderUtils` class for a consistent look.

## 🛠️ Features

### 🔌 Advanced Mod Compatibility
Deep integration for popular 1.12.2 magic and tech mods:
- **Thaumcraft / Botania / Blood Magic / Astral Sorcery:** Real-time tracking of Mana, Vis, LP, and Starlight.
- **Performance Engine:** Real-time **TPS** and **MSPT** tracking with dynamic color-coding.

### 🎨 Clean Two-Column Layout
- **Left Side:** Game information (coordinates, world data, entities).
- **Right Side:** System information (Java, CPU, GPU, memory).
- Smooth slide-in animations for a modern feel.

## ⌨️ Keybinds & Customization

All F3-shortcuts are now **fully rebindable** in the standard Minecraft **Controls** menu!

| Shortcut   | Function |
|:-----------| :--- |
| **F3 + X** | Toggle Performance Graph (Locks HUD open) |
| **F3 + J** | **Toggle Text Background Boxes** (New!) |
| **F3 + K** | Toggle Compass |
| **F3 + C** | Toggle Coordinates |
| **F3 + S** | Toggle System Info |
| **F3 + F** | Toggle FPS Display |
| **F3 + R** | Toggle Rotation |
| **F3 + W** | Toggle World Info |
| **F3 + E** | Toggle Entities |
| **F3 + T** | Toggle Targeted Block |
| **F3 + D** | Toggle Dimension |
| **F3 + M** | Toggle Magic/Mod Modules |
| **F3 + Q** | Show Help List in Chat |

## ⚙️ Configuration
The mod generates a detailed config file at `config/betterf3reborn.cfg`.
- **Colors:** Full RGB Hex format support.
- **Layout:** Adjust `userScale`, `compassYOffset`, and `backgroundAlpha`.
- **Visibility:** Toggle every single line of information.

## 📥 Installation
1. Download the mod from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/better-f3-reborn) or [Modrinth](https://modrinth.com/mod/better-f3-reborn).
2. Install [Minecraft Forge 1.12.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html).
3. Place the `.jar` file in your `mods` folder.
4. **Recommendation:** Delete your old `betterf3reborn.cfg` to apply the new 2.1.1 default values.

📜 License: MIT - Feel free to use it in any modpack!

Made with ❤️ for the Minecraft 1.12.2 community.

<img width="640" height="480" alt="Better F3 Reborn Gameplay" src="https://github.com/user-attachments/assets/cac7f361-6777-4552-9f40-6b1858f902f2" /> 
