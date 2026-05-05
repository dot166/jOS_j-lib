package io.github.dot166.jlib


/**
 * Represents a generic pair of two values that is mutable.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * MutablePair exhibits value semantics, i.e. two pairs are equal if both components are equal.
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @property first First value.
 * @property second Second value.
 * @constructor Creates a new instance of MutablePair.
 */
public data class MutablePair<A, B>(
    public var first: A,
    public var second: B
) {

    /**
     * Returns string representation of the [MutablePair] including its [first] and [second] values.
     */
    public override fun toString(): String = "($first, $second)"

    public override fun equals(other: Any?): Boolean {
        return if (other is MutablePair<A, B>?) {
            if (other != null) {
                first == other.first && second == other.second
            } else {
                false
            }
        } else {
            false
        }
    }

    public override fun hashCode(): Int {
        var result = first?.hashCode() ?: 0
        result = 31 * result + (second?.hashCode() ?: 0)
        return result
    }
}

/**
 * Creates a tuple of type [MutablePair] from this and [that].
 *
 * This can be useful for creating [Map] literals with less noise
 */
public infix fun <A, B> A.to(that: B): MutablePair<A, B> = MutablePair(this, that)

/**
 * Converts this pair into a list.
 */
public fun <T> MutablePair<T, T>.toList(): List<T> = listOf(first, second)

/**
 * Represents a triad of values that is mutable.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Triple exhibits value semantics, i.e. two triples are equal if all three components are equal.
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @param C type of the third value.
 * @property first First value.
 * @property second Second value.
 * @property third Third value.
 * @constructor Creates a new instance of MutableTriple.
 */
public data class MutableTriple<A, B, C>(
    public var first: A,
    public var second: B,
    public var third: C
) {

    /**
     * Returns string representation of the [MutableTriple] including its [first], [second] and [third] values.
     */
    public override fun toString(): String = "($first, $second, $third)"

    public override fun equals(other: Any?): Boolean {
        return if (other is MutableTriple<A, B, C>?) {
            if (other != null) {
                first == other.first && second == other.second && third == other.third
            } else {
                false
            }
        } else {
            false
        }
    }

    public override fun hashCode(): Int {
        var result = first?.hashCode() ?: 0
        result = 31 * result + (second?.hashCode() ?: 0)
        result = 31 * result + (third?.hashCode() ?: 0)
        return result
    }
}

/**
 * Converts this triple into a list.
 */
public fun <T> MutableTriple<T, T, T>.toList(): List<T> = listOf(first, second, third)

