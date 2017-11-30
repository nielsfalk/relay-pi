package de.nielsfalk.pi.relay

import kotlinx.coroutines.experimental.launch
import java.lang.Runtime.*

/**
 * @author Niels Falk
 */
val state: MutableState = (0..7).map { it to false }.toMap().toMutableMap()

fun gpioInit() {
    for ((pin, value) in state){
        pin.setOutputMode()
        pin.set(value)
    }
}

fun all(newState: Boolean) {
    (0..7).forEach { pin -> pin.set(newState) }
}

fun Pin.set(newState: Boolean): Boolean {
    //on the relay-board 1 == off / 0 == on
    if (state[this] != newState) {
        state[this] = newState
        val onOff = if (newState) 0 else 1
        "gpio write ${this} $onOff"()
    }
    return newState
}

fun set(map: State) {
    for ((pin, onOff) in map) {
        pin.set(onOff)
    }
}

fun Pin.setOutputMode(){
    "gpio mode $this out"()
}

private operator fun String.invoke() {
    launch { getRuntime().exec(this@invoke) }
}

internal typealias Pin = Int
internal typealias State = Map<Pin, Boolean>
internal typealias MutableState = MutableMap<Pin, Boolean>
