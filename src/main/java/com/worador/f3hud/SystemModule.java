package com.worador.f3hud;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;
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
        if (!isEnabledInConfig()) return lines;

        // Lazy Loading der Hardware-Daten
        if (cachedCPU == null) {
            cachedCPU = fetchCPUName();
            cpuThreads = Runtime.getRuntime().availableProcessors();
        }
        if (cachedGPU == null) {
            cachedGPU = cleanGPUName(GL11.glGetString(GL11.GL_RENDERER));
        }

        // 1. CPU & Threads
        lines.add(new InfoLine("CPU: ", cachedCPU + " (" + cpuThreads + "T)", ModConfig.colors.colorSystem));

        // 2. GPU
        lines.add(new InfoLine("GPU: ", cachedGPU, ModConfig.colors.colorSystem));

        // 3. RAM (Heap-Auslastung)
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        int percentage = (int) (used * 100 / max);

        int ramColor = (percentage > 85) ? 0xFF5555 : 0x55FF55;
        String memInfo = String.format(Locale.US, "%d%% %d/%dMB", percentage, used / 1048576L, max / 1048576L);
        lines.add(new InfoLine("Mem: ", memInfo, ramColor));

        // 4. Display & Driver
        String opengl = GL11.glGetString(GL11.GL_VERSION);
        String displayInfo = mc.displayWidth + "x" + mc.displayHeight + " (" + getGPUDriver(opengl) + ")";
        lines.add(new InfoLine("Display: ", displayInfo, 0xAAAAAA));

        return lines;
    }

    private String cleanGPUName(String raw) {
        if (raw == null) return "Unknown GPU";

        // Entferne Hersteller-Präfixe und unnötigen Ballast für das HUD-Layout
        String s = raw.replace("(TM)", "").replace("(R)", "")
                .replace("Corporation", "").replace("Integrated", "")
                .replace("Series", "").replace("Graphics", "").replace("PCIe/SSE2", "");

        // Mesa/Linux Strings abschneiden: "AMD Radeon Vega 7 (radeonsi...)" -> "AMD Radeon Vega 7"
        if (s.contains("(")) s = s.substring(0, s.indexOf("("));
        if (s.contains(",")) s = s.substring(0, s.indexOf(","));

        return s.replaceAll("AMD |NVIDIA |Intel ", "").replaceAll("\\s{2,}", " ").trim();
    }

    private String fetchCPUName() {
        String os = System.getProperty("os.name").toLowerCase();
        String name = "Unknown CPU";

        try {
            if (os.contains("win")) {
                // Windows-Weg: Registry Abfrage
                Process process = Runtime.getRuntime().exec("reg query \"HKLM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\" /v ProcessorNameString");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("REG_SZ")) {
                            String[] parts = line.split("REG_SZ");
                            if (parts.length > 1) name = parts[1].trim();
                        }
                    }
                }
            } else if (os.contains("linux")) {
                // Linux-Weg: procfs (Sicherer als Befehle auszuführen)
                File cpuInfo = new File("/proc/cpuinfo");
                if (cpuInfo.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(cpuInfo))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("model name")) {
                                name = line.split(":")[1].trim();
                                break;
                            }
                        }
                    }
                }
            } else if (os.contains("mac")) {
                // Mac-Weg: sysctl
                Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    name = reader.readLine();
                }
            }
        } catch (Exception ignored) {}

        // Fallback auf Umgebungsvariable
        if (name == null || name.contains("Unknown")) {
            name = System.getenv("PROCESSOR_IDENTIFIER");
        }

        return formatCPUName(name);
    }

    private String formatCPUName(String name) {
        if (name == null) return "Generic CPU";
        return name.replace("(TM)", "").replace("(R)", "")
                .replace("Intel Core ", "").replace("AMD Ryzen ", "Ryzen ")
                .replace("with Radeon Graphics", "")
                .replace("CPU", "").replace("Processor", "")
                .replaceAll("\\s{2,}", " ").trim();
    }

    private String getGPUDriver(String opengl) {
        if (opengl == null) return "Unknown";
        if (opengl.contains("Mesa")) return "Mesa";
        if (opengl.contains("NVIDIA")) return "NV";
        if (opengl.contains("AMD") || opengl.contains("ATI")) return "AMD";
        if (opengl.contains("Intel")) return "Intel";
        return "Generic";
    }
}