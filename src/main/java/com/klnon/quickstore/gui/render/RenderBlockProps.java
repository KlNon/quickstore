package com.klnon.quickstore.gui.render;

import com.google.common.base.Objects;
import net.minecraft.util.math.BlockPos;

import javax.annotation.concurrent.Immutable;

@Immutable
public class RenderBlockProps {
	private final int color;
	private final BlockPos pos;

	public RenderBlockProps(BlockPos pos, int color) {
		this.pos = pos;
		this.color = color;
	}

	public RenderBlockProps(int x, int y, int z, int color) {
		this( new BlockPos(x, y, z), color );
	}

	public int getColor() {
		return color;
	}

	public BlockPos getPos() {
		return pos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if(this.pos.getX() == ((RenderBlockProps) o).getPos().getX() && this.pos.getY() == ((RenderBlockProps) o).getPos().getY() && this.pos.getZ() == ((RenderBlockProps) o).getPos().getZ()){
			return true;
		}
		RenderBlockProps that = (RenderBlockProps) o;
		return Objects.equal(pos, that.pos);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
