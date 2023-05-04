package com.example.project5;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewActivity extends AppCompatActivity {
    private ExpenseManager myExpenseManager = ExpenseManager.getInstance();
    private Calendar selectedDate;
    String flag;
    int year;
    int month;
    int day;
    private DatePickerDialog datePickerDialog;
    private TextView dateTextView;
    private Spinner typeSpinner;
    private Spinner filterSpinner;
    private Button pickDateButton;
    private EditText lowRangeEditText;
    private EditText highRangeEditText;
    private Button searchButton;
    private ListView expensesListView;
    private TextView totalExpenses;
    private Button deleteButton;
    private ArrayAdapter<String> adapter;
    String itemToDelete;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        dateTextView = (TextView)findViewById(R.id.date_textview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        filterSpinner = findViewById(R.id.filter_spinner);
        pickDateButton = findViewById(R.id.VAcalendar_picker_btn);
        lowRangeEditText = findViewById(R.id.low_edittext);
        highRangeEditText = findViewById(R.id.high_edittext);
        searchButton = findViewById(R.id.searchBtn);
        expensesListView = findViewById(R.id.expenseListView);
        totalExpenses = findViewById(R.id.totalExpenseTextView);
        deleteButton = findViewById(R.id.delete_button);

        selectedDate = Calendar.getInstance();

        year = selectedDate.get(Calendar.YEAR);
        month = selectedDate.get(Calendar.MONTH);
        day = selectedDate.get(Calendar.DAY_OF_MONTH);
        dateTextView.setText(day + "/" + (month + 1) + "/" + year);

        typeSpinner.setEnabled(false);

        List<String> expenseTypes = myExpenseManager.getExpenseTypes().stream().map(x->x.getTypeName()).collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                itemToDelete = expensesListView.getItemAtPosition(i).toString();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void va_calendar_pressed(View view){
        selectedDate = Calendar.getInstance();
        year = selectedDate.get(Calendar.YEAR);
        month = selectedDate.get(Calendar.MONTH);
        day = selectedDate.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(ViewActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int moth, int day) {
                        dateTextView.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void filter_type_pressed(View view){
        String filter_opt = filterSpinner.getSelectedItem().toString();
        if (filter_opt.equals("Fecha")){
            pickDateButton.setEnabled(true);
            searchButton.setEnabled(true);

            typeSpinner.setEnabled(false);
            lowRangeEditText.setEnabled(false);
            highRangeEditText.setEnabled(false);

            flag = "Fecha";
        }
        else if (filter_opt.equals("Tipo")) {
            typeSpinner.setEnabled(true);
            searchButton.setEnabled(true);

            pickDateButton.setEnabled(false);
            lowRangeEditText.setEnabled(false);
            highRangeEditText.setEnabled(false);

            flag = "Tipo";
        }
        else {
            lowRangeEditText.setEnabled(true);
            highRangeEditText.setEnabled(true);
            searchButton.setEnabled(true);

            typeSpinner.setEnabled(false);
            pickDateButton.setEnabled(false);

            flag = "Rango";
        }
    }
    public void search_pressed(View view){
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        HashMap<String, List<Expense>> hashMap = (HashMap<String, List<Expense>>) myExpenseManager.getExpenseHashMap();
        Double total = 0.0;

        if (flag.equals("Fecha")){
            // Parse the selected date from the DatePickerDialog

            ArrayList<Expense> allExpenses = (ArrayList<Expense>) hashMap.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            filteredExpenses = (ArrayList<Expense>) allExpenses.stream().filter(e -> e.getDate().equals(dateTextView.getText().toString())).
                    collect(Collectors.toList());

            if (filteredExpenses.size() == 0){
                Toast toast = Toast.makeText(this, "No se encontro gastos coincidentes.", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        else if (flag.equals("Tipo")){

            String selectedType = typeSpinner.getSelectedItem().toString();
            filteredExpenses = (ArrayList<Expense>) hashMap.get(selectedType);


            if (filteredExpenses.size() == 0){
                Toast toast = Toast.makeText(this, "No se encontro gastos coincidentes.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else {
            double minVal = Double.parseDouble(String.valueOf(lowRangeEditText.getText()));
            double maxVal = Double.parseDouble(String.valueOf(highRangeEditText.getText()));
            ArrayList<Expense> allExpenses = (ArrayList<Expense>) hashMap.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            filteredExpenses = (ArrayList<Expense>) allExpenses.stream().
                    filter(e -> e.getAmount()>=minVal&&e.getAmount()<=maxVal).collect(Collectors.toList());
            if (filteredExpenses.size() == 0){
                Toast toast = Toast.makeText(this, "No se encontro gastos coincidentes.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        for(Expense myExpense:filteredExpenses){
            total+=myExpense.getAmount();
        }
        totalExpenses.setText("$"+total);
        ArrayAdapter<Expense> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredExpenses);
        expensesListView.setAdapter(adapter);

        highRangeEditText.setText("");
        lowRangeEditText.setText("");
    }

    public void delete_pressed(View view){
        String[] splitedItem = itemToDelete.split("-");
        String type = splitedItem[0];
        String detail = splitedItem[1];
        String date = splitedItem[2];
        String amount = splitedItem[3];
        List<Expense> toDeleteExpenses = myExpenseManager.getExpenseHashMap().get(type);
        for (int i = 0; i<toDeleteExpenses.size();i++){
            if (detail.equals(toDeleteExpenses.get(i).getDetail()) &&
                    date.equals(toDeleteExpenses.get(i).getDate()) &&
                    amount.equals(String.valueOf(toDeleteExpenses.get(i).getAmount()))){
                myExpenseManager.getExpenseHashMap().get(type).remove(i);
                Toast toast = Toast.makeText(this, "Eliminado correctamente.", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
        }
        search_pressed(view);
    }
}
