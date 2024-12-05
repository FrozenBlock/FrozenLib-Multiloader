package net.frozenblock.lib.block.api.set_type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockSetType.PressurePlateSensitivity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BlockSetTypeBuilder {
	private boolean openableByHand = true;
	private boolean openableByWindCharge = true;
	private boolean buttonActivatedByArrows = true;
	private BlockSetType.PressurePlateSensitivity pressurePlateActivationRule;
	private SoundType soundGroup;
	private SoundEvent doorCloseSound;
	private SoundEvent doorOpenSound;
	private SoundEvent trapdoorCloseSound;
	private SoundEvent trapdoorOpenSound;
	private SoundEvent pressurePlateClickOffSound;
	private SoundEvent pressurePlateClickOnSound;
	private SoundEvent buttonClickOffSound;
	private SoundEvent buttonClickOnSound;

	public BlockSetTypeBuilder() {
		this.pressurePlateActivationRule = PressurePlateSensitivity.EVERYTHING;
		this.soundGroup = SoundType.WOOD;
		this.doorCloseSound = SoundEvents.WOODEN_DOOR_CLOSE;
		this.doorOpenSound = SoundEvents.WOODEN_DOOR_OPEN;
		this.trapdoorCloseSound = SoundEvents.WOODEN_TRAPDOOR_CLOSE;
		this.trapdoorOpenSound = SoundEvents.WOODEN_TRAPDOOR_OPEN;
		this.pressurePlateClickOffSound = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF;
		this.pressurePlateClickOnSound = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON;
		this.buttonClickOffSound = SoundEvents.WOODEN_BUTTON_CLICK_OFF;
		this.buttonClickOnSound = SoundEvents.WOODEN_BUTTON_CLICK_ON;
	}

	public BlockSetTypeBuilder openableByHand(boolean openableByHand) {
		this.openableByHand = openableByHand;
		return this;
	}

	public BlockSetTypeBuilder openableByWindCharge(boolean openableByWindCharge) {
		this.openableByWindCharge = openableByWindCharge;
		return this;
	}

	public BlockSetTypeBuilder buttonActivatedByArrows(boolean buttonActivatedByArrows) {
		this.buttonActivatedByArrows = buttonActivatedByArrows;
		return this;
	}

	public BlockSetTypeBuilder pressurePlateActivationRule(BlockSetType.PressurePlateSensitivity activationRule) {
		this.pressurePlateActivationRule = activationRule;
		return this;
	}

	public BlockSetTypeBuilder soundGroup(SoundType soundGroup) {
		this.soundGroup = soundGroup;
		return this;
	}

	public BlockSetTypeBuilder doorCloseSound(SoundEvent doorCloseSound) {
		this.doorCloseSound = doorCloseSound;
		return this;
	}

	public BlockSetTypeBuilder doorOpenSound(SoundEvent doorOpenSound) {
		this.doorOpenSound = doorOpenSound;
		return this;
	}

	public BlockSetTypeBuilder trapdoorCloseSound(SoundEvent trapdoorCloseSound) {
		this.trapdoorCloseSound = trapdoorCloseSound;
		return this;
	}

	public BlockSetTypeBuilder trapdoorOpenSound(SoundEvent trapdoorOpenSound) {
		this.trapdoorOpenSound = trapdoorOpenSound;
		return this;
	}

	public BlockSetTypeBuilder pressurePlateClickOffSound(SoundEvent pressurePlateClickOffSound) {
		this.pressurePlateClickOffSound = pressurePlateClickOffSound;
		return this;
	}

	public BlockSetTypeBuilder pressurePlateClickOnSound(SoundEvent pressurePlateClickOnSound) {
		this.pressurePlateClickOnSound = pressurePlateClickOnSound;
		return this;
	}

	public BlockSetTypeBuilder buttonClickOffSound(SoundEvent buttonClickOffSound) {
		this.buttonClickOffSound = buttonClickOffSound;
		return this;
	}

	public BlockSetTypeBuilder buttonClickOnSound(SoundEvent buttonClickOnSound) {
		this.buttonClickOnSound = buttonClickOnSound;
		return this;
	}

	public static @NotNull BlockSetTypeBuilder copyOf(@NotNull BlockSetTypeBuilder builder) {
		BlockSetTypeBuilder copy = new BlockSetTypeBuilder();
		copy.openableByHand(builder.openableByHand);
		copy.openableByWindCharge(builder.openableByWindCharge);
		copy.buttonActivatedByArrows(builder.buttonActivatedByArrows);
		copy.pressurePlateActivationRule(builder.pressurePlateActivationRule);
		copy.soundGroup(builder.soundGroup);
		copy.doorCloseSound(builder.doorCloseSound);
		copy.doorOpenSound(builder.doorOpenSound);
		copy.trapdoorCloseSound(builder.trapdoorCloseSound);
		copy.trapdoorOpenSound(builder.trapdoorOpenSound);
		copy.pressurePlateClickOffSound(builder.pressurePlateClickOffSound);
		copy.pressurePlateClickOnSound(builder.pressurePlateClickOnSound);
		copy.buttonClickOffSound(builder.buttonClickOffSound);
		copy.buttonClickOnSound(builder.buttonClickOnSound);
		return copy;
	}

	public static @NotNull BlockSetTypeBuilder copyOf(@NotNull BlockSetType setType) {
		BlockSetTypeBuilder copy = new BlockSetTypeBuilder();
		copy.openableByHand(setType.canOpenByHand());
		copy.openableByWindCharge(setType.canOpenByWindCharge());
		copy.buttonActivatedByArrows(setType.canButtonBeActivatedByArrows());
		copy.pressurePlateActivationRule(setType.pressurePlateSensitivity());
		copy.soundGroup(setType.soundType());
		copy.doorCloseSound(setType.doorClose());
		copy.doorOpenSound(setType.doorOpen());
		copy.trapdoorCloseSound(setType.trapdoorClose());
		copy.trapdoorOpenSound(setType.trapdoorOpen());
		copy.pressurePlateClickOffSound(setType.pressurePlateClickOff());
		copy.pressurePlateClickOnSound(setType.pressurePlateClickOn());
		copy.buttonClickOffSound(setType.buttonClickOff());
		copy.buttonClickOnSound(setType.buttonClickOn());
		return copy;
	}

	public @NotNull BlockSetType register(ResourceLocation id) {
		return BlockSetType.register(this.build(id));
	}

	@Contract("_ -> new")
	public @NotNull BlockSetType build(@NotNull ResourceLocation id) {
		return new BlockSetType(
			id.toString(),
			this.openableByHand,
			this.openableByWindCharge,
			this.buttonActivatedByArrows,
			this.pressurePlateActivationRule,
			this.soundGroup,
			this.doorCloseSound,
			this.doorOpenSound,
			this.trapdoorCloseSound,
			this.trapdoorOpenSound,
			this.pressurePlateClickOffSound,
			this.pressurePlateClickOnSound,
			this.buttonClickOffSound,
			this.buttonClickOnSound
		);
	}
}
