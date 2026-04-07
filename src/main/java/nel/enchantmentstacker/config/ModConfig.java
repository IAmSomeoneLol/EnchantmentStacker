package nel.enchantmentstacker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/enchantmentstacker.json");

    public boolean enableFixedAnvilCost = true;
    public int repairCostLevelAmount = 3;
    public int repairCostMaterialAmount = 1;
    public double percentRepairedPerAction = 0.3333;

    public boolean allowArmorStacker = true;
    public boolean allowSwordStacker = true;
    public boolean allowAxeStacker = true;
    public boolean allowHoeExpanded = true;
    public boolean allowBowStacker = true;
    public boolean allowCrossbowStacker = true;
    public boolean allowMaceStacker = true;
    public boolean allowTheUnEnchantable = true;

    // Map to hold all Vanilla Enchantment Max Levels
    public Map<String, Integer> vanillaEnchantmentMaxLevels = new HashMap<>();

    private static ModConfig instance;

    public static ModConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                instance = new ModConfig();
            }
        } else {
            instance = new ModConfig();
        }

        if (instance.vanillaEnchantmentMaxLevels.isEmpty()) {
            instance.populateDefaultLevels();
        }
        save();
    }

    public static void save() {
        CONFIG_FILE.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException ignored) {}
    }

    public int getCustomMaxLevel(String translationKey) {
        return vanillaEnchantmentMaxLevels.getOrDefault(translationKey, -1);
    }

    private void populateDefaultLevels() {
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.protection", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.fire_protection", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.feather_falling", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.blast_protection", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.projectile_protection", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.respiration", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.aqua_affinity", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.thorns", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.depth_strider", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.frost_walker", 2);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.binding_curse", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.sharpness", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.smite", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.bane_of_arthropods", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.knockback", 2);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.fire_aspect", 2);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.looting", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.sweeping_edge", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.efficiency", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.silk_touch", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.unbreaking", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.fortune", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.power", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.punch", 2);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.flame", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.infinity", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.luck_of_the_sea", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.lure", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.loyalty", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.impaling", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.riptide", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.channeling", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.multishot", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.quick_charge", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.piercing", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.mending", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.vanishing_curse", 1);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.soul_speed", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.swift_sneak", 3);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.density", 5);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.breach", 4);
        vanillaEnchantmentMaxLevels.put("enchantment.minecraft.wind_burst", 3);
    }
}