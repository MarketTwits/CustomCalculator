package com.example.customcalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.customcalculator.databinding.FragmentCalcragmentBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var canAddOperation = false
private var canAddDecimal = true

class CalculatorFragment : Fragment() {
    lateinit var binding: FragmentCalcragmentBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalcragmentBinding.inflate(inflater, container, false)
        setListener()
        return binding.root
    }

    fun numberAction(button: Button)
    {
        //if(view is Button)
        //{
            if(button.text == ".")
            {
                if(canAddDecimal)
                    binding.workingsTV.append(button.text)

                canAddDecimal = false
            }
            else
              binding.workingsTV.append(button.text)

            canAddOperation = true
        //}
    }

    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            binding.workingsTV.append((view).text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction()
    {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }

    fun backSpaceAction()
    {
        val length = binding.workingsTV.length()
        if(length > 0)
          binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
    }

    fun equalsAction()
    {
       binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
    fun setListener(){
        with(binding){
            btAc.setOnClickListener{allClearAction()}
            btClean.setOnClickListener{backSpaceAction()}

            btDivision.setOnClickListener{operationAction(btDivision)}
            btMultiply.setOnClickListener{operationAction(btMultiply)}
            btPlus.setOnClickListener{operationAction(btPlus)}
            btMinus.setOnClickListener{operationAction(btMinus)}

            btEqually.setOnClickListener{equalsAction()}

            btDot.setOnClickListener{numberAction(btDot)}
            bt0.setOnClickListener{numberAction(bt0)}
            bt1.setOnClickListener{numberAction(bt1)}
            bt2.setOnClickListener{numberAction(bt2)}
            bt3.setOnClickListener{numberAction(bt3)}
            bt4.setOnClickListener{numberAction(bt4)}
            bt5.setOnClickListener{numberAction(bt5)}
            bt6.setOnClickListener{numberAction(bt6)}
            bt7.setOnClickListener{numberAction(bt7)}
            bt8.setOnClickListener{numberAction(bt8)}
            bt9.setOnClickListener{numberAction(bt9)}
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            CalculatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}