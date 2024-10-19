package moe.nea.ultranotifier.util

import moe.nea.ultranotifier.Constants
import net.minecraft.util.Identifier

fun identifier(namespace: String, path: String): Identifier {
	return Identifier(namespace, path)
}

fun vanillaIdentifier(path: String) = identifier("minecraft", path)
fun ultraIdentifier(path: String) = identifier(Constants.MOD_ID, path)
