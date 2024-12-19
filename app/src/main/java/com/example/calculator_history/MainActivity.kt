package com.example.calculator_history

import android.app.backup.SharedPreferencesBackupHelper
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.calculator_history.reco.MainAdapter
import com.example.calculator_history.reco.MainDatabase
import com.example.calculator_history.reco.MainList
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var database: MainDatabase
    private lateinit var adapter: MainAdapter

    var operator = true // Used to track if the last input was an operator
    var deci = true // Used to track if a decimal point has been added
    var value = "" // The current expression value
    var result = "" // The evaluated result
    private var i:Int = 1

    lateinit var setvalue: TextView
    lateinit var setresult: TextView
    lateinit var reco_history: RecyclerView
    lateinit var history_l6: LinearLayout

    lateinit var currentDate: String
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setvalue = findViewById(R.id.value)
        setresult = findViewById(R.id.result)
        reco_history = findViewById(R.id.reco_history)
        history_l6 = findViewById(R.id.history_l6)


        database = Room.databaseBuilder(
            applicationContext,
            MainDatabase::class.java,
            "myMainList"
        ).build()


        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        handler = Handler(Looper.getMainLooper())
        updateTimeRunnable = object : Runnable {
            override fun run() {
                // Update TextView with current date
                currentDate = dateFormat.format(Date()).toString()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(updateTimeRunnable)

        adapter = MainAdapter(ArrayList()){ data ->
            result = data.result
            value = data.value
            for_back()
            updateValue()
        }

        reco_history.layoutManager = LinearLayoutManager(this)
        reco_history.adapter = adapter

    }
    private fun loadItems() {
        lifecycleScope.launch {
            val items = database.contactDao().getContact()
            items.collect { itemList ->
                adapter.updateList(itemList)
            }
        }
    }

    // Functions to handle digit buttons (0-9)
    fun a0(view: View) {
        value += "0"
        operator = true
        updateValue()
    }
    fun a00(view: View) {
        value += "00"
        operator = true
        updateValue()
    }
    fun a1(view: View) {
        value += "1"
        operator = true
        updateValue()
    }
    fun a2(view: View) {
        value += "2"
        operator = true
        updateValue()
    }
    fun a3(view: View) {
        value += "3"
        operator = true
        updateValue()
    }
    fun a4(view: View) {
        value += "4"
        operator = true
        updateValue()
    }
    fun a5(view: View) {
        value += "5"
        operator = true
        updateValue()
    }
    fun a6(view: View) {
        value += "6"
        operator = true
        updateValue()
    }
    fun a7(view: View) {
        value += "7"
        operator = true
        updateValue()
    }
    fun a8(view: View) {
        value += "8"
        operator = true
        updateValue()
    }
    fun a9(view: View) {
        value += "9"
        operator = true
        updateValue()
    }

    // Clear the input and result
    fun ac(view: View) {
        value = ""
        setresult.setText("0")
        operator = true
        deci = true
        updateValue()
    }

    // Show the result
    fun a_equal(view: View) {
        if (value != result) {
            // Get SharedPreferences for both date and counter
            val sharedPreferences: SharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
            val storedDate: String? = sharedPreferences.getString("date", "")
            val storedCounter: String? = sharedPreferences.getString("no", "0")

            // Check if the current date matches the stored date
            if (currentDate == storedDate) {
                // If dates match, use the stored counter value
                i = storedCounter?.toInt() ?: 1
            }
            if (currentDate != storedDate) {
                i = 1
            }

            // Insert data into the database
            lifecycleScope.launch {
                database.contactDao().insertData(
                    MainList(
                        0,
                        currentDate,
                        "$i",        // Store the incremented counter
                        "$value",    // Store the expression
                        "$result"    // Store the result
                    )
                )
            }

            // Update the value and result on the UI
            value = result
            updateValue()

            // If the date has changed, reset the counter and save the new date
            if (currentDate != storedDate) {
                savetemporarydata(currentDate) // Save the new date
                sharedPreferences.edit().putString("no", "2").apply()

            } else {
                // Increment the counter
                i++
                // Update the counter in SharedPreferences
                sharedPreferences.edit().putString("no", "$i").apply()
            }
        }
    }

    private fun savetemporarydata(currentDate: String) {
        val sharepre: SharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharepre.edit()
        editor.putString("date",currentDate)
        editor.commit()
    }

    // Backspace functionality
    fun a_back(view: View) {
        if (value.isNotEmpty()) {
            val lastChar = value.last()
            if (lastChar in arrayOf('+', '-', '÷', 'x')) {
                operator = true
            }
            if (lastChar == '.') {
                deci = true
            }

            value = value.substring(0, value.length - 1)
            updateValue()
        }
    }

    // Adding operators
    fun a_add(view: View) {
        addOperator("+")
    }

    fun a_minus(view: View) {
        addOperator("-")
    }

    fun a_divide(view: View) {
        addOperator("÷")
    }

    fun a_multi(view: View) {
        addOperator("x")
    }

    // Adding a decimal point
    fun a_dot(view: View) {
        if (deci) {
            value += "."
            deci = false
            updateValue()
        }
    }

    private fun updateValue() {
        if (value.isNotEmpty() && value.last().isDigit()) {
            calculateResult()
        }
        if (value.isEmpty()){
            value += "0"
            calculateResult()
        }
        setvalue.text = value
    }
    // Function to calculate the result using ExpressionBuilder
    private fun calculateResult() {
        try {
            val formattedExpression = value.replace("÷", "/").replace("x", "*")
            val expression = ExpressionBuilder(formattedExpression).build()
            val resultValue = expression.evaluate()
            result = resultValue.toString()
            setresult.text = result
        } catch (e: Exception) {
            setresult.text = "Error"
        }
    }

    private fun addOperator(op: String) {
        if (operator && value.isNotEmpty()) {
            val lastChar = value.last()
            if (lastChar !in arrayOf('+', '-', '÷', 'x')) {
                value += op
                operator = false
                updateValue()
            }
        }
    }

    fun a_history(view: View){
        loadItems()
        history_l6.visibility = View.VISIBLE
    }
    fun back(view: View){
        for_back()
    }
    private fun for_back(){
        history_l6.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun clear(view: View) {
        lifecycleScope.launch {
            database.contactDao().clearAllData()
        }
    }

}
