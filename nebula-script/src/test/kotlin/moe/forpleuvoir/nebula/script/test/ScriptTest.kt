package moe.forpleuvoir.nebula.script.test

import groovy.lang.GroovyShell
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class ScriptTest {


    @Test
    fun test1() = measureTime {
        val script = """
            static def call(close){
                close()
            }
            def list = [6,6,5,]
            
            call{
                println("Hello World")
            }
            print(list)
        """.trimIndent()
        GroovyShell().evaluate(script)

    }.let { println(it) }


}

data class Test(val name: String)
