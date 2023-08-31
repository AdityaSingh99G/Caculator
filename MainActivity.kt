 package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    var equalclicked = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun OnAllclearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        binding.resultTv.visibility = View.GONE

    }

    fun OnEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
        equalclicked = true
        binding.resultTv.text=""

    }

    fun OnOperatorClick(view: View) {
        if (lastNumeric && !stateError) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun OnbackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try {
            val lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char Error", e.toString())
        }
    }

    fun OnClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false

    }

    fun OnDigitClick(view: View) {

        if (stateError) {
            binding.dataTv.text = (view as Button).text
            stateError = false
        }
        else if(equalclicked){
            OnAllclearClick(view)
            binding.dataTv.append((view as Button).text)
            equalclicked=false
        }
        else {
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    @SuppressLint("SetTextI18n")
    fun onEqual() {
        if (lastNumeric && !stateError) {
            //  this line of code takes the text content from the dataTv UI element and converts it into a String named txt.
            //  .text: This part retrieves the content of the selected UI element. In this case, it retrieves the text content of the TextView.
            val txt = binding.dataTv.text.toString()
            //  ExpressionBuilder(txt): This creates an instance of the ExpressionBuilder class and passes the txt string as input.
            //  .build(): This method is called on the ExpressionBuilder instance to actually build the expression.
            //  It parses the input string (txt) and creates an expression that can be evaluated.
            expression = ExpressionBuilder(txt).build()
            //  Here, you use the ExpressionBuilder class from the 'exp4j' library to create and build an expression from the txt.
            //ExpressionBuilder(expressionString): You create an instance of the ExpressionBuilder class by passing the txt to its constructor.
            // This class is responsible for parsing and processing mathematical expressions.
            // here parsing means categorising it into different tokens like operator,operants etc
            // remember  ExpressionBuilder(txt) is a instance(bole to object)
            // build() ----   This method finalizes the creation of the expression object based on the provided expression string and prepares it for evaluation.
            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=" + result.toString()

            } catch (ex: ArithmeticException) {
                Log.e("Evaluate Error", ex.toString())
                binding.resultTv.text = "Errorrrr"
                stateError = true
                lastNumeric = false
            }

        }
    }

    fun OnPointClick(view: View) {
        if (!stateError && !lastDot){
            binding.dataTv.append((view as Button).text)
            lastDot= true
            lastNumeric=false
        }


    }


}




