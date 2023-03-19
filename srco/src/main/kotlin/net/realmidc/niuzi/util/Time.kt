package net.realmidc.niuzi.util

/**
 * @author xiaomu
 * @since 2022/8/14 17:06
 */
val DEFAULT_TIME_KEY = mapOf(
    "年" to 365 * 24 * 60 * 60, "月" to 30 * 24 * 60 * 60, "天" to 24 * 60 * 60, "时" to 60 * 60, "分" to 60, "秒" to 1
)

/**
 * 根据数字（单位：秒）获取描述性时间长度字符串，如 1 年 1 月 4 天 5 小时 1 分钟 4 秒
 *
 * @return 时间长度
 */
fun Long.getTimeLong(): String {
    var time = this / 1000L
    var cache: Long
    val result = StringBuilder()

    if (time <= 0) {
        return "{0} {1}".replaceWithOrder(time, "秒")
    }

    for (entry in DEFAULT_TIME_KEY.entries) {
        cache = time % entry.value
        val number = (time - cache) / entry.value
        time -= number * entry.value
        if (number != 0L) {
            result.append("{0} {1} ".replaceWithOrder(number, entry.key))
        }
    }
    return result.toString().trim { it <= ' ' }
}