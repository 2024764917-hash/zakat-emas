package com.wardina.zakatemas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etValue;
    private RadioGroup rgType;
    private RadioButton rbKeep;
    private TextView tvTotalValue, tvPayableValue, tvTotalZakat;

    // URL GitHub Aplikasi [cite: 22, 23]
    private final String githubUrl = "https://github.com/wardina/zakat-emas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tajuk pada Title Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gold Zakat Calculator");
        }

        // Inisialisasi UI [cite: 6, 10]
        etWeight = findViewById(R.id.etWeight);
        etValue = findViewById(R.id.etValue);
        rgType = findViewById(R.id.rgType);
        rbKeep = findViewById(R.id.rbKeep);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvPayableValue = findViewById(R.id.tvPayableValue);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);

        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnAbout = findViewById(R.id.btnAbout);

        btnCalculate.setOnClickListener(v -> calculateZakat());

        // Pindah ke skrin AboutActivity
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString().trim();
        String valueStr = etValue.getText().toString().trim();

        // Validasi Input (Good Design Practice) [cite: 30]
        if (weightStr.isEmpty()) {
            etWeight.setError("Please enter gold weight");
            return;
        }
        if (valueStr.isEmpty()) {
            etValue.setError("Please enter current gold value");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double value = Double.parseDouble(valueStr);

        // Nilai Uruf X (Keep = 85g, Wear = 200g) [cite: 8, 16]
        double urufX = rbKeep.isChecked() ? 85.0 : 200.0;

        // 1. Jumlah Nilai Emas [cite: 11]
        double totalValue = weight * value;

        // 2. Nilai Emas yang Layak Zakat [cite: 12, 13]
        double weightMinusX = weight - urufX;
        double payableValue = (weightMinusX > 0) ? weightMinusX * value : 0.0;

        // 3. Jumlah Zakat (2.5%) [cite: 14, 15]
        double totalZakat = payableValue * 0.025;

        // Papar Output [cite: 10]
        tvTotalValue.setText(String.format("Total Gold Value: RM %.2f", totalValue));
        tvPayableValue.setText(String.format("Zakat Payable Value: RM %.2f", payableValue));
        tvTotalZakat.setText(String.format("Total Zakat: RM %.2f", totalZakat));

        Toast.makeText(this, "Calculation Successful", Toast.LENGTH_SHORT).show();
    }

    // --- Setup Butang Share di ActionBar  ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, 1, Menu.NONE, "Share App URL");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(android.R.drawable.ic_menu_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Gold Zakat Calculator App: " + githubUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}