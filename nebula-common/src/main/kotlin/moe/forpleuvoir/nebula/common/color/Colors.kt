@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.common.color

object Colors {

    @Suppress("NOTHING_TO_INLINE")
    private inline fun lazyColor(red: Int, green: Int, blue: Int): Lazy<Color> {
        return lazy {
            Color(0xFF000000.toInt() or (red shl 16) or (green shl 8) or blue)
        }
    }

    @JvmStatic
    val BLACK: Color by lazyColor(0, 0, 0)

    @JvmStatic
    val NIGHT: Color by lazyColor(12, 9, 10)

    @JvmStatic
    val CHARCOAL: Color by lazyColor(52, 40, 44)

    @JvmStatic
    val OIL: Color by lazyColor(59, 49, 49)

    @JvmStatic
    val DARK_GRAY: Color by lazyColor(58, 59, 60)

    @JvmStatic
    val LIGHT_BLACK: Color by lazyColor(69, 69, 69)

    @JvmStatic
    val BLACK_CAT: Color by lazyColor(65, 56, 57)

    @JvmStatic
    val IRIDIUM: Color by lazyColor(61, 60, 58)

    @JvmStatic
    val BLACK_EEL: Color by lazyColor(70, 62, 63)

    @JvmStatic
    val BLACK_COW: Color by lazyColor(76, 70, 70)

    @JvmStatic
    val GRAY_WOLF: Color by lazyColor(80, 74, 75)

    @JvmStatic
    val VAMPIRE_GRAY: Color by lazyColor(86, 80, 81)

    @JvmStatic
    val IRON_GRAY: Color by lazyColor(82, 89, 93)

    @JvmStatic
    val GRAY_DOLPHIN: Color by lazyColor(92, 88, 88)

    @JvmStatic
    val CARBON_GRAY: Color by lazyColor(98, 93, 93)

    @JvmStatic
    val ASH_GRAY: Color by lazyColor(102, 99, 98)

    @JvmStatic
    val DIMGRAY: Color by lazyColor(105, 105, 105)

    @JvmStatic
    val NARDO_GRAY: Color by lazyColor(104, 106, 108)

    @JvmStatic
    val CLOUDY_GRAY: Color by lazyColor(109, 105, 104)

    @JvmStatic
    val SMOKEY_GRAY: Color by lazyColor(114, 110, 109)

    @JvmStatic
    val ALIEN_GRAY: Color by lazyColor(115, 111, 110)

    @JvmStatic
    val SONIC_SILVER: Color by lazyColor(117, 117, 117)

    @JvmStatic
    val PLATINUM_GRAY: Color by lazyColor(121, 121, 121)

    @JvmStatic
    val GRANITE: Color by lazyColor(131, 126, 124)

    @JvmStatic
    val GRAY: Color by lazyColor(128, 128, 128)

    @JvmStatic
    val BATTLESHIP_GRAY: Color by lazyColor(132, 132, 130)

    @JvmStatic
    val GUNMETAL_GRAY: Color by lazyColor(141, 145, 141)

    @JvmStatic
    val DARKGRAY: Color by lazyColor(169, 169, 169)

    @JvmStatic
    val GRAY_CLOUD: Color by lazyColor(182, 182, 180)

    @JvmStatic
    val SILVER: Color by lazyColor(192, 192, 192)

    @JvmStatic
    val PALE_SILVER: Color by lazyColor(201, 192, 187)

    @JvmStatic
    val GRAY_GOOSE: Color by lazyColor(209, 208, 206)

    @JvmStatic
    val PLATINUM_SILVER: Color by lazyColor(206, 206, 206)

    @JvmStatic
    val LIGHTGRAY: Color by lazyColor(211, 211, 211)

    @JvmStatic
    val SILVER_WHITE: Color by lazyColor(218, 219, 221)

    @JvmStatic
    val GAINSBORO: Color by lazyColor(220, 220, 220)

    @JvmStatic
    val PLATINUM: Color by lazyColor(229, 228, 226)

    @JvmStatic
    val METALLIC_SILVER: Color by lazyColor(188, 198, 204)

    @JvmStatic
    val BLUE_GRAY: Color by lazyColor(152, 175, 199)

    @JvmStatic
    val ROMAN_SILVER: Color by lazyColor(131, 137, 150)

    @JvmStatic
    val LIGHTSLATEGRAY: Color by lazyColor(119, 136, 153)

    @JvmStatic
    val SLATEGRAY: Color by lazyColor(112, 128, 144)

    @JvmStatic
    val RAT_GRAY: Color by lazyColor(109, 123, 141)

    @JvmStatic
    val SLATE_GRANITE_GRAY: Color by lazyColor(101, 115, 131)

    @JvmStatic
    val JET_GRAY: Color by lazyColor(97, 109, 126)

    @JvmStatic
    val MIST_BLUE: Color by lazyColor(100, 109, 126)

    @JvmStatic
    val MARBLE_BLUE: Color by lazyColor(86, 109, 126)

    @JvmStatic
    val SLATE_BLUE_GREY: Color by lazyColor(115, 124, 161)

    @JvmStatic
    val LIGHT_PURPLE_BLUE: Color by lazyColor(114, 143, 206)

    @JvmStatic
    val AZURE_BLUE: Color by lazyColor(72, 99, 160)

    @JvmStatic
    val BLUE_JAY: Color by lazyColor(43, 84, 126)

    @JvmStatic
    val CHARCOAL_BLUE: Color by lazyColor(54, 69, 79)

    @JvmStatic
    val DARK_BLUE_GREY: Color by lazyColor(41, 70, 91)

    @JvmStatic
    val DARK_SLATE: Color by lazyColor(43, 56, 86)

    @JvmStatic
    val DEEP_SEA_BLUE: Color by lazyColor(18, 52, 86)

    @JvmStatic
    val NIGHT_BLUE: Color by lazyColor(21, 27, 84)

    @JvmStatic
    val MIDNIGHTBLUE: Color by lazyColor(25, 25, 112)

    @JvmStatic
    val NAVY: Color by lazyColor(0, 0, 128)

    @JvmStatic
    val DENIM_DARK_BLUE: Color by lazyColor(21, 27, 141)

    @JvmStatic
    val DARKBLUE: Color by lazyColor(0, 0, 139)

    @JvmStatic
    val LAPIS_BLUE: Color by lazyColor(21, 49, 126)

    @JvmStatic
    val NEW_MIDNIGHT_BLUE: Color by lazyColor(0, 0, 160)

    @JvmStatic
    val EARTH_BLUE: Color by lazyColor(0, 0, 165)

    @JvmStatic
    val COBALT_BLUE: Color by lazyColor(0, 32, 194)

    @JvmStatic
    val MEDIUMBLUE: Color by lazyColor(0, 0, 205)

    @JvmStatic
    val BLUEBERRY_BLUE: Color by lazyColor(0, 65, 194)

    @JvmStatic
    val CANARY_BLUE: Color by lazyColor(41, 22, 245)

    @JvmStatic
    val BLUE: Color by lazyColor(0, 0, 255)

    @JvmStatic
    val SAMCO_BLUE: Color by lazyColor(0, 2, 255)

    @JvmStatic
    val BRIGHT_BLUE: Color by lazyColor(9, 9, 255)

    @JvmStatic
    val BLUE_ORCHID: Color by lazyColor(31, 69, 252)

    @JvmStatic
    val SAPPHIRE_BLUE: Color by lazyColor(37, 84, 199)

    @JvmStatic
    val BLUE_EYES: Color by lazyColor(21, 105, 199)

    @JvmStatic
    val BRIGHT_NAVY_BLUE: Color by lazyColor(25, 116, 210)

    @JvmStatic
    val BALLOON_BLUE: Color by lazyColor(43, 96, 222)

    @JvmStatic
    val ROYALBLUE: Color by lazyColor(65, 105, 225)

    @JvmStatic
    val OCEAN_BLUE: Color by lazyColor(43, 101, 236)

    @JvmStatic
    val BLUE_RIBBON: Color by lazyColor(48, 110, 255)

    @JvmStatic
    val BLUE_DRESS: Color by lazyColor(21, 125, 236)

    @JvmStatic
    val NEON_BLUE: Color by lazyColor(21, 137, 255)

    @JvmStatic
    val DODGERBLUE: Color by lazyColor(30, 144, 255)

    @JvmStatic
    val GLACIAL_BLUE_ICE: Color by lazyColor(54, 139, 193)

    @JvmStatic
    val STEELBLUE: Color by lazyColor(70, 130, 180)

    @JvmStatic
    val SILK_BLUE: Color by lazyColor(72, 138, 199)

    @JvmStatic
    val WINDOWS_BLUE: Color by lazyColor(53, 126, 199)

    @JvmStatic
    val BLUE_IVY: Color by lazyColor(48, 144, 199)

    @JvmStatic
    val BLUE_KOI: Color by lazyColor(101, 158, 199)

    @JvmStatic
    val COLUMBIA_BLUE: Color by lazyColor(135, 175, 199)

    @JvmStatic
    val BABY_BLUE: Color by lazyColor(149, 185, 199)

    @JvmStatic
    val CORNFLOWERBLUE: Color by lazyColor(100, 149, 237)

    @JvmStatic
    val SKY_BLUE_DRESS: Color by lazyColor(102, 152, 255)

    @JvmStatic
    val ICEBERG: Color by lazyColor(86, 165, 236)

    @JvmStatic
    val BUTTERFLY_BLUE: Color by lazyColor(56, 172, 236)

    @JvmStatic
    val DEEPSKYBLUE: Color by lazyColor(0, 191, 255)

    @JvmStatic
    val MIDDAY_BLUE: Color by lazyColor(59, 185, 255)

    @JvmStatic
    val CRYSTAL_BLUE: Color by lazyColor(92, 179, 255)

    @JvmStatic
    val DENIM_BLUE: Color by lazyColor(121, 186, 236)

    @JvmStatic
    val DAY_SKY_BLUE: Color by lazyColor(130, 202, 255)

    @JvmStatic
    val LIGHTSKYBLUE: Color by lazyColor(135, 206, 250)

    @JvmStatic
    val SKYBLUE: Color by lazyColor(135, 206, 235)

    @JvmStatic
    val JEANS_BLUE: Color by lazyColor(160, 207, 236)

    @JvmStatic
    val BLUE_ANGEL: Color by lazyColor(183, 206, 236)

    @JvmStatic
    val PASTEL_BLUE: Color by lazyColor(180, 207, 236)

    @JvmStatic
    val LIGHT_DAY_BLUE: Color by lazyColor(173, 223, 255)

    @JvmStatic
    val SEA_BLUE: Color by lazyColor(194, 223, 255)

    @JvmStatic
    val HEAVENLY_BLUE: Color by lazyColor(198, 222, 255)

    @JvmStatic
    val ROBIN_EGG_BLUE: Color by lazyColor(189, 237, 255)

    @JvmStatic
    val POWDERBLUE: Color by lazyColor(176, 224, 230)

    @JvmStatic
    val CORAL_BLUE: Color by lazyColor(175, 220, 236)

    @JvmStatic
    val LIGHTBLUE: Color by lazyColor(173, 216, 230)

    @JvmStatic
    val LIGHTSTEELBLUE: Color by lazyColor(176, 207, 222)

    @JvmStatic
    val GULF_BLUE: Color by lazyColor(201, 223, 236)

    @JvmStatic
    val PASTEL_LIGHT_BLUE: Color by lazyColor(213, 214, 234)

    @JvmStatic
    val LAVENDER_BLUE: Color by lazyColor(227, 228, 250)

    @JvmStatic
    val WHITE_BLUE: Color by lazyColor(219, 233, 250)

    @JvmStatic
    val LAVENDER: Color by lazyColor(230, 230, 250)

    @JvmStatic
    val WATER: Color by lazyColor(235, 244, 250)

    @JvmStatic
    val ALICEBLUE: Color by lazyColor(240, 248, 255)

    @JvmStatic
    val GHOSTWHITE: Color by lazyColor(248, 248, 255)

    @JvmStatic
    val AZURE: Color by lazyColor(240, 255, 255)

    @JvmStatic
    val LIGHTCYAN: Color by lazyColor(224, 255, 255)

    @JvmStatic
    val LIGHT_SLATE: Color by lazyColor(204, 255, 255)

    @JvmStatic
    val ELECTRIC_BLUE: Color by lazyColor(154, 254, 255)

    @JvmStatic
    val TRON_BLUE: Color by lazyColor(125, 253, 254)

    @JvmStatic
    val BLUE_ZIRCON: Color by lazyColor(87, 254, 255)

    @JvmStatic
    val AQUA: Color by lazyColor(0, 255, 255)

    @JvmStatic
    val CYAN: Color by lazyColor(10, 255, 255)

    @JvmStatic
    val CELESTE: Color by lazyColor(80, 235, 236)

    @JvmStatic
    val BLUE_DIAMOND: Color by lazyColor(78, 226, 236)

    @JvmStatic
    val BRIGHT_TURQUOISE: Color by lazyColor(22, 226, 245)

    @JvmStatic
    val BLUE_LAGOON: Color by lazyColor(142, 235, 236)

    @JvmStatic
    val PALETURQUOISE: Color by lazyColor(175, 238, 238)

    @JvmStatic
    val PALE_BLUE_LILY: Color by lazyColor(207, 236, 236)

    @JvmStatic
    val LIGHT_TEAL: Color by lazyColor(179, 217, 217)

    @JvmStatic
    val TIFFANY_BLUE: Color by lazyColor(129, 216, 208)

    @JvmStatic
    val BLUE_HOSTA: Color by lazyColor(119, 191, 199)

    @JvmStatic
    val CYAN_OPAQUE: Color by lazyColor(146, 199, 199)

    @JvmStatic
    val NORTHERN_LIGHTS_BLUE: Color by lazyColor(120, 199, 199)

    @JvmStatic
    val BLUE_GREEN: Color by lazyColor(123, 204, 181)

    @JvmStatic
    val MEDIUMAQUAMARINE: Color by lazyColor(102, 205, 170)

    @JvmStatic
    val MAGIC_MINT: Color by lazyColor(170, 240, 209)

    @JvmStatic
    val LIGHT_AQUAMARINE: Color by lazyColor(147, 255, 232)

    @JvmStatic
    val AQUAMARINE: Color by lazyColor(127, 255, 212)

    @JvmStatic
    val BRIGHT_TEAL: Color by lazyColor(1, 249, 198)

    @JvmStatic
    val TURQUOISE: Color by lazyColor(64, 224, 208)

    @JvmStatic
    val MEDIUMTURQUOISE: Color by lazyColor(72, 209, 204)

    @JvmStatic
    val DEEP_TURQUOISE: Color by lazyColor(72, 204, 205)

    @JvmStatic
    val JELLYFISH: Color by lazyColor(70, 199, 199)

    @JvmStatic
    val BLUE_TURQUOISE: Color by lazyColor(67, 198, 219)

    @JvmStatic
    val DARKTURQUOISE: Color by lazyColor(0, 206, 209)

    @JvmStatic
    val MACAW_BLUE_GREEN: Color by lazyColor(67, 191, 199)

    @JvmStatic
    val LIGHTSEAGREEN: Color by lazyColor(32, 178, 170)

    @JvmStatic
    val SEAFOAM_GREEN: Color by lazyColor(62, 169, 159)

    @JvmStatic
    val CADETBLUE: Color by lazyColor(95, 158, 160)

    @JvmStatic
    val DEEP_SEA: Color by lazyColor(59, 156, 156)

    @JvmStatic
    val DARKCYAN: Color by lazyColor(0, 139, 139)

    @JvmStatic
    val TEAL_GREEN: Color by lazyColor(0, 130, 127)

    @JvmStatic
    val TEAL: Color by lazyColor(0, 128, 128)

    @JvmStatic
    val TEAL_BLUE: Color by lazyColor(0, 124, 128)

    @JvmStatic
    val MEDIUM_TEAL: Color by lazyColor(4, 95, 95)

    @JvmStatic
    val DARK_TEAL: Color by lazyColor(4, 93, 93)

    @JvmStatic
    val DEEP_TEAL: Color by lazyColor(3, 62, 62)

    @JvmStatic
    val DARKSLATEGRAY: Color by lazyColor(37, 56, 60)

    @JvmStatic
    val GUNMETAL: Color by lazyColor(44, 53, 57)

    @JvmStatic
    val BLUE_MOSS_GREEN: Color by lazyColor(60, 86, 91)

    @JvmStatic
    val BEETLE_GREEN: Color by lazyColor(76, 120, 126)

    @JvmStatic
    val GRAYISH_TURQUOISE: Color by lazyColor(94, 125, 126)

    @JvmStatic
    val GREENISH_BLUE: Color by lazyColor(48, 125, 126)

    @JvmStatic
    val AQUAMARINE_STONE: Color by lazyColor(52, 135, 129)

    @JvmStatic
    val SEA_TURTLE_GREEN: Color by lazyColor(67, 141, 128)

    @JvmStatic
    val DULL_SEA_GREEN: Color by lazyColor(78, 137, 117)

    @JvmStatic
    val DARK_GREEN_BLUE: Color by lazyColor(31, 99, 87)

    @JvmStatic
    val DEEP_SEA_GREEN: Color by lazyColor(48, 103, 84)

    @JvmStatic
    val BOTTLE_GREEN: Color by lazyColor(0, 106, 78)

    @JvmStatic
    val SEAGREEN: Color by lazyColor(46, 139, 87)

    @JvmStatic
    val ELF_GREEN: Color by lazyColor(27, 138, 107)

    @JvmStatic
    val DARK_MINT: Color by lazyColor(49, 144, 110)

    @JvmStatic
    val JADE: Color by lazyColor(0, 163, 108)

    @JvmStatic
    val EARTH_GREEN: Color by lazyColor(52, 165, 111)

    @JvmStatic
    val CHROME_GREEN: Color by lazyColor(26, 162, 96)

    @JvmStatic
    val EMERALD: Color by lazyColor(80, 200, 120)

    @JvmStatic
    val MINT: Color by lazyColor(62, 180, 137)

    @JvmStatic
    val MEDIUMSEAGREEN: Color by lazyColor(60, 179, 113)

    @JvmStatic
    val METALLIC_GREEN: Color by lazyColor(124, 157, 142)

    @JvmStatic
    val CAMOUFLAGE_GREEN: Color by lazyColor(120, 134, 107)

    @JvmStatic
    val SAGE_GREEN: Color by lazyColor(132, 139, 121)

    @JvmStatic
    val HAZEL_GREEN: Color by lazyColor(97, 124, 88)

    @JvmStatic
    val VENOM_GREEN: Color by lazyColor(114, 140, 0)

    @JvmStatic
    val OLIVEDRAB: Color by lazyColor(107, 142, 35)

    @JvmStatic
    val OLIVE: Color by lazyColor(128, 128, 0)

    @JvmStatic
    val DARKOLIVEGREEN: Color by lazyColor(85, 107, 47)

    @JvmStatic
    val MILITARY_GREEN: Color by lazyColor(78, 91, 49)

    @JvmStatic
    val GREEN_LEAVES: Color by lazyColor(58, 95, 11)

    @JvmStatic
    val ARMY_GREEN: Color by lazyColor(75, 83, 32)

    @JvmStatic
    val FERN_GREEN: Color by lazyColor(102, 124, 38)

    @JvmStatic
    val FALL_FOREST_GREEN: Color by lazyColor(78, 146, 88)

    @JvmStatic
    val IRISH_GREEN: Color by lazyColor(8, 160, 75)

    @JvmStatic
    val PINE_GREEN: Color by lazyColor(56, 124, 68)

    @JvmStatic
    val MEDIUM_FOREST_GREEN: Color by lazyColor(52, 114, 53)

    @JvmStatic
    val JUNGLE_GREEN: Color by lazyColor(52, 124, 44)

    @JvmStatic
    val CACTUS_GREEN: Color by lazyColor(34, 116, 66)

    @JvmStatic
    val FORESTGREEN: Color by lazyColor(34, 139, 34)

    @JvmStatic
    val GREEN: Color by lazyColor(0, 128, 0)

    @JvmStatic
    val DARKGREEN: Color by lazyColor(0, 100, 0)

    @JvmStatic
    val DEEP_GREEN: Color by lazyColor(5, 102, 8)

    @JvmStatic
    val DEEP_EMERALD_GREEN: Color by lazyColor(4, 99, 7)

    @JvmStatic
    val HUNTER_GREEN: Color by lazyColor(53, 94, 59)

    @JvmStatic
    val DARK_FOREST_GREEN: Color by lazyColor(37, 65, 23)

    @JvmStatic
    val LOTUS_GREEN: Color by lazyColor(0, 66, 37)

    @JvmStatic
    val SEAWEED_GREEN: Color by lazyColor(67, 124, 23)

    @JvmStatic
    val SHAMROCK_GREEN: Color by lazyColor(52, 124, 23)

    @JvmStatic
    val GREEN_ONION: Color by lazyColor(106, 161, 33)

    @JvmStatic
    val MOSS_GREEN: Color by lazyColor(138, 154, 91)

    @JvmStatic
    val GRASS_GREEN: Color by lazyColor(63, 155, 11)

    @JvmStatic
    val GREEN_PEPPER: Color by lazyColor(74, 160, 44)

    @JvmStatic
    val DARK_LIME_GREEN: Color by lazyColor(65, 163, 23)

    @JvmStatic
    val PARROT_GREEN: Color by lazyColor(18, 173, 43)

    @JvmStatic
    val CLOVER_GREEN: Color by lazyColor(62, 160, 85)

    @JvmStatic
    val DINOSAUR_GREEN: Color by lazyColor(115, 161, 108)

    @JvmStatic
    val GREEN_SNAKE: Color by lazyColor(108, 187, 60)

    @JvmStatic
    val ALIEN_GREEN: Color by lazyColor(108, 196, 23)

    @JvmStatic
    val GREEN_APPLE: Color by lazyColor(76, 196, 23)

    @JvmStatic
    val LIMEGREEN: Color by lazyColor(50, 205, 50)

    @JvmStatic
    val PEA_GREEN: Color by lazyColor(82, 208, 23)

    @JvmStatic
    val KELLY_GREEN: Color by lazyColor(76, 197, 82)

    @JvmStatic
    val ZOMBIE_GREEN: Color by lazyColor(84, 197, 113)

    @JvmStatic
    val GREEN_PEAS: Color by lazyColor(137, 195, 92)

    @JvmStatic
    val DOLLAR_BILL_GREEN: Color by lazyColor(133, 187, 101)

    @JvmStatic
    val FROG_GREEN: Color by lazyColor(153, 198, 142)

    @JvmStatic
    val TURQUOISE_GREEN: Color by lazyColor(160, 214, 180)

    @JvmStatic
    val DARKSEAGREEN: Color by lazyColor(143, 188, 143)

    @JvmStatic
    val BASIL_GREEN: Color by lazyColor(130, 159, 130)

    @JvmStatic
    val GRAY_GREEN: Color by lazyColor(162, 173, 156)

    @JvmStatic
    val IGUANA_GREEN: Color by lazyColor(156, 176, 113)

    @JvmStatic
    val CITRON_GREEN: Color by lazyColor(143, 179, 29)

    @JvmStatic
    val ACID_GREEN: Color by lazyColor(176, 191, 26)

    @JvmStatic
    val AVOCADO_GREEN: Color by lazyColor(178, 194, 72)

    @JvmStatic
    val PISTACHIO_GREEN: Color by lazyColor(157, 194, 9)

    @JvmStatic
    val SALAD_GREEN: Color by lazyColor(161, 201, 53)

    @JvmStatic
    val YELLOWGREEN: Color by lazyColor(154, 205, 50)

    @JvmStatic
    val PASTEL_GREEN: Color by lazyColor(119, 221, 119)

    @JvmStatic
    val HUMMINGBIRD_GREEN: Color by lazyColor(127, 232, 23)

    @JvmStatic
    val NEBULA_GREEN: Color by lazyColor(89, 232, 23)

    @JvmStatic
    val STOPLIGHT_GO_GREEN: Color by lazyColor(87, 233, 100)

    @JvmStatic
    val NEON_GREEN: Color by lazyColor(22, 245, 41)

    @JvmStatic
    val JADE_GREEN: Color by lazyColor(94, 251, 110)

    @JvmStatic
    val LIME_MINT_GREEN: Color by lazyColor(54, 245, 127)

    @JvmStatic
    val SPRINGGREEN: Color by lazyColor(0, 255, 127)

    @JvmStatic
    val MEDIUMSPRINGGREEN: Color by lazyColor(0, 250, 154)

    @JvmStatic
    val EMERALD_GREEN: Color by lazyColor(95, 251, 23)

    @JvmStatic
    val LIME: Color by lazyColor(0, 255, 0)

    @JvmStatic
    val LAWNGREEN: Color by lazyColor(124, 252, 0)

    @JvmStatic
    val BRIGHT_GREEN: Color by lazyColor(102, 255, 0)

    @JvmStatic
    val CHARTREUSE: Color by lazyColor(127, 255, 0)

    @JvmStatic
    val YELLOW_LAWN_GREEN: Color by lazyColor(135, 247, 23)

    @JvmStatic
    val ALOE_VERA_GREEN: Color by lazyColor(152, 245, 22)

    @JvmStatic
    val DULL_GREEN_YELLOW: Color by lazyColor(177, 251, 23)

    @JvmStatic
    val LEMON_GREEN: Color by lazyColor(173, 248, 2)

    @JvmStatic
    val GREENYELLOW: Color by lazyColor(173, 255, 47)

    @JvmStatic
    val CHAMELEON_GREEN: Color by lazyColor(189, 245, 22)

    @JvmStatic
    val NEON_YELLOW_GREEN: Color by lazyColor(218, 238, 1)

    @JvmStatic
    val YELLOW_GREEN_GROSBEAK: Color by lazyColor(226, 245, 22)

    @JvmStatic
    val TEA_GREEN: Color by lazyColor(204, 251, 93)

    @JvmStatic
    val SLIME_GREEN: Color by lazyColor(188, 233, 84)

    @JvmStatic
    val ALGAE_GREEN: Color by lazyColor(100, 233, 134)

    @JvmStatic
    val LIGHTGREEN: Color by lazyColor(144, 238, 144)

    @JvmStatic
    val DRAGON_GREEN: Color by lazyColor(106, 251, 146)

    @JvmStatic
    val PALEGREEN: Color by lazyColor(152, 251, 152)

    @JvmStatic
    val MINT_GREEN: Color by lazyColor(152, 255, 152)

    @JvmStatic
    val GREEN_THUMB: Color by lazyColor(181, 234, 170)

    @JvmStatic
    val ORGANIC_BROWN: Color by lazyColor(227, 249, 166)

    @JvmStatic
    val LIGHT_JADE: Color by lazyColor(195, 253, 184)

    @JvmStatic
    val LIGHT_MINT_GREEN: Color by lazyColor(194, 229, 211)

    @JvmStatic
    val LIGHT_ROSE_GREEN: Color by lazyColor(219, 249, 219)

    @JvmStatic
    val CHROME_WHITE: Color by lazyColor(232, 241, 212)

    @JvmStatic
    val HONEYDEW: Color by lazyColor(240, 255, 240)

    @JvmStatic
    val MINTCREAM: Color by lazyColor(245, 255, 250)

    @JvmStatic
    val LEMONCHIFFON: Color by lazyColor(255, 250, 205)

    @JvmStatic
    val PARCHMENT: Color by lazyColor(255, 255, 194)

    @JvmStatic
    val CREAM: Color by lazyColor(255, 255, 204)

    @JvmStatic
    val CREAM_WHITE: Color by lazyColor(255, 253, 208)

    @JvmStatic
    val LIGHTGOLDENRODYELLOW: Color by lazyColor(250, 250, 210)

    @JvmStatic
    val LIGHTYELLOW: Color by lazyColor(255, 255, 224)

    @JvmStatic
    val BEIGE: Color by lazyColor(245, 245, 220)

    @JvmStatic
    val CORNSILK: Color by lazyColor(255, 248, 220)

    @JvmStatic
    val BLONDE: Color by lazyColor(251, 246, 217)

    @JvmStatic
    val CHAMPAGNE: Color by lazyColor(247, 231, 206)

    @JvmStatic
    val ANTIQUEWHITE: Color by lazyColor(250, 235, 215)

    @JvmStatic
    val PAPAYAWHIP: Color by lazyColor(255, 239, 213)

    @JvmStatic
    val BLANCHEDALMOND: Color by lazyColor(255, 235, 205)

    @JvmStatic
    val BISQUE: Color by lazyColor(255, 228, 196)

    @JvmStatic
    val WHEAT: Color by lazyColor(245, 222, 179)

    @JvmStatic
    val MOCCASIN: Color by lazyColor(255, 228, 181)

    @JvmStatic
    val PEACH: Color by lazyColor(255, 229, 180)

    @JvmStatic
    val LIGHT_ORANGE: Color by lazyColor(254, 216, 177)

    @JvmStatic
    val PEACHPUFF: Color by lazyColor(255, 218, 185)

    @JvmStatic
    val CORAL_PEACH: Color by lazyColor(251, 213, 171)

    @JvmStatic
    val NAVAJOWHITE: Color by lazyColor(255, 222, 173)

    @JvmStatic
    val GOLDEN_BLONDE: Color by lazyColor(251, 231, 161)

    @JvmStatic
    val GOLDEN_SILK: Color by lazyColor(243, 227, 195)

    @JvmStatic
    val DARK_BLONDE: Color by lazyColor(240, 226, 182)

    @JvmStatic
    val LIGHT_GOLD: Color by lazyColor(241, 229, 172)

    @JvmStatic
    val VANILLA: Color by lazyColor(243, 229, 171)

    @JvmStatic
    val TAN_BROWN: Color by lazyColor(236, 229, 182)

    @JvmStatic
    val DIRTY_WHITE: Color by lazyColor(232, 228, 201)

    @JvmStatic
    val PALEGOLDENROD: Color by lazyColor(238, 232, 170)

    @JvmStatic
    val KHAKI: Color by lazyColor(240, 230, 140)

    @JvmStatic
    val CARDBOARD_BROWN: Color by lazyColor(237, 218, 116)

    @JvmStatic
    val HARVEST_GOLD: Color by lazyColor(237, 226, 117)

    @JvmStatic
    val SUN_YELLOW: Color by lazyColor(255, 232, 124)

    @JvmStatic
    val CORN_YELLOW: Color by lazyColor(255, 243, 128)

    @JvmStatic
    val PASTEL_YELLOW: Color by lazyColor(250, 248, 132)

    @JvmStatic
    val NEON_YELLOW: Color by lazyColor(255, 255, 51)

    @JvmStatic
    val YELLOW: Color by lazyColor(255, 255, 0)

    @JvmStatic
    val CANARY_YELLOW: Color by lazyColor(255, 239, 0)

    @JvmStatic
    val BANANA_YELLOW: Color by lazyColor(245, 226, 22)

    @JvmStatic
    val MUSTARD_YELLOW: Color by lazyColor(255, 219, 88)

    @JvmStatic
    val GOLDEN_YELLOW: Color by lazyColor(255, 223, 0)

    @JvmStatic
    val BOLD_YELLOW: Color by lazyColor(249, 219, 36)

    @JvmStatic
    val RUBBER_DUCKY_YELLOW: Color by lazyColor(255, 216, 1)

    @JvmStatic
    val GOLD: Color by lazyColor(255, 215, 0)

    @JvmStatic
    val BRIGHT_GOLD: Color by lazyColor(253, 208, 23)

    @JvmStatic
    val CHROME_GOLD: Color by lazyColor(255, 206, 68)

    @JvmStatic
    val GOLDEN_BROWN: Color by lazyColor(234, 193, 23)

    @JvmStatic
    val DEEP_YELLOW: Color by lazyColor(246, 190, 0)

    @JvmStatic
    val MACARONI_AND_CHEESE: Color by lazyColor(242, 187, 102)

    @JvmStatic
    val SAFFRON: Color by lazyColor(251, 185, 23)

    @JvmStatic
    val NEON_GOLD: Color by lazyColor(253, 189, 1)

    @JvmStatic
    val BEER: Color by lazyColor(251, 177, 23)

    @JvmStatic
    val ORANGE_YELLOW: Color by lazyColor(255, 174, 66)

    @JvmStatic
    val CANTALOUPE: Color by lazyColor(255, 166, 47)

    @JvmStatic
    val CHEESE_ORANGE: Color by lazyColor(255, 166, 0)

    @JvmStatic
    val ORANGE: Color by lazyColor(255, 165, 0)

    @JvmStatic
    val BROWN_SAND: Color by lazyColor(238, 154, 77)

    @JvmStatic
    val SANDYBROWN: Color by lazyColor(244, 164, 96)

    @JvmStatic
    val BROWN_SUGAR: Color by lazyColor(226, 167, 111)

    @JvmStatic
    val CAMEL_BROWN: Color by lazyColor(193, 154, 107)

    @JvmStatic
    val DEER_BROWN: Color by lazyColor(230, 191, 131)

    @JvmStatic
    val BURLYWOOD: Color by lazyColor(222, 184, 135)

    @JvmStatic
    val TAN: Color by lazyColor(210, 180, 140)

    @JvmStatic
    val LIGHT_FRENCH_BEIGE: Color by lazyColor(200, 173, 127)

    @JvmStatic
    val SAND: Color by lazyColor(194, 178, 128)

    @JvmStatic
    val SAGE: Color by lazyColor(188, 184, 138)

    @JvmStatic
    val FALL_LEAF_BROWN: Color by lazyColor(200, 181, 96)

    @JvmStatic
    val GINGER_BROWN: Color by lazyColor(201, 190, 98)

    @JvmStatic
    val BRONZE_GOLD: Color by lazyColor(201, 174, 93)

    @JvmStatic
    val DARKKHAKI: Color by lazyColor(189, 183, 107)

    @JvmStatic
    val OLIVE_GREEN: Color by lazyColor(186, 184, 108)

    @JvmStatic
    val BRASS: Color by lazyColor(181, 166, 66)

    @JvmStatic
    val COOKIE_BROWN: Color by lazyColor(199, 163, 23)

    @JvmStatic
    val METALLIC_GOLD: Color by lazyColor(212, 175, 55)

    @JvmStatic
    val BEE_YELLOW: Color by lazyColor(233, 171, 23)

    @JvmStatic
    val SCHOOL_BUS_YELLOW: Color by lazyColor(232, 163, 23)

    @JvmStatic
    val GOLDENROD: Color by lazyColor(218, 165, 32)

    @JvmStatic
    val ORANGE_GOLD: Color by lazyColor(212, 160, 23)

    @JvmStatic
    val CARAMEL: Color by lazyColor(198, 142, 23)

    @JvmStatic
    val DARKGOLDENROD: Color by lazyColor(184, 134, 11)

    @JvmStatic
    val CINNAMON: Color by lazyColor(197, 137, 23)

    @JvmStatic
    val PERU: Color by lazyColor(205, 133, 63)

    @JvmStatic
    val BRONZE: Color by lazyColor(205, 127, 50)

    @JvmStatic
    val TIGER_ORANGE: Color by lazyColor(200, 129, 65)

    @JvmStatic
    val COPPER: Color by lazyColor(184, 115, 51)

    @JvmStatic
    val DARK_GOLD: Color by lazyColor(170, 108, 57)

    @JvmStatic
    val METALLIC_BRONZE: Color by lazyColor(169, 113, 66)

    @JvmStatic
    val DARK_ALMOND: Color by lazyColor(171, 120, 78)

    @JvmStatic
    val WOOD: Color by lazyColor(150, 111, 51)

    @JvmStatic
    val OAK_BROWN: Color by lazyColor(128, 101, 23)

    @JvmStatic
    val ANTIQUE_BRONZE: Color by lazyColor(102, 93, 30)

    @JvmStatic
    val HAZEL: Color by lazyColor(142, 118, 24)

    @JvmStatic
    val DARK_YELLOW: Color by lazyColor(139, 128, 0)

    @JvmStatic
    val DARK_MOCCASIN: Color by lazyColor(130, 120, 57)

    @JvmStatic
    val KHAKI_GREEN: Color by lazyColor(138, 134, 93)

    @JvmStatic
    val MILLENNIUM_JADE: Color by lazyColor(147, 145, 124)

    @JvmStatic
    val DARK_BEIGE: Color by lazyColor(159, 140, 118)

    @JvmStatic
    val BULLET_SHELL: Color by lazyColor(175, 155, 96)

    @JvmStatic
    val ARMY_BROWN: Color by lazyColor(130, 123, 96)

    @JvmStatic
    val SANDSTONE: Color by lazyColor(120, 109, 95)

    @JvmStatic
    val TAUPE: Color by lazyColor(72, 60, 50)

    @JvmStatic
    val MOCHA: Color by lazyColor(73, 61, 38)

    @JvmStatic
    val MILK_CHOCOLATE: Color by lazyColor(81, 59, 28)

    @JvmStatic
    val GRAY_BROWN: Color by lazyColor(61, 54, 53)

    @JvmStatic
    val DARK_COFFEE: Color by lazyColor(59, 47, 47)

    @JvmStatic
    val OLD_BURGUNDY: Color by lazyColor(67, 48, 46)

    @JvmStatic
    val WESTERN_CHARCOAL: Color by lazyColor(73, 65, 63)

    @JvmStatic
    val BAKERS_BROWN: Color by lazyColor(92, 51, 23)

    @JvmStatic
    val DARK_BROWN: Color by lazyColor(101, 67, 33)

    @JvmStatic
    val SEPIA_BROWN: Color by lazyColor(112, 66, 20)

    @JvmStatic
    val DARK_BRONZE: Color by lazyColor(128, 74, 0)

    @JvmStatic
    val COFFEE: Color by lazyColor(111, 78, 55)

    @JvmStatic
    val BROWN_BEAR: Color by lazyColor(131, 92, 59)

    @JvmStatic
    val RED_DIRT: Color by lazyColor(127, 82, 23)

    @JvmStatic
    val SEPIA: Color by lazyColor(127, 70, 44)

    @JvmStatic
    val SIENNA: Color by lazyColor(160, 82, 45)

    @JvmStatic
    val SADDLEBROWN: Color by lazyColor(139, 69, 19)

    @JvmStatic
    val DARK_SIENNA: Color by lazyColor(138, 65, 23)

    @JvmStatic
    val SANGRIA: Color by lazyColor(126, 56, 23)

    @JvmStatic
    val BLOOD_RED: Color by lazyColor(126, 53, 23)

    @JvmStatic
    val CHESTNUT: Color by lazyColor(149, 69, 53)

    @JvmStatic
    val CORAL_BROWN: Color by lazyColor(158, 70, 56)

    @JvmStatic
    val CHESTNUT_RED: Color by lazyColor(195, 74, 44)

    @JvmStatic
    val MAHOGANY: Color by lazyColor(192, 64, 0)

    @JvmStatic
    val RED_GOLD: Color by lazyColor(235, 84, 6)

    @JvmStatic
    val RED_FOX: Color by lazyColor(195, 88, 23)

    @JvmStatic
    val DARK_BISQUE: Color by lazyColor(184, 101, 0)

    @JvmStatic
    val LIGHT_BROWN: Color by lazyColor(181, 101, 29)

    @JvmStatic
    val PETRA_GOLD: Color by lazyColor(183, 103, 52)

    @JvmStatic
    val RUST: Color by lazyColor(195, 98, 65)

    @JvmStatic
    val COPPER_RED: Color by lazyColor(203, 109, 81)

    @JvmStatic
    val ORANGE_SALMON: Color by lazyColor(196, 116, 81)

    @JvmStatic
    val CHOCOLATE: Color by lazyColor(210, 105, 30)

    @JvmStatic
    val SEDONA: Color by lazyColor(204, 102, 0)

    @JvmStatic
    val PAPAYA_ORANGE: Color by lazyColor(229, 103, 23)

    @JvmStatic
    val HALLOWEEN_ORANGE: Color by lazyColor(230, 108, 44)

    @JvmStatic
    val NEON_ORANGE: Color by lazyColor(255, 103, 0)

    @JvmStatic
    val BRIGHT_ORANGE: Color by lazyColor(255, 95, 31)

    @JvmStatic
    val PUMPKIN_ORANGE: Color by lazyColor(248, 114, 23)

    @JvmStatic
    val CARROT_ORANGE: Color by lazyColor(248, 128, 23)

    @JvmStatic
    val DARKORANGE: Color by lazyColor(255, 140, 0)

    @JvmStatic
    val CONSTRUCTION_CONE_ORANGE: Color by lazyColor(248, 116, 49)

    @JvmStatic
    val INDIAN_SAFFRON: Color by lazyColor(255, 119, 34)

    @JvmStatic
    val SUNRISE_ORANGE: Color by lazyColor(230, 116, 81)

    @JvmStatic
    val MANGO_ORANGE: Color by lazyColor(255, 128, 64)

    @JvmStatic
    val CORAL: Color by lazyColor(255, 127, 80)

    @JvmStatic
    val BASKET_BALL_ORANGE: Color by lazyColor(248, 129, 88)

    @JvmStatic
    val LIGHT_SALMON_ROSE: Color by lazyColor(249, 150, 107)

    @JvmStatic
    val LIGHTSALMON: Color by lazyColor(255, 160, 122)

    @JvmStatic
    val DARKSALMON: Color by lazyColor(233, 150, 122)

    @JvmStatic
    val TANGERINE: Color by lazyColor(231, 138, 97)

    @JvmStatic
    val LIGHT_COPPER: Color by lazyColor(218, 138, 103)

    @JvmStatic
    val SALMON_PINK: Color by lazyColor(255, 134, 116)

    @JvmStatic
    val SALMON: Color by lazyColor(250, 128, 114)

    @JvmStatic
    val PEACH_PINK: Color by lazyColor(249, 139, 136)

    @JvmStatic
    val LIGHTCORAL: Color by lazyColor(240, 128, 128)

    @JvmStatic
    val PASTEL_RED: Color by lazyColor(246, 114, 128)

    @JvmStatic
    val PINK_CORAL: Color by lazyColor(231, 116, 113)

    @JvmStatic
    val BEAN_RED: Color by lazyColor(247, 93, 89)

    @JvmStatic
    val VALENTINE_RED: Color by lazyColor(229, 84, 81)

    @JvmStatic
    val INDIANRED: Color by lazyColor(205, 92, 92)

    @JvmStatic
    val TOMATO: Color by lazyColor(255, 99, 71)

    @JvmStatic
    val SHOCKING_ORANGE: Color by lazyColor(229, 91, 60)

    @JvmStatic
    val ORANGERED: Color by lazyColor(255, 69, 0)

    @JvmStatic
    val RED: Color by lazyColor(255, 0, 0)

    @JvmStatic
    val NEON_RED: Color by lazyColor(253, 28, 3)

    @JvmStatic
    val SCARLET_RED: Color by lazyColor(255, 36, 0)

    @JvmStatic
    val RUBY_RED: Color by lazyColor(246, 34, 23)

    @JvmStatic
    val FERRARI_RED: Color by lazyColor(247, 13, 26)

    @JvmStatic
    val FIRE_ENGINE_RED: Color by lazyColor(246, 40, 23)

    @JvmStatic
    val LAVA_RED: Color by lazyColor(228, 34, 23)

    @JvmStatic
    val LOVE_RED: Color by lazyColor(228, 27, 23)

    @JvmStatic
    val GRAPEFRUIT: Color by lazyColor(220, 56, 31)

    @JvmStatic
    val CHERRY_RED: Color by lazyColor(194, 70, 65)

    @JvmStatic
    val CHILLI_PEPPER: Color by lazyColor(193, 27, 23)

    @JvmStatic
    val FIREBRICK: Color by lazyColor(178, 34, 34)

    @JvmStatic
    val TOMATO_SAUCE_RED: Color by lazyColor(178, 24, 7)

    @JvmStatic
    val BROWN: Color by lazyColor(165, 42, 42)

    @JvmStatic
    val CARBON_RED: Color by lazyColor(167, 13, 42)

    @JvmStatic
    val CRANBERRY: Color by lazyColor(159, 0, 15)

    @JvmStatic
    val SAFFRON_RED: Color by lazyColor(147, 19, 20)

    @JvmStatic
    val CRIMSON_RED: Color by lazyColor(153, 0, 0)

    @JvmStatic
    val WINE_RED: Color by lazyColor(153, 0, 18)

    @JvmStatic
    val DARKRED: Color by lazyColor(139, 0, 0)

    @JvmStatic
    val MAROON: Color by lazyColor(128, 0, 0)

    @JvmStatic
    val BURGUNDY: Color by lazyColor(140, 0, 26)

    @JvmStatic
    val VERMILION: Color by lazyColor(126, 25, 27)

    @JvmStatic
    val DEEP_RED: Color by lazyColor(128, 5, 23)

    @JvmStatic
    val RED_BLOOD: Color by lazyColor(102, 0, 0)

    @JvmStatic
    val BLOOD_NIGHT: Color by lazyColor(85, 22, 6)

    @JvmStatic
    val DARK_SCARLET: Color by lazyColor(86, 3, 25)

    @JvmStatic
    val BLACK_BEAN: Color by lazyColor(61, 12, 2)

    @JvmStatic
    val CHOCOLATE_BROWN: Color by lazyColor(63, 0, 15)

    @JvmStatic
    val MIDNIGHT: Color by lazyColor(43, 27, 23)

    @JvmStatic
    val PURPLE_LILY: Color by lazyColor(85, 10, 53)

    @JvmStatic
    val PURPLE_MAROON: Color by lazyColor(129, 5, 65)

    @JvmStatic
    val PLUM_PIE: Color by lazyColor(125, 5, 65)

    @JvmStatic
    val PLUM_VELVET: Color by lazyColor(125, 5, 82)

    @JvmStatic
    val DARK_RASPBERRY: Color by lazyColor(135, 38, 87)

    @JvmStatic
    val VELVET_MAROON: Color by lazyColor(126, 53, 77)

    @JvmStatic
    val ROSY_FINCH: Color by lazyColor(127, 78, 82)

    @JvmStatic
    val DULL_PURPLE: Color by lazyColor(127, 82, 93)

    @JvmStatic
    val PUCE: Color by lazyColor(127, 90, 88)

    @JvmStatic
    val ROSE_DUST: Color by lazyColor(153, 112, 112)

    @JvmStatic
    val PASTEL_BROWN: Color by lazyColor(177, 144, 127)

    @JvmStatic
    val ROSY_PINK: Color by lazyColor(179, 132, 129)

    @JvmStatic
    val ROSYBROWN: Color by lazyColor(188, 143, 143)

    @JvmStatic
    val KHAKI_ROSE: Color by lazyColor(197, 144, 142)

    @JvmStatic
    val LIPSTICK_PINK: Color by lazyColor(196, 135, 147)

    @JvmStatic
    val PINK_BROWN: Color by lazyColor(196, 129, 137)

    @JvmStatic
    val OLD_ROSE: Color by lazyColor(192, 128, 129)

    @JvmStatic
    val DUSTY_PINK: Color by lazyColor(213, 138, 148)

    @JvmStatic
    val PINK_DAISY: Color by lazyColor(231, 153, 163)

    @JvmStatic
    val ROSE: Color by lazyColor(232, 173, 170)

    @JvmStatic
    val DUSTY_ROSE: Color by lazyColor(201, 169, 166)

    @JvmStatic
    val SILVER_PINK: Color by lazyColor(196, 174, 173)

    @JvmStatic
    val GOLD_PINK: Color by lazyColor(230, 199, 194)

    @JvmStatic
    val ROSE_GOLD: Color by lazyColor(236, 197, 192)

    @JvmStatic
    val DEEP_PEACH: Color by lazyColor(255, 203, 164)

    @JvmStatic
    val PASTEL_ORANGE: Color by lazyColor(248, 184, 139)

    @JvmStatic
    val DESERT_SAND: Color by lazyColor(237, 201, 175)

    @JvmStatic
    val UNBLEACHED_SILK: Color by lazyColor(255, 221, 202)

    @JvmStatic
    val PIG_PINK: Color by lazyColor(253, 215, 228)

    @JvmStatic
    val PALE_PINK: Color by lazyColor(242, 212, 215)

    @JvmStatic
    val BLUSH: Color by lazyColor(255, 230, 232)

    @JvmStatic
    val MISTYROSE: Color by lazyColor(255, 228, 225)

    @JvmStatic
    val PINK_BUBBLE_GUM: Color by lazyColor(255, 223, 221)

    @JvmStatic
    val LIGHT_ROSE: Color by lazyColor(251, 207, 205)

    @JvmStatic
    val LIGHT_RED: Color by lazyColor(255, 204, 203)

    @JvmStatic
    val WARM_PINK: Color by lazyColor(246, 198, 189)

    @JvmStatic
    val DEEP_ROSE: Color by lazyColor(251, 187, 185)

    @JvmStatic
    val PINK: Color by lazyColor(255, 192, 203)

    @JvmStatic
    val LIGHTPINK: Color by lazyColor(255, 182, 193)

    @JvmStatic
    val SOFT_PINK: Color by lazyColor(255, 184, 191)

    @JvmStatic
    val DONUT_PINK: Color by lazyColor(250, 175, 190)

    @JvmStatic
    val BABY_PINK: Color by lazyColor(250, 175, 186)

    @JvmStatic
    val FLAMINGO_PINK: Color by lazyColor(249, 167, 176)

    @JvmStatic
    val PASTEL_PINK: Color by lazyColor(254, 163, 170)

    @JvmStatic
    val ROSE_PINK: Color by lazyColor(231, 161, 176)

    @JvmStatic
    val CADILLAC_PINK: Color by lazyColor(227, 138, 174)

    @JvmStatic
    val CARNATION_PINK: Color by lazyColor(247, 120, 161)

    @JvmStatic
    val PASTEL_ROSE: Color by lazyColor(229, 120, 143)

    @JvmStatic
    val BLUSH_RED: Color by lazyColor(229, 110, 148)

    @JvmStatic
    val PALEVIOLETRED: Color by lazyColor(219, 112, 147)

    @JvmStatic
    val PURPLE_PINK: Color by lazyColor(209, 101, 135)

    @JvmStatic
    val TULIP_PINK: Color by lazyColor(194, 90, 124)

    @JvmStatic
    val BASHFUL_PINK: Color by lazyColor(194, 82, 131)

    @JvmStatic
    val DARK_PINK: Color by lazyColor(231, 84, 128)

    @JvmStatic
    val DARK_HOT_PINK: Color by lazyColor(246, 96, 171)

    @JvmStatic
    val HOTPINK: Color by lazyColor(255, 105, 180)

    @JvmStatic
    val WATERMELON_PINK: Color by lazyColor(252, 108, 133)

    @JvmStatic
    val VIOLET_RED: Color by lazyColor(246, 53, 138)

    @JvmStatic
    val HOT_DEEP_PINK: Color by lazyColor(245, 40, 135)

    @JvmStatic
    val BRIGHT_PINK: Color by lazyColor(255, 0, 127)

    @JvmStatic
    val DEEPPINK: Color by lazyColor(255, 20, 147)

    @JvmStatic
    val NEON_PINK: Color by lazyColor(245, 53, 170)

    @JvmStatic
    val CHROME_PINK: Color by lazyColor(255, 51, 170)

    @JvmStatic
    val NEON_HOT_PINK: Color by lazyColor(253, 52, 156)

    @JvmStatic
    val PINK_CUPCAKE: Color by lazyColor(228, 94, 157)

    @JvmStatic
    val ROYAL_PINK: Color by lazyColor(231, 89, 172)

    @JvmStatic
    val DIMORPHOTHECA_MAGENTA: Color by lazyColor(227, 49, 157)

    @JvmStatic
    val PINK_LEMONADE: Color by lazyColor(228, 40, 124)

    @JvmStatic
    val RED_PINK: Color by lazyColor(250, 42, 85)

    @JvmStatic
    val RASPBERRY: Color by lazyColor(227, 11, 93)

    @JvmStatic
    val CRIMSON: Color by lazyColor(220, 20, 60)

    @JvmStatic
    val BRIGHT_MAROON: Color by lazyColor(195, 33, 72)

    @JvmStatic
    val ROSE_RED: Color by lazyColor(194, 30, 86)

    @JvmStatic
    val ROGUE_PINK: Color by lazyColor(193, 40, 105)

    @JvmStatic
    val BURNT_PINK: Color by lazyColor(193, 34, 103)

    @JvmStatic
    val PINK_VIOLET: Color by lazyColor(202, 34, 107)

    @JvmStatic
    val MAGENTA_PINK: Color by lazyColor(204, 51, 139)

    @JvmStatic
    val MEDIUMVIOLETRED: Color by lazyColor(199, 21, 133)

    @JvmStatic
    val DARK_CARNATION_PINK: Color by lazyColor(193, 34, 131)

    @JvmStatic
    val RASPBERRY_PURPLE: Color by lazyColor(179, 68, 108)

    @JvmStatic
    val PINK_PLUM: Color by lazyColor(185, 59, 143)

    @JvmStatic
    val ORCHID: Color by lazyColor(218, 112, 214)

    @JvmStatic
    val DEEP_MAUVE: Color by lazyColor(223, 115, 212)

    @JvmStatic
    val VIOLET: Color by lazyColor(238, 130, 238)

    @JvmStatic
    val FUCHSIA_PINK: Color by lazyColor(255, 119, 255)

    @JvmStatic
    val BRIGHT_NEON_PINK: Color by lazyColor(244, 51, 255)

    @JvmStatic
    val MAGENTA: Color by lazyColor(255, 0, 255)

    @JvmStatic
    val CRIMSON_PURPLE: Color by lazyColor(226, 56, 236)

    @JvmStatic
    val HELIOTROPE_PURPLE: Color by lazyColor(212, 98, 255)

    @JvmStatic
    val TYRIAN_PURPLE: Color by lazyColor(196, 90, 236)

    @JvmStatic
    val MEDIUMORCHID: Color by lazyColor(186, 85, 211)

    @JvmStatic
    val PURPLE_FLOWER: Color by lazyColor(167, 74, 199)

    @JvmStatic
    val ORCHID_PURPLE: Color by lazyColor(176, 72, 181)

    @JvmStatic
    val RICH_LILAC: Color by lazyColor(182, 102, 210)

    @JvmStatic
    val PASTEL_VIOLET: Color by lazyColor(210, 145, 188)

    @JvmStatic
    val MAUVE_TAUPE: Color by lazyColor(145, 95, 109)

    @JvmStatic
    val VIOLA_PURPLE: Color by lazyColor(126, 88, 126)

    @JvmStatic
    val EGGPLANT: Color by lazyColor(97, 64, 81)

    @JvmStatic
    val PLUM_PURPLE: Color by lazyColor(88, 55, 89)

    @JvmStatic
    val GRAPE: Color by lazyColor(94, 90, 128)

    @JvmStatic
    val PURPLE_NAVY: Color by lazyColor(78, 81, 128)

    @JvmStatic
    val SLATEBLUE: Color by lazyColor(106, 90, 205)

    @JvmStatic
    val BLUE_LOTUS: Color by lazyColor(105, 96, 236)

    @JvmStatic
    val BLURPLE: Color by lazyColor(88, 101, 242)

    @JvmStatic
    val LIGHT_SLATE_BLUE: Color by lazyColor(115, 106, 255)

    @JvmStatic
    val MEDIUMSLATEBLUE: Color by lazyColor(123, 104, 238)

    @JvmStatic
    val PERIWINKLE_PURPLE: Color by lazyColor(117, 117, 207)

    @JvmStatic
    val VERY_PERI: Color by lazyColor(102, 103, 171)

    @JvmStatic
    val BRIGHT_GRAPE: Color by lazyColor(111, 45, 168)

    @JvmStatic
    val PURPLE_AMETHYST: Color by lazyColor(108, 45, 199)

    @JvmStatic
    val BRIGHT_PURPLE: Color by lazyColor(106, 13, 173)

    @JvmStatic
    val DEEP_PERIWINKLE: Color by lazyColor(84, 83, 166)

    @JvmStatic
    val DARKSLATEBLUE: Color by lazyColor(72, 61, 139)

    @JvmStatic
    val PURPLE_HAZE: Color by lazyColor(78, 56, 126)

    @JvmStatic
    val PURPLE_IRIS: Color by lazyColor(87, 27, 126)

    @JvmStatic
    val DARK_PURPLE: Color by lazyColor(75, 1, 80)

    @JvmStatic
    val DEEP_PURPLE: Color by lazyColor(54, 1, 63)

    @JvmStatic
    val MIDNIGHT_PURPLE: Color by lazyColor(46, 26, 71)

    @JvmStatic
    val PURPLE_MONSTER: Color by lazyColor(70, 27, 126)

    @JvmStatic
    val INDIGO: Color by lazyColor(75, 0, 130)

    @JvmStatic
    val BLUE_WHALE: Color by lazyColor(52, 45, 126)

    @JvmStatic
    val REBECCAPURPLE: Color by lazyColor(102, 51, 153)

    @JvmStatic
    val PURPLE_JAM: Color by lazyColor(106, 40, 126)

    @JvmStatic
    val DARKMAGENTA: Color by lazyColor(139, 0, 139)

    @JvmStatic
    val PURPLE: Color by lazyColor(128, 0, 128)

    @JvmStatic
    val FRENCH_LILAC: Color by lazyColor(134, 96, 142)

    @JvmStatic
    val DARKORCHID: Color by lazyColor(153, 50, 204)

    @JvmStatic
    val DARKVIOLET: Color by lazyColor(148, 0, 211)

    @JvmStatic
    val PURPLE_VIOLET: Color by lazyColor(141, 56, 201)

    @JvmStatic
    val JASMINE_PURPLE: Color by lazyColor(162, 59, 236)

    @JvmStatic
    val PURPLE_DAFFODIL: Color by lazyColor(176, 65, 255)

    @JvmStatic
    val CLEMATIS_VIOLET: Color by lazyColor(132, 45, 206)

    @JvmStatic
    val BLUEVIOLET: Color by lazyColor(138, 43, 226)

    @JvmStatic
    val PURPLE_SAGE_BUSH: Color by lazyColor(122, 93, 199)

    @JvmStatic
    val LOVELY_PURPLE: Color by lazyColor(127, 56, 236)

    @JvmStatic
    val NEON_PURPLE: Color by lazyColor(157, 0, 255)

    @JvmStatic
    val PURPLE_PLUM: Color by lazyColor(142, 53, 239)

    @JvmStatic
    val AZTECH_PURPLE: Color by lazyColor(137, 59, 255)

    @JvmStatic
    val MEDIUMPURPLE: Color by lazyColor(147, 112, 219)

    @JvmStatic
    val LIGHT_PURPLE: Color by lazyColor(132, 103, 215)

    @JvmStatic
    val CROCUS_PURPLE: Color by lazyColor(145, 114, 236)

    @JvmStatic
    val PURPLE_MIMOSA: Color by lazyColor(158, 123, 255)

    @JvmStatic
    val PERIWINKLE: Color by lazyColor(204, 204, 255)

    @JvmStatic
    val PALE_LILAC: Color by lazyColor(220, 208, 255)

    @JvmStatic
    val LAVENDER_PURPLE: Color by lazyColor(150, 123, 182)

    @JvmStatic
    val ROSE_PURPLE: Color by lazyColor(176, 159, 202)

    @JvmStatic
    val LILAC: Color by lazyColor(200, 162, 200)

    @JvmStatic
    val MAUVE: Color by lazyColor(224, 176, 255)

    @JvmStatic
    val BRIGHT_LILAC: Color by lazyColor(216, 145, 239)

    @JvmStatic
    val PURPLE_DRAGON: Color by lazyColor(195, 142, 199)

    @JvmStatic
    val PLUM: Color by lazyColor(221, 160, 221)

    @JvmStatic
    val BLUSH_PINK: Color by lazyColor(230, 169, 236)

    @JvmStatic
    val PASTEL_PURPLE: Color by lazyColor(242, 162, 232)

    @JvmStatic
    val BLOSSOM_PINK: Color by lazyColor(249, 183, 255)

    @JvmStatic
    val WISTERIA_PURPLE: Color by lazyColor(198, 174, 199)

    @JvmStatic
    val PURPLE_THISTLE: Color by lazyColor(210, 185, 211)

    @JvmStatic
    val THISTLE: Color by lazyColor(216, 191, 216)

    @JvmStatic
    val PURPLE_WHITE: Color by lazyColor(223, 211, 227)

    @JvmStatic
    val PERIWINKLE_PINK: Color by lazyColor(233, 207, 236)

    @JvmStatic
    val COTTON_CANDY: Color by lazyColor(252, 223, 255)

    @JvmStatic
    val LAVENDER_PINOCCHIO: Color by lazyColor(235, 221, 226)

    @JvmStatic
    val DARK_WHITE: Color by lazyColor(225, 217, 209)

    @JvmStatic
    val ASH_WHITE: Color by lazyColor(233, 228, 212)

    @JvmStatic
    val WHITE_CHOCOLATE: Color by lazyColor(237, 230, 214)

    @JvmStatic
    val SOFT_IVORY: Color by lazyColor(250, 240, 221)

    @JvmStatic
    val OFF_WHITE: Color by lazyColor(248, 240, 227)

    @JvmStatic
    val PEARL_WHITE: Color by lazyColor(248, 246, 240)

    @JvmStatic
    val RED_WHITE: Color by lazyColor(243, 232, 234)

    @JvmStatic
    val LAVENDERBLUSH: Color by lazyColor(255, 240, 245)

    @JvmStatic
    val PEARL: Color by lazyColor(253, 238, 244)

    @JvmStatic
    val EGG_SHELL: Color by lazyColor(255, 249, 227)

    @JvmStatic
    val OLDLACE: Color by lazyColor(254, 240, 227)

    @JvmStatic
    val LINEN: Color by lazyColor(250, 240, 230)

    @JvmStatic
    val SEASHELL: Color by lazyColor(255, 245, 238)

    @JvmStatic
    val BONE_WHITE: Color by lazyColor(249, 246, 238)

    @JvmStatic
    val RICE: Color by lazyColor(250, 245, 239)

    @JvmStatic
    val FLORALWHITE: Color by lazyColor(255, 250, 240)

    @JvmStatic
    val IVORY: Color by lazyColor(255, 255, 240)

    @JvmStatic
    val WHITE_GOLD: Color by lazyColor(255, 255, 244)

    @JvmStatic
    val LIGHT_WHITE: Color by lazyColor(255, 255, 247)

    @JvmStatic
    val WHITESMOKE: Color by lazyColor(245, 245, 245)

    @JvmStatic
    val COTTON: Color by lazyColor(251, 251, 249)

    @JvmStatic
    val SNOW: Color by lazyColor(255, 250, 250)

    @JvmStatic
    val MILK_WHITE: Color by lazyColor(254, 252, 255)

    @JvmStatic
    val HALF_WHITE: Color by lazyColor(255, 254, 250)

    @JvmStatic
    val WHITE: Color by lazyColor(255, 255, 255)

}