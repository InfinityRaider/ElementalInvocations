package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.item.ItemWand;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.network.MessageUpdateGuiEnchantment;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.registry.EnchantmentRegistry;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.text.TextFormatting;
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
import java.util.function.BiPredicate;

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
        if(player instanceof EntityPlayerMP) {
            return new ContainerEnchantOverride((EntityPlayerMP) player, world, new BlockPos(x, y, z));
        }
        return null;
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

    public boolean checkWorldRequirements(World world, BlockPos pos, ItemStack wand) {
        if(!world.isRemote && wand != null && wand.getItem() instanceof ItemWand) {
            return ((ItemWand) wand.getItem()).getWandCore(wand).map(core -> {
                switch(core.element()) {
                    case LIFE: return checkWorldRequirementLife(world, pos);
                    case AIR: return checkWorldRequirementAir(world, pos);
                    case FIRE: return checkWorldRequirementFire(world, pos);
                    case DEATH: return checkWorldRequirementDeath(world, pos);
                    case EARTH: return checkWorldRequirementEarth(world, pos);
                    case WATER: return checkWorldRequirementWater(world, pos);
                    default: return false;
                }
            }).orElse(false);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public String getWorldRequirementHint(World world, BlockPos pos, ItemStack wand) {
        String def = "tooltip." + Reference.MOD_ID.toLowerCase() + ".ench.requirements_unknown";
        if(wand != null && wand.getItem() instanceof ItemWand) {
            return ((ItemWand) wand.getItem()).getWandCore(wand).map(core ->
                    "tooltip." + Reference.MOD_ID.toLowerCase() + ".ench.requirements_" + core.element().name().toLowerCase()
            ).orElse(def);
        }
        return def;
    }

    public void applyPowerUpEffect(EntityPlayer player, World world, BlockPos pos, ItemStack wand) {
        if(!world.isRemote && wand != null && wand.getItem() instanceof ItemWand) {
            ((ItemWand) wand.getItem()).getWandCore(wand).ifPresent(wandCore -> {
                switch (wandCore.element()) {
                    case LIFE:
                        this.applyPowerUpEffectLife(player, world, pos, wandCore.tier() + 1);
                        break;
                    case AIR:
                        this.applyPowerUpEffectAir(player, world, pos, wandCore.tier() + 1);
                        break;
                    case FIRE:
                        this.applyPowerUpEffectFire(player, world, pos, wandCore.tier() + 1);
                        break;
                    case DEATH:
                        this.applyPowerUpEffectDeath(player, world, pos, wandCore.tier() + 1);
                        break;
                    case EARTH:
                        this.applyPowerUpEffectEarth(player, world, pos, wandCore.tier() + 1);
                        break;
                    case WATER:
                        this.applyPowerUpEffectWater(player, world, pos, wandCore.tier() + 1);
                        break;
                }
            });
        }
        //TODO: particles
    }

    protected boolean checkWorldRequirementLife(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -2, 2, -1, 0, -2, 2, (w, p) -> w.getBlockState(p).getBlock() instanceof BlockBush) >= 5;
    }

    protected boolean checkWorldRequirementAir(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -1, 1, 0, 0, 1, 1, (w, p) -> w.getHeight(p.getX(), p.getZ()) >= p.getY() + 1) <= 0;
    }

    protected boolean checkWorldRequirementFire(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -2, 2, -1, -1, -2, 2, (w, p) -> w.getBlockState(p).getBlock().isFireSource(w, p, EnumFacing.UP)) >= 9;
    }

    protected boolean checkWorldRequirementDeath(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -2, 2, -1, 1, -2, 2, (w, p) -> w.getBlockState(p).getBlock() instanceof BlockSkull) >= 4;
    }

    protected boolean checkWorldRequirementEarth(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -3, 3, -3, 3, -3, 3, (w, p) -> w.getBlockState(p).getBlock() == Blocks.OBSIDIAN) >= 5;
    }

    protected boolean checkWorldRequirementWater(World world, BlockPos pos) {
        return this.checkCriterion(world, pos, -2, 2, -1, -1, -2, 2, (w, p) -> w.getBlockState(p).getBlock() == Blocks.WATER) >= 8;
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectLife(EntityPlayer player, World world, BlockPos pos, int potency) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 0; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos posAt = pos.add(x, y, z);
                    for (int i = 0; i < potency; i++) {
                        IBlockState state = world.getBlockState(posAt);
                        if (state.getBlock() instanceof IGrowable) {
                            IGrowable growable = (IGrowable) state.getBlock();
                            if (growable.canGrow(world, posAt, state, world.isRemote)) {
                                growable.grow(world, player.getRNG(), posAt, state);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectAir(EntityPlayer player, World world, BlockPos pos, int potency) {
        for(int i = 0; i < potency; i++) {
            EntityLightningBolt lightningBolt = new EntityLightningBolt(world, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5, (i % 2) != 0);
            world.addWeatherEffect(lightningBolt);
        }
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectFire(EntityPlayer player, World world, BlockPos pos, int potency) {
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
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectDeath(EntityPlayer player, World world, BlockPos pos, int potency) {
        world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-2, -1, -2), pos.add(3, 2, 3))).forEach(
                entity -> MagicDamageHandler.getInstance().dealDamage(entity, potency, player, Element.DEATH, potency)
        );
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectEarth(EntityPlayer player, World world, BlockPos pos, int potency) {
        int counter = potency;
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
        for (int i = 0; i < Math.min(list.size(), potency); i++) {
            int index = world.rand.nextInt(list.size());
            world.setBlockState(list.get(index), Blocks.COBBLESTONE.getDefaultState());
        }
    }

    @SuppressWarnings("unused")
    protected void applyPowerUpEffectWater(EntityPlayer player, World world, BlockPos pos, int potency) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos posAt = pos.add(x, -1, z);
                IBlockState state = world.getBlockState(posAt);
                if (state.getBlock() == Blocks.WATER) {
                    world.setBlockToAir(posAt);
                }
            }
        }

    }

    protected int checkCriterion(World world, BlockPos pos, int minX, int maxX, int minY, int maxY, int minZ, int maxZ, BiPredicate<World, BlockPos> criterion) {
        int counter = 0;
        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) {
                for(int z = minZ; z <= maxZ; z++) {
                    if(criterion.test(world, pos.add(x, y, z))) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    protected void setFire(World world, BlockPos pos) {
        if(world.isAirBlock(pos) && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
    }

    public static class ContainerEnchantOverride extends ContainerEnchantment {
        private final EntityPlayerMP player;
        private final World world;
        private final BlockPos pos;

        private boolean needsReq;
        private boolean worldReq;

        public ContainerEnchantOverride(EntityPlayerMP player, World world, BlockPos pos) {
            super(player.inventory, world, pos);
            this.player = player;
            this.world = world;
            this.pos = pos;
        }

        @Override
        public void onCraftMatrixChanged(IInventory inventory) {
            ItemStack stack = inventory.getStackInSlot(0);
            if(stack != null && stack.getItem() instanceof ItemWand) {
                if(!this.world.isRemote) {
                    for (int i = 0; i < 3; i++) {
                        this.enchantLevels[i] = 0;
                        this.enchantClue[i] = -1;
                        this.worldClue[i] = -1;
                        this.needsReq = false;
                        this.worldReq = false;
                    }
                    ItemWand wand = (ItemWand) stack.getItem();
                    if (wand.isEnchantable(stack)) {
                        int tier = wand.getPotency(stack);
                        this.enchantLevels[0] = ModConfiguration.getInstance().getLevelsForTierLevelup() * (tier + 2);
                        this.enchantClue[0] = Enchantment.getEnchantmentID(EnchantmentRegistry.getInstance().ENCHANTMENT_AUGMENT_WAND);
                        this.worldClue[0] = tier + 2;
                        this.needsReq = !player.capabilities.isCreativeMode;
                        this.worldReq = EnchantingHandler.getInstance().checkWorldRequirements(this.world, this.pos, stack);
                    }
                    this.detectAndSendChanges();
                }
            } else {
                super.onCraftMatrixChanged(inventory);
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
                                player.addExperienceLevel(-req);
                                lapis.setCount(lapis.getCount() - 1);
                                if (lapis.getCount() <= 0) {
                                    this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                                }
                            }
                            player.addStat(StatList.ITEM_ENCHANTED);
                            this.tableInventory.markDirty();
                            this.xpSeed = player.getXPSeed();
                            this.onCraftMatrixChanged(this.tableInventory);
                            this.world.playSound(null, this.pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                            if(!player.capabilities.isCreativeMode) {
                                EnchantingHandler.getInstance().applyPowerUpEffect(player, this.world, this.pos, stack);
                            }
                        }
                    }
                }
                return true;
            } else {
                return super.enchantItem(player, id);
            }
        }

        @Override
        public void detectAndSendChanges() {
            super.detectAndSendChanges();
            new MessageUpdateGuiEnchantment(this.pos, this.needsReq, this.worldReq).sendTo(this.player);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GuiEnchantOverride extends GuiEnchantment {
        private final ContainerEnchantment container;

        private BlockPos pos;
        private boolean needsReq;
        private boolean reqsMet;

        public GuiEnchantOverride(InventoryPlayer inventory, World worldIn, IWorldNameable nameable) {
            super(inventory, worldIn, nameable);
            this.container = (ContainerEnchantment) this.inventorySlots;
        }

        public void updateStatus(BlockPos pos, boolean needsReq, boolean status) {
            this.pos = pos;
            this.needsReq = needsReq;
            this.reqsMet = status;
        }

        public boolean areReqsMet() {
            return (!this.needsReq) || this.reqsMet;
        }

        @Override
        public void drawHoveringText(List<String> textLines, int x, int y) {
            if(textLines.size() > 1) {
                int k = this.container.enchantLevels[0];
                Enchantment enchantment = Enchantment.getEnchantmentByID(this.container.enchantClue[0]);
                int l = this.container.worldClue[0];
                if (this.isPointInRegion(60, 14, 108, 17, x, y) && k > 0 && l >= 0 && enchantment == EnchantmentRegistry.getInstance().ENCHANTMENT_AUGMENT_WAND) {
                    if (!this.areReqsMet()) {
                        ItemStack stack = this.container.tableInventory.getStackInSlot(0);
                        textLines.add(2, TextFormatting.RED + TranslationHelper.translateToLocal(
                                EnchantingHandler.getInstance().getWorldRequirementHint(Minecraft.getMinecraft().world, this.pos, stack)));
                    } else {
                        textLines.add(2, TextFormatting.GRAY  + TranslationHelper.translateToLocal(
                                "tooltip." + Reference.MOD_ID.toLowerCase() + ".ench.requirements_met"));
                    }
                }
            }
            super.drawHoveringText(textLines, x, y);
        }
    }
}