package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val PENDING_OPERATION = "Pending_Operation"
private const val STATE_OPERAND1 = "Operand1"

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    //private val result by lazy { findViewById<EditText>(R.id.result) }
    private lateinit var newNumber: EditText
    //private val newNumber by lazy { findViewById<EditText>(R.id.newNumber) }
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // Variables que agarraran los operadores y el tipo de calculo
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //defino las vistas que utilizaré para tenerlas referenciadas
        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        //botones de ingreso de datos, aqui pongo sus referencias
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        //botones de operacion
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
        val buttonPlus = findViewById<Button>(R.id.buttonPlus)
        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
        val buttonNeg: Button = findViewById(R.id.buttonNeg)
        val buttonClear = findViewById<Button>(R.id.buttonClear)

        val listener = View.OnClickListener { view ->
            val b = view as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { view ->
            val op = (view as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if (value.isEmpty()){
                newNumber.append("-")
            } else{
                try{
                    var negative = value.toDouble()
                    negative *= -1
                    newNumber.setText(negative.toString())
                } catch (e: NumberFormatException){
                    //es un "-" o un "."
                    newNumber.setText("")
                }
            }
        }

        buttonClear.setOnClickListener {
            newNumber.setText("")
            displayOperation.text = ""
            result.setText("")
            operand1 = null
        }
    }

    private fun performOperation(value: Double, operation: String){
        if(operand1 == null) operand1 = value
        else {
            if (pendingOperation == "=") pendingOperation = operation
            when (pendingOperation){
                "=" -> operand1 = value
                "/" -> operand1 = if(value == 0.0) Double.NaN // Handle attempt to divide by zero
                                  else operand1!! / value
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null){
            outState.putDouble(STATE_OPERAND1, operand1!!)
        }
        outState.putString(PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        pendingOperation = savedInstanceState.getString(PENDING_OPERATION).toString()
        displayOperation.text = savedInstanceState.getString(PENDING_OPERATION)
    }
}