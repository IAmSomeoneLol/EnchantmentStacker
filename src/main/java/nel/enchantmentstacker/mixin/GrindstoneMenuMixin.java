package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void onCreateExtractionResult(CallbackInfo ci) {
        if (!ModConfig.get().enableGrindstoneExtraction) return;

        GrindstoneMenu menu = (GrindstoneMenu) (Object) this;
        ItemStack top = menu.getSlot(0).getItem();
        ItemStack bottom = menu.getSlot(1).getItem();

        boolean topIsBook = top.is(Items.BOOK);
        boolean bottomIsBook = bottom.is(Items.BOOK);

        ItemEnchantments topEnch = top.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (topEnch.isEmpty()) topEnch = top.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        ItemEnchantments bottomEnch = bottom.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (bottomEnch.isEmpty()) bottomEnch = bottom.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        // Slot checker for book
        if ((topIsBook && !bottomEnch.isEmpty()) || (bottomIsBook && !topEnch.isEmpty())) {

            ItemEnchantments enchantsToTransfer = topIsBook ? bottomEnch : topEnch;

            // Enchantment gen
            ItemStack resultBook = new ItemStack(Items.ENCHANTED_BOOK);
            resultBook.set(DataComponents.STORED_ENCHANTMENTS, enchantsToTransfer);

            // Numero tres enchantment book
            menu.getSlot(2).set(resultBook);
            menu.broadcastChanges();
            ci.cancel();
        }
    }
}