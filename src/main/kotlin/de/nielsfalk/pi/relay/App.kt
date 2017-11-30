package de.nielsfalk.pi.relay

import de.nielsfalk.pi.relay.Direction.toggle
import de.nielsfalk.pi.relay.Direction.valueOf
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.ContentType.Text
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

/**
 * @author Niels Falk
 */

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        gpioInit()
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging)
        routing {
            get("/") {
                call.respondHtml { page() }
            }
            post("/pin/{pinNumber}") {
                call.parameters["pinNumber"]?.toIntOrNull()?.let { pin ->
                    cancelPattern()
                    val state = pin.set(call.parameters["on"] == "true")
                    call.respondText("pin $pin is now $state")
                }
            }
            post("/all") {
                val onOff = call.parameters["on"] == "true"
                cancelPattern()
                all(onOff)
                call.respondText("all $onOff")
            }
            post("/{pattern}") {
                call.parameters["pattern"]?.let {
                    val direction = call.parameters["direction"]?.let { valueOf(it) } ?: toggle
                    val speed = call.parameters["speed"]?.toLongOrNull() ?: 300
                    lightPattern(it, direction, speed)
                    call.respondText("pattern $it $direction $speed")
                }
            }
        }
    }.start(wait = true)
}
