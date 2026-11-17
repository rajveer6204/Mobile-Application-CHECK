package com.example.hello.demos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.example.hello.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private TextView tvExpression;
    private TextView tvResult;

    private String currentExpression = "";
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Calculator Demo");
        toolbar.setNavigationOnClickListener(v -> finish());

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        setupDigitButtons();
        setupOperatorButtons();
        setupActionButtons();
    }

    private void setupDigitButtons() {
        int[] digitIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9, R.id.btnDot
        };

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            String text = b.getText().toString();

            if (text.equals(".")) {
                if (canAppendDot()) {
                    currentExpression += ".";
                }
            } else {
                currentExpression += text;
            }

            updateDisplay();
        };

        for (int id : digitIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setupOperatorButtons() {
        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            String op = b.getText().toString();

            if (currentExpression.isEmpty()) {
                if (op.equals("-") || op.equals("−")) {
                    currentExpression += "−";
                }
                updateDisplay();
                return;
            }

            char lastChar = currentExpression.charAt(currentExpression.length() - 1);
            if (isOperator(lastChar)) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1) + op;
            } else {
                currentExpression += op;
            }

            updateDisplay();
        };

        findViewById(R.id.btnPlus).setOnClickListener(listener);
        findViewById(R.id.btnMinus).setOnClickListener(listener);
        findViewById(R.id.btnMultiply).setOnClickListener(listener);
        findViewById(R.id.btnDivide).setOnClickListener(listener);
        findViewById(R.id.btnPercent).setOnClickListener(listener);
    }

    private void setupActionButtons() {
        findViewById(R.id.btnAC).setOnClickListener(v -> {
            currentExpression = "";
            tvExpression.setText("");
            tvResult.setText("0");
        });

        findViewById(R.id.btnDel).setOnClickListener(v -> {
            if (!currentExpression.isEmpty()) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                updateDisplay();
            }
        });

        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> toggleSign());

        findViewById(R.id.btnEqual).setOnClickListener(v -> calculateFinalResult());
    }

    private void updateDisplay() {
        tvExpression.setText(currentExpression);

        if (currentExpression.isEmpty()) {
            tvResult.setText("0");
            return;
        }

        String liveEval = evaluateExpression(currentExpression);
        if (!liveEval.startsWith("Cannot") && !liveEval.startsWith("Error")) {
            tvResult.setText(liveEval);
        }
    }

    private void calculateFinalResult() {
        if (currentExpression.isEmpty()) return;

        String finalResult = evaluateExpression(currentExpression);
        if (!finalResult.startsWith("Cannot") && !finalResult.startsWith("Error")) {
            currentExpression = finalResult.replace(",", "");
            tvExpression.setText("");
            tvResult.setText(finalResult);
        } else {
            tvResult.setText(finalResult);
        }
    }

    private void toggleSign() {
        if (currentExpression.isEmpty()) return;

        if (currentExpression.startsWith("−") || currentExpression.startsWith("-")) {
            currentExpression = currentExpression.substring(1);
        } else {
            currentExpression = "−" + currentExpression;
        }
        updateDisplay();
    }

    private boolean canAppendDot() {
        if (currentExpression.isEmpty()) return true;

        for (int i = currentExpression.length() - 1; i >= 0; i--) {
            char c = currentExpression.charAt(i);
            if (c == '.') return false;
            if (isOperator(c)) return true;
        }
        return true;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '−' || c == '×' || c == '*' || c == '÷' || c == '/' || c == '%';
    }

    private String evaluateExpression(String input) {
        try {
            String expr = input.replace("×", "*").replace("÷", "/").replace("−", "-");
            if (expr.endsWith("+") || expr.endsWith("-") || expr.endsWith("*") || expr.endsWith("/")) {
                expr = expr.substring(0, expr.length() - 1);
            }
            if (expr.isEmpty()) return "0";

            List<String> tokens = tokenize(expr);
            if (tokens.isEmpty()) return "0";

            double result = parseTokens(tokens);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "Cannot divide by 0";
            }

            return decimalFormat.format(result);
        } catch (ArithmeticException ae) {
            return "Cannot divide by 0";
        } catch (Exception e) {
            return "Format Error";
        }
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numberBuffer.append(c);
            } else if (c == '-' && (i == 0 || isOperator(expr.charAt(i - 1)))) {
                numberBuffer.append(c);
            } else if (isOperator(c)) {
                if (numberBuffer.length() > 0) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }

        if (numberBuffer.length() > 0) {
            tokens.add(numberBuffer.toString());
        }

        return tokens;
    }

    private double parseTokens(List<String> tokens) {
        List<String> pass1 = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            if (token.equals("*") || token.equals("/") || token.equals("%")) {
                if (pass1.isEmpty()) throw new IllegalArgumentException("Invalid format");
                double prev = Double.parseDouble(pass1.remove(pass1.size() - 1));

                if (token.equals("%")) {
                    pass1.add(String.valueOf(prev / 100.0));
                    i++;
                    continue;
                }

                if (i + 1 >= tokens.size()) throw new IllegalArgumentException("Incomplete expression");
                double next = Double.parseDouble(tokens.get(i + 1));

                if (token.equals("*")) {
                    pass1.add(String.valueOf(prev * next));
                } else {
                    if (next == 0) throw new ArithmeticException("Division by zero");
                    pass1.add(String.valueOf(prev / next));
                }
                i += 2;
            } else {
                pass1.add(token);
                i++;
            }
        }

        if (pass1.isEmpty()) return 0.0;
        double result = Double.parseDouble(pass1.get(0));

        int j = 1;
        while (j < pass1.size()) {
            String op = pass1.get(j);
            if (j + 1 >= pass1.size()) break;
            double nextVal = Double.parseDouble(pass1.get(j + 1));

            if (op.equals("+")) {
                result += nextVal;
            } else if (op.equals("-")) {
                result -= nextVal;
            }
            j += 2;
        }

        return result;
    }
}
