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

            // ----------------- ANVIL & GRINDSTONE SETTINGS ------------------
            ConfigCategory anvilCat = builder.getOrCreateCategory(Component.literal("Anvil & Grindstone Settings"));

            anvilCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Fixed Anvil Cost"), config.enableFixedAnvilCost)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Locks the anvil repair costs to a static amount and prevents the 'Too Expensive!' mechanic."))
                    .setSaveConsumer(newValue -> config.enableFixedAnvilCost = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Level Cost"), config.repairCostLevelAmount)
                    .setDefaultValue(3)
                    .setTooltip(Component.literal("The flat XP level cost for anvil repairs."))
                    .setSaveConsumer(newValue -> config.repairCostLevelAmount = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Material Cost"), config.repairCostMaterialAmount)
                    .setDefaultValue(1)
                    .setTooltip(Component.literal("The flat material cost (e.g., 1 Iron Ingot) for anvil repairs."))
                    .setSaveConsumer(newValue -> config.repairCostMaterialAmount = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startDoubleField(Component.literal("Percent Repaired Per Action"), config.percentRepairedPerAction)
                    .setDefaultValue(0.3333)
                    .setTooltip(Component.literal("How much durability is restored per material. 0.3333 means 3 materials fully repairs the item."))
                    .setSaveConsumer(newValue -> config.percentRepairedPerAction = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Grindstone Extraction"), config.enableGrindstoneExtraction)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Place an Enchanted Item and a Book in the Grindstone to strip the enchants onto an Enchanted Book."))
                    .setSaveConsumer(newValue -> config.enableGrindstoneExtraction = newValue)
                    .build());


            // -----------------WEAPON STACKERS ---------------
            ConfigCategory weaponCat = builder.getOrCreateCategory(Component.literal("Weapon Stackers"));

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Sword Stacker"), config.allowSwordStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows combining Sharpness, Smite, and Bane of Arthropods on a single Sword."))
                    .setSaveConsumer(newValue -> config.allowSwordStacker = newValue)
                    .build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Axe Stacker"), config.allowAxeStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Combines Sharpness, Smite, and Bane of Arthropods. Allows Axes to inherit Sword enchants (Fire Aspect, Looting, Knockback). Blacklists Sweeping Edge."))
                    .setSaveConsumer(newValue -> config.allowAxeStacker = newValue)
                    .build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Mace Stacker"), config.allowMaceStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Combines Breach and Density. Allows Maces to inherit Sword enchants (Sharpness, Smite, Fire Aspect, Looting, etc.)."))
                    .setSaveConsumer(newValue -> config.allowMaceStacker = newValue)
                    .build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Bow Stacker"), config.allowBowStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows combining Infinity and Mending on a single Bow."))
                    .setSaveConsumer(newValue -> config.allowBowStacker = newValue)
                    .build());

            weaponCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Crossbow Stacker"), config.allowCrossbowStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Combines Multishot and Piercing. Allows Crossbows to inherit Bow enchants (Power, Flame, Punch, Infinity, Mending)."))
                    .setSaveConsumer(newValue -> config.allowCrossbowStacker = newValue)
                    .build());


            // --------------- ARMOR & TOOL TWEAKS ------------------
            ConfigCategory toolCat = builder.getOrCreateCategory(Component.literal("Armor & Tool Tweaks"));

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Armor Stacker"), config.allowArmorStacker)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows combining Protection, Fire Protection, Blast Protection, and Projectile Protection on Armor pieces."))
                    .setSaveConsumer(newValue -> config.allowArmorStacker = newValue)
                    .build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Hoe Expanded"), config.allowHoeExpanded)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows Hoes to inherit Sword enchants (Sharpness, Smite, Bane of Arthropods, Fire Aspect, Looting, etc.)."))
                    .setSaveConsumer(newValue -> config.allowHoeExpanded = newValue)
                    .build());

            toolCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow The Un-Enchantable"), config.allowTheUnEnchantable)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows Shields, Elytra, Shears, Flint & Steel, and Brushes to receive Unbreaking and Mending natively. Shears also receive Efficiency."))
                    .setSaveConsumer(newValue -> config.allowTheUnEnchantable = newValue)
                    .build());


            // ------ VANILLA MAX LEVELS -------------
            ConfigCategory levelsCat = builder.getOrCreateCategory(Component.literal("Vanilla Max Levels"));

            for (Map.Entry<String, Integer> entry : config.vanillaEnchantmentMaxLevels.entrySet()) {
                String readableName = entry.getKey().replace("enchantment.minecraft.", "").replace("_", " ");
                readableName = readableName.substring(0, 1).toUpperCase() + readableName.substring(1);

                levelsCat.addEntry(entryBuilder.startIntField(Component.literal(readableName), entry.getValue())
                        .setDefaultValue(entry.getValue())
                        .setTooltip(Component.literal("Overrides the maximum level for " + readableName + ". Set to -1 to use vanilla default."))
                        .setSaveConsumer(newValue -> config.vanillaEnchantmentMaxLevels.put(entry.getKey(), newValue))
                        .build());
            }

            builder.setSavingRunnable(ModConfig::save);
            return builder.build();
        };
    }
}