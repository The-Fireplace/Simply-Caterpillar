package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class PartsReinforcement extends PartsTabbed{
	public ItemStack[] reinforcementMap = new ItemStack[16];
	public List<byte[]> replacers = new ArrayList();

	public PartsReinforcement()
	{
		this.setMap();
		this.setReplacers();

		boolean found = false;
		NBTTagCompound tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "ReinforcementDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.hasKey("reinforcement"))
			{
				this.readNBT(tmpNBT.getCompoundTag("reinforcement"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "ReinforcementDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.hasKey("reinforcement"))
				{
					this.readNBT(tmpNBT.getCompoundTag("reinforcement"));
				}
			}
		}
	}

	private void setMap() {
		for (int i = 0; i < 16; i++) {
			this.reinforcementMap[i] = new ItemStack(Blocks.COBBLESTONE);
		}
	}

	private void setReplacers() {
		for (int i = 0; i < 4; i++) {
			this.replacers.add(new byte[5]);
		}
		this.replacers.get(0)[0] = 0;
		this.replacers.get(0)[1] = 1;
		this.replacers.get(0)[2] = 1;
		this.replacers.get(0)[3] = 1;
		this.replacers.get(0)[4] = 0;

		this.replacers.get(1)[0] = 0;
		this.replacers.get(1)[1] = 1;
		this.replacers.get(1)[2] = 1;
		this.replacers.get(1)[3] = 0;
		this.replacers.get(1)[4] = 0;

		this.replacers.get(2)[0] = 0;
		this.replacers.get(2)[1] = 1;
		this.replacers.get(2)[2] = 1;
		this.replacers.get(2)[3] = 0;
		this.replacers.get(2)[4] = 0;

		this.replacers.get(3)[0] = 1;
		this.replacers.get(3)[1] = 1;
		this.replacers.get(3)[2] = 1;
		this.replacers.get(3)[3] = 0;
		this.replacers.get(3)[4] = 0;
	}

	@Override
	public void readNBT(NBTTagCompound NBTconCat)
	{
		super.readNBT(NBTconCat);
		this.reinforcementMap = new  ItemStack[16];
		this.reinforcementMap = Reference.MainNBT.readItemStacks(NBTconCat);
		if (this.reinforcementMap.length < 16)
		{
			this.reinforcementMap = new  ItemStack[16];
			for (int i = 0; i < 16; i++) {
				this.reinforcementMap[i] = new ItemStack(Blocks.COBBLESTONE);
			}
		}


		this.replacers.clear();
		for (int i = 0; i < 4; i++) {
			if (NBTconCat.hasKey("replacers" + i))
			{
				this.replacers.add(NBTconCat.getByteArray("replacers" + i));
			}
		}
		// convert old saves.
		if (this.replacers.get(0).length == 4 || this.replacers.size() != 4)
		{
			this.replacers.clear();
			this.setReplacers();
		}
		if (this.reinforcementMap.length !=  16)
		{
			this.setMap();
		}
	}
	@Override
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = Reference.MainNBT.writeItemStacks(this.reinforcementMap);
		NBTconCat = super.saveNBT(NBTconCat);
		for (int i = 0; i < 4; i++) {
			NBTconCat.setByteArray("replacers" + i, this.replacers.get(i));
		}

		return NBTconCat;
	}
}
