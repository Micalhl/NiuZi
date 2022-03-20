package net.realmidc.niuzi.util

import java.util.*

// 为重构做准备

fun randomDouble(nextInt: Int): Double = Random().nextDouble() + Random().nextInt(nextInt)