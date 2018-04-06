package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.item.ItemWand;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.registry.EnchantmentRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class EnchantingHandler implements IGuiHandler {
    private static final EnchantingHandler INSTANCE = new EnchantingHandler();

    public static EnchantingHandler getInstance() {
        return INSTANCE;
    }

    private EnchantingHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEnchantingInteract(PlayerInteractEvent.RightClickBlock event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if(block == Blocks.ENCHANTING_TABLE) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            event.setUseBlock(Event.Result.DENY);
            if(!event.getWorld().isRemote) {
                event.getEntityPlayer().openGui(ElementalInvocations.instance, 0, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
            }
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerEnchantOverride(player.inventory, world, new BlockPos(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if(tile instanceof TileEntityEnchantmentTable) {
            return new GuiEnchantOverride(player.inventory, world, (TileEntityEnchantmentTable) tile);
        }
        return null;
    }

    public static class ContainerEnchantOverride extends ContainerEnchantment {
        private final World world;
        private final BlockPos pos;

        public ContainerEnchantOverride(InventoryPlayer playerInv, World world, BlockPos pos) {
            super(playerInv, world, pos);
            this.world = world;
            this.pos = pos;
        }

        @Override
        public void onCraftMatrixChanged(IInventory inventoryIn) {
            ItemStack stack = inventoryIn.getStackInSlot(0);
            if(stack != null && stack.getItem() instanceof ItemWand) {
                if(!this.world.isRemote) {
                    for (int i = 0; i < 3; i++) {
                        this.enchantLevels[i] = 0;
                        this.enchantClue[i] = -1;
                        this.worldClue[i] = -1;
                    }
                    ItemWand wand = (ItemWand) stack.getItem();
                    if (wand.isEnchantable(stack) && EnchantingHandler.getInstance().checkWorldRequirements(this.world, this.pos, stack)) {
                        int tier = wand.getPotency(stack);
                        this.enchantLevels[0] = ModConfiguration.getInstance().getLevelsForTierLevelup() * (tier + 2);
                        this.enchantClue[0] = Enchantment.getEnchantmentID(EnchantmentRegistry.getInstance().ENCHANTMENT_AUGMENT_WAND);
                        this.worldClue[0] = tier + 2;
                    }
                    this.detectAndSendChanges();
                }
            } else {
                super.onCraftMatrixChanged(inventoryIn);
            }
        }

        @Override
        public boolean enchantItem(EntityPlayer player, int id) {
            ItemStack stack = this.tableInventory.getStackInSlot(0);
            if(stack != null && stack.getItem() instanceof ItemWand) {
                if(!this.world.isRemote) {
                    ItemWand wand = (ItemWand) stack.getItem();
                    if (wand.isEnchantable(stack)) {
                        int req = this.enchantLevels[0];
                        if((req > 0 && player.experienceLevel >= req) || player.capabilities.isCreativeMode) {
                            wand.incrementPotency(stack);
                            if(!player.capabilities.isCreativeMode) {
                                ItemStack lapis = this.tableInventory.getStackInSlot(1);
                                player.removeExperienceLevel(req);
                                lapis.stackSize -= 1;
                                if (lapis.stackSize <= 0) {
                                    this.tableInventory.setInventorySlotContents(1, null);
                                }
                            }
                            player.addStat(StatList.ITEM_ENCHANTED);
                            this.tableInventory.markDirty();
                            this.xpSeed = player.getXPSeed();
                            this.onCraftMatrixChanged(this.tableInventory);
                            this.world.playSound(null, this.pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                            EnchantingHandler.getInstance().onWandPowerUp(player, this.world, this.pos, stack);
                        }
                    }
                }
                return true;
            } else {
                return super.enchantItem(player, id);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GuiEnchantOverride extends GuiEnchantment {
        public GuiEnchantOverride(InventoryPlayer inventory, World worldIn, IWorldNameable nameable) {
            super(inventory, worldIn, nameable);
        }


    }

    public boolean checkWorldRequirements(World world, BlockPos pos, ItemStack wand) {
        //TODO
        return true;
    }

    public String getWorldRequirementHint(World world, BlockPos pos, ItemStack wand) {
        //TODO;
        return "";
    }

    public void onWandPowerUp(EntityPlayer player, World world, BlockPos pos, ItemStack wand) {
        if(!world.isRemote && wand != null && wand.getItem() instanceof ItemWand) {
            ((ItemWand) wand.getItem()).getWandCore(wand).ifPresent(wandCore -> {
                switch (wandCore.element()) {
                    case LIFE:
                        //TODO
                        break;

                    case AIR:
                        EntityLightningBolt lightningBolt = new EntityLightningBolt(world, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5, true);
                        world.spawnEntity(lightningBolt);
                        break;

                    case FIRE:
                        this.setFire(world, pos.north());
                        this.setFire(world, pos.north().north());
                        this.setFire(world, pos.north().east());
                        this.setFire(world, pos.east());
                        this.setFire(world, pos.east().east());
                        this.setFire(world, pos.east().south());
                        this.setFire(world, pos.south());
                        this.setFire(world, pos.south().south());
                        this.setFire(world, pos.south().west());
                        this.setFire(world, pos.west());
                        this.setFire(world, pos.west().north());
                        break;

                    case DEATH:
                        world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-2, -1, -2), pos.add(3, 2, 3))).forEach(
                                entity -> MagicDamageHandler.getInstance().dealDamage(entity, wandCore.tier(), player, wandCore.element(), wandCore.tier() + 1)
                        );
                        break;

                    case EARTH:
                        int counter = wandCore.tier() + 1;
                        List<BlockPos> list = new ArrayList<>();
                        for (int x = -3; x <= 3 && counter > 0; x++) {
                            for (int y = -3; y <= 3 && counter > 0; y++) {
                                for (int z = -3; z <= 3 && counter > 0; z++) {
                                    BlockPos posAt = pos.add(x, y, z);
                                    IBlockState state = world.getBlockState(posAt);
                                    if (state.getBlock() == Blocks.OBSIDIAN) {
                                        list.add(posAt);
                                        counter--;
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < Math.min(list.size(), wandCore.tier() + 1); i++) {
                            int index = world.rand.nextInt(list.size());
                            world.setBlockState(list.get(index), Blocks.COBBLESTONE.getDefaultState());
                        }
                        break;

                    case WATER:
                        for (int x = -2; x <= 2; x++) {
                            for (int z = -2; z <= 2; z++) {
                                BlockPos posAt = pos.add(x, -1, z);
                                IBlockState state = world.getBlockState(posAt);
                                if (state.getBlock() == Blocks.WATER) {
                                    world.setBlockToAir(posAt);
                                }
                            }
                        }
                        break;
                }
            });
        }
        //TODO: particles
    }

    private void setFire(World world, BlockPos pos) {
        if(world.isAirBlock(pos) && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
    }
}