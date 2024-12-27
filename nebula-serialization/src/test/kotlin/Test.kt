import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.common.util.primitive.replace
import moe.forpleuvoir.nebula.serialization.annotation.SerializerName
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.*
import moe.forpleuvoir.nebula.serialization.gson.parseToJsonObject
import moe.forpleuvoir.nebula.serialization.gson.toJsonString
import moe.forpleuvoir.nebula.serialization.json.JsonParser
import moe.forpleuvoir.nebula.serialization.json.JsonSerializer.Companion.dumpAsJson
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.time.measureTime


class SerializationTest {


    @OptIn(ExperimentalApi::class)
    @Test
    fun test2() {
        val a = """
            {
              "open_screen": {
                "keys": [
                  "key.keyboard.h",
                  "key.keyboard.s"
                ],
                "setting": {
                  "key_environment": "in_game",
                  "next_action": "continue",
                  "exact_match": true,
                  "trigger_mode": "on_release",
                  "repeatTriggerInterval": 5,
                  "long_press_time": 20
                }
              },
              "tutorial_step": "NONE",
              "pick_player_head_on_creative": true,
              "render_info_addon": {
                "show_enchantment_when_switch": {
                  "key_bind": {
                    "keys": [
                    ],
                    "setting": {
                      "key_environment": "in_game",
                      "next_action": "cancel",
                      "exact_match": true,
                      "trigger_mode": "on_release",
                      "repeatTriggerInterval": 5,
                      "long_press_time": 20
                    }
                  },
                  "value": false
                },
                "always_render_barrier": {
                  "key_bind": {
                    "keys": [
                    ],
                    "setting": {
                      "key_environment": "in_game",
                      "next_action": "continue",
                      "exact_match": true,
                      "trigger_mode": "on_release",
                      "repeatTriggerInterval": 5,
                      "long_press_time": 20
                    }
                  },
                  "value": true
                },
                "always_render_light": {
                  "key_bind": {
                    "keys": [
                    ],
                    "setting": {
                      "key_environment": "in_game",
                      "next_action": "continue",
                      "exact_match": true,
                      "trigger_mode": "on_release",
                      "repeatTriggerInterval": 5,
                      "long_press_time": 20
                    }
                  },
                  "value": true
                },
                "disable_scoreboard_sidebar_render": {
                  "key_bind": {
                    "keys": [
                    ],
                    "setting": {
                      "key_environment": "in_game",
                      "next_action": "continue",
                      "exact_match": true,
                      "trigger_mode": "on_release",
                      "repeatTriggerInterval": 5,
                      "long_press_time": 20
                    }
                  },
                  "value": true
                },
                "drop_entity": {
                  "distance": 500.0,
                  "enable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "only_y_rotation": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "experience_orb_value": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "name": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "count": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "map_id": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "additional_tooltip": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "jukebox_playable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "trim": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "stored_enchantments": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "enchantments": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "dyed_color": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "lore": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "attribute_modifiers": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "unbreakable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "ominous_bottle_amplifier": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "suspicious_stew_effect": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "can_break": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "can_place_on": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "durability": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "item_id": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": false
                  },
                  "components": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": false
                  }
                },
                "tnt": {
                  "tnt_fuse": "Box",
                  "only_y_rotation": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": false
                  }
                },
                "gamma_override": {
                  "enable": {
                    "key_bind": {
                      "keys": [
                        "key.keyboard.h"
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "cancel",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "gamma": 20.0
                }
              },
              "gameplay": {
                "auto_rebirth": {
                  "key_bind": {
                    "keys": [
                    ],
                    "setting": {
                      "key_environment": "in_game",
                      "next_action": "continue",
                      "exact_match": true,
                      "trigger_mode": "on_release",
                      "repeatTriggerInterval": 5,
                      "long_press_time": 20
                    }
                  },
                  "value": true
                }
              },
              "chat": {
                "chat_inject": {
                  "enable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "inject_mapping": [
                    [
                      "^-?\\d+(\\.\\d+)?${'$'}",
                      "#{message}"
                    ]
                  ]
                },
                "chat_filter": {
                  "enable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": false
                  },
                  "filter_mapping": [
                    ".*321.*"
                  ]
                },
                "chat_bubble": {
                  "enable": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "only_y_rotation": {
                    "key_bind": {
                      "keys": [
                      ],
                      "setting": {
                        "key_environment": "in_game",
                        "next_action": "continue",
                        "exact_match": true,
                        "trigger_mode": "on_release",
                        "repeatTriggerInterval": 5,
                        "long_press_time": 20
                      }
                    },
                    "value": true
                  },
                  "offset": {
                    "x": 0.0,
                    "y": 0.0
                  },
                  "scale": {
                    "x": 1.3,
                    "y": 1.3
                  },
                  "max_width": 267,
                  "texture_color": "#FFFF315D",
                  "text_color": "#FFFFFFFF",
                  "duration": "10s",
                  "fade_in_duration": "250ms",
                  "fade_out_duration": "250ms",
                  "match_mapping": [
                    [
                      "#single",
                      "<(?<name>[^>]+)>\\s(?<message>.+)"
                    ]
                  ]
                }
              }
            }
        """.trimIndent()
        val obj = JsonParser.parse(a).asObject
        println(obj)
        println("-************")
        println(obj["task_manager"]?.asObject["script_common_lib"]?.asString)
        println(obj.dumpAsJson(true))

    }

    @Test
    fun test1() {
        SerializationTest::class.companionObject?.let { clazz ->
            SerializationTest::class.companionObjectInstance!!
            clazz.declaredFunctions.first().let {
                println(it.parameters.size)
                println(it.parameters.last().name)
                println(it.name)
                println(it.returnType.classifier == T::class)
            }
        }
        println()
    }

    data class DT(
        val name: String,
        val list: List<String>,
        val map: Map<String, Any>,
        @SerializerName("ddd")
        val dt2: DT2
    )

    data class DT2(
        @SerializerName("a_name")
        val name: String
    )

    @OptIn(ExperimentalApi::class)
    @Test
    fun test3() {
        serializeObject {
            "name" to "forpleuvoir"
            "list" to serializeArray("aa", "b")
            "map" {
                "test_key" to "test_value"
            }
            "ddd" {
                "a_name" to "Guts"
            }
        }.toJsonString().let {
            println(it)
        }
    }

    @OptIn(ExperimentalApi::class)
    @Test
    fun test4() {
        serializeArray("12", 666, serializeArray("aa", "bb")).deserialization(List::class).apply {
            println(this)
        }
    }

    fun <E : Enum<E>> a(type: KClass<E>) {
    }

}

fun main() {
    test3()
}

@OptIn(ExperimentalApi::class)
fun test1() {
    val json = """
		{
          "name": "John Doe",
          "age": 30,
          "address": {
            "street": "123 Main St",
            "city": "Cityville"
          },
          "contacts": [
            {
              "type": "email",
              "value": "john.do\"e@example.com"
            },
            {
              "type": "phone",
              "value": "+1234567890"
            }
          ],
          "notes": " {\"nestedKey\":\"nested\\\"Value\"}",
          "nestedJson": {
            "key1": "value1?§aa",
            "key2": "value2"
          },
          "url": "https://maven.forpleuvoir.moe"
        }
	""".trimIndent()
    val obj: SerializeObject
    measureTime {
        obj = JsonParser.parse(json).asObject
    }.let { println(it) }
//    println(obj)
//    println(obj["notes"]?.asString?.replace(JsonParser.ESCAPE))
    println(JsonParser.parse(obj["notes"]!!.asString.replace(JsonParser.ESCAPE)).asObject["nestedKey"]!!.asString)
    println(json.parseToJsonObject.get("notes").asString)
//    println(obj.dumpAsJson(true))
//    println(obj["contacts"]?.dumpAsJson(true))
}

@OptIn(ExperimentalApi::class, ExperimentalReflectionOnLambdas::class)
fun test3() {
    val o = object {
        var aa = 65
        var bb = "bb"

        fun serialization(): SerializeObject {
            return serializeObject {
                "aa" to aa
                "bb" to bb
                "test" {
                    "test" to "aa"
                    "aa" {

                    }
                }
            }
        }

        override fun toString(): String {
            return "(aa=$aa, bb='$bb')"
        }

    }

    val obj = object {
        private val a = 65
        val b = 'b'
        val c = "ccc"
        val d = true
        val e = BigInteger.valueOf(4544)
        val f = BigDecimal.valueOf(45.11145)
        val g = null
        val h = arrayOf(6, "c", "asdas", o)
        val j = o
        val color = Colors.BLACK
        val t = T.V1
    }

    SerializePrimitive(10f).checkValue<Int>()
        .check<Float> {
            it.toInt()
        }.getOrThrow().let {
            println(it)
        }

//    (serializeArray(1, 2, 3, 4) as SerializeElement)
    obj.toSerializeObject()
        .checkType<SerializeObject, String> {
            it["t"].toString()
        }.getOrThrow().let {
            println(it)
        }
//    o.toSerializeObject().let {
//        println(it.toString())
//    }
//    println(obj.toSerializeObject().dumpAsJson(true))
}

class DT {
    val a: Int = 10
    val b: Int = 5
}


enum class T {

    V1, V2;

//    companion object {
//        fun deserialization(serializeElement: SerializeElement): T {
//            return T.valueOf(serializeElement.asString)
//        }
//
//    }
}


