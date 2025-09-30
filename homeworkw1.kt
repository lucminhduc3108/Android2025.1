import kotlin.math.abs

class Fraction(var numerator: Int, var denominator: Int) : Comparable<Fraction> {

    init {
        require(denominator != 0) { "Mẫu số không được bằng 0" }
        normalizeSign()
    }

    private fun normalizeSign() {
        if (denominator < 0) {
            numerator = -numerator
            denominator = -denominator
        }
    }

    private fun gcd(a: Int, b: Int): Int {
        var x = abs(a)
        var y = abs(b)
        if (x == 0 && y == 0) return 1
        while (y != 0) {
            val t = x % y
            x = y
            y = t
        }
        return if (x == 0) 1 else x
    }

    fun simplify(): Fraction {
        val g = gcd(numerator, denominator)
        if (g != 0) {
            numerator /= g
            denominator /= g
        }
        normalizeSign()
        return this
    }

    fun plus(other: Fraction): Fraction {
        val a = this.numerator.toLong()
        val b = this.denominator.toLong()
        val c = other.numerator.toLong()
        val d = other.denominator.toLong()
        val n = a * d + b * c
        val m = b * d
        return Fraction(n.toInt(), m.toInt()).simplify()
    }

    override fun compareTo(other: Fraction): Int {
        val left = this.numerator.toLong() * other.denominator.toLong()
        val right = other.numerator.toLong() * this.denominator.toLong()
        return when {
            left < right -> -1
            left > right -> 1
            else -> 0
        }
    }

    override fun toString(): String = "$numerator/$denominator"

    companion object {
        // Nhập phân số từ bàn phím
        fun readFromKeyboard(index: Int? = null): Fraction {
            while (true) {
                try {
                    if (index != null) println("Nhập phân số thứ ${index + 1}:")
                    print("  Tử số (≠ 0): ")
                    val n = readln().toInt()
                    print("  Mẫu số (≠ 0): ")
                    val d = readln().toInt()
                    if (n == 0 || d == 0) {
                        println("  ❌ Tử số hoặc mẫu số bằng 0. Nhập lại!")
                        continue
                    }
                    return Fraction(n, d)
                } catch (e: NumberFormatException) {
                    println("  ❌ Vui lòng nhập số nguyên hợp lệ!")
                } catch (e: IllegalArgumentException) {
                    println("  ❌ ${e.message}")
                }
            }
        }
    }
}

fun main() {
    // Nhập số lượng phân số
    val n = readSize()
    val fractions = MutableList(n) { i -> Fraction.readFromKeyboard(i) }

    println("\n=== MẢNG PHÂN SỐ VỪA NHẬP ===")
    printFractionList(fractions)

    // Rút gọn
    fractions.forEach { it.simplify() }
    println("\n=== MẢNG SAU KHI TỐI GIẢN ===")
    printFractionList(fractions)

    // Tổng
    var sum = Fraction(0, 1)
    for (f in fractions) sum = sum.plus(f)
    println("\n=== TỔNG CÁC PHÂN SỐ ===")
    println("Tổng = ${sum.simplify()}")

    // Max
    val maxFrac = fractions.maxOrNull()
    println("\n=== PHÂN SỐ LỚN NHẤT ===")
    println("Max = $maxFrac")

    // Sắp xếp giảm dần
    val sortedDesc = fractions.sortedDescending()
    println("\n=== MẢNG SẮP XẾP GIẢM DẦN ===")
    printFractionList(sortedDesc)
}

fun readSize(): Int {
    while (true) {
        try {
            print("Nhập số lượng phân số n (>0): ")
            val n = readln().toInt()
            if (n <= 0) {
                println("  ❌ n phải > 0.")
                continue
            }
            return n
        } catch (e: NumberFormatException) {
            println("  ❌ Vui lòng nhập số nguyên hợp lệ!")
        }
    }
}

fun printFractionList(list: List<Fraction>) {
    if (list.isEmpty()) {
        println("(rỗng)")
        return
    }
    println(list.joinToString(", ") { it.toString() })
}
