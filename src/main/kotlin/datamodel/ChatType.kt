package moe.nea.ultranotifier.datamodel

import moe.nea.ultranotifier.UltraNotifier
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.event.TickEvent
import moe.nea.ultranotifier.event.UltraSubscribe
import moe.nea.ultranotifier.event.VisibleChatMessageAddedEvent
import moe.nea.ultranotifier.util.GsonUtil
import moe.nea.ultranotifier.util.duplicatesBy
import moe.nea.ultranotifier.util.minecrat.MC
import moe.nea.ultranotifier.util.minecrat.category
import moe.nea.ultranotifier.util.minecrat.getFormattedTextCompat
import moe.nea.ultranotifier.util.minecrat.removeFormattingCodes
import net.minecraft.text.Text
import util.KSerializable
import java.util.function.Predicate
import java.util.regex.Pattern

data class ChatTypeId(
	val id: String
)

@KSerializable
data class ChatType(
	val name: String,
	val patterns: List<ChatPattern>,
)

data class ChatPattern(
	val text: String
) {
	val pattern = Pattern.compile(text)
	val predicate: Predicate<String> =
//#if JAVA > 11
		pattern.asMatchPredicate()
//#else
//$$		Predicate { it: String -> pattern.matcher(it).matches() }
//#endif
}

data class CategoryId(val id: String)

@KSerializable
data class ChatCategory(
	val id: CategoryId,
	val label: String,
	val chatTypes: Set<ChatTypeId>,
)

data class ChatUniverse(
	val name: String,
	val types: Map<ChatTypeId, ChatType>,
	val categories: List<ChatCategory>,
) {
	fun categorize(
		text: String
	): CategorizedChatLine {
		val types = this.types
			.asSequence()
			.filter {
				it.value.patterns.any {
					it.predicate.test(text)
				}
			}
			.map {
				it.key
			}
			.toSet()
		return CategorizedChatLine(
			text, types
		)
	}
}

data class CategorizedChatLine(
	val text: String,
	val types: Set<ChatTypeId>,
//	val categories: Set<ChatCategory>,
)

@KSerializable
data class UniverseMeta(
	// TODO: implement the ip filter
	val ipFilter: List<ChatPattern> = listOf(),
	val name: String,
)

interface HasCategorizedChatLine {
	val categorizedChatLine_ultraNotifier: CategorizedChatLine
}

data class UniverseId(
	val id: String
)

private fun loadAllUniverses(): Map<UniverseId, ChatUniverse> = buildMap {
	for (file in UltraNotifier.configFolder
		.resolve("universes/")
		.listFiles() ?: emptyArray()) {
		runCatching {
			val meta = GsonUtil.read<UniverseMeta>(file.resolve("meta.json"))
			val types = GsonUtil.read<Map<ChatTypeId, ChatType>>(file.resolve("chattypes.json"))
			val categories = GsonUtil.read<List<ChatCategory>>(file.resolve("categories.json"))
			// Validate categories linking properly
			for (category in categories) {
				for (chatType in category.chatTypes) {
					if (chatType in types.keys) {
						UltraNotifier.logger.warn("Missing definition for $chatType required by ${category.id} in $file")
					}
				}
			}
			for (category in categories.asSequence().duplicatesBy { it.id }) {
				UltraNotifier.logger.warn("Found duplicate category ${category.id} in $file")
			}

			put(
				UniverseId(file.name),
				ChatUniverse(
					meta.name,
					types,
					categories,
				))
		}.getOrElse {
			UltraNotifier.logger.warn("Could not load universe at $file", it)
		}
	}
}

object ChatCategoryArbiter : SubscriptionTarget {
	val specialAll = CategoryId("special-all")

	var allUniverses = loadAllUniverses()

	var activeUniverse: ChatUniverse? = allUniverses.values.single()
	private val allCategoryList = listOf(
		ChatCategory(specialAll, "All", setOf())
	)

	val categories // TODO: memoize
		get() = (activeUniverse?.categories ?: listOf()) + allCategoryList

	var selectedCategoryId = specialAll
		set(value) {
			field = value
			selectedCategory = findCategory(value)
		}
	private var lastSelectedId = selectedCategoryId
	var selectedCategory: ChatCategory = findCategory(selectedCategoryId)
		private set

	@UltraSubscribe
	fun onTick(event: TickEvent) {
		if (lastSelectedId != selectedCategoryId) {
			MC.chatHud.reset()
			lastSelectedId = selectedCategoryId
		}
	}

	@UltraSubscribe
	fun onVisibleChatMessage(event: VisibleChatMessageAddedEvent) {
		val cl = event.chatLine.category
		if (selectedCategory.id == specialAll)
			return
		if (cl.types.none { it in selectedCategory.chatTypes })
			event.cancel()
	}

	fun findCategory(id: CategoryId) = categories.find { it.id == id }!!

	fun categorize(content: Text): CategorizedChatLine {
		val stringContent = content.getFormattedTextCompat().removeFormattingCodes()
		return activeUniverse?.categorize(stringContent) ?: CategorizedChatLine(stringContent, setOf())
	}
}

