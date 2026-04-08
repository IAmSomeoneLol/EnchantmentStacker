package nel.enchantmentstacker.mixin;

import nel.enchantmentstacker.config.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    // Bypass the Grindstones stricts slot rules to allow Books
    @ModifyVariable(method = "addSlot", at = @At("HEAD"), argsOnly = true)
    private Slot allowBooksInGrindstone(Slot slot) {
        if ((Object) this instanceof GrindstoneMenu) {
            // Isolate the two input slots
            if (slot.container.getContainerSize() == 2) {
                return new Slot(slot.container, slot.getContainerSlot(), slot.x, slot.y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        // If the feature is enabled then let the empty book inside
                        if (ModConfig.get().enableGrindstoneExtraction && stack.is(Items.BOOK)) {
                            return true;
                        }
                        // else fall back to whatever the vanilla slot allows
                        return slot.mayPlace(stack);
                    }
                };
            }
        }
        return slot;
    }

    // Fix for book not being allowed
    private static final ThreadLocal<Integer> SAVED_BOOK_SLOT = ThreadLocal.withInitial(() -> -1);
    private static final ThreadLocal<ItemStack> SAVED_BOOK_STACK = ThreadLocal.withInitial(() -> ItemStack.EMPTY);

    @Inject(method = "clicked", at = @At("HEAD"))
    private void onGrindstoneExtractClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (ModConfig.get().enableGrindstoneExtraction) {

            if ((Object) this instanceof GrindstoneMenu menu && slotId == 2) {

                if (slotId >= menu.slots.size() || slotId < 0) return;

                Slot resultSlot = menu.getSlot(2);

                if (resultSlot.hasItem() && resultSlot.getItem().is(Items.ENCHANTED_BOOK)) {
                    ItemStack top = menu.getSlot(0).getItem();
                    ItemStack bottom = menu.getSlot(1).getItem();

                    boolean topIsBook = top.is(Items.BOOK);
                    boolean bottomIsBook = bottom.is(Items.BOOK);

                    ItemEnchantments topEnch = top.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                    if (topEnch.isEmpty()) topEnch = top.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

                    ItemEnchantments bottomEnch = bottom.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                    if (bottomEnch.isEmpty()) bottomEnch = bottom.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

                    if ((topIsBook && !bottomEnch.isEmpty()) || (bottomIsBook && !topEnch.isEmpty())) {

                        ItemStack targetItem = topIsBook ? bottom : top;
                        ItemStack bookItem = topIsBook ? top : bottom;
                        int bookSlotIndex = topIsBook ? 0 : 1;

                        ItemStack stripped = targetItem.copy();
                        stripped.setCount(1);
                        stripped.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                        stripped.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

                        if (!player.getInventory().add(stripped)) {
                            player.drop(stripped, false);
                        }

                        // Book calc
                        ItemStack remainingBooks = bookItem.copy();
                        remainingBooks.shrink(1); // Consume exactly 1 book
                        SAVED_BOOK_SLOT.set(bookSlotIndex);
                        SAVED_BOOK_STACK.set(remainingBooks);

                        // Strip the enchants - vanilla calculates XP
                        targetItem.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                        targetItem.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                    }
                }
            }
        }
    }

    // Grindstone book fix;
    @Inject(method = "clicked", at = @At("RETURN"))
    private void restoreBooksAfterClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        int savedSlot = SAVED_BOOK_SLOT.get();
        if (savedSlot != -1) {
            if ((Object) this instanceof GrindstoneMenu menu) {
                ItemStack savedStack = SAVED_BOOK_STACK.get();
                if (!savedStack.isEmpty()) {
                    menu.getSlot(savedSlot).set(savedStack);
                }
            }
            SAVED_BOOK_SLOT.set(-1);
            SAVED_BOOK_STACK.set(ItemStack.EMPTY);
        }
    }
}