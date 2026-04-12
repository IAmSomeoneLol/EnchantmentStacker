package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.EnchantmentStacker;
import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.contents.TranslatableContents;
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

    // Converts "enchantment.minecraft.smite" into "minecraft:smite" to match the config lists!
    private static String getEnchKey(Enchantment enchantment) {
        if (enchantment.description().getContents() instanceof TranslatableContents translatable) {
            String key = translatable.getKey();
            if (key.startsWith("enchantment.")) {
                String trimmed = key.substring(12);
                int dotIndex = trimmed.indexOf('.');
                if (dotIndex != -1) {
                    return trimmed.substring(0, dotIndex) + ":" + trimmed.substring(dotIndex + 1);
                }
            }
            return key;
        }
        return "";
    }

    @Inject(method = "isSupportedItem", at = @At("HEAD"))
    private void captureContextFromSupported(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentStacker.CURRENT_ITEM.set(stack);
    }

    @Inject(method = "isPrimaryItem", at = @At("HEAD"))
    private void captureContextFromPrimary(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentStacker.CURRENT_ITEM.set(stack);
    }

    @Inject(method = "areCompatible", at = @At("HEAD"), cancellable = true)
    private static void forceCompatibility(Holder<Enchantment> a, Holder<Enchantment> b, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        String keyA = getEnchKey(a.value());
        String keyB = getEnchKey(b.value());

        // Preserve Silk Touch and Fortune incompatibility permanently
        if ((keyA.equals("minecraft:silk_touch") && keyB.equals("minecraft:fortune")) ||
                (keyA.equals("minecraft:fortune") && keyB.equals("minecraft:silk_touch"))) return;

        ItemStack current = EnchantmentStacker.CURRENT_ITEM.get();
        if (current == null || current.isEmpty()) return;

        boolean isSword = current.is(ItemTags.SWORDS);
        boolean isAxe = current.is(ItemTags.AXES);
        boolean isHoe = current.is(ItemTags.HOES);
        boolean isMace = current.is(Items.MACE);
        boolean isTrident = current.is(Items.TRIDENT);
        boolean isArmor = current.is(ItemTags.TRIMMABLE_ARMOR);
        boolean isBow = current.is(Items.BOW);
        boolean isCrossbow = current.is(Items.CROSSBOW);
        boolean isBook = current.is(Items.BOOK) || current.is(Items.ENCHANTED_BOOK);

        // Check if the current tool is unlocked AND if BOTH enchantments are in its custom list
        if ((isSword || isBook) && config.unlockedSword && config.swordEnchantments.contains(keyA) && config.swordEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isAxe || isBook) && config.unlockedAxe && config.axeEnchantments.contains(keyA) && config.axeEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isMace || isBook) && config.unlockedMace && config.maceEnchantments.contains(keyA) && config.maceEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isTrident || isBook) && config.unlockedTrident && config.tridentEnchantments.contains(keyA) && config.tridentEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isBow || isBook) && config.unlockedBow && config.bowEnchantments.contains(keyA) && config.bowEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isCrossbow || isBook) && config.unlockedCrossbow && config.crossbowEnchantments.contains(keyA) && config.crossbowEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isArmor || isBook) && config.unlockedArmor && config.armorEnchantments.contains(keyA) && config.armorEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
        if ((isHoe || isBook) && config.unlockedHoe && config.hoeEnchantments.contains(keyA) && config.hoeEnchantments.contains(keyB)) { cir.setReturnValue(true); return; }
    }

    @Inject(method = "isSupportedItem", at = @At("HEAD"), cancellable = true)
    private void expandSupportedItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        Enchantment enchantment = (Enchantment) (Object) this;
        String key = getEnchKey(enchantment);

        if (config.unlockedSword && stack.is(ItemTags.SWORDS) && config.swordEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedAxe && stack.is(ItemTags.AXES) && config.axeEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedMace && stack.is(Items.MACE) && config.maceEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedTrident && stack.is(Items.TRIDENT) && config.tridentEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedBow && stack.is(Items.BOW) && config.bowEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedCrossbow && stack.is(Items.CROSSBOW) && config.crossbowEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedArmor && stack.is(ItemTags.TRIMMABLE_ARMOR) && config.armorEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedHoe && stack.is(ItemTags.HOES) && config.hoeEnchantments.contains(key)) cir.setReturnValue(true);

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
        String key = getEnchKey(enchantment);

        if (config.unlockedSword && stack.is(ItemTags.SWORDS) && config.swordEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedAxe && stack.is(ItemTags.AXES) && config.axeEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedMace && stack.is(Items.MACE) && config.maceEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedTrident && stack.is(Items.TRIDENT) && config.tridentEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedBow && stack.is(Items.BOW) && config.bowEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedCrossbow && stack.is(Items.CROSSBOW) && config.crossbowEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedArmor && stack.is(ItemTags.TRIMMABLE_ARMOR) && config.armorEnchantments.contains(key)) cir.setReturnValue(true);
        if (config.unlockedHoe && stack.is(ItemTags.HOES) && config.hoeEnchantments.contains(key)) cir.setReturnValue(true);

        if (config.allowTheUnEnchantable) {
            if (stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.BRUSH)) {
                if (key.equals("minecraft:unbreaking") || key.equals("minecraft:mending")) cir.setReturnValue(true);
                if (stack.is(Items.SHEARS) && key.equals("minecraft:efficiency")) cir.setReturnValue(true);
            }
        }
    }
}