package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemModule extends InfoModule {

    private static String cachedCPU = null;
    private static String cachedGPU = null;
    private static int cpuThreads = 0;

    @Override
    public String getName() {
        return "System";
    }

    @Override
    protected boolean isEnabledInConfig() {
        return ModConfig.modules.showSystem;
    }

    @Override
    public List<InfoLine> getLines() {
        List<InfoLine> lines = new ArrayList<>();

        // Strict Gating: Check config first, before any expensive operations
        if (!isEnabledInConfig()) {
            return lines;
        }

        if (cachedCPU == null) {
            cachedCPU = fetchCPUName();
            cpuThreads = Runtime.getRuntime().availableProcessors();
        }
        if (cachedGPU == null) {
            cachedGPU = cleanGPUName(GL11.glGetString(GL11.GL_RENDERER));
        }

        // 1. CPU & Threads
        lines.add(new InfoLine("CPU: ", cachedCPU + " (" + cpuThreads + "T)", ModConfig.colors.colorSystem));

        // 2. GPU (Radikal gekürzt)
        lines.add(new InfoLine("GPU: ", cachedGPU, ModConfig.colors.colorSystem));

        // 3. RAM
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        long percentage = (used * 100L / max);

        int ramColor = (percentage > 85) ? 0xFF5555 : 0x55FF55;
        String memInfo = String.format(Locale.US, "%d%% %d/%dMB", percentage, used / 1048576L, max / 1048576L);
        lines.add(new InfoLine("Mem: ", memInfo, ramColor));

        // 4. Display & Driver
        String openglVersion = GL11.glGetString(GL11.GL_VERSION);
        String displayInfo = mc.displayWidth + "x" + mc.displayHeight + " (" + getGPUDriver(openglVersion) + ")";
        lines.add(new InfoLine("Display: ", displayInfo, 0xAAAAAA));

        return lines;
    }

    private String cleanGPUName(String raw) {
        if (raw == null) return "Unknown GPU";

        // 1. Unnötige Markenzeichen entfernen
        String s = raw.replace("(TM)", "").replace("(R)", "")
                .replace("Corporation", "")
                .replace("Integrated", "");

        // 2. Linux/Mesa-Spezifisch:
        // Mesa-Strings sehen oft so aus: "AMD Radeon RX 6600 (radeonsi, navi23, LLVM 15.0.7, DRM 3.49...)"
        // Wir schneiden alles ab der ersten Klammer oder dem ersten Komma weg,
        // falls davor schon Informationen stehen.
        if (s.contains("(") && s.indexOf("(") > 5) {
            s = s.substring(0, s.indexOf("("));
        }
        if (s.contains(",") && s.indexOf(",") > 5) {
            s = s.substring(0, s.indexOf(","));
        }

        // 3. Allgemeine Bereinigung für alle Hersteller
        return s.replace("AMD ", "")
                .replace("NVIDIA ", "")
                .replace("Intel(R) ", "")
                .replace("Intel ", "")
                .replace("Graphics", "")
                .replace("Series", "")
                .replace("PCIe/SSE2", "")
                .replaceAll("\\s{2,}", " ") // Doppelte Leerzeichen entfernen
                .trim();
    }

    private String fetchCPUName() {
        String os = System.getProperty("os.name").toLowerCase();
        String name = "Unknown CPU";
        try {
            if (os.contains("win")) {
                Process process = Runtime.getRuntime().exec("reg query \"HKLM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\" /v ProcessorNameString");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("REG_SZ")) {
                            name = line.split("REG_SZ")[1].trim();
                        }
                    }
                }
            } else if (os.contains("linux")) {
                try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("model name")) {
                            name = line.split(":")[1].trim();
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        if (name.equals("Unknown CPU")) {
            name = System.getenv("PROCESSOR_IDENTIFIER") != null ? System.getenv("PROCESSOR_IDENTIFIER") : "Unknown CPU";
        }

        // Radikale Kürzung für CPUs
        return name.replace("(TM)", "").replace("(R)", "")
                .replace("Intel(R) Core(TM) ", "")
                .replace("AMD Ryzen ", "Ryzen ")
                .replace("CPU", "").replace("Processor", "")
                .replace("Six-Core", "")
                .replace("Eight-Core", "")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    private String getGPUDriver(String openglVersion) {
        if (openglVersion == null) return "Unknown";
        if (openglVersion.contains("Mesa")) return "Mesa";
        if (openglVersion.contains("NVIDIA")) return "NV";
        if (openglVersion.contains("AMD") || openglVersion.contains("ATI")) return "AMD";
        if (openglVersion.contains("Intel")) return "Intel";
        return "Gen";
    }

    @Override
    public int getHeight() {
        return 11 * 4 + 2;
    }
}
