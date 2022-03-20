package net.realmidc.niuzi.entity

import net.realmidc.niuzi.enums.Sex

/**
 * @author Ting
 * @date 2022/2/16 4:58 PM
 */
data class NiuZi(val owner: Long, val name: String, val length: Double, val sex: Sex, val level: Int, val points: Int)
