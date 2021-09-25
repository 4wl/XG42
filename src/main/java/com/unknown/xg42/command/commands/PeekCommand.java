package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.SyntaxChunk;
import com.unknown.xg42.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class PeekCommand extends Command {

    public static TileEntityShulkerBox sb;

    public PeekCommand() {
        super("peek", SyntaxChunk.EMPTY);
        setDescription("Look inside the contents of a shulker box without opening it");
    }

    @Override
    public void call(String[] args) {
        ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();

        if (is.getItem() instanceof ItemShulkerBox) {
            Test entityBox = new Test();
            entityBox.setBlockType(((ItemShulkerBox) is.getItem()).getBlock());
            entityBox.setWorld(Wrapper.getWorld());
            entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
            sb = entityBox;
        } else {
            Command.sendChatMessage("You aren't carrying a shulker box.");
        }
    }
    private class Test extends TileEntityShulkerBox {
        public void setBlockType(Block blockType){
            this.blockType = blockType;
        }
    }
}
