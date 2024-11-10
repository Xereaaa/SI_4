import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.currencyconverter.R



class MainActivity : AppCompatActivity() {

    private lateinit var currencyService: CurrencyService
    private val apiKey = "32537c4ce7b1e14988ecb8b1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyService = retrofit.create(CurrencyService::class.java)

        loadCurrencies()

        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun loadCurrencies() {
        val currencies = listOf("USD", "EUR", "INR", "GBP", "JPY", "CAD") // Add more as needed
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
    }

    private fun convertCurrency() {
        val amount = editTextAmount.text.toString().toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()

        currencyService.getExchangeRates(apiKey, fromCurrency).enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                val rates = response.body()?.rates
                if (rates != null && rates.containsKey(toCurrency)) {
                    val rate = rates[toCurrency]!!
                    val convertedAmount = amount * rate
                    textViewResult.text = String.format("%.2f %s", convertedAmount, toCurrency)
                } else {
                    Toast.makeText(this@MainActivity, "Conversion failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load exchange rates", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
