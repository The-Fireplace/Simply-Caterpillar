package the_fireplace.caterpillar.containers;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerDrillHead extends Container
{
	private IInventory playerInventory;
	private TileEntityDrillHead myCaterpillarTe;
	public ContainerDrillHead(EntityPlayer player, TileEntityDrillHead tileEntityInventoryIn)
	{
		this.playerInventory = player.inventory;
		this.myCaterpillarTe = tileEntityInventoryIn;
		this.myCaterpillarTe.myDrillHead = this;
		int i;
		int j;
		int ID = 0;

		//Burner
		this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + (4) * 18, 7 + (3) * 18));

		//Left Side
		for (i = 0; i < 4; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + j * 18, -100));
			}
		}

		//Right Side
		for (i = 0; i < 4; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + (j + 6) * 18, -100));
			}
		}

		Reference.printDebug("Slot Count: "+ID);

		//Player Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(this.playerInventory, i, 8 + i * 18, 142));
		}
	}
	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return this.myCaterpillarTe.isUsableByPlayer(playerIn);
	}

	@Override
	public List<ItemStack> getInventory()//TODO: Verify that this works; potentially need to use Caterpillar.getInventory
	{
		List<ItemStack> list = Lists.newArrayList();

		list.add(this.myCaterpillarTe.fuelSlotStack);

		for (int i = 0; i < this.myCaterpillarTe.getCurrentInventory().length; ++i)
		{
			list.add(this.myCaterpillarTe.getCurrentInventory()[i]);
		}

		return list;
	}

	@Override
	public void detectAndSendChanges(){}

	private boolean isWood(ItemStack itemstack1)
	{
		Block thisBlock = Block.getBlockFromItem(itemstack1.getItem());
		if (thisBlock != null)
		{
			if (thisBlock.getDefaultState().getMaterial().equals(Material.WOOD))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn)
	{
		if (this.myCaterpillarTe != null)
		{
			if (this.myCaterpillarTe.tabs.selected.isCrafting)
			{
				if (slotId < this.getInventory().size() - 36)
				{
					if (slotId > -1)
					{
						ItemStack decoration =  null;

						ItemStack whattoKeep = playerIn.inventory.getItemStack();
						if (whattoKeep != null)
						{
							decoration = new ItemStack(whattoKeep.getItem(), 1, whattoKeep.getItemDamage());
						}
						Slot slot1 = this.getSlot(slotId);
						slot1.putStack(decoration);
						this.detectAndSendChanges();
						return null;
					}
				}
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (this.myCaterpillarTe == null)
			{
				return null;
			}

			if (index < this.inventorySlots.size() - 36)
			{
				if (!this.mergeItemStack(itemstack1, this.inventorySlots.size() - 36, this.inventorySlots.size(), false))
				{
					return null;
				}
			}
			else if (this.myCaterpillarTe.tabs.selected.equals(Caterpillar.GuiTabs.MAIN))
			{
				if (TileEntityFurnace.isItemFuel(itemstack1) && !this.isWood(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else
				{
					if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size() - 36, false))
					{
						return null;
					}
				}
			}
			else //if (!this.mergeItemStack(itemstack1, 0, myCaterpillarTe.getSizeInventory(), false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}
	public void updateCaterpillar()
	{
		this.myCaterpillarTe.myDrillHead = this;
	}
}