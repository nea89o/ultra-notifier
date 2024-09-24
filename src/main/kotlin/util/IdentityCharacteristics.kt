package moe.nea.ultranotifier.util

class IdentityCharacteristics<T>(val value: T) {
	override fun hashCode(): Int {
		return System.identityHashCode(value)
	}

	override fun equals(other: Any?): Boolean {
		return value === other
	}

	override fun toString(): String {
		return "IdentityCharacteristics($value)"
	}
}
