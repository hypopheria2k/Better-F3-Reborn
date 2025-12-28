# Better F3 Reborn

[![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-green.svg)](https://minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-14.23.5.2859-orange.svg)](https://files.minecraftforge.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java CI with Gradle](https://github.com/hypopheria2k/better-f3-reborn/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/hypopheria2k/better-f3-reborn/actions)

A modern, feature-rich debug HUD overhaul for Minecraft 1.12.2 - bringing Better F3's clean design to older versions!



<img width="1280" height="939" alt="2025-12-27_00 17 43" src="https://github.com/user-attachments/assets/c3151e81-33dc-453d-84d1-b82ef4f4b11c" />

## ✨ Features

### 🎨 Clean Two-Column Layout
- **Left Side:** Game information (coordinates, world data, entities)
- **Right Side:** System information (Java, CPU, GPU, memory)
- Smooth slide-in animations from both sides

### 📊 Performance Graph
- Real-time FPS tracking with visual curve
- Color-coded performance (Green: 60+, Yellow: 30-60, Red: <30)
- Min/Avg/Max statistics
- Toggle with **F3 + G**

### 🎮 Information Modules
- **Coordinates:** Block position, XYZ, Chunk coordinates, Chunk relative position
- **Rotation:** Yaw and Pitch display
- **World Info:** Chunks, facing direction, light levels, biome, days played, slime chunks
- **Dimension:** Shows current dimension (Overworld/Nether/End)
- **Region File:** Shows .mca file and local chunk position
- **Targeted Block:** Block you're looking at with ID
- **Server Info:** Server details and tick rate
- **Entities:** Particles, monsters, creatures, water creatures counts
- **System Info:** Java version, memory usage, CPU, GPU, display resolution, OpenGL version, GPU driver

### ⌨️ Keybinds (F3 + Key)
- **F3 + C** - Toggle Coordinates
- **F3 + E** - Toggle Entities
- **F3 + S** - Toggle System Info
- **F3 + W** - Toggle World Info
- **F3 + R** - Toggle Rotation
- **F3 + T** - Toggle Targeted Block
- **F3 + D** - Toggle Dimension
- **F3 + G** - Toggle Performance Graph
- **F3 + F** - Toggle FPS Display

### ⚙️ Highly Configurable
Edit `config/f3hud.cfg` to customize:
- Enable/disable individual modules
- Customize all colors (RGB hex format)
- Adjust position and scale
- Animation speed and distance
- Background transparency

## 📥 Installation

1. Download the mod from [CurseForge](https://www.curseforge.com) or [Modrinth](https://modrinth.com/mod/better-f3-reborn)
2. Install [Minecraft Forge 1.12.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html) (recommended: 14.23.5.2859)
3. Place the `.jar` file in your `.minecraft/mods` folder
4. Launch Minecraft and press **F3** to see the new HUD!

## 🖼️ Screenshots

### Main HUD
<img width="1280" height="939" alt="2025-12-27_00 18 08" src="https://github.com/user-attachments/assets/c5d21bdc-c734-43d0-87b3-8fc572a8ebd7" />

### Performance Graph
<img width="1280" height="939" alt="2025-12-27_00 17 55" src="https://github.com/user-attachments/assets/7fb3af34-6a97-462f-9a49-459d1e54a328" />

## 🔧 Configuration Example
```cfg
modules {
    B:showCoordinates=true
    B:showFPS=true
    B:showPerformanceGraph=false
    B:showSystem=true
}

colors {
    I:colorFPS=16776005        # Yellow (0xFFFF55)
    I:colorX=16733525          # Red (0xFF5555)
    I:colorY=5570645           # Green (0x55FF55)
    I:colorZ=5592575           # Blue (0x5555FF)
}

position {
    D:scale=0.75
    I:leftOffsetX=4
    I:leftOffsetY=4
}

animation {
    B:enableAnimation=true
    D:animationSpeed=0.1
    I:slideDistance=50
    I:backgroundAlpha=160
}
```

## 🛠️ Building from Source
### Prerequisites
**Java 8 JDK**: Required for compiling Minecraft 1.12.2 mods. (Recommended: [Temurin 8](https://adoptium.net/temurin/releases/?version=8))
```bash
# Clone the repository
git clone https://github.com/yourusername/better-f3-reborn.git
cd better-f3-reborn

# Build with Gradle
./gradlew build

# The built mod will be in build/libs/
```

## 💡 Why "Reborn"?

Better F3 is an amazing mod for modern Minecraft versions, but 1.12.2 modpacks still thrive today! This mod brings that clean, modern debug HUD experience back to 1.12.2 with full feature parity.

## 🙏 Credits

- Inspired by [Better F3 Modrinth](https://modrinth.com/mod/betterf3) [Better F3 Curseforge](https://www.curseforge.com/minecraft/mc-mods/betterf3) by cominixo
- Created with assistance from Claude (Anthropic)
- Built for the 1.12.2 modding community

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Bug Reports & Feature Requests

Found a bug or have a feature request? Open an issue on [GitHub Issues](https://github.com/hypopheria2k/better-f3-reborn/issues)!

## ❤️ Support

If you enjoy this mod, consider:
- ⭐ Starring the repository on GitHub
- 📦 Downloading from CurseForge/Modrinth
- 💬 Sharing with your friends
- 🐛 Reporting bugs to help improve the mod

---

**Made with ❤️ for the Minecraft 1.12.2 community**
