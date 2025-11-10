package com.example.numberlist   // đổi đúng package của bạn

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var etN: EditText
    private lateinit var tvEmpty: TextView
    private lateinit var lv: ListView
    private lateinit var adapter: ArrayAdapter<Int>

    private lateinit var rbOdd: RadioButton
    private lateinit var rbPrime: RadioButton
    private lateinit var rbPerfect: RadioButton
    private lateinit var rbEven: RadioButton
    private lateinit var rbSquare: RadioButton
    private lateinit var rbFibo: RadioButton

    private var changing = false

    enum class Type { ODD, EVEN, PRIME, PERFECT, SQUARE, FIBO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ánh xạ view
        etN = findViewById(R.id.etN)
        tvEmpty = findViewById(R.id.tvEmpty)
        lv = findViewById(R.id.lvNumbers)
        rbOdd = findViewById(R.id.rbOdd)
        rbPrime = findViewById(R.id.rbPrime)
        rbPerfect = findViewById(R.id.rbPerfect)
        rbEven = findViewById(R.id.rbEven)
        rbSquare = findViewById(R.id.rbSquare)
        rbFibo = findViewById(R.id.rbFibo)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lv.adapter = adapter

        // danh sách các nút
        val radios = listOf(rbOdd, rbPrime, rbPerfect, rbEven, rbSquare, rbFibo)

        // chọn mặc định
        rbOdd.isChecked = true

        // chỉ cho phép 1 nút được chọn
        radios.forEach { rb ->
            rb.setOnClickListener {
                if (changing) return@setOnClickListener
                changing = true
                radios.filter { it != rb }.forEach { it.isChecked = false }
                changing = false
                updateList()
            }
        }

        // cập nhật khi nhập số
        etN.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateList()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        updateList()
    }

    private fun getN(): Int = etN.text.toString().trim().toIntOrNull() ?: 0

    private fun currentType(): Type = when {
        rbOdd.isChecked -> Type.ODD
        rbEven.isChecked -> Type.EVEN
        rbPrime.isChecked -> Type.PRIME
        rbPerfect.isChecked -> Type.PERFECT
        rbSquare.isChecked -> Type.SQUARE
        rbFibo.isChecked -> Type.FIBO
        else -> Type.ODD
    }

    private fun updateList() {
        val n = getN()
        if (n <= 0) {
            showEmpty()
            return
        }

        val list = when (currentType()) {
            Type.ODD -> (1 until n).filter { it % 2 != 0 }
            Type.EVEN -> (1 until n).filter { it % 2 == 0 }
            Type.PRIME -> (2 until n).filter { isPrime(it) }
            Type.PERFECT -> (2 until n).filter { isPerfect(it) }
            Type.SQUARE -> (1 until n).filter { isSquare(it) }
            Type.FIBO -> fiboLessThan(n)
        }

        if (list.isEmpty()) showEmpty() else showList(list)
    }

    private fun showEmpty() {
        tvEmpty.visibility = TextView.VISIBLE
        lv.visibility = ListView.GONE
        adapter.clear()
    }

    private fun showList(list: List<Int>) {
        tvEmpty.visibility = TextView.GONE
        lv.visibility = ListView.VISIBLE
        adapter.clear()
        adapter.addAll(list)
    }

    private fun isPrime(x: Int): Boolean {
        if (x < 2) return false
        if (x == 2 || x == 3) return true
        if (x % 2 == 0 || x % 3 == 0) return false
        var i = 5
        while (i * i <= x) {
            if (x % i == 0 || x % (i + 2) == 0) return false
            i += 6
        }
        return true
    }

    private fun isSquare(x: Int): Boolean {
        val r = sqrt(x.toDouble()).toInt()
        return r * r == x
    }

    private fun isPerfect(x: Int): Boolean {
        if (x <= 1) return false
        var sum = 1
        for (i in 2..sqrt(x.toDouble()).toInt()) {
            if (x % i == 0) {
                sum += i
                if (i != x / i) sum += x / i
            }
        }
        return sum == x
    }

    private fun fiboLessThan(n: Int): List<Int> {
        if (n <= 1) return emptyList()
        val list = mutableListOf(1, 1)
        while (true) {
            val next = list[list.size - 1] + list[list.size - 2]
            if (next >= n || next < 0) break
            list.add(next)
        }
        return list
    }
}
