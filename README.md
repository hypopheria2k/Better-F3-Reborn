# Better F3 Reborn

[![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-green.svg)](https://minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-14.23.5.2859-orange.svg)](https://files.minecraftforge.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java CI with Gradle](https://github.com/hypopheria2k/better-f3-reborn/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/hypopheria2k/better-f3-reborn/actions)

**⚠️ Note on Branch Structure:** The original codebase has been moved to the `main-legacy` branch. The current `main` branch now contains the new modular system with expanded magic mod compatibility and real-time performance tracking.

A modern, feature-rich debug HUD overhaul for Minecraft 1.12.2 - bringing Better F3's clean design to older versions!

<img width="1280" height="939" alt="Better F3 Reborn Preview" src="https://github.com/user-attachments/assets/c3151e81-33dc-453d-84d1-b82ef4f4b11c" />

## ✨ Features

### 🛠️ New: Advanced Mod Compatibility (v2.0.0)
The HUD now features deep integration for popular 1.12.2 magic and tech mods:
- **Thaumcraft:** Real-time Aura Vis/Flux levels, Warp tracking, and scanned Aspects.
- **Botania:** Precise Mana tracking for Tablets and Buffers.
- **Blood Magic:** LP Network monitoring and Altar capacity.
- **Astral Sorcery:** Starlight concentration and Celestial data.
- **Performance Engine:** Real-time **TPS** and **MSPT** (Milliseconds Per Tick) tracking with dynamic color-coding (Green/Yellow/Red) based on actual server load.

### 🛠️ Fixed & Polished (v1.1.0)
- **No More Debug Leak:** Fixed issues where other mods would "leak" their vanilla debug information into the HUD. It now provides a clean, isolated experience even in large modpacks.
- **Improved Performance Graph Logic:** Toggle the graph with **F3 + X**. The HUD now intelligently stays open when the graph is active and can be closed by pressing **F3** again.
- **Stability:** Resolved configuration crashes and improved compatibility with coremods.

### 🎨 Clean Two-Column Layout
- **Left Side:** Game information (coordinates, world data, entities)
- **Right Side:** System information (Java, CPU, GPU, memory)
- Smooth slide-in animations from both sides for a modern feel.

### 📊 Performance Graph
- Real-time FPS tracking with a visual curve.
- Color-coded performance (Green: 60+, Yellow: 30-60, Red: <30).
- Min/Avg/Max statistics included.
- **Toggle with F3 + X** (Locks the HUD open for easy monitoring while moving).

### 🎮 Information Modules
- **Coordinates:** Block position, XYZ, Chunk coordinates, Slime chunks.
- **World Info:** Facing direction, light levels, biome, days played.
- **System Info:** Java version, memory usage, CPU, GPU, OpenGL version & Display details.
- **Targeted Info:** Detailed information about the block you are currently looking at.

### ⌨️ Keybinds & Customization
Take full control over your debug interface:
- **Native Keybinding Support:** All F3-shortcuts are now registered in the standard Minecraft **Controls** menu. You can rebind any toggle to your preferred key!
- **Default Shortcuts (F3 + Key):**
  - **F3 + X** - Toggle Performance Graph (Locks HUD open)
  - **F3 + C** - Toggle Coordinates
  - **F3 + E** - Toggle Entities
  - **F3 + S** - Toggle System Info
  - **F3 + W** - Toggle World Info
  - **F3 + R** - Toggle Rotation
  - **F3 + K** - Toggle Compass
  - **F3 + T** - Toggle Targeted Block
  - **F3 + D** - Toggle Dimension
  - **F3 + F** - Toggle FPS Display
  - **F3 + M** - Toggle Magic/Mod Modules (New in v2.0.0)

## 📥 Installation

1. Download the mod from [CurseForge](https://www.curseforge.com) or [Modrinth](https://modrinth.com/mod/better-f3-reborn).
2. Install [Minecraft Forge 1.12.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html) (recommended: 14.23.5.2859).
3. Place the `.jar` file in your `.minecraft/mods` folder.
4. Launch Minecraft and press **F3** to see the new HUD!

## 🖼️ Screenshots

### Main HUD
<img width="1280" height="939" alt="Main HUD Screenshot" src="https://github.com/user-attachments/assets/c5d21bdc-c734-43d0-87b3-8fc572a8ebd7" />

### Performance Graph
<img width="1280" height="939" alt="Performance Graph Screenshot" src="https://github.com/user-attachments/assets/7fb3af34-6a97-462f-9a49-459d1e54a328" />

## ⚙️ Configuration
The mod generates a config file at `config/betterf3reborn.cfg`.
You can customize:
- Individual module visibility.
- All colors (RGB Hex format).
- Scale, offsets, and animation speed.
- Background transparency.

## 🛠️ Building from Source
**Java 8 JDK** is required for compiling.
```bash
# Clone the repository
git clone [https://github.com/hypopheria2k/better-f3-reborn.git](https://github.com/hypopheria2k/better-f3-reborn.git)
cd better-f3-reborn

# Build with Gradle
./gradlew clean build
```

🙏 Credits & Compatibility

Inspired by Better F3 by cominixo.
```
Compatibility tested with: Astral Sorcery, Blood Magic, Botania, Thaumcraft, Stellar API, Baubles, BetterFps, BlahajASM, EntityCulling, FPS Reducer, FermiumBooter, FoamFix, GPUTape, InGame Info XML, LunatriusCore, MixinBooter, Stellar Sky.
```
📜 License

This project is licensed under the MIT License - see the LICENSE file for details.
🐛 Bug Reports

Found a bug? Please open an issue on GitHub Issues!

Made with ❤️ for the Minecraft 1.12.2 community