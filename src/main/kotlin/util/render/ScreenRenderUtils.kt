package moe.nea.ultranotifier.util.render

//#if MC > 1.16
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import juuxel.libninepatch.NinePatch
import juuxel.libninepatch.TextureRenderer
import moe.nea.ultranotifier.util.minecrat.MC
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import java.awt.Color

//#endif

object ScreenRenderUtils {
	//#if MC > 1.16
	@JvmStatic
	fun umatrix(
		matrixStack: MatrixStack
	) = UMatrixStack(matrixStack)

	//#endif
	//#if MC >= 1.20
	@JvmStatic
	fun umatrix(
		context: DrawContext
	) = UMatrixStack(context.matrices)
	//#endif

	@JvmStatic
	fun umatrix() = UMatrixStack()

	fun fillRect(
		matrixStack: UMatrixStack,
		left: Double, top: Double,
		right: Double, bottom: Double,
		color: Color,
	) {
		val buffer = UGraphics.getFromTessellator()
		buffer.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR)
		buffer.pos(matrixStack, left, top, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, left, bottom, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, right, bottom, 0.0).color(color).endVertex()
		buffer.pos(matrixStack, right, top, 0.0).color(color).endVertex()
		buffer.drawDirect()
	}

	fun renderTexture(
		identifier: Identifier,
		matrixStack: UMatrixStack,
		left: Double, top: Double,
		right: Double, bottom: Double,
	) {
		UGraphics.bindTexture(0, identifier)
		val graphics = UGraphics.getFromTessellator()
		graphics.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_TEXTURE)
		graphics.pos(matrixStack, left, top, 0.0).tex(0.0, 0.0).endVertex()
		graphics.pos(matrixStack, left, bottom, 0.0).tex(0.0, 1.0).endVertex()
		graphics.pos(matrixStack, right, bottom, 0.0).tex(1.0, 1.0).endVertex()
		graphics.pos(matrixStack, right, top, 0.0).tex(1.0, 0.0).endVertex()
		graphics.drawDirect()
	}

	fun renderNineSlice(
		ninePatch: NinePatch<Identifier>,
		matrixStack: UMatrixStack,
		left: Double, top: Double,
		right: Double, bottom: Double,
	) {
		class Saver : TextureRenderer<Identifier> {
			override fun draw(
				texture: Identifier?,
				x: Int,
				y: Int,
				width: Int,
				height: Int,
				u1: Float,
				v1: Float,
				u2: Float,
				v2: Float
			) {
				this.texture = texture
			}

			var texture: Identifier? = null
		}

		val saver = Saver()
		ninePatch.draw(saver, 1, 1)
		UGraphics.bindTexture(0, saver.texture!!)
		val graphics = UGraphics.getFromTessellator()
		graphics.beginWithDefaultShader(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_TEXTURE)
		ninePatch.draw(object : TextureRenderer<Identifier> {
			override fun draw(
				texture: Identifier,
				x: Int,
				y: Int,
				width: Int,
				height: Int,
				u1: Float,
				v1: Float,
				u2: Float,
				v2: Float
			) {
				val x1 = left + x.toDouble()
				val y1 = top + y.toDouble()
				val x2 = x1 + width
				val y2 = y1 + height
				graphics.pos(matrixStack, x1, y1, 0.0)
					.tex(u1.toDouble(), v1.toDouble())
					.endVertex()
				graphics.pos(matrixStack, x1, y2, 0.0)
					.tex(u1.toDouble(), v2.toDouble())
					.endVertex()
				graphics.pos(matrixStack, x2, y2, 0.0)
					.tex(u2.toDouble(), v2.toDouble())
					.endVertex()
				graphics.pos(matrixStack, x2, y1, 0.0)
					.tex(u2.toDouble(), v1.toDouble())
					.endVertex()
			}
		}, (right - left).toInt(), (bottom - top).toInt())
		graphics.drawDirect()
	}

	fun getTextWidth(text: String): Int {
		return MC.font.getWidth(text)
	}

	fun renderText(matrixStack: UMatrixStack, x: Double, y: Double, text: String) {
		UGraphics.drawString(matrixStack, text, x.toFloat(), y.toFloat(), -1, false)
	}

}
