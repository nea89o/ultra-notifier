package moe.nea.ultranotifier.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
//#if MC < 1.20.4
//$$import com.mojang.blaze3d.platform.GlStateManager
//#endif

class DrawingContext(
//#if MC > 1.20.4
	val vanilla: DrawContext,
//#endif
) {
	fun pushMatrix() {
//#if MC > 1.20.4
		vanilla.matrices.push()
//#else
//$$		GlStateManager.pushMatrix()
//#endif
	}
	fun popMatrix() {
//#if MC > 1.20.4
		vanilla.matrices.pop()
//#else
//$$		GlStateManager.popMatrix()
//#endif
	}
	fun translate(x: Double, y: Double, z: Double) {
//#if MC > 1.20.4
		vanilla.matrices.translate(x, y, z)
//#else
//$$		GlStateManager.translate(x, y, z)
//#endif
	}
	fun scale(x: Float, y: Float, z: Float) {
//#if MC > 1.20.4
		vanilla.matrices.scale(x, y, z)
//#else
//$$		GlStateManager.scale(x, y, z)
//#endif
	}

}

abstract class BaseScreen(title: Text) : Screen(
//#if MC >= 11400
	title
//#endif
) {

}
