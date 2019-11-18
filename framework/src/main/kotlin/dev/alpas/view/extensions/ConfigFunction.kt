package dev.alpas.view.extensions

import com.mitchellbosecke.pebble.extension.Function
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate

internal class ConfigFunction : Function {
    override fun getArgumentNames(): List<String> {
        return listOf("key", "default")
    }

    @Suppress("UNCHECKED_CAST")
    override fun execute(args: Map<String, Any>?, self: PebbleTemplate, ctx: EvaluationContext, line: Int): Any {
        val name = args?.get("key") as String? ?: throw Exception("config() requires a key.")
        val default = args?.get("default")
        val configs = ctx.getVariable("_configs") as? Map<*, Any?>
        return configs?.get(name) ?: default ?: ""
    }
}
