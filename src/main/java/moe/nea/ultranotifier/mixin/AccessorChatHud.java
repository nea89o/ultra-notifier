package moe.nea.ultranotifier.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface AccessorChatHud {
	@Invoker("getLineHeight")
	int getLineHeight_ultranotifier();
}
