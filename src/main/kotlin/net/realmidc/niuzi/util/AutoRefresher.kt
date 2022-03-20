package net.realmidc.niuzi.util

import net.realmidc.niuzi.data.CumData
import net.realmidc.niuzi.data.HongZhongData
import java.util.*
import kotlin.concurrent.schedule

object AutoRefresher {

    init {
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            HongZhongData.dataMap.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { HongZhongData.dataMap.remove(it) }
        }
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            CumData.dataMap.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { CumData.dataMap.remove(it) }
        }
        println("已注册计时器")
    }

}