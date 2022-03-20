package net.realmidc.niuzi.enums

/**
 * @author Ting
 * @date 2022/2/16 5:03 PM
 */
enum class Sex {
    MALE, FEMALE;

    fun toChinese(): String {
        return if (equals(MALE)) "男" else "女"
    }
}