package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentFirstBinding

import kotlin.math.pow
import kotlin.math.exp
import kotlin.math.round
import kotlin.math.sqrt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Встановлення обробки для кнопки "Обчислити"
        binding.resButton.setOnClickListener {

            // Зчитування даних з EditText'ів через binding
            val num1 = binding.number1.text.toString().toDoubleOrNull() ?: 0.0 // потужність
            val num2 = binding.number2.text.toString().toDoubleOrNull() ?: 0.0 // відхилення 1
            val num3 = binding.number3.text.toString().toDoubleOrNull() ?: 0.0 // відхилення 2
            val num4 = binding.number4.text.toString().toDoubleOrNull() ?: 0.0 // вартість

            val δ1 = calculate(num1, num2)
            val δ2 = calculate(num1, num3)

            // Обчислення результатів
            val result1 = (num1 * 24 * (δ1)) * num4
            val result2 = (num1 * 24 * (1 - δ1)) * num4
            val res1 = result2 - result1
            val result3 = (num1 * 24 * (δ2)) * num4
            val result4 = (num1 * 24 * (1 - δ2)) * num4
            val res2 = result3 - result4


            // Відображення результатів
            binding.profit1.text = getString(R.string.profit_text1, result1)
            binding.penalty1.text = getString(R.string.penalty_text1, result2)
            binding.totalProfit1.text = getString(R.string.profit_text_1, res1)
            binding.profit2.text = getString(R.string.profit_text2, result3)
            binding.penalty2.text = getString(R.string.penalty_text2, result4)
            binding.profit.text = getString(R.string.profit_text_2, res2)
        }

    }
    // Функція обчислення части енергії, що генерується без небалансів
    private fun calculate(pC: Double, sigma: Double): Double {
        val lowerBound = 4.75 // нижня межа інтегрування
        val upperBound = 5.25 // верхня межа інтегрування
        val numIntervals = 1000 // кількість інтервалів для інтегрування

        // Змінна для накопичення площі під кривою
        var area = 0.0
        val intervalWidth = (upperBound - lowerBound) / numIntervals

        // Цикл для інтегрування
        for (i in 0 until numIntervals) {
            val x1 = lowerBound + i * intervalWidth
            val x2 = lowerBound + (i + 1) * intervalWidth

            val pD1 = (1 / (sigma * sqrt(2 * Math.PI))) * exp(-((x1 - pC).pow(2) / (2 * sigma.pow(2))))
            val pD2 = (1 / (sigma * sqrt(2 * Math.PI))) * exp(-((x2 - pC).pow(2) / (2 * sigma.pow(2))))

            area += (pD1 + pD2) * intervalWidth / 2
        }

        // Заокруглення до двох знаків після коми
        area = (area * 100).let { round(it) / 100 }
        return area
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}