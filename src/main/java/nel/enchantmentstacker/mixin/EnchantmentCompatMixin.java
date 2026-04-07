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

    private static String getEnchKey(Enchantment enchantment) {
        if (enchantment.description().getContents() instanceof TranslatableContents translatable) {
            return translatable.getKey();
        }
        return "";
    }

    @Inject(method = "areCompatible", at = @At("HEAD"), cancellable = true)
    private static void forceCompatibility(Holder<Enchantment> a, Holder<Enchantment> b, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        String keyA = getEnchKey(a.value());
        String keyB = getEnchKey(b.value());

        // Preserve Silk Touch and Fortune incompatibility permanently
        boolean isSilkAndFortune = (keyA.equals("enchantment.minecraft.silk_touch") && keyB.equals("enchantment.minecraft.fortune")) ||
                (keyA.equals("enchantment.minecraft.fortune") && keyB.equals("enchantment.minecraft.silk_touch"));
        if (isSilkAndFortune) return;

        // GRAB THE ITEM FROM MEMORY
        ItemStack current = EnchantmentStacker.CURRENT_ITEM.get();

        // IDENTIFY USING TAGS AND REGISTRY INSTEAD OF CLASSES/COMPONENTS
        boolean isSword = current != null && current.is(ItemTags.SWORDS);
        boolean isAxe = current != null && current.is(ItemTags.AXES);
        boolean isHoe = current != null && current.is(ItemTags.HOES);
        boolean isMace = current != null && current.is(Items.MACE);
        boolean isArmor = current != null && current.is(ItemTags.TRIMMABLE_ARMOR);
        boolean isBow = current != null && current.is(Items.BOW);
        boolean isCrossbow = current != null && current.is(Items.CROSSBOW);
        boolean isBook = current != null && (current.is(Items.BOOK) || current.is(Items.ENCHANTED_BOOK));

        // Damage Enchants
        boolean isDamageA = keyA.equals("enchantment.minecraft.sharpness") || keyA.equals("enchantment.minecraft.smite") || keyA.equals("enchantment.minecraft.bane_of_arthropods");
        boolean isDamageB = keyB.equals("enchantment.minecraft.sharpness") || keyB.equals("enchantment.minecraft.smite") || keyB.equals("enchantment.minecraft.bane_of_arthropods");
        if (isDamageA && isDamageB) {
            if (isSword && config.allowSwordStacker) cir.setReturnValue(true);
            else if (isAxe && config.allowAxeStacker) cir.setReturnValue(true);
            else if (isMace && config.allowMaceStacker) cir.setReturnValue(true);
            else if (isBook && (config.allowSwordStacker || config.allowAxeStacker || config.allowMaceStacker)) cir.setReturnValue(true);
            return;
        }

        // Protection Enchants
        boolean isProtectionA = keyA.contains("protection");
        boolean isProtectionB = keyB.contains("protection");
        if (isProtectionA && isProtectionB) {
            if ((isArmor || isBook) && config.allowArmorStacker) cir.setReturnValue(true);
            return;
        }

        // Bow Enchants
        boolean isBowEnchA = keyA.equals("enchantment.minecraft.infinity") || keyA.equals("enchantment.minecraft.mending");
        boolean isBowEnchB = keyB.equals("enchantment.minecraft.infinity") || keyB.equals("enchantment.minecraft.mending");
        if (isBowEnchA && isBowEnchB) {
            if ((isBow || isBook) && config.allowBowStacker) cir.setReturnValue(true);
            return;
        }

        // Crossbow Enchants
        boolean isCrossbowA = keyA.equals("enchantment.minecraft.multishot") || keyA.equals("enchantment.minecraft.piercing");
        boolean isCrossbowB = keyB.equals("enchantment.minecraft.multishot") || keyB.equals("enchantment.minecraft.piercing");
        if (isCrossbowA && isCrossbowB) {
            if ((isCrossbow || isBook) && config.allowCrossbowStacker) cir.setReturnValue(true);
            return;
        }

        // Mace Specific Enchants
        boolean isMaceA = keyA.equals("enchantment.minecraft.breach") || keyA.equals("enchantment.minecraft.density");
        boolean isMaceB = keyB.equals("enchantment.minecraft.breach") || keyB.equals("enchantment.minecraft.density");
        if (isMaceA && isMaceB) {
            if ((isMace || isBook) && config.allowMaceStacker) cir.setReturnValue(true);
            return;
        }
    }

    @Inject(method = "isSupportedItem", at = @At("HEAD"), cancellable = true)
    private void expandSupportedItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        Enchantment enchantment = (Enchantment) (Object) this;
        String key = getEnchKey(enchantment);

        // Axe, Hoe, & Mace borrow Sword enchants
        if ((config.allowAxeStacker && stack.is(ItemTags.AXES)) ||
                (config.allowHoeExpanded && stack.is(ItemTags.HOES)) ||
                (config.allowMaceStacker && stack.is(Items.MACE))) {
            if (enchantment.isSupportedItem(new ItemStack(Items.DIAMOND_SWORD))) cir.setReturnValue(true);
        }

        // Crossbow borrows Bow enchants
        if (config.allowCrossbowStacker && stack.is(Items.CROSSBOW)) {
            if (enchantment.isSupportedItem(new ItemStack(Items.BOW))) cir.setReturnValue(true);
        }

        // Handle Un-Enchantable Items
        if (config.allowTheUnEnchantable) {
            if (stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.BRUSH)) {
                if (key.equals("enchantment.minecraft.unbreaking") || key.equals("enchantment.minecraft.mending")) cir.setReturnValue(true);
                if (stack.is(Items.SHEARS) && key.equals("enchantment.minecraft.efficiency")) cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isPrimaryItem", at = @At("HEAD"), cancellable = true)
    private void expandPrimaryItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = ModConfig.get();
        Enchantment enchantment = (Enchantment) (Object) this;
        String key = getEnchKey(enchantment);

        if ((config.allowAxeStacker && stack.is(ItemTags.AXES)) ||
                (config.allowHoeExpanded && stack.is(ItemTags.HOES)) ||
                (config.allowMaceStacker && stack.is(Items.MACE))) {
            if (enchantment.isPrimaryItem(new ItemStack(Items.DIAMOND_SWORD))) cir.setReturnValue(true);
        }

        if (config.allowCrossbowStacker && stack.is(Items.CROSSBOW)) {
            if (enchantment.isPrimaryItem(new ItemStack(Items.BOW))) cir.setReturnValue(true);
        }

        if (config.allowTheUnEnchantable) {
            if (stack.is(Items.SHIELD) || stack.is(Items.ELYTRA) || stack.is(Items.SHEARS) || stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.BRUSH)) {
                if (key.equals("enchantment.minecraft.unbreaking") || key.equals("enchantment.minecraft.mending")) cir.setReturnValue(true);
                if (stack.is(Items.SHEARS) && key.equals("enchantment.minecraft.efficiency")) cir.setReturnValue(true);
            }
        }
    }
}