package de.nielsfalk.pi.relay

import de.nielsfalk.pi.relay.Direction.*
import org.amshove.kluent.shouldEqual
import org.junit.Test

/**
 * @author Niels Falk
 */
class LightPatternKtTest {
    private val pattern = lightPatterns["knightRider"]!!

    @Test
    fun forward() {
        //given
        val sequence = pattern.repeatingSequence(direction = forward)
        val iterator = sequence.iterator()

        //when
        val states = (0..8).map { iterator.next() }

        //then
        states.map { it.asString() } shouldEqual listOf(
                "#       ",
                " #      ",
                "  #     ",
                "   #    ",
                "    #   ",
                "     #  ",
                "      # ",
                "       #",
                "#       ")
    }

    @Test
    fun backwards() {
        //given
        val sequence = pattern.repeatingSequence(direction = backward)
        val iterator = sequence.iterator()

        //when
        val states = (0..8).map { iterator.next() }

        //then
        states.map { it.asString() } shouldEqual listOf(
                "       #",
                "      # ",
                "     #  ",
                "    #   ",
                "   #    ",
                "  #     ",
                " #      ",
                "#       ",
                "       #")
    }

    @Test
    fun toggle() {
        //given
        val sequence = pattern.repeatingSequence(direction = toggle)
        val iterator = sequence.iterator()

        //when
        val states = (0..15).map { iterator.next() }

        //then
        states.map { it.asString() } shouldEqual listOf(

                "       #",
                "      # ",
                "     #  ",
                "    #   ",
                "   #    ",
                "  #     ",
                " #      ",
                "#       ",
                " #      ",
                "  #     ",
                "   #    ",
                "    #   ",
                "     #  ",
                "      # ",
                "       #",
                "      # ")
    }
}

private fun State.asString(): String {
    return (0..7)
            .map {
                get(it).let {
                    if (it == true) "#" else " "
                }
            }
            .joinToString(separator = "")
}
