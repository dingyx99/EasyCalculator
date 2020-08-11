package me.dingyx99.easycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.TypeEvaluator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    //For calculation
    private double num1, num2, res;
    private String show;
    private enum State {STATE_INIT, STATE_I1, STATE_I2, STATE_R};
    private State S;
    private enum Operator {OP_ADD, OP_MINUS, OP_MUL, OP_DIV, OP_PER,OP_NONE};
    private Operator O = Operator.OP_NONE;
    private int point_count;

   //For display
    private TextView tv_display;
    private Button bt_0, bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9;
    private Button bt_add, bt_minus, bt_multiply, bt_divide, bt_percent, bt_equal;
    private Button bt_point, bt_reverse, bt_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        //ClearListener
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_init();
                tv_display.setText(show);
            }
        });
        //NumberListener
        Button.OnClickListener num_proc = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (S) {
                    case STATE_I1:
                    case STATE_I2:
                        show += (String)v.getTag();
                        break;
                    case STATE_INIT:
                        show = (String)v.getTag();
                        S = State.STATE_I1;
                        O = Operator.OP_NONE;
                        break;
                    case STATE_R:
                        set_init();
                        show = (String)v.getTag();
                        S = State.STATE_I1;
                        O = Operator.OP_NONE;
                        break;
                    default:
                        //You can add a error message here.
                        return;
                }
                tv_display.setText(show);
            }
        };
        //OperatorListener
        Button.OnClickListener op_proc = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (S) {
                    case STATE_I1:
                        num1 = Double.valueOf(show);
                        set_i2((Operator)v.getTag());
                        break;
                    case STATE_I2:
                    case STATE_INIT:
                        break;
                    case STATE_R:
                        num1 = res;
                        set_i2((Operator)v.getTag());
                        break;
                    default:
                        return ;
                }
            }
        };

        bt_0.setOnClickListener(num_proc);
        bt_1.setOnClickListener(num_proc);
        bt_2.setOnClickListener(num_proc);
        bt_3.setOnClickListener(num_proc);
        bt_4.setOnClickListener(num_proc);
        bt_5.setOnClickListener(num_proc);
        bt_6.setOnClickListener(num_proc);
        bt_7.setOnClickListener(num_proc);
        bt_8.setOnClickListener(num_proc);
        bt_9.setOnClickListener(num_proc);
        bt_add.setOnClickListener(op_proc);
        bt_minus.setOnClickListener(op_proc);
        bt_multiply.setOnClickListener(op_proc);
        bt_divide.setOnClickListener(op_proc);
        bt_percent.setOnClickListener(op_proc);

        bt_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (S) {
                    case STATE_I1:
                        if(point_count == 0) {
                            show += ".";
                            point_count++;
                        }
                        point_count = 0;
                        break;
                    case STATE_I2:
                        if(point_count == 0) {
                            show += ".";
                            point_count++;
                        }
                        break;
                    case STATE_INIT:
                        show += ".";
                        point_count++;
                        S = State.STATE_I1;
                        O = Operator.OP_NONE;
                        break;
                    case STATE_R:
                        break;
                    default:
                        return ;
                }
                tv_display.setText(show);
            }
        });

        bt_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (S) {
                    case STATE_I1:
                    case STATE_I2:
                        reverse();
                        break;
                    case STATE_INIT:
                    case STATE_R:
                        break;
                    default:
                        return ;
                }
                tv_display.setText(show);
            }
        });

        bt_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S == State.STATE_I2) {
                    num2 = Double.valueOf(show);
                    switch (O) {
                        case OP_NONE:
                            return ;
                        case OP_ADD:
                            res = num1 + num2;
                            break;
                        case OP_MINUS:
                            res = num1 - num2;
                            break;
                        case OP_MUL:
                            res = num1 * num2;
                            break;
                        case OP_DIV:
                            if(num2 != 0) {
                                res = num1 / num2;
                            }
                            else {
                                raiseToast();
                            }
                            break;
                        case OP_PER:
                            if(num2 != 0) {
                                res = num1 % num2;
                            }
                            else {
                                raiseToast();
                            }
                            break;
                    }
                    set_R(res);
                    BigDecimal result = new BigDecimal(res);
                    show = String.valueOf(result.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue());
                    tv_display.setText(show);
                }
            }
        });

        set_init();
        tv_display.setText(show);
    }

    private void bindView() {
        //Join variables with layout
        tv_display = (TextView)findViewById(R.id.result);
        bt_0 = (Button)findViewById(R.id.zero);
        bt_0.setTag("0");
        bt_1 = (Button)findViewById(R.id.one);
        bt_1.setTag("1");
        bt_2 = (Button)findViewById(R.id.two);
        bt_2.setTag("2");
        bt_3 = (Button)findViewById(R.id.three);
        bt_3.setTag("3");
        bt_4 = (Button)findViewById(R.id.four);
        bt_4.setTag("4");
        bt_5 = (Button)findViewById(R.id.five);
        bt_5.setTag("5");
        bt_6 = (Button)findViewById(R.id.six);
        bt_6.setTag("6");
        bt_7 = (Button)findViewById(R.id.seven);
        bt_7.setTag("7");
        bt_8 = (Button)findViewById(R.id.eight);
        bt_8.setTag("8");
        bt_9 = (Button)findViewById(R.id.nine);
        bt_9.setTag("9");
        bt_add = (Button)findViewById(R.id.plus);
        bt_add.setTag(Operator.OP_ADD);
        bt_minus = (Button)findViewById(R.id.minus);
        bt_minus.setTag(Operator.OP_MINUS);
        bt_multiply = (Button)findViewById(R.id.multiply);
        bt_multiply.setTag(Operator.OP_MUL);
        bt_divide = (Button)findViewById(R.id.div);
        bt_divide.setTag(Operator.OP_DIV);
        bt_percent = (Button)findViewById(R.id.percent);
        bt_percent.setTag(Operator.OP_PER);
        bt_equal = (Button)findViewById(R.id.equal);
        bt_point = (Button)findViewById(R.id.point);
//        bt_point.setTag(".");
        bt_reverse = (Button)findViewById(R.id.reverse);
        bt_clear = (Button)findViewById(R.id.clear);
    }

    private void set_init() {
        S = State.STATE_INIT;
        O = Operator.OP_NONE;
        show = "0";
        num1 = 0;
        num2 = 0;
        res = 0;
        point_count = 0;
    }

    private void set_i1() {
        S = State.STATE_I1;
        O = Operator.OP_NONE;
    }

    private void set_i2(Operator op) {
        S = State.STATE_I2;
        O = op;
        show = "";
    }

    private void set_R(double r) {
        S = State.STATE_R;
        O = Operator.OP_NONE;
        res = r;
    }

    private void reverse() {
        StringBuilder s = new StringBuilder(show);
        if(show.charAt(0) == '-') {
            show = show.substring(1, show.length());
        }
        else if (show.charAt(0) != '-'){
            s.insert(0,"-");
            show = s.toString();
        }
    }

    private void raiseToast() {
        Toast.makeText(this,"除数不可为0", Toast.LENGTH_SHORT).show();
        set_init();
    }
}
