package moe.nea.ultranotifier.util.minecrat
import net.minecraft.text.Text

//#if MC > 1.16
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
//#endif
//#if MC > 1.20
import net.minecraft.text.MutableText
import net.minecraft.text.PlainTextContent
//#endif

fun Text.getDirectlyContainedText() =
//#if MC < 1.16
//$$	this.unformattedComponentText
//#elseif MC < 1.20
//$$    this.asString()
//#else
        (this.content as? PlainTextContent)?.string().orEmpty()
//#endif

fun Text?.getFormattedTextCompat(): String =
//#if MC < 1.16
//$$	this?.formattedText.orEmpty()
//#else
run {
    this ?: return@run ""
    val sb = StringBuilder()
    for (component in iterator()) {
        sb.append(component.style.color?.toChatFormatting()?.toString() ?: "§r")
        sb.append(component.getDirectlyContainedText())
        sb.append("§r")
    }
    sb.toString()
}

private val textColorLUT = Formatting.entries
    .mapNotNull { formatting -> formatting.colorValue?.let { it to formatting } }
    .toMap()

fun TextColor.toChatFormatting(): Formatting? {
    return textColorLUT[this.rgb]
}

fun Text.iterator(): Sequence<Text> {
    return sequenceOf(this) + siblings.asSequence().flatMap { it.iterator() } // TODO: in theory we want to properly inherit styles here
}
//#endif

//#if MC > 1.20
fun MutableText.withColor(formatting: Formatting): Text {
    return this.styled { it.withColor(formatting) }
}
//#endif

fun CharSequence.removeFormattingCodes(): String {
	var nextParagraph = indexOf('§')
	if (nextParagraph < 0) return this.toString()
	val stringBuffer = StringBuilder(this.length)
	var readIndex = 0
	while (nextParagraph >= 0) {
		stringBuffer.append(this, readIndex, nextParagraph)
		readIndex = nextParagraph + 2
		nextParagraph = indexOf('§', startIndex = readIndex)
		if (readIndex > this.length)
			readIndex = this.length
	}
	stringBuffer.append(this, readIndex, this.length)
	return stringBuffer.toString()
}
