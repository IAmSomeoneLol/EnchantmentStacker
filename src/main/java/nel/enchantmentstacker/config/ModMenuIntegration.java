package nel.enchantmentstacker.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = ModConfig.get();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Enchantment Stacker Configuration"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // ------------------ ANVIL & GRINDSTONE ------------------
            ConfigCategory anvilCat = builder.getOrCreateCategory(Component.literal("Anvil & Grindstone"));

            anvilCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Fixed Anvil Cost"), config.enableFixedAnvilCost)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Locks the anvil repair costs to a static amount and prevents 'Too Expensive!'"))
                    .setSaveConsumer(newValue -> config.enableFixedAnvilCost = newValue).build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Level Cost"), config.repairCostLevelAmount)
                    .setDefaultValue(3)
                    .setSaveConsumer(newValue -> config.repairCostLevelAmount = newValue).build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Material Cost"), config.repairCostMaterialAmount)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> config.repairCostMaterialAmount = newValue).build());

            anvilCat.addEntry(entryBuilder.startDoubleField(Component.literal("Percent Repaired Per Action"), config.percentRepairedPerAction)
                    .setDefaultValue(0.3333)
                    .setTooltip(Component.literal("How much durability is restored per material. 0.3333 means 3 materials fully repair the item."))
                    .setSaveConsumer(newValue -> config.percentRepairedPerAction = newValue).build());

            anvilCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Grindstone Extraction"), config.enableGrindstoneExtraction)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Place an Enchanted Item and a Book in the Grindstone to safely extract enchants."))
                    .setSaveConsumer(newValue -> config.enableGrindstoneExtraction = newValue).build());


            // ------------------ UNLOCKED: WEAPONS ------------------
            ConfigCategory weaponCat = builder.getOrCreateCategory(Component.literal("Unlocked: Weapons"));

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Sword"), config.unlockedSword)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedSword = newValue).build());
            weaponCat.addEntry(entryBuilder.startStrList(Component.literal("Sword Enchantment List"), config.swordEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.swordEnchantments = newValue).build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Axe"), config.unlockedAxe)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedAxe = newValue).build());
            weaponCat.addEntry(entryBuilder.startStrList(Component.literal("Axe Enchantment List"), config.axeEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.axeEnchantments = newValue).build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Mace"), config.unlockedMace)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedMace = newValue).build());
            weaponCat.addEntry(entryBuilder.startStrList(Component.literal("Mace Enchantment List"), config.maceEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.maceEnchantments = newValue).build());


            // ------------------ UNLOCKED: RANGED & TRIDENT ------------------
            ConfigCategory rangedCat = builder.getOrCreateCategory(Component.literal("Unlocked: Ranged & Trident"));

            rangedCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Trident"), config.unlockedTrident)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedTrident = newValue).build());
            rangedCat.addEntry(entryBuilder.startStrList(Component.literal("Trident Enchantment List"), config.tridentEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.tridentEnchantments = newValue).build());

            rangedCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Bow"), config.unlockedBow)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedBow = newValue).build());
            rangedCat.addEntry(entryBuilder.startStrList(Component.literal("Bow Enchantment List"), config.bowEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.bowEnchantments = newValue).build());

            rangedCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Crossbow"), config.unlockedCrossbow)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedCrossbow = newValue).build());
            rangedCat.addEntry(entryBuilder.startStrList(Component.literal("Crossbow Enchantment List"), config.crossbowEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.crossbowEnchantments = newValue).build());


            // ------------------ UNLOCKED: ARMOR & TOOLS ------------------
            ConfigCategory toolCat = builder.getOrCreateCategory(Component.literal("Unlocked: Armor & Tools"));

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Armor"), config.unlockedArmor)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedArmor = newValue).build());
            toolCat.addEntry(entryBuilder.startStrList(Component.literal("Armor Enchantment List"), config.armorEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.armorEnchantments = newValue).build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Pickaxe"), config.unlockedPickaxe)
                    .setDefaultValue(false)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedPickaxe = newValue).build());
            toolCat.addEntry(entryBuilder.startStrList(Component.literal("Pickaxe Enchantment List"), config.pickaxeEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:efficiency)"))
                    .setSaveConsumer(newValue -> config.pickaxeEnchantments = newValue).build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Shovel"), config.unlockedShovel)
                    .setDefaultValue(false)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist. Allows Shovels to inherit Sword enchants (Sharpness, Smite, Fire Aspect, etc.)."))
                    .setSaveConsumer(newValue -> config.unlockedShovel = newValue).build());
            toolCat.addEntry(entryBuilder.startStrList(Component.literal("Shovel Enchantment List"), config.shovelEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:efficiency)"))
                    .setSaveConsumer(newValue -> config.shovelEnchantments = newValue).build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Unlocked - Hoe"), config.unlockedHoe)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("When TRUE, strictly enforces the whitelist below. When FALSE, falls back to vanilla rules."))
                    .setSaveConsumer(newValue -> config.unlockedHoe = newValue).build());
            toolCat.addEntry(entryBuilder.startStrList(Component.literal("Hoe Enchantment List"), config.hoeEnchantments)
                    .setTooltip(Component.literal("Format: namespace:id (e.g., minecraft:sharpness)"))
                    .setSaveConsumer(newValue -> config.hoeEnchantments = newValue).build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow The Un-Enchantable"), config.allowTheUnEnchantable)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows Shields, Elytra, Shears, Flint & Steel, and Brushes to receive Unbreaking and Mending."))
                    .setSaveConsumer(newValue -> config.allowTheUnEnchantable = newValue).build());


            // ------------------ VANILLA MAX LEVELS ------------------
            ConfigCategory levelsCat = builder.getOrCreateCategory(Component.literal("Vanilla Max Levels"));

            for (Map.Entry<String, Integer> entry : config.vanillaEnchantmentMaxLevels.entrySet()) {
                levelsCat.addEntry(entryBuilder.startIntField(Component.literal(entry.getKey()), entry.getValue())
                        .setDefaultValue(entry.getValue())
                        .setTooltip(Component.literal("Overrides the maximum level. Set to -1 to use vanilla default."))
                        .setSaveConsumer(newValue -> config.vanillaEnchantmentMaxLevels.put(entry.getKey(), newValue))
                        .build());
            }

            builder.setSavingRunnable(ModConfig::save);
            return builder.build();
        };
    }
}