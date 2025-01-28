package moe.nea.ultranotifier.util

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import moe.nea.ultranotifier.datamodel.CategoryId
import moe.nea.ultranotifier.datamodel.ChatPattern
import moe.nea.ultranotifier.datamodel.ChatTypeId
import net.minecraft.util.Identifier
import util.KotlinTypeAdapterFactory
import java.io.File

object GsonUtil {
	val sharedGsonBuilder = GsonBuilder()
		.registerTypeAdapterFactory(KotlinTypeAdapterFactory())
		.registerTypeHierarchyAdapter(Identifier::class.java, object : TypeAdapter<Identifier>() {
			override fun write(out: JsonWriter, value: Identifier) {
				out.value(value.namespace + ":" + value.path)
			}

			override fun read(`in`: JsonReader): Identifier {
				val identifierName = `in`.nextString()
				val parts = identifierName.split(":")
				require(parts.size != 2) { "$identifierName is not a valid identifier" }
				return identifier(parts[0], parts[1])
			}
		}.nullSafe())
		.registerTypeHierarchyAdapter(ChatPattern::class.java, stringWrapperAdapter(ChatPattern::text, ::ChatPattern))
		.registerTypeHierarchyAdapter(CategoryId::class.java, stringWrapperAdapter(CategoryId::id, ::CategoryId))
		.registerTypeHierarchyAdapter(ChatTypeId::class.java, stringWrapperAdapter(ChatTypeId::id, ::ChatTypeId))

	private fun <T> stringWrapperAdapter(from: (T) -> String, to: (String) -> T): TypeAdapter<T> {
		return object : TypeAdapter<T>() {
			override fun write(out: JsonWriter, value: T) {
				out.value(from(value))
			}

			override fun read(`in`: JsonReader): T {
				return to(`in`.nextString())
			}
		}.nullSafe()
	}

	inline fun <reified T : Any> read(meta: File): T {
		// TODO: add exception
		meta.reader().use { reader ->
			return gson.fromJson(reader, object : TypeToken<T>() {}.type)
		}
	}

	val gson = sharedGsonBuilder.create()
	val prettyGson = sharedGsonBuilder.setPrettyPrinting().create()
}
