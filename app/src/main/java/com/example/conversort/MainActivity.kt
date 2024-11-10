package com.example.conversort

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init| del SDK de AdMob
        MobileAds.initialize(this) { }

        // ref a los elementos de la UI
        val editTemperature = findViewById<EditText>(R.id.editTemperature)
        val spinnerFrom = findViewById<Spinner>(R.id.spinnerFrom)
        val spinnerTo = findViewById<Spinner>(R.id.spinnerTo)
        val buttonConvert = findViewById<Button>(R.id.buttonConvert)
        val textResult = findViewById<TextView>(R.id.textResult)

        // item del Spinner con las escalas de temperatura
        val scales = arrayOf("Celsius", "Fahrenheit", "Kelvin")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, scales)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        // pone "Celsius" como la opción seleccionada por defecto
        spinnerFrom.setSelection(0)
        // la config del evento de clic en el botón "Convertir"
        buttonConvert.setOnClickListener {
            val tempStr = editTemperature.text.toString()

            if (tempStr.isEmpty()) {
                textResult.text = "Por favor ingresa una temperatura válida"
                return@setOnClickListener
            }

            val temperature = tempStr.toDoubleOrNull()
            if (temperature == null) {
                textResult.text = "Por favor ingresa un número válido"
                return@setOnClickListener
            }

            val fromScale = spinnerFrom.selectedItem.toString()
            val toScale = spinnerTo.selectedItem.toString()

            val result = convertTemperature(temperature, fromScale, toScale)
            textResult.text = result
        }
    }

    // la fun para convertir temperaturas entre escalas
    private fun convertTemperature(temperature: Double, fromScale: String, toScale: String): String {
        var result = 0.0

        // convert de acuerdo a las escalas
        when (fromScale) {
            "Celsius" -> result = when (toScale) {
                "Fahrenheit" -> (temperature * 9 / 5) + 32
                "Kelvin" -> temperature + 273.15
                else -> temperature
            }
            "Fahrenheit" -> result = when (toScale) {
                "Celsius" -> (temperature - 32) * 5 / 9
                "Kelvin" -> (temperature - 32) * 5 / 9 + 273.15
                else -> temperature
            }
            "Kelvin" -> result = when (toScale) {
                "Celsius" -> temperature - 273.15
                "Fahrenheit" -> (temperature - 273.15) * 9 / 5 + 32
                else -> temperature
            }
        }

        // da     el resultado formateado con la escala
        return String.format("%.2f", result) + " $toScale"
    }
}
