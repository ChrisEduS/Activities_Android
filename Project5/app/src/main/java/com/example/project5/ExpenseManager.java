package com.example.project5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    private static ExpenseManager instance;
    private Map<String, List<Expense>> expensesHashMap;
    private List<Type> expenseTypes;

    private ExpenseManager() {
        expensesHashMap = new HashMap<>();
        expensesHashMap.put("Vivienda", new ArrayList<>(Arrays.asList(new Expense(new Type("Vivienda", 250.0), "Silla", 50.0,
                "12/4/2023"))));
        expensesHashMap.put("Alimentacion", new ArrayList<>(Arrays.asList(new Expense(new Type("Alimentacion", 200.0), "Cena", 25.0,
                "12/4/2023"))));
        expensesHashMap.put("Salud", new ArrayList<>(Arrays.asList(new Expense(new Type("Salud", 500.0), "Pastillas", 15.0,
                "13/4/2023"))));
        expensesHashMap.put("Educacion", new ArrayList<>(Arrays.asList(new Expense(new Type("Educacion", 300.0), "Libro", 55.0,
                "13/4/2023"))));
        expensesHashMap.put("Ropa", new ArrayList<>(Arrays.asList(new Expense(new Type("Ropa", 200.0), "Traje", 75.0,
                "14/4/2023"))));
        expenseTypes = new ArrayList<>();
        expenseTypes.add(new Type("Vivienda", 250.0));
        expenseTypes.add(new Type("Alimentacion", 200.0));
        expenseTypes.add(new Type("Salud", 500.0));
        expenseTypes.add(new Type("Educacion", 300.0));
        expenseTypes.add(new Type("Ropa", 200.0));
    }

    public static synchronized ExpenseManager getInstance() {
        if (instance == null) {
            instance = new ExpenseManager();
        }
        return instance;
    }

    public void addExpense(Expense expense){
        String key = expense.getType().getTypeName();
        if (expensesHashMap.containsKey(key)){
            expensesHashMap.get(key).add(expense);
        }
        else {
            expensesHashMap.put(key, new ArrayList<>());
            expensesHashMap.get(key).add(expense);
        }
    }
    public Map<String,List<Expense>> getExpenseHashMap(){return expensesHashMap;}

    public void addExpenseType(Type type) {
        expenseTypes.add(type);
    }

    public List<Type> getExpenseTypes() {
        return expenseTypes;
    }
}