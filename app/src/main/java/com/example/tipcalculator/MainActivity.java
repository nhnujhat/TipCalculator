package com.example.tipcalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] person = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    Double tipamnt, totalamnt;
    double per_person_bill;
    DecimalFormat df;
    EditText amount;
    TextView tip, total, perperson;
    double no_of_person = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = findViewById(R.id.amount);
        tip = findViewById(R.id.tip);
        total = findViewById(R.id.total);
        TextView percentage = findViewById(R.id.percentage);
        SeekBar seekBar = findViewById(R.id.seekBar);
        Spinner spin = findViewById(R.id.person);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, person);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);

        df = new DecimalFormat("0.00");

        perperson = findViewById(R.id.perperson);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>0&&Double.parseDouble(amount.getText().toString())>0) {
                    percentage.setText(progress + "%");

                    tipamnt = (Double.parseDouble(amount.getText().toString()) * progress / 100);
                    totalamnt = Double.parseDouble(amount.getText().toString()) + tipamnt;
                    per_person_bill = totalamnt;

                    tip.setText("$" + df.format(tipamnt));
                    total.setText("$" + df.format(totalamnt));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
                if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("sms_body", "Bill: $" + amount.getText().toString() + " Tip: " + tip.getText().toString() + " Total: " + total.getText().toString() + " Per person bill: " + perperson.getText().toString());
                startActivity(intent);
                return true;
            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("The spinner is used to split the total amount");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(totalamnt!=null) {
            no_of_person = Double.parseDouble(adapterView.getItemAtPosition(i).toString());
            per_person_bill = totalamnt / no_of_person;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_no:
                if (checked)
                    if(totalamnt>0) perperson.setText("$" + df.format(per_person_bill));
                    break;
            case R.id.radio_tip:
                if (checked) {
                    if(totalamnt>0) {
                        double temp = totalamnt - tipamnt + Math.ceil(tipamnt);
                        perperson.setText("$" + df.format(temp / no_of_person));
                    }
                    break;
                }
            case R.id.radio_total:
                if (checked&&totalamnt>0)
                    if(totalamnt>0) perperson.setText("$" + Math.ceil(per_person_bill));
                    break;
        }
    }
}