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
import com.androidcalc.ertai87.androidcalc.model.RPNModel;

public class AndroidCalc extends AppCompatActivity {

    int barsize = 0;
    boolean rpn = false;
    boolean op2 = false; //true = 2nd op mode, false = basic op mode (for landscape-only ops)
    private final AndroidCalc context = this;

    private BinaryFragment binfrag;
    private OctalFragment octfrag;
    private DecimalFragment decfrag;
    private HexadecimalFragment hexfrag;

    private BasicOpsFragment basicfrag;
    private SecondOpsFragment secfrag;

    private Model model;

    private TextView display;
    private Button mempush;
    private Button mempop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        barsize = CalcConstants.sizeMap.get(new Pair<>(
                this.getResources().getConfiguration().orientation,
                this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK));
        if (barsize == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Error!");
            alertDialog.setMessage("barsize not set for this configuration!");

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    System.exit(1);
                }
            });

            alertDialog.show();
        }

        if (savedInstanceState == null) {
            model = new BasicModel();
        } else {
            model = (Model) savedInstanceState.getSerializable("model");
            op2 = savedInstanceState.getBoolean("op2");
            rpn = savedInstanceState.getBoolean("rpn");
        }

        display = (TextView) findViewById(R.id.display);

        mempush = (Button) findViewById(R.id.mempush);
        mempush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.memPush();
                Toast.makeText(context, "Memory Pushed", Toast.LENGTH_SHORT).show();
                updateDisplay(model.getDisplayVal());
            }
        });

        mempop = (Button) findViewById(R.id.mempop);
        mempop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.memPop()) {
                    Toast.makeText(context, "Memory Popped", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Memory Is Empty", Toast.LENGTH_SHORT).show();
                }
                updateDisplay(model.getDisplayVal());
            }
        });

        (findViewById(R.id.memclear)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.memClear();
                Toast.makeText(context, "Memory Cleared", Toast.LENGTH_SHORT).show();
                updateDisplay(model.getDisplayVal());
            }
        });

        (findViewById(R.id.reset)).setOnClickListener(new OnClickListener() {
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

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            basicfrag = new BasicOpsFragment();
            secfrag = new SecondOpsFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        switchBase(model.getBase());
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchOpsMode(op2);
        }
        updateDisplay("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calc_menu, menu);
        if (rpn) {
            menu.findItem(R.id.rpn).setChecked(true);
        }
        switch (model.getBase()) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.basic:
                switchRPNMode(false);
                item.setChecked(true);
                return true;
            case R.id.rpn:
                switchRPNMode(true);
                item.setChecked(true);
                return true;
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

    private void switchRPNMode(boolean r) {
        if (r == rpn) return; else rpn = r;
        if (r == true) {
            model = new RPNModel((BasicModel)model);
        }else{
            model = new BasicModel((RPNModel)model);
        }
        updateDisplay(model.getDisplayVal());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("model", model);
        outState.putBoolean("op2", op2);
        outState.putBoolean("rpn", rpn);
        super.onSaveInstanceState(outState);
    }

    public void switchOpsMode(boolean mode){
        op2 = mode;
        if (!op2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.opsroot, basicfrag).commit();
            getSupportFragmentManager().executePendingTransactions();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.opsroot, secfrag).commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        (findViewById(R.id.neg)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.inputNeg();
                updateDisplay(model.getDisplayVal());
            }
        });

        (findViewById(R.id.square)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.doUnaryOp('^');
                updateDisplay(model.getDisplayVal());
            }
        });

        (findViewById(R.id.sqrt)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.doUnaryOp('/');
                updateDisplay(model.getDisplayVal());
            }
        });

        if (!op2){
            (findViewById(R.id.sin)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('s');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.cos)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('c');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.tan)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('t');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.log)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        model.doUnaryOp('l');
                        updateDisplay(model.getDisplayVal());
                    }catch(NumberFormatException e){
                        if (e.getMessage().contains("Infinity or NaN")){
                            handleInvalidLog();
                        }else throw e;
                    }

                }
            });

            (findViewById(R.id.second)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchOpsMode(true);
                }
            });
        }else{
            (findViewById(R.id.sininv)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('d');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.cosinv)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('v');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.taninv)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp('y');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.ex)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.doUnaryOp(';');
                    updateDisplay(model.getDisplayVal());
                }
            });

            (findViewById(R.id.second)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchOpsMode(false);
                }
            });
        }
    }

    public void switchBase(int newBase) {
        model.setBase(newBase);
        switch (model.getBase()) {
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
        if (model.getBase() >= 2) {
            (findViewById(R.id.Num0)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNum('0');
                }
            });
            (findViewById(R.id.Num1)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNum('1');
                }
            });
            if (model.getBase() >= 8) {
                (findViewById(R.id.Num2)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('2');
                    }
                });
                (findViewById(R.id.Num3)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('3');
                    }
                });
                (findViewById(R.id.Num4)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('4');
                    }
                });
                (findViewById(R.id.Num5)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('5');
                    }
                });
                (findViewById(R.id.Num6)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('6');
                    }
                });
                (findViewById(R.id.Num7)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputNum('7');
                    }
                });
                if (model.getBase() >= 10) {
                    (findViewById(R.id.Num8)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputNum('8');
                        }
                    });
                    (findViewById(R.id.Num9)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputNum('9');
                        }
                    });
                    if (model.getBase() >= 16) {
                        (findViewById(R.id.NumA)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('A');
                            }
                        });
                        (findViewById(R.id.NumB)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('B');
                            }
                        });
                        (findViewById(R.id.NumC)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('C');
                            }
                        });
                        (findViewById(R.id.NumD)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('D');
                            }
                        });
                        (findViewById(R.id.NumE)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('E');
                            }
                        });
                        (findViewById(R.id.NumF)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputNum('F');
                            }
                        });
                    }
                }
            }
        }
        (findViewById(R.id.plus)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.doBinaryOp('+');
                    updateDisplay(model.getDisplayVal());
                } catch (ArithmeticException e) {
                    if("Division by zero".equals(e.getMessage())) {
                        handleDivisionByZero();
                    }else throw e;
                }
            }
        });
        (findViewById(R.id.minus)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.doBinaryOp('-');
                    updateDisplay(model.getDisplayVal());
                } catch (ArithmeticException e) {
                    if("Division by zero".equals(e.getMessage())) {
                        handleDivisionByZero();
                    }else throw e;
                }
            }
        });
        (findViewById(R.id.mult)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.doBinaryOp('*');
                    updateDisplay(model.getDisplayVal());
                } catch (ArithmeticException e) {
                    if("Division by zero".equals(e.getMessage())) {
                        handleDivisionByZero();
                    }else throw e;
                }
            }
        });
        (findViewById(R.id.div)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.doBinaryOp('/');
                    updateDisplay(model.getDisplayVal());
                } catch (ArithmeticException e) {
                    if("Division by zero".equals(e.getMessage())) {
                        handleDivisionByZero();
                    }else throw e;
                }
            }
        });
        (findViewById(R.id.point)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.inputPoint();
                updateDisplay(model.getDisplayVal());
            }
        });
        (findViewById(R.id.equals)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.eval();
                    updateDisplay(model.getDisplayVal());
                } catch (ArithmeticException e) {
                    if("Division by zero".equals(e.getMessage())) {
                        handleDivisionByZero();
                    }else throw e;
                }
            }
        });
        updateDisplay(model.getDisplayVal());
    }

    //when num or memory is updated, updates the screen
    public void updateDisplay(String todisplay) {
        if (todisplay.length() > barsize) {
            //if num doesn't fit into the display text box
            int i = 0;
            for (; i < todisplay.length() && todisplay.charAt(i) != '.'; i++) ;
            if (i <= barsize) {
                //if the fractional part causes the overflow, truncate the fractional part
                todisplay = todisplay.substring(0, barsize);
            } else {
                //if the integer part causes the overflow, move into scientific notation
                todisplay = todisplay.substring(0, i);
                String head = "" + todisplay.charAt(0);
                String tail = todisplay.substring(1, i);
                while (tail.length() != 0 && tail.charAt(tail.length() - 1) == '0') tail = tail.substring(0, tail.length() - 1);
                String exponent = "E" + (todisplay.length() - 1);
                todisplay = head + ("".equals(tail) ? "" : "." + tail);
                todisplay = todisplay.substring(0, Math.min(barsize - exponent.length(), todisplay.length())) + exponent;
            }
        }
        mempush.setText("MemPush (" + model.getMemSize() + ")");
        mempop.setText("MemPop (" + model.getMemSize() + ")");
        if (rpn){
            mempop.setVisibility(View.GONE);
            findViewById(R.id.equals).setVisibility(View.GONE);
        }else{
            mempop.setVisibility(View.VISIBLE);
            findViewById(R.id.equals).setVisibility(View.VISIBLE);
        }
        display.setText(todisplay);
    }

    private void inputNum(char num) {
        if (display.getText().length() < barsize || model.getNewNum()) {
            model.inputNum("" + num);
            updateDisplay(model.getDisplayVal());
        }
    }

    private void handleDivisionByZero(){
        final AlertDialog alertDialog = new AlertDialog.Builder(AndroidCalc.this).create();
        alertDialog.setTitle("Division by zero!");
        alertDialog.setMessage("Division by zero not yet supported by the laws of mathematics.  Please try again on a future update.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        model.resetOperation();
    }

    private void handleInvalidLog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(AndroidCalc.this).create();
        alertDialog.setTitle("Invalid Log!");
        alertDialog.setMessage("Unable to take a log of that number.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
