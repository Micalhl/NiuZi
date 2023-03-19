package net.realmidc.niuzi.data

import net.realmidc.niuzi.data.impl.*

object TDataManager {

    private val DATAS = listOf(
        HongZhongData(),
        DoiData(),
        LeaveData(),
        LoveRequestData()
    )

    fun init() {
        DATAS.forEach { it.load() }
    }

    fun disable() {
        DATAS.forEach { it.save() }
    }

}