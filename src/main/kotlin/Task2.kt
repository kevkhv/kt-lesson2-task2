import kotlin.math.roundToInt

const val type0 = "Vk Pay"
const val type1 = "Mastercard/Maestro"
const val type2 = "Visa/Мир"

fun main() {
    val transfer = 15100000                      // Сумма перевода
    val currentCard = type2                      // Тип текущей карта
    var transfersMonthVk = 0                     // Счетчик суммы переводов в месяц с счета ВК
    var transfersMonthCredit = 59000000          // Счетчик суммы исходящих переводов в месяц с карты Mastercard/Maestro,Visa/Мир
    var transfersMonthDebit = 59000000           // Счетчик суммы входящих переводов в месяц с карты Mastercard/Maestro,Visa/Мир
    var transfersDays = 0                        // Счетчик суммы переводов в день с карты Mastercard/Maestro,Visa/Мир
    var booleanDebitCreditTransfer = true                // Переключатель входящий/исходящий перевод
    if (checkLimits(currentCard,transfer, transfersDays, transfersMonthCredit,transfersMonthDebit, transfersMonthVk)) {  // Проверяем все возможные лимиты
        val commission = calculateCommission(transfersMonthCredit, transfer, currentCard)                // Считаем комиссию
        if (currentCard == type0) {                                           // Увеличиваем счетчики на сумму перевода
            transfersMonthVk += transfer
        } else {
            if (booleanDebitCreditTransfer) transfersMonthCredit += transfer else transfersMonthDebit += transfer
            transfersDays += transfer
        }
        val totalSum = transfer + commission
        println("Сумма перевода ${getStringRubles(transfer)}")
        println("Коммисия за перевод составляет ${getStringRubles(commission)}")
        println("Итого с учетом комиссии ${getStringRubles(totalSum)}")
    } else println("Превышены лимиты")
}

fun calculateCommission(lastTransfersMonth: Int, transfer: Int, type: String = "Vk Pay"): Int {
    val maxLimitMaestro = 7500000
    return when (type) {
        type0 -> 0
        type1 -> if ((transfer + lastTransfersMonth) < maxLimitMaestro) 0 else (transfer * 0.0060 + 2000).roundToInt()
        type2 -> if ((transfer * 0.0075) <= 3500) 3500 else (transfer * 0.0075).roundToInt()
        else -> error("Карта не поддерживается")
    }
}

fun checkLimits(
    type: String,
    transfer: Int,
    transferDays: Int,
    transfersMonthCredit: Int,
    transfersMonthDebit:Int,
    transferMonthVk: Int
): Boolean {
    val dayMaxLimit = 15000000
    val debitMonthMaxLimit = 60000000
    val creditMonthMaxLimit = 60000000
    val vkPayLimit = 1500000
    val vkPayMaxMonthLimit = 4000000
    return when (type) {
        type0 -> !(transfer > vkPayLimit || transferMonthVk > vkPayMaxMonthLimit)
        else -> !(transferDays > dayMaxLimit || transfersMonthDebit > debitMonthMaxLimit ||
                transfersMonthCredit > creditMonthMaxLimit)
        }
    }

fun getStringRubles(money: Int): String {
    return "${money / 100} рублей ${money % 100} копеек"
}