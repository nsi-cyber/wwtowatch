package utils


import kotlinx.datetime.LocalDate

fun String.formatDate(): String {
    val inputDate = LocalDate.parse(this)
    return "${inputDate.dayOfMonth} ${
        inputDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    } ${inputDate.year}"
}