package nel.enchantmentstacker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/enchantmentstacker.json");

    // Anvil & Grindstone Settings
    public boolean enableFixedAnvilCost = true;
    public int repairCostLevelAmount = 3;
    public int repairCostMaterialAmount = 1;
    public double percentRepairedPerAction = 0.3333;
    public boolean enableGrindstoneExtraction = true;

    // Unlocked - Weapons
    public boolean unlockedSword = true;
    public List<String> swordEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting",
            "minecraft:sweeping_edge", "minecraft:unbreaking", "minecraft:mending"
    ));

    public boolean unlockedAxe = true;
    public List<String> axeEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting",
            "minecraft:efficiency", "minecraft:silk_touch", "minecraft:fortune",
            "minecraft:unbreaking", "minecraft:mending", "minecraft:cleaving"
    ));

    public boolean unlockedMace = true;
    public List<String> maceEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:breach", "minecraft:density", "minecraft:wind_burst",
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting",
            "minecraft:unbreaking", "minecraft:mending"
    ));

    // Unlocked - Ranged & Trident
    public boolean unlockedTrident = true;
    public List<String> tridentEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:riptide", "minecraft:loyalty", "minecraft:channeling", "minecraft:impaling",
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:looting", "minecraft:fire_aspect", "minecraft:unbreaking", "minecraft:mending"
    ));

    public boolean unlockedBow = true;
    public List<String> bowEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:power", "minecraft:punch", "minecraft:flame",
            "minecraft:infinity", "minecraft:mending", "minecraft:unbreaking"
    ));

    public boolean unlockedCrossbow = true;
    public List<String> crossbowEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:multishot", "minecraft:piercing", "minecraft:quick_charge",
            "minecraft:power", "minecraft:punch", "minecraft:flame",
            "minecraft:infinity", "minecraft:mending", "minecraft:unbreaking"
    ));

    // Unlocked - Armor & Tools
    public boolean unlockedArmor = true;
    public List<String> armorEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:protection", "minecraft:fire_protection",
            "minecraft:blast_protection", "minecraft:projectile_protection",
            "minecraft:thorns", "minecraft:respiration", "minecraft:aqua_affinity",
            "minecraft:depth_strider", "minecraft:frost_walker", "minecraft:soul_speed",
            "minecraft:swift_sneak", "minecraft:unbreaking", "minecraft:mending"
    ));

    public boolean unlockedPickaxe = false;
    public List<String> pickaxeEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:efficiency", "minecraft:silk_touch", "minecraft:fortune",
            "minecraft:unbreaking", "minecraft:mending"
    ));

    public boolean unlockedShovel = false;
    public List<String> shovelEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:efficiency", "minecraft:silk_touch", "minecraft:fortune",
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:knockback", "minecraft:fire_aspect", "minecraft:looting",
            "minecraft:unbreaking", "minecraft:mending"
    ));

    public boolean unlockedHoe = true;
    public List<String> hoeEnchantments = new ArrayList<>(Arrays.asList(
            "minecraft:efficiency", "minecraft:silk_touch", "minecraft:fortune",
            "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods",
            "minecraft:fire_aspect", "minecraft:looting", "minecraft:unbreaking", "minecraft:mending"
    ));

    // Un-Enchantables
    public boolean allowTheUnEnchantable = true;

    // Vanilla Max Levels
    public Map<String, Integer> vanillaEnchantmentMaxLevels = new HashMap<>();

    private static ModConfig instance;

    public static ModConfig get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, ModConfig.class);
                if (instance == null) instance = new ModConfig();
            } catch (Exception e) {
                instance = new ModConfig();
            }
        } else {
            instance = new ModConfig();
        }

        if (instance.vanillaEnchantmentMaxLevels == null) {
            instance.vanillaEnchantmentMaxLevels = new HashMap<>();
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
        } catch (Exception ignored) {}
    }

    public int getCustomMaxLevel(String translationKey) {
        return vanillaEnchantmentMaxLevels.getOrDefault(translationKey, -1);
    }

    private void populateDefaultLevels() {
        vanillaEnchantmentMaxLevels.put("minecraft:protection", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:fire_protection", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:feather_falling", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:blast_protection", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:projectile_protection", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:respiration", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:aqua_affinity", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:thorns", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:depth_strider", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:frost_walker", 2);
        vanillaEnchantmentMaxLevels.put("minecraft:binding_curse", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:sharpness", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:smite", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:bane_of_arthropods", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:knockback", 2);
        vanillaEnchantmentMaxLevels.put("minecraft:fire_aspect", 2);
        vanillaEnchantmentMaxLevels.put("minecraft:looting", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:sweeping_edge", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:efficiency", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:silk_touch", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:unbreaking", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:fortune", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:power", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:punch", 2);
        vanillaEnchantmentMaxLevels.put("minecraft:flame", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:infinity", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:luck_of_the_sea", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:lure", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:loyalty", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:impaling", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:riptide", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:channeling", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:multishot", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:quick_charge", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:piercing", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:mending", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:vanishing_curse", 1);
        vanillaEnchantmentMaxLevels.put("minecraft:soul_speed", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:swift_sneak", 3);
        vanillaEnchantmentMaxLevels.put("minecraft:density", 5);
        vanillaEnchantmentMaxLevels.put("minecraft:breach", 4);
        vanillaEnchantmentMaxLevels.put("minecraft:wind_burst", 3);
    }
}