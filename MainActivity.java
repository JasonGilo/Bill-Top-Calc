package com.gethirednow.tip;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    private EditText billValue;
    private TextView tipValue;
    private TextView totalValue;
    private TextView tipPercentageValue;
    private SeekBar seekTipPercentValue;
    private String current = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        billValue = (EditText) findViewById(R.id.bill);
        tipValue = (TextView) findViewById(R.id.tip);
        totalValue = (TextView) findViewById(R.id.total);
        tipPercentageValue = (TextView) findViewById(R.id.tipPercentage);
        seekTipPercentValue = (SeekBar) findViewById(R.id.seekTipPercent);

        if (billValue.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        billValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        billValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        billValue.setKeyListener(DigitsKeyListener.getInstance(false, true));

        seekTipPercentValue.setMax(30);
        seekTipPercentValue.setProgress(15);
        tipPercentageValue.setText(String.valueOf(seekTipPercentValue.getProgress()));

        if (!billValue.getText().toString().equals("")) {
            calculateTip();
        }

        billValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    billValue.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                    current = formatted;
                    billValue.setText(formatted);
                    billValue.setSelection(formatted.length());
                    billValue.addTextChangedListener(this);
                }
            }
        });

        seekTipPercentValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tipPercentageValue.setText(String.valueOf(progress));
                if (!billValue.getText().toString().equals("")) {
                    calculateTip();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        billValue.addTextChangedListener(new TextWatcher() {

        @Override
        public void afterTextChanged (Editable s){
        }

        @Override
        public void beforeTextChanged (CharSequence s,int start,
        int count, int after){
        }

        @Override
        public void onTextChanged (CharSequence s,int start,
        int before, int count){
            calculateTip();
        }
    });
}



    private void calculateTip(){
        String cleanString = billValue.getText().toString().replaceAll("[$,]", "");
        double billNum = Double.parseDouble(cleanString);
        double tip = billNum*.01*seekTipPercentValue.getProgress();
        double total = billNum+tip;
        String formatTip = NumberFormat.getCurrencyInstance().format((tip));
        tipValue.setText(formatTip);
        String formatTotal = NumberFormat.getCurrencyInstance().format((total));
        totalValue.setText(formatTotal);
    }

}
