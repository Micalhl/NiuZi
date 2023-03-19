package net.realmidc.niuzi.util

import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.data.TempStorage
import java.util.*
import kotlin.concurrent.schedule

object AutoRefresher {

    init {
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            TempStorage.hongzhongdata.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { TempStorage.hongzhongdata.remove(it) }
        }
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            TempStorage.cumdata.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { TempStorage.cumdata.remove(it) }
        }
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            TempStorage.doidata.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { TempStorage.doidata.remove(it) }
        }
        PluginMain.logger.info("已注册计时器")
    }

}