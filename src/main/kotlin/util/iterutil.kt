package moe.nea.ultranotifier.util


fun <T, K : Any> Sequence<T>.duplicatesBy(keyFunc: (T) -> K): Sequence<T> {
	return object : Sequence<T> {
		override fun iterator(): Iterator<T> {
			val observed = HashSet<K>()
			val oldIterator = this@duplicatesBy.iterator()

			return object : Iterator<T> {
				var next: T? = null
				var hasNext = false
				override fun hasNext(): Boolean {
					if (hasNext) return true
					while (oldIterator.hasNext()) {
						val elem = oldIterator.next()
						val key = keyFunc(elem)
						if (observed.add(key))
							continue
						hasNext = true
						next = elem
					}
					return hasNext
				}

				override fun next(): T {
					if (!hasNext()) throw NoSuchElementException()
					hasNext = false
					val elem = next as T
					next = null
					return elem
				}
			}
		}
	}
}
