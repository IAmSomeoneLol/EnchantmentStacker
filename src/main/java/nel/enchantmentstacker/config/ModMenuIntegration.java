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

            // Anvil settings Category
            ConfigCategory anvilCat = builder.getOrCreateCategory(Component.literal("Anvil Settings"));

            anvilCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Fixed Anvil Cost"), config.enableFixedAnvilCost)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.enableFixedAnvilCost = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Level Cost"), config.repairCostLevelAmount)
                    .setDefaultValue(3)
                    .setSaveConsumer(newValue -> config.repairCostLevelAmount = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startIntField(Component.literal("Repair Material Cost"), config.repairCostMaterialAmount)
                    .setDefaultValue(1)
                    .setSaveConsumer(newValue -> config.repairCostMaterialAmount = newValue)
                    .build());

            anvilCat.addEntry(entryBuilder.startDoubleField(Component.literal("Percent Repaired Per Action"), config.percentRepairedPerAction)
                    .setDefaultValue(0.3333)
                    .setSaveConsumer(newValue -> config.percentRepairedPerAction = newValue)
                    .build());

            // Stck Settings Category
            ConfigCategory stackerCat = builder.getOrCreateCategory(Component.literal("Enchantment Stackers"));

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Sword Stacker"), config.allowSwordStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowSwordStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Armor Stacker"), config.allowArmorStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowArmorStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Axe Stacker"), config.allowAxeStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowAxeStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Hoe Expanded"), config.allowHoeExpanded)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowHoeExpanded = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Bow Stacker"), config.allowBowStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowBowStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Crossbow Stacker"), config.allowCrossbowStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowCrossbowStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow Mace Stacker"), config.allowMaceStacker)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.allowMaceStacker = newValue)
                    .build());

            stackerCat.addEntry(entryBuilder.startBooleanToggle(Component.literal("Allow The Un-Enchantable"), config.allowTheUnEnchantable)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Allows Elytra, Shield, Shears, Flint & Steel, and Brushes to receive enchants natively."))
                    .setSaveConsumer(newValue -> config.allowTheUnEnchantable = newValue)
                    .build());

            // Vanilla Max Levels Category
            ConfigCategory levelsCat = builder.getOrCreateCategory(Component.literal("Vanilla Max Levels"));

            for (Map.Entry<String, Integer> entry : config.vanillaEnchantmentMaxLevels.entrySet()) {
                String readableName = entry.getKey().replace("enchantment.minecraft.", "").replace("_", " ");
                readableName = readableName.substring(0, 1).toUpperCase() + readableName.substring(1);

                levelsCat.addEntry(entryBuilder.startIntField(Component.literal(readableName), entry.getValue())
                        .setDefaultValue(entry.getValue())
                        .setSaveConsumer(newValue -> config.vanillaEnchantmentMaxLevels.put(entry.getKey(), newValue))
                        .build());
            }

            builder.setSavingRunnable(ModConfig::save);
            return builder.build();
        };
    }
}