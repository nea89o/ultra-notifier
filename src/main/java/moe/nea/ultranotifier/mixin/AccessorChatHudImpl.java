package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.util.minecrat.AccessorChatHud;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChatHud.class)
public abstract class AccessorChatHudImpl implements AccessorChatHud {

	//#if MC>1.17
	@Shadow
	protected abstract int getLineHeight();
	//#endif
	@Override
	public int getLineHeight_ultranotifier() {
		//#if MC>1.17
		return getLineHeight();
		//#else
		//$$return 9; // TODO: better typing here
		//#endif
	}
}
