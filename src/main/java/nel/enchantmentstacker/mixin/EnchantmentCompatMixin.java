package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.EnchantmentStacker;
import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentCompatMixin {

    private static String getEnchKeyFromInstance(Enchantment enchantment) {
        String str = enchantment.description().toString();
        int start = str.indexOf("enchantment.");
        if (start != -1) {
            int end = start;
            while (end < str.length() && (Character.isLetterOrDigit(str.charAt(end)) || str.charAt(end) == '.' || str.charAt(end) == '_' || str.charAt(end) == '-')) {
                end++;
            }
            String fullKey = str.substring(start, end);
            String trimmed = fullKey.substring(12);
            int dotIndex = trimmed.indexOf('.');
            if (dotIndex != -1) {
                return trimmed.substring(0, dotIndex) + ":" + trimmed.substring(dotIndex + 1);
            } else {
                return "minecraft:" + trimmed;
            }
        }
        return "";
    }

    private static String getEnchKey(Holder<Enchantment> holder) {
        if (holder.unwrapKey().isPresent()) {
            String str = holder.unwrapKey().get().toString();
            int start = str.indexOf(" / ");
            if (start != -1) {
                int end = str.indexOf("]", start);
                if (end != -1) {
                    return str.substring(start + 3, end);
                }
            }
        }
        return getEnchKeyFromInstance(holder.value());
    }

    @Inject(method = "areCompatible", at = @At("HEAD"), cancellable = true)
    private static void forceCompatibility(Holder<Enchantment> a, Holder<Enchantment> b, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        String keyA = getEnchKey(a);
        String keyB = getEnchKey(b);

        if (keyA.isEmpty() || keyB.isEmpty()) return;

        ItemStack current = EnchantmentStacker.CURRENT_ITEM.get();
        if (current == null || current.isEmpty()) return;

        boolean isSword = current.is(ItemTags.SWORDS);
        boolean isAxe = current.is(ItemTags.AXES);
        boolean isMace = current.is(Items.MACE);
        boolean isTrident = current.is(Items.TRIDENT);
        boolean isBow = current.is(Items.BOW);
        boolean isCrossbow = current.is(Items.CROSSBOW);
        boolean isArmor = current.is(ItemTags.TRIMMABLE_ARMOR);
        boolean isPickaxe = current.is(ItemTags.PICKAXES);
        boolean isShovel = current.is(ItemTags.SHOVELS);
        boolean isHoe = current.is(ItemTags.HOES);
        boolean isBook = current.is(Items.BOOK) || current.is(Items.ENCHANTED_BOOK);

        // STRICT WHITELIST CHECK
        if (isBook) {
            if (config.unlockedSword && config.swordEnchantments.contains(keyA) && config.swordEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedAxe && config.axeEnchantments.contains(keyA) && config.axeEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedMace && config.maceEnchantments.contains(keyA) && config.maceEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedTrident && config.tridentEnchantments.contains(keyA) && config.tridentEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedBow && config.bowEnchantments.contains(keyA) && config.bowEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedCrossbow && config.crossbowEnchantments.contains(keyA) && config.crossbowEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedArmor && config.armorEnchantments.contains(keyA) && config.armorEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedPickaxe && config.pickaxeEnchantments.contains(keyA) && config.pickaxeEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedShovel && config.shovelEnchantments.contains(keyA) && config.shovelEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
            if (config.unlockedHoe && config.hoeEnchantments.contains(keyA) && config.hoeEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        } else {
            if (isSword && config.unlockedSword) { cir.setReturnValue(config.swordEnchantments.contains(keyA) && config.swordEnchantments.contains(keyB)); return; }
            if (isAxe && config.unlockedAxe) { cir.setReturnValue(config.axeEnchantments.contains(keyA) && config.axeEnchantments.contains(keyB)); return; }
            if (isMace && config.unlockedMace) { cir.setReturnValue(config.maceEnchantments.contains(keyA) && config.maceEnchantments.contains(keyB)); return; }
            if (isTrident && config.unlockedTrident) { cir.setReturnValue(config.tridentEnchantments.contains(keyA) && config.tridentEnchantments.contains(keyB)); return; }
            if (isBow && config.unlockedBow) { cir.setReturnValue(config.bowEnchantments.contains(keyA) && config.bowEnchantments.contains(keyB)); return; }
            if (isCrossbow && config.unlockedCrossbow) { cir.setReturnValue(config.crossbowEnchantments.contains(keyA) && config.crossbowEnchantments.contains(keyB)); return; }
            if (isArmor && config.unlockedArmor) { cir.setReturnValue(config.armorEnchantments.contains(keyA) && config.armorEnchantments.contains(keyB)); return; }
            if (isPickaxe && config.unlockedPickaxe) { cir.setReturnValue(config.pickaxeEnchantments.contains(keyA) && config.pickaxeEnchantments.contains(keyB)); return; }
            if (isShovel && config.unlockedShovel) { cir.setReturnValue(config.shovelEnchantments.contains(keyA) && config.shovelEnchantments.contains(keyB)); return; }
            if (isHoe && config.unlockedHoe) { cir.setReturnValue(config.hoeEnchantments.contains(keyA) && config.hoeEnchantments.contains(keyB)); return; }
        }
    }

    @Inject(method = "isSupportedItem", at = @At("HEAD"), cancellable = true)
    private void expandSupportedItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        Enchantment enchantment = (Enchantment) (Object) this;
        String key = getEnchKeyFromInstance(enchantment);

        if (key.isEmpty()) return;
        if (stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK)) return;

        // STRICT WHITELIST CHECK
        if (config.unlockedSword && stack.is(ItemTags.SWORDS)) { cir.setReturnValue(config.swordEnchantments.contains(key)); return; }
        if (config.unlockedAxe && stack.is(ItemTags.AXES)) { cir.setReturnValue(config.axeEnchantments.contains(key)); return; }
        if (config.unlockedMace && stack.is(Items.MACE)) { cir.setReturnValue(config.maceEnchantments.contains(key)); return; }
        if (config.unlockedTrident && stack.is(Items.TRIDENT)) { cir.setReturnValue(config.tridentEnchantments.contains(key)); return; }
        if (config.unlockedBow && stack.is(Items.BOW)) { cir.setReturnValue(config.bowEnchantments.contains(key)); return; }
        if (config.unlockedCrossbow && stack.is(Items.CROSSBOW)) { cir.setReturnValue(config.crossbowEnchantments.contains(key)); return; }
        if (config.unlockedArmor && stack.is(ItemTags.TRIMMABLE_ARMOR)) { cir.setReturnValue(config.armorEnchantments.contains(key)); return; }
        if (config.unlockedPickaxe && stack.is(ItemTags.PICKAXES)) { cir.setReturnValue(config.pickaxeEnchantments.contains(key)); return; }
        if (config.unlockedShovel && stack.is(ItemTags.SHOVELS)) { cir.setReturnValue(config.shovelEnchantments.contains(key)); return; }
        if (config.unlockedHoe && stack.is(ItemTags.HOES)) { cir.setReturnValue(config.hoeEnchantments.contains(key)); return; }

        if (config.allowTheUnEnchantable) {
            if (stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.BRUSH)) {
                if (key.equals("minecraft:unbreaking") || key.equals("minecraft:mending")) cir.setReturnValue(true);
                if (stack.is(Items.SHEARS) && key.equals("minecraft:efficiency")) cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isPrimaryItem", at = @At("HEAD"), cancellable = true)
    private void expandPrimaryItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        Enchantment enchantment = (Enchantment) (Object) this;
        String key = getEnchKeyFromInstance(enchantment);

        if (key.isEmpty()) return;
        if (stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK)) return;

        // STRICT WHITELIST CHECK
        if (config.unlockedSword && stack.is(ItemTags.SWORDS)) { cir.setReturnValue(config.swordEnchantments.contains(key)); return; }
        if (config.unlockedAxe && stack.is(ItemTags.AXES)) { cir.setReturnValue(config.axeEnchantments.contains(key)); return; }
        if (config.unlockedMace && stack.is(Items.MACE)) { cir.setReturnValue(config.maceEnchantments.contains(key)); return; }
        if (config.unlockedTrident && stack.is(Items.TRIDENT)) { cir.setReturnValue(config.tridentEnchantments.contains(key)); return; }
        if (config.unlockedBow && stack.is(Items.BOW)) { cir.setReturnValue(config.bowEnchantments.contains(key)); return; }
        if (config.unlockedCrossbow && stack.is(Items.CROSSBOW)) { cir.setReturnValue(config.crossbowEnchantments.contains(key)); return; }
        if (config.unlockedArmor && stack.is(ItemTags.TRIMMABLE_ARMOR)) { cir.setReturnValue(config.armorEnchantments.contains(key)); return; }
        if (config.unlockedPickaxe && stack.is(ItemTags.PICKAXES)) { cir.setReturnValue(config.pickaxeEnchantments.contains(key)); return; }
        if (config.unlockedShovel && stack.is(ItemTags.SHOVELS)) { cir.setReturnValue(config.shovelEnchantments.contains(key)); return; }
        if (config.unlockedHoe && stack.is(ItemTags.HOES)) { cir.setReturnValue(config.hoeEnchantments.contains(key)); return; }

        if (config.allowTheUnEnchantable) {
            if (stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.BRUSH)) {
                if (key.equals("minecraft:unbreaking") || key.equals("minecraft:mending")) cir.setReturnValue(true);
                if (stack.is(Items.SHEARS) && key.equals("minecraft:efficiency")) cir.setReturnValue(true);
            }
        }
    }
}