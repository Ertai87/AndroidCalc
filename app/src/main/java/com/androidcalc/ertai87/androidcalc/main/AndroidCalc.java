package com.androidcalc.ertai87.androidcalc.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidcalc.ertai87.androidcalc.R;
import com.androidcalc.ertai87.androidcalc.common.CalcConstants;
import com.androidcalc.ertai87.androidcalc.model.BasicModel;
import com.androidcalc.ertai87.androidcalc.model.Model;

public class AndroidCalc extends AppCompatActivity {

    int barsize = 0;
    boolean rpn = false;
    boolean op2; //true = 2nd op mode, false = basic op mode (for landscape-only ops)
    private final AndroidCalc context = this;

    private BinaryFragment binfrag;
    private OctalFragment octfrag;
    private DecimalFragment decfrag;
    private HexadecimalFragment hexfrag;

    private Model model;

    private TextView display;
    private Button mempush;
    private Button mempop;
    private Button memclr;
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
                model.memPush();
                Toast.makeText(context, "Memory Pushed", Toast.LENGTH_SHORT).show();
                updateDisplay(model.getDisplayVal());
            }
        });

        mempop = (Button)findViewById(R.id.mempop);
        mempop.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (model.memPop()) {
                    Toast.makeText(context, "Memory Popped", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Memory Is Empty", Toast.LENGTH_SHORT).show();
                }
                updateDisplay(model.getDisplayVal());
            }
        });

        memclr = (Button)findViewById(R.id.memclear);
        memclr.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                model.memClear();
                Toast.makeText(context, "Memory Cleared", Toast.LENGTH_SHORT).show();
                updateDisplay(model.getDisplayVal());
            }
        });

        ((Button)findViewById(R.id.reset)).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                model.resetCurrent();
                updateDisplay(model.getDisplayVal());
            }
        });

        binfrag = new BinaryFragment();
        octfrag = new OctalFragment();
        decfrag = new DecimalFragment();
        hexfrag = new HexadecimalFragment();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calc_menu, menu);
        if (rpn){
            menu.findItem(R.id.rpn).setChecked(true);
        }
        switch (model.getBase()){
            case 2:
                menu.findItem(R.id.bin).setChecked(true);
                break;
            case 8:
                menu.findItem(R.id.oct).setChecked(true);
                break;
            case 16:
                menu.findItem(R.id.hex).setChecked(true);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            /*
            TODO: RPN stuff
            case R.id.basic:
                switchRPNMode(false);
                item.setChecked(true);
                return true;
            case R.id.rpn:
                switchRPNMode(true);
                item.setChecked(true);
                return true;
                */
            case R.id.dec:
                switchBase(10);
                item.setChecked(true);
                return true;
            case R.id.bin:
                switchBase(2);
                item.setChecked(true);
                return true;
            case R.id.oct:
                switchBase(8);
                item.setChecked(true);
                return true;
            case R.id.hex:
                switchBase(16);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchBase(int newBase){
        model.setBase(newBase);
        switch (model.getBase()){
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.root, binfrag).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case 8:
                getSupportFragmentManager().beginTransaction().replace(R.id.root, octfrag).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case 10:
                getSupportFragmentManager().beginTransaction().replace(R.id.root, decfrag).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case 16:
                getSupportFragmentManager().beginTransaction().replace(R.id.root, hexfrag).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
        }
        if (model.getBase() >= 2){
            ((Button)findViewById(R.id.Num0)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNum('0');
                }
            });
            ((Button)findViewById(R.id.Num1)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNum('1');
                }
            });
            if (model.getBase() >= 8) {
                ((Button) findViewById(R.id.Num2)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('2');
                    }
                });
                ((Button) findViewById(R.id.Num3)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('3');
                    }
                });
                ((Button) findViewById(R.id.Num4)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('4');
                    }
                });
                ((Button) findViewById(R.id.Num5)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('5');
                    }
                });
                ((Button) findViewById(R.id.Num6)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('6');
                    }
                });
                ((Button) findViewById(R.id.Num7)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('7');
                    }
                });
                if (model.getBase() >= 10) {
                    ((Button) findViewById(R.id.Num8)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputNum('8');
                        }
                    });
                    ((Button) findViewById(R.id.Num9)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputNum('9');
                        }
                    });
                    if (model.getBase() >= 16){
                        ((Button) findViewById(R.id.NumA)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('A');
                            }
                        });
                        ((Button) findViewById(R.id.NumB)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('B');
                            }
                        });
                        ((Button) findViewById(R.id.NumC)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('C');
                            }
                        });
                        ((Button) findViewById(R.id.NumD)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('D');
                            }
                        });
                        ((Button) findViewById(R.id.NumE)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('E');
                            }
                        });
                        ((Button) findViewById(R.id.NumF)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('F');
                            }
                        });
                    }
                }
            }
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
                updateDisplay(model.getDisplayVal());
            }
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
        mempush.setText("MEMPUSH (" + model.getMemSize() + ")");
        mempop.setText("MEMPOP (" + model.getMemSize() + ")");
        display.setText(todisplay);
    }

    private void inputNum(char num){
        if(display.getText().length() < barsize) {
            model.inputNum("" + num);
            updateDisplay(model.getDisplayVal());
        }
    }

}
