package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ExpenseManager myExpenseManager= ExpenseManager.getInstance();
    private Calendar selectedDate;
    int year;
    int month;
    int day;
    private DatePickerDialog datePickerDialog;
    private TextView dateTextView;
    private Spinner typeSpinner;
    private EditText detailEditText;
    private EditText valueEditText;
    private TextView maxTextView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        dateTextView = (TextView) findViewById(R.id.date_textView);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        detailEditText = (EditText)findViewById(R.id.details_edittext);
        valueEditText = (EditText)findViewById(R.id.value_edittext);
        maxTextView = (TextView) findViewById(R.id.max_text_view);

        selectedDate = Calendar.getInstance();

        year = selectedDate.get(Calendar.YEAR);
        month = selectedDate.get(Calendar.MONTH);
        day = selectedDate.get(Calendar.DAY_OF_MONTH);
        dateTextView.setText(day + "/" + (month + 1) + "/" + year);

        Spinner typeSpinner = findViewById(R.id.type_spinner);
        update_adapter();

        typeSpinner.setSelection(0);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Update the TextView with the selected item
                for (Type type: myExpenseManager.getExpenseTypes()){
                    if (type.getTypeName().equals(typeSpinner.getSelectedItem().toString())){
                        maxTextView.setText("<= "+String.valueOf(type.getMaxVal())+"$");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void calendar_pressed(View view) {
        selectedDate = Calendar.getInstance();
        year = selectedDate.get(Calendar.YEAR);
        month = selectedDate.get(Calendar.MONTH);
        day = selectedDate.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int moth, int day) {
                        dateTextView.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
        }

        public void add_btn_pressed(View view){
            try {
                String str_date = (String) dateTextView.getText();
                String type = typeSpinner.getSelectedItem().toString();
                String detail = detailEditText.getText().toString();
                Double value = Double.parseDouble(valueEditText.getText().toString());

                if(detail.equals("")) {
                    throw new Exception("1");
                }
                boolean added = false;
                for(Type t:myExpenseManager.getExpenseTypes()){
                    if(value<=(t.getMaxVal()) && t.getTypeName().equals(type)){
                        Expense myExpense = new Expense(t,detail,value,str_date);
                        myExpenseManager.addExpense(myExpense);
                        added = true;
                        break;
                    }
                }
                if (added) {
                    Toast toast = Toast.makeText(this, "Gasto Ingresado Correctamente.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(this, "Se ha excedido del limite.", Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (Exception ex) {
                if (ex.getMessage().equals("1")) {
                    Toast toast = Toast.makeText(this, "Falta ingresar detalle.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(this, "Error: Valor menor o igual cero / Vacio.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            detailEditText.setText("");
            valueEditText.setText("");
        }
        public void view_pressed(View view){
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            startActivity(intent);
        }
        public void modify_pressed(View view){
            Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
            startActivity(intent);
        }
        @Override
        protected void onRestart() {
            super.onRestart();
            typeSpinner.setSelection(1);
            update_adapter();
        }
    public void update_adapter(){
        List<String> expenseTypes = myExpenseManager.getExpenseTypes().stream().map(x->x.getTypeName()).collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }
}