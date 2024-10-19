package moe.nea.ultranotifier.gui

import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import moe.nea.ultranotifier.util.render.ScreenRenderUtils
import moe.nea.ultranotifier.util.ultraIdentifier
import java.awt.Color

class MessageUi : UScreen() {
	override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
		super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
		ScreenRenderUtils.fillRect(matrixStack, 0.0, 0.0, width.toDouble(), height.toDouble(), Color.RED)
		ScreenRenderUtils.renderTexture(
			ultraIdentifier("textures/gui/square_panel.png"),
			matrixStack,
			200.0, 0.0, 300.0, 100.0
		)
	}
}
