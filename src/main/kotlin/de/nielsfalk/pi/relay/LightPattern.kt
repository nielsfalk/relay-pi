package de.nielsfalk.pi.relay

import de.nielsfalk.pi.relay.Direction.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.buildSequence

/**
 * @author Niels Falk
 */
val lightPatterns = mapOf(
        "knightRider" to listOf(
                "#       ",
                " #      ",
                "  #     ",
                "   #    ",
                "    #   ",
                "     #  ",
                "      # ",
                "       #"),

        "newKnightRider" to listOf(
                "#      #",
                "##    ##",
                "###  ###",
                "########",
                " ###### ",
                "  ####  ",
                "   ##   "),

        "diamant" to listOf(
                "#       ",
                "##      ",
                "###     ",
                "####    ",
                "#####   ",
                "######  ",
                "####### ",
                "########",
                " #######",
                "  ######",
                "   #####",
                "    ####",
                "     ###",
                "      ##",
                "       #")
)

enum class Direction {
    toggle, forward, backward
}

internal fun List<String>.repeatingSequence(direction: Direction) :Sequence<State> = buildSequence {
    val firstIndex = 0
    val lastIndex = size - 1
    var currentIndex = if (direction == forward) firstIndex else lastIndex
    var currentDirection = if (direction == toggle) backward else direction
    while (true) {
        yield(get(currentIndex).toState())
        currentIndex += if (currentDirection == forward) 1 else -1
        when {
            direction == forward && currentIndex > lastIndex -> currentIndex = firstIndex
            direction == backward && currentIndex < firstIndex -> currentIndex = lastIndex
            direction == toggle && currentIndex == firstIndex -> currentDirection = forward
            direction == toggle && currentIndex == lastIndex -> currentDirection = backward
        }
    }
}

private fun String.toState() :State =
        toCharArray().mapIndexed { index, character ->
            index to (character == '#')
        }.toMap()

var job: Job? = null

fun lightPattern(name: String, direction: Direction, speed: Long) {
    lightPatterns[name]?.let {
        cancelPattern()
        job = launch {
            for (state in it.repeatingSequence(direction)) {
                set(state)
                delay(speed)
            }
        }
    }
}

fun cancelPattern() {
    job?.let {
        it.cancel()
        all(false)
        job = null
    }
}