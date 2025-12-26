package com.worador.f3hud;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SystemModule extends InfoModule {
    
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
        
        // Java Version
        String javaVersion = System.getProperty("java.version");
        lines.add(new InfoLine("Java Version: ", javaVersion, ModConfig.colors.colorSystem));
        
        // Memory Usage
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long used = total - Runtime.getRuntime().freeMemory();
        String memText = (used / 1024L / 1024L) + "/" + (max / 1024L / 1024L) + " MB";
        lines.add(new InfoLine("Memory Usage: ", memText, ModConfig.colors.colorSystem));
        
        // Allocated Memory
        String allocText = (max * 55 / 100 / 1024L / 1024L) + "MB";
        lines.add(new InfoLine("Allocated Memory: ", allocText, ModConfig.colors.colorSystem));
        
        // CPU
        String cpuName = getCPUName();
        lines.add(new InfoLine("CPU: ", cpuName, ModConfig.colors.colorSystem));
        
        // Display
        int width = mc.displayWidth;
        int height = mc.displayHeight;
        lines.add(new InfoLine("Display: ", width + " x " + height, ModConfig.colors.colorSystem));
        
        // GPU
        String gpuName = GL11.glGetString(GL11.GL_RENDERER);
        lines.add(new InfoLine("GPU: ", gpuName, ModConfig.colors.colorSystem));
        
        // OpenGL Version
        String openglVersion = GL11.glGetString(GL11.GL_VERSION);
        lines.add(new InfoLine("OpenGL Version: ", openglVersion, ModConfig.colors.colorSystem));
        
        // GPU Driver
        String gpuDriver = getGPUDriver(openglVersion);
        lines.add(new InfoLine("GPU Driver: ", gpuDriver, ModConfig.colors.colorSystem));
        
        return lines;
    }
    
    private String getCPUName() {
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("linux")) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("model name")) {
                        reader.close();
                        String cpuName = line.split(":")[1].trim();
                        if (cpuName.length() > 50) {
                            cpuName = cpuName.substring(0, 47) + "...";
                        }
                        return cpuName;
                    }
                }
                reader.close();
            } catch (Exception e) {
                // Fallback
            }
        }
        else if (os.contains("win")) {
            try {
                String cpuId = System.getenv("PROCESSOR_IDENTIFIER");
                if (cpuId != null && !cpuId.isEmpty()) {
                    return cpuId;
                }
            } catch (Exception e) {
                // Fallback
            }
        }
        else if (os.contains("mac")) {
            try {
                Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
                BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
                String cpuName = reader.readLine();
                reader.close();
                if (cpuName != null && !cpuName.isEmpty()) {
                    return cpuName;
                }
            } catch (Exception e) {
                // Fallback
            }
        }
        
        String arch = System.getProperty("os.arch");
        return "Unknown CPU (" + arch + ")";
    }
    
    private String getGPUDriver(String openglVersion) {
        if (openglVersion == null || openglVersion.isEmpty()) {
            return "Unknown";
        }
        
        try {
            if (openglVersion.contains("Mesa")) {
                int mesaIndex = openglVersion.indexOf("Mesa");
                String mesaPart = openglVersion.substring(mesaIndex);
                String[] parts = mesaPart.split("\\s+");
                if (parts.length >= 2) {
                    return parts[0] + " " + parts[1];
                }
                return mesaPart.split("\\(")[0].trim();
            }
            else if (openglVersion.contains("NVIDIA")) {
                int nvidiaIndex = openglVersion.indexOf("NVIDIA");
                String nvidiaPart = openglVersion.substring(nvidiaIndex);
                return nvidiaPart.trim();
            }
            else if (openglVersion.contains("AMD") || openglVersion.contains("ATI")) {
                return openglVersion;
            }
            else if (openglVersion.contains("Intel")) {
                return openglVersion;
            }
            else {
                String[] parts = openglVersion.split("\\s+");
                if (parts.length > 1) {
                    return parts[parts.length - 1];
                }
            }
        } catch (Exception e) {
            // Fallback
        }
        
        if (openglVersion.length() > 30) {
            return openglVersion.substring(0, 27) + "...";
        }
        return openglVersion;
    }
}