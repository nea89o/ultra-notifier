package moe.nea.ultranotifier.mixin;

import moe.nea.ultranotifier.gui.ChatUi;
import moe.nea.ultranotifier.util.render.ScreenRenderUtils;
import net.minecraft.client.gui.screen.ChatScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#if MC>1.16
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
//#endif

@Mixin(ChatScreen.class)
public abstract class ChatScreenTabRenderer {

	private ChatUi chatUi_ultraNotifier = new ChatUi((ChatScreen) (Object) this);

	@Unique
	ChatUi chatUi() {return chatUi_ultraNotifier;}

	@Inject(
//#if MC > 1.16
		method = "render",
//#else
//$$		method="drawScreen",
//#endif
		at = @At("HEAD"))
	private void onRender(
//#if MC >1.20
		DrawContext context,
//#elseif MC > 1.16
//$$		MatrixStack context,
//#endif
		int mouseX, int mouseY,
		float delta,
		CallbackInfo ci) {
		chatUi().renderButtons(
			ScreenRenderUtils.umatrix(
				//#if MC > 1.16
				context
				//#endif
			),
			mouseX, mouseY
		);
	}

	@Inject(
		method = "mouseClicked",
		at = @At(value = "INVOKE",
			//#if MC > 1.16
			target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(DDI)Z",
			//#else
			//$$target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V",
			//#endif
			opcode = Opcodes.INVOKESPECIAL)
	)
	private void onMouseClick(
		//#if MC > 1.16
		double mouseX, double mouseY,
		//#else
		//$$int mouseX, int mouseY,
		//#endif
		int button,
		//#if MC > 1.16
		CallbackInfoReturnable<Boolean> cir
		//#else
		//$$CallbackInfo cir
		//#endif
	) {
		chatUi().clickMouse(mouseX, mouseY, button);
	}
}
