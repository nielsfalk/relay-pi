package de.nielsfalk.pi.relay

import kotlinx.html.*
import kotlinx.html.ButtonType.button
import kotlinx.html.InputType.radio
import kotlinx.html.InputType.range
import kotlinx.html.ScriptType.textJavaScript

/**
 * @author Niels Falk
 */

val names = listOf("Blue/white",
        "Glass sphere",
        "Blue",
        "Lava blue",
        "Lava Red",
        "Billow",
        "Dodecahedron",
        "Blue/white")

data class Relay(val name: String, val state: Boolean, val pin: Int)

val relays get() = state.map { (pin, state) -> Relay(names[pin], state, pin) }.reversed()

val script = Relay::class.java.getResource("/script.js")!!.readText()

fun HTML.page() {
    head {
        title { +"relay-pi" }
        link(rel = "stylesheet", href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
        link(rel = "stylesheet", href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css")
        script(src = "https://code.jquery.com/jquery-3.2.1.js")
        script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
        script(type = textJavaScript) {
            unsafe { +script }
        }
    }
    body {
        h1 { +"relay-pi" }
        for ((name, state, pin) in relays) {
            onOffToggle(state,
                    name = "pin$pin",
                    label = name,
                    changeAction = "pin")
        }
        div(classes = "form-group col-md-12") {
            label(classes = "control-label") { +"all" }
            div(classes = "btn-group") {
                //language=JavaScript
                button("on", """allPins('true')""")
                //language=JavaScript
                button("off", """allPins('false')""")
            }
        }
        div(classes = "form-group col-lg-2") {
            input(type = range, classes = "speed") {
                min = "25"
                max = "1500"
                value = "300"
                //language=JavaScript
                onChange = "speed(this.value)"
            }
        }
        btnSelect(name = "direction", changeAction = "ff", values = Direction.values().map { it.name }, state = Direction.toggle.name)
        div(classes = "form-group col-md-12") {
            div(classes = "btn-group-vertical") {
                for (patternName in lightPatterns.keys) {
                    //language=JavaScript
                    button(patternName, """pattern('$patternName')""")
                }
            }
        }
    }
}

private fun DIV.button(label: String, action: String) {
    button(type = button, classes = "btn btn-lg btn-success") {
        onClick = action
        +label
    }
}

private fun BODY.onOffToggle(state: Boolean? = null, name: String, label: String = name, changeAction: String, values: List<Boolean> = listOf(true, false)) {
    div(classes = "form-group col-md-12") {
        div(classes = "btn-group") {
            attributes["data-toggle"] = "buttons"
            for (onOff in values) {
                label(classes = "btn btn-lg btn-success $name ${if (state == onOff) "active" else ""}") {
                    input(type = radio, name = name) {
                        onChange = """$changeAction("$name",this.value)"""
                        value = "$onOff"
                        autoComplete = false
                    }
                    +if (onOff) "on" else "off"
                }
            }
        }
        label(classes = "control-label") { +label }
    }
}

private fun BODY.btnSelect(state: String? = null, name: String, changeAction: String, values: List<String>) {
    div(classes = "form-group col-md-12") {
        div(classes = "btn-group-vertical") {
            attributes["data-toggle"] = "buttons"
            for (itemValue in values) {
                label(classes = "btn btn-lg btn-success $name ${if (state == itemValue) "active" else ""}") {
                    input(type = radio, name = name) {
                        onChange = """$changeAction("$name",this.value)"""
                        value = itemValue
                        autoComplete = false
                    }
                    +itemValue
                }
            }
        }
    }
}

