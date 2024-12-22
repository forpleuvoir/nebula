package moe.forpleuvoir.nebula.script.test

import groovy.lang.GroovyShell
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class ScriptTest {


    @Test
    fun test1() {
        val shell = GroovyShell()
        val script = """
            static def call(close){
                close()
            }
            def list = [6,6,5,]
            
            call{
                println("Hello World")
            }
            println(list)
        """.trimIndent()
        repeat(10) {
            measureTime {
                shell.evaluate(script)
            }.let { println(it) }
        }
    }


}

data class Test(val name: String)
