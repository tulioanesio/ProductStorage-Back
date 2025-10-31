package com.unisul.product_storage.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.*;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class SystemInfoController {

    @GetMapping("/management/system")
    public Map<String, Object> getSystemInfo() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        com.sun.management.OperatingSystemMXBean sunOs =
                (os instanceof com.sun.management.OperatingSystemMXBean) ? (com.sun.management.OperatingSystemMXBean) os : null;

        long uptimeSeconds = TimeUnit.MILLISECONDS.toSeconds(runtime.getUptime());
        long pid = ProcessHandle.current().pid();

        long heapUsed = memory.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapTotal = memory.getHeapMemoryUsage().getCommitted() / (1024 * 1024);
        long rss = (sunOs != null ? sunOs.getCommittedVirtualMemorySize() : heapUsed) / (1024 * 1024);
        long external = memory.getNonHeapMemoryUsage().getUsed() / (1024 * 1024);

        double processCpuLoad = sunOs != null ? sunOs.getProcessCpuLoad() * 100 : -1;
        double systemCpuLoad = sunOs != null ? sunOs.getSystemCpuLoad() * 100 : -1;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "OK");

        Map<String, Object> system = new LinkedHashMap<>();
        system.put("uptime", uptimeSeconds);
        system.put("platform", os.getName().toLowerCase());
        system.put("architecture", os.getArch());
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("pid", pid);

        Map<String, Object> memoryMap = new LinkedHashMap<>();
        memoryMap.put("rss", rss);
        memoryMap.put("heapTotal", heapTotal);
        memoryMap.put("heapUsed", heapUsed);
        memoryMap.put("external", external);
        memoryMap.put("unit", "MB");
        system.put("memory", memoryMap);

        Map<String, Object> cpuMap = new LinkedHashMap<>();
        cpuMap.put("user", round(processCpuLoad));
        cpuMap.put("system", round(systemCpuLoad));
        system.put("cpu", cpuMap);

        response.put("system", system);
        response.put("timestamp", Instant.now().toString());

        return response;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}