package com.androidcalc.ertai87.androidcalc.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidcalc.ertai87.androidcalc.R;
import com.androidcalc.ertai87.androidcalc.common.CalcConstants;
import com.androidcalc.ertai87.androidcalc.model.BasicModel;
import com.androidcalc.ertai87.androidcalc.model.Model;

public class AndroidCalc extends AppCompatActivity {

    int barsize = 0;
    boolean op2; //true = 2nd op mode, false = basic op mode (for landscape-only ops)

    private DecimalFragment decfrag;

    private Model model;

    private TextView display;
    private Button mempush;
    private Button mempop;
    private Button memclr;
    private Button reset;
    private Button second;
    private Button sin;
    private Button cos;
    private Button tan;
    private Button log;
    private Button square;
    private Button sqrt;
    private Button neg;
    private RadioButton basicmode;
    private RadioButton rpnmode;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        barsize = CalcConstants.sizeMap.get(new Pair<Integer, Integer>(
            this.getResources().getConfiguration().orientation,
            this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK));
        if (barsize == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(AndroidCalc.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Error!");

            // Setting Dialog Message
            alertDialog.setMessage("barsize not set for this configuration!");

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which)
                {
                    System.exit(1);
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        //Using a full alternation rather than search-default pairs for readability of code
        if (savedInstanceState == null){
            model = new BasicModel();
        }else{
            //TODO: Fill this in
        }

        display = (TextView)findViewById(R.id.display);

        mempush = (Button)findViewById(R.id.mempush);
        mempush.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("mempush");
                //TODO: Fill this in
            }
        });

        mempop = (Button)findViewById(R.id.mempop);
        mempop.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("mempop");
                //TODO: Fill this in
            }
        });

        memclr = (Button)findViewById(R.id.memclear);
        memclr.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("memclear");
                //TODO: Fill this in
            }
        });

        reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("reset");
                model.resetCurrent();
                updateDisplay(model.getDisplayVal());
            }
        });

        decfrag = new DecimalFragment();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            neg = (Button)findViewById(R.id.neg);
            neg.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            square = (Button)findViewById(R.id.square);
            square.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            sqrt = (Button)findViewById(R.id.sqrt);
            sqrt.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            sin = (Button)findViewById(R.id.sin);
            sin.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            cos = (Button)findViewById(R.id.cos);
            cos.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            tan = (Button)findViewById(R.id.tan);
            tan.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            log = (Button)findViewById(R.id.log);
            log.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            second = (Button)findViewById(R.id.second);
            second.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    //TODO: Fill this in
                }
            });

            op2 = false;
        }
        updateDisplay("0");
    }

    @Override
    public void onStart(){
        super.onStart();
        switchBase(10);
    }

    public void switchBase(int base){
        switch (base){
            case 10:
                getSupportFragmentManager().beginTransaction().replace(R.id.root, decfrag).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
        }
        if (base >= 10){
            ((Button)findViewById(R.id.Num0)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("0");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num1)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("1");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num2)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("2");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num3)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("3");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num4)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("4");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num5)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("5");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num6)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("6");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num7)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("7");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num8)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("8");
                    updateDisplay(model.getDisplayVal());
                }
            });
            ((Button)findViewById(R.id.Num9)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.inputNum("9");
                    updateDisplay(model.getDisplayVal());
                }
            });
        }
        ((Button)findViewById(R.id.plus)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.add();
                updateDisplay(model.getDisplayVal());
            }
        });
        ((Button)findViewById(R.id.minus)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.subtract();
                updateDisplay(model.getDisplayVal());            }
        });
        ((Button)findViewById(R.id.mult)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.multiply();
                updateDisplay(model.getDisplayVal());
            }
        });
        ((Button)findViewById(R.id.div)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.divide();
                    updateDisplay(model.getDisplayVal());
                }catch (ArithmeticException e){
                    final AlertDialog alertDialog = new AlertDialog.Builder(AndroidCalc.this).create();
                    alertDialog.setTitle("Division by zero!");
                    alertDialog.setMessage("Division by zero not yet supported by the laws of mathematics.  Please try again on a future update.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        ((Button)findViewById(R.id.point)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.inputPoint();
                updateDisplay(model.getDisplayVal());
            }
        });
        ((Button)findViewById(R.id.equals)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.eval();
                updateDisplay(model.getDisplayVal());
            }
        });
        updateDisplay(model.getDisplayVal());
    }

    //when num or memory is updated, updates the screen
    public void updateDisplay(String todisplay){
        if (todisplay.length() > barsize){
            //if num doesn't fit into the display text box
            int i = 0;
            for (;i < todisplay.length() && todisplay.charAt(i) != '.'; i++);
            if (i <= barsize){
                //if the fractional part causes the overflow, truncate the fractional part
                todisplay = todisplay.substring(0, barsize);
            }else{
                //if the integer part causes the overflow, move into scientific notation
                todisplay = todisplay.substring(0, i);
                String head = todisplay.charAt(0) + ".";
                String tail = todisplay.substring(1, i);
                String exponent = "E" + (todisplay.length() - 1);
                todisplay = (head + tail).substring(0, barsize - exponent.length()) + exponent;
            }
        }
        display.setText(todisplay);
    }
}
