package com.calc;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import com.calc.R;

import java.math.*;
import java.util.*;

/* TODO:
 * Add support for larger screens
 */
public class AndroidCalc extends FragmentActivity implements DecimalFragment.DecFragListener, BinaryFragment.BinFragListener, OctalFragment.OctFragListener, HexFragment.HexFragListener {
	
	//fires when a numerical button is pushed
	protected class NumClickListener implements OnClickListener{
		int num;
		public NumClickListener(int i){
			num = i;
		}
		public void onClick(View v){
			if (clicks < maxclicks){
				clickNum(num);
				if (!(clicks == 0 && num == 0)){
					clicks++;
				}
			}
		}
	}
	
	//Fires when an operation is pushed
	protected class OpClickListener implements OnClickListener{
		char mode;
		public OpClickListener(char m){
			mode = m;
		}
		public void onClick(View v){
			if (!rpn){
				clickOpBasic(mode);
			}else{
				clickOpRPN(mode);
			}
		}
	}
	
	//Fires when MemPush button is pushed
	protected class MemPushListener implements OnClickListener{
		public void onClick(View v){
			memPush();
		}
	}
	
	//Fires when MemPop button is pushed
	protected class MemPopListener implements OnClickListener{
		public void onClick(View v){
			memPop();
		}
	}
	
	//Fires when MemClear button is pushed
	protected class MemClrListener implements OnClickListener{
		public void onClick(View v){
			memClear();
		}
	}
	
	//Fired when "Reset Current" button is clicked
	protected class ResetListener implements OnClickListener{
		public void onClick(View v){
			num = new BigDecimal("0");
        	newnum = true;
            append = false;
            flt = false;
            negexp = 0;
            clicks = 0;
            mode = '=';
            prevnum = new BigDecimal("0");
			updateDisplay();
		}
	}
	
	/*NOTE: maxclicks - 1 is used here instead of maxclicks because, if this is the maxclicks-th click,
	 * the user can't input any numbers after the decimal point, which is nonintuitive
	 */
	protected class PointClickListener implements OnClickListener{
		public void onClick(View v){
			if (flt == false && clicks < maxclicks - 1){
				clickPoint();
				//an extra click to account for the 0 at the beginning of "0.xxx"
				if (num.equals(BigInteger.ZERO)){
					clicks++;
				}
				clicks++;
			}
		}
	}
	
	//fires when negate button is clicked
	protected class signChangeListener implements OnClickListener{
		public void onClick(View v){
			clickNeg();
		}
	}
	
	//Fires for X^2, sqrt, trig, and log functions
	protected class oneOperandOpClickListener implements OnClickListener{
		char mode;
		public oneOperandOpClickListener(char m){
			mode = m;
		}
		public void onClick(View v){
			clickOneOperandOp(mode);
		}
	}
	
	//fires when 2nd button is clicked
	protected class opChangeListener implements OnClickListener{
		public void onClick(View v){
			clickOpChange();
		}
	}
	
	BigDecimal num; //the current number being inputted
	BigDecimal prevnum; //the previous number in the case of an operation
	Stack<BigDecimal> memory; //stores memory for non-RPN mode and the RPN stack in RPN mode
	boolean append; //true = num presses should append to num; false = num presses should replace num
	boolean flt; //true = new nums to append go after decimal point; false = new nums to append go before point
	boolean newnum; //true = num has been refreshed; false = num has not been refreshed
	boolean op2; //true = 2nd op mode, false = basic op mode (for landscape-only ops)
	char mode; //stores which operation is currently happening, "=" if no op
	int zeroes; //used for outputting ending zeroes after a decimal point;
	int clicks; //the display can't handle more than 13 digits, so...
	int memsize; //stores size of memory
	int maxclicks; //the maximum number of clicks; changes based on orientation, otherwise would be final
	int base; //the mathematical base of calculation (e.g. decimal = base 10)
	int negexp; //the number of digits after the decimal point
	
	boolean rpn; //true = rpn mode active; false = basic mode active
	
	DecimalFragment decfrag;
	BinaryFragment binfrag;
	OctalFragment octfrag;
	HexFragment hexfrag;
	
	TextView display;
	Button numButtons[] = new Button[16];
	Button plus;
	Button minus;
	Button mult;
	Button div;
	Button eq;
	Button point;
	Button mempush;
	Button mempop;
	Button memclr;
	Button reset;
	Button second;
	Button sin;
	Button cos;
	Button tan;
	Button log;
	Button square;
	Button sqrt;
	Button neg;
	RadioButton basicmode;
	RadioButton rpnmode;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        	maxclicks = 14;
        }else{
        	maxclicks = 28;
        }
                
        display = (TextView)findViewById(R.id.display);
        
        mempush = (Button)findViewById(R.id.mempush);
        mempush.setOnClickListener(new MemPushListener());
        mempop = (Button)findViewById(R.id.mempop);
        mempop.setOnClickListener(new MemPopListener());
        memclr = (Button)findViewById(R.id.memclear);
        memclr.setOnClickListener(new MemClrListener());
        
        reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new ResetListener());
        
        decfrag = new DecimalFragment();
        binfrag = new BinaryFragment();
        octfrag = new OctalFragment();
        hexfrag = new HexFragment();
        
      //Using a full alternation rather than search-default pairs for readability of code
        if (savedInstanceState == null){
        	num = new BigDecimal("0");
        	newnum = true;
            append = false;
            flt = false;
            clicks = 0;
            mode = '=';
            memory = new Stack<BigDecimal>();
            memsize = 0;
            prevnum = new BigDecimal("0");
            rpn = false;
            negexp = 0;
            switchBase(10);
        }else{
        	num = new BigDecimal(savedInstanceState.getString("num"));
        	newnum = savedInstanceState.getBoolean("newnum");
            append = savedInstanceState.getBoolean("append");
            flt = savedInstanceState.getBoolean("flt");
            clicks = savedInstanceState.getInt("clicks");
            mode = savedInstanceState.getChar("mode");
            prevnum = new BigDecimal(savedInstanceState.getString("prevnum"));
            negexp = savedInstanceState.getInt("negexp");
            
            switchBase(savedInstanceState.getInt("base"));
            switchRPNMode(savedInstanceState.getBoolean("rpn"));
                        
            memsize = savedInstanceState.getInt("memsize");
            memory = new Stack<BigDecimal>();
            String[] memArray = savedInstanceState.getStringArray("memory");
            for (int i=0;i<memsize;i++){
            	memory.push(new BigDecimal(memArray[i]));
            }
        }
        
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
        	neg = (Button)findViewById(R.id.neg);
        	neg.setOnClickListener(new signChangeListener());
        	
        	square = (Button)findViewById(R.id.square);
        	square.setOnClickListener(new oneOperandOpClickListener('^'));        	
        	sqrt = (Button)findViewById(R.id.sqrt);
        	sqrt.setOnClickListener(new oneOperandOpClickListener('/'));       	
        	sin = (Button)findViewById(R.id.sin);
        	sin.setOnClickListener(new oneOperandOpClickListener('s'));
        	cos = (Button)findViewById(R.id.cos);
        	cos.setOnClickListener(new oneOperandOpClickListener('c'));
        	tan = (Button)findViewById(R.id.tan);
        	tan.setOnClickListener(new oneOperandOpClickListener('t'));        	
        	log = (Button)findViewById(R.id.log);
        	log.setOnClickListener(new oneOperandOpClickListener('l'));
        	
        	second = (Button)findViewById(R.id.second);
        	second.setOnClickListener(new opChangeListener());
        	
        	op2 = false;
        }
        updateDisplay();
    }
    
    //when num or memory is updated, updates the screen
    public void updateDisplay(){
    	String todisplay;
    	if (base != 10){
    		todisplay = toBaseString(num, base);
    	}else{
    		/*stripTrailingZeros doesn't work properly on the device when num is 0.000....., so we have to
    		 * do a bit of awkward hardcoding here...works fine on the emulator, though...
    		 */
    		if (num.compareTo(BigDecimal.ZERO) == 0){
    			todisplay = "0";
    		}else{
    			todisplay = num.stripTrailingZeros().toPlainString();
    		}
    	}
		if (todisplay.length() > maxclicks){
			//if num doesn't fit into the display text box
			int i = 0;
			for (;i < todisplay.length() && todisplay.charAt(i) != '.'; i++);
			if (i <= maxclicks){
				//if the fractional part causes the overflow, truncate the fractional part
				todisplay = todisplay.substring(0, maxclicks);
			}else{
				//if the integer part causes the overflow, move into scientific notation
				todisplay = todisplay.substring(0, i);
				String head = todisplay.charAt(0) + ".";
				String tail = todisplay.substring(1, i);
				String exponent = "E" + (todisplay.length() - 1);
				todisplay = (head + tail).substring(0, maxclicks - exponent.length()) + exponent;
			}
		}else{
		   	if (flt == true){
		   		if (num.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0){
		   			//if num is exactly an integer we need to process decimal point manually
		   			todisplay += ".";
		   		}
		   		for (int i=0;i<zeroes;i++){
			    	todisplay += "0";
				}
		   	}	    
		}
		display.setText(todisplay);
    	
    	if (!rpn){
	    	mempush.setText("MemPush (" + memsize + ")");
	    	mempop.setText("MemPop (" + memsize + ")");
    	}else{
    		memclr.setText("Reset Stack (" + memsize + ")");
    		mempush.setText("Push (" + memsize + ")");
    	}
    }
    
    /*This section is for methods that fire when a button is clicked*/
    
    //fires when one of the number buttons is clicked
    public void clickNum(int x){
    	if (append == false){
    		num = new BigDecimal("0");
    		newnum = true;
    		append = true;
    	}
    	if (flt == false){
    		num = num.multiply(new BigDecimal(base));
    		num = num.add(new BigDecimal(x));
    	}else{
			negexp++;
    		if (x == 0){
    			zeroes++;
    		}else{
    			zeroes = 0;
    			BigDecimal toappend = new BigDecimal(x);
    			for (int i=0;i<negexp;i++){
    				toappend = toappend.divide(new BigDecimal(base));
    			}
    			num = num.add(toappend);
    		}
    	}
    	updateDisplay();
    }
    
    //fires when an op (plus, minus, mult, div, eq) is clicked and basic mode is enabled
    public void clickOpBasic(char m){
    	if (newnum == true){
	    	eval();
	    	prevnum = num;
	    	newnum = false;
	    	append = false;
	    	flt = false;
	    	negexp = 0;
	    	clicks = 0;
	    	mode = m;
	    	updateDisplay();
    	}else{
    		mode = m;
    	}
    }
    
    //fires when an op (plus, minus, mult, div, eq) is clicked and RPN mode is enabled
    public void clickOpRPN(char m){
    	if (memsize >= 1){
	    	prevnum = num;
	    	num = memory.pop();
	    	memsize--;
	    	mode = m;
	    	eval();
	    	newnum = false;
	    	append = false;
	    	flt = false;
	    	negexp = 0;
	    	clicks = 0;
	    	updateDisplay();
    	}else{
    		Toast.makeText(this, "Not enough operands", Toast.LENGTH_SHORT).show();
    	}
    }
    
    //fires when X^2, sqrt, trig, or log button is clicked
    public void clickOneOperandOp(char mode){
    	num = evalOneOp(mode);
    	newnum = false;
    	append = false;
    	flt = false;
    	negexp = 0;
    	clicks = 0;
    	updateDisplay();
    }
    
    //fires when the decimal point button is clicked
    public void clickPoint(){
    	if (append == false){
    		num = new BigDecimal("0");
    		newnum = true;
    		append = true;
    	}
    	flt = true;
    	negexp = 0;
    	zeroes = 0;
    	num = new BigDecimal(num.toPlainString() + ".0");
    	updateDisplay();
    }
    
    //fires when +/- button is clicked
    public void clickNeg(){
    	if (append){
			if (num.compareTo(BigDecimal.ZERO) == 1){
				clicks++;
			}else{
				clicks--;
			}
		}
		num = num.negate();
		updateDisplay();
    }
    
    //fires when MemPush is clicked
    public void memPush(){
		memory.push(num);
		memsize++;
		Toast.makeText(this, "Memory Pushed", Toast.LENGTH_SHORT).show();
		num = new BigDecimal("0");
		newnum = true;
		append = false;
		flt = false;
		negexp = 0;
		clicks = 0;
		updateDisplay();
    }
    
    //fires when MemPop is clicked
    public void memPop(){
    	if (!memory.empty()){
    		num = memory.pop();
	    	memsize--;
    		newnum = true;
    		append = false;
    		flt = false;
    		negexp = 0;
    		clicks = 0;
    		Toast.makeText(this, "Memory Popped", Toast.LENGTH_SHORT).show();
	    	updateDisplay();
    	}else{
    		Toast.makeText(this, "Memory is empty!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    //fires when MemClear is clicked
    public void memClear(){
    	memory = new Stack<BigDecimal>();
    	memsize = 0;
    	Toast.makeText(this, "Memory Cleared", Toast.LENGTH_SHORT).show();
    	updateDisplay();
    }
    
    //fires when the Basic/RPN mode is changed
    public void switchRPNMode(boolean mode){
    	if (rpn == mode) return;
    	rpn = mode;
    	memory = new Stack<BigDecimal>();
    	memsize = 0;
    	if (mode == true){
    		mempop.setVisibility(View.GONE);
    		memclr.setText(R.string.rpnclear);
    		mempush.setText(R.string.rpnpush);
    	}else{
    		mempop.setVisibility(View.VISIBLE);
    		memclr.setText(R.string.memclear);
    		mempush.setText(R.string.mempush);
    	}
    }
    
    //fires when base is switched
    public void switchBase(int b){
    	base = b;
    	switch (b){
    	case 2:
        	getSupportFragmentManager().beginTransaction().replace(R.id.root, binfrag).commit();
    		break;
    	case 8:
        	getSupportFragmentManager().beginTransaction().replace(R.id.root, octfrag).commit();
    		break;
    	case 10:
        	getSupportFragmentManager().beginTransaction().replace(R.id.root, decfrag).commit();
    		break;
    	case 16:
        	getSupportFragmentManager().beginTransaction().replace(R.id.root, hexfrag).commit();
    		break;
    	}
    	
    	updateDisplay();
    }
    
    public void clickOpChange(){
    	if (op2){
    		sin.setText(R.string.sin);
        	sin.setOnClickListener(new oneOperandOpClickListener('s'));
    		cos.setText(R.string.cos);
        	cos.setOnClickListener(new oneOperandOpClickListener('c'));
    		tan.setText(R.string.tan);
        	tan.setOnClickListener(new oneOperandOpClickListener('t'));        	
    		log.setText(R.string.log);
        	log.setOnClickListener(new oneOperandOpClickListener('l'));
    	}else{
    		sin.setText(R.string.sininv);
        	sin.setOnClickListener(new oneOperandOpClickListener('d'));
    		cos.setText(R.string.cosinv);
        	cos.setOnClickListener(new oneOperandOpClickListener('v'));
    		tan.setText(R.string.taninv);
        	tan.setOnClickListener(new oneOperandOpClickListener('y'));        	
    		log.setText(R.string.ex);
        	log.setOnClickListener(new oneOperandOpClickListener(';'));
    	}
    	
    	op2 = !op2;
    }
    
    /*This section is for helper methods*/
    
    //sets buttons up with listeners for a Decimal layout; forward-declared in DecimalFragment.DecFragListener
    public void setDecButtons(View root){
    	setButtons(root);
    }
    
    //sets buttons up with listeners for a Binary layout; forward-declared in BinaryFragment.BinFragListener
    public void setBinButtons(View root){
    	setButtons(root);
    }
    
    //sets buttons up with listeners for an Octal layout; forward-declared in OctalFragment.OctFragListener
    public void setOctButtons(View root){
    	setButtons(root);
    }
    
    //sets buttons up with listeners for a Hex layout; forward-declared in HexFragment.HexFragListener
    public void setHexButtons(View root){
    	setButtons(root);
    }
    
    //Does the work for the 4 above methods; done for code-factoring purposes
    public void setButtons(View root){
    	numButtons[0] = (Button)(root.findViewById(R.id.Num0));
        numButtons[1] = (Button)(root.findViewById(R.id.Num1));
        if (base > 2){
	        numButtons[2] = (Button)(root.findViewById(R.id.Num2));
		    numButtons[3] = (Button)(root.findViewById(R.id.Num3));
		    numButtons[4] = (Button)(root.findViewById(R.id.Num4));
		    numButtons[5] = (Button)(root.findViewById(R.id.Num5));
		    numButtons[6] = (Button)(root.findViewById(R.id.Num6));
		    numButtons[7] = (Button)(root.findViewById(R.id.Num7));
		    if (base > 8){
			    numButtons[8] = (Button)(root.findViewById(R.id.Num8));
			    numButtons[9] = (Button)(root.findViewById(R.id.Num9));
			    if (base > 10){
				    numButtons[10] = (Button)(root.findViewById(R.id.NumA));
				    numButtons[11] = (Button)(root.findViewById(R.id.NumB));
				    numButtons[12] = (Button)(root.findViewById(R.id.NumC));
				    numButtons[13] = (Button)(root.findViewById(R.id.NumD));
				    numButtons[14] = (Button)(root.findViewById(R.id.NumE));
				    numButtons[15] = (Button)(root.findViewById(R.id.NumF));
			    }
		    }
        }
	    
        for (int i = 0; i < base; i++){
        	numButtons[i].setOnClickListener(new NumClickListener(i));
        }
	    
	    plus = (Button)(root.findViewById(R.id.plus));
        plus.setOnClickListener(new OpClickListener('+'));
        minus = (Button)(root.findViewById(R.id.minus));
        minus.setOnClickListener(new OpClickListener('-'));
        mult = (Button)(root.findViewById(R.id.mult));
        mult.setOnClickListener(new OpClickListener('x'));
        div = (Button)(root.findViewById(R.id.div));
        div.setOnClickListener(new OpClickListener('/'));
        eq = (Button)(root.findViewById(R.id.equals));
        eq.setOnClickListener(new OpClickListener('='));
        
        point = (Button)(root.findViewById(R.id.point));
        point.setOnClickListener(new PointClickListener());
    }
    
    //handles two-operand functions
    public void eval(){
    	switch (mode){
    	case '+':
    		num = prevnum.add(num);
    		break;
    	case '-':
    		num = prevnum.subtract(num);
    		break;
    	case 'x':
    		num = prevnum.multiply(num);
    		break;
    	case '/':
    		num = prevnum.divide(num, 30, BigDecimal.ROUND_DOWN);
    		break;
    	}
    	mode = '=';
    }
    
    //handles single-operand functions
    public BigDecimal evalOneOp(char mode){
    	BigDecimal ret = BigDecimal.ZERO;
    	switch(mode){
    	case '^':
    		ret = num.pow(2);
    		break;
    	case '/':
    		ret = new BigDecimal(Math.sqrt(num.doubleValue()));
    		break;
    	case 's':
    		ret = new BigDecimal(Math.sin(num.doubleValue()));
    		break;
    	case 'c':
    		ret = new BigDecimal(Math.cos(num.doubleValue()));
    		break;
    	case 't':
    		ret = new BigDecimal(Math.tan(num.doubleValue()));
    		break;
    	case 'l':
    		ret = new BigDecimal(Math.log10(num.doubleValue()));
    		break;
    	case 'd':
    		ret = new BigDecimal(Math.asin(num.doubleValue()));
    		break;
    	case 'v':
    		ret = new BigDecimal(Math.acos(num.doubleValue()));
    		break;
    	case 'y':
    		ret = new BigDecimal(Math.atan(num.doubleValue()));
    		break;
    	case ';':
    		ret = new BigDecimal(Math.exp(num.doubleValue()));
    		break;
    	}
    	return ret;
    }
    
    //returns a String representation of n_base
    String toBaseString(BigDecimal n, int base){
    	String ret = "";
    	boolean neg = false;
    	BigDecimal b = new BigDecimal(base);
    	if (n.compareTo(BigDecimal.ZERO) == -1){
    		neg = true;
    		n = BigDecimal.ZERO.subtract(n);
    	}
    	BigInteger intpart = n.toBigInteger();
    	n = n.remainder(BigDecimal.ONE);
    	while (intpart.compareTo(BigInteger.ZERO) != 0){
    		if (intpart.remainder(b.toBigInteger()).compareTo(new BigInteger("9")) == 1){
    			ret = "" + ((char)('A' + intpart.remainder(b.toBigInteger()).intValue() - 10)) + ret;
    		}else{
    			ret = "" + intpart.remainder(b.toBigInteger()) + ret;
    		}
    		intpart = intpart.divide(b.toBigInteger());
    	}
    	if (ret == ""){
    		ret = "0";
    	}
    	if (n.compareTo(BigDecimal.ZERO) != 0){
    		ret += ".";
    		for (int i=0;i < maxclicks && n.compareTo(BigDecimal.ZERO) != 0; i++){
    			n = n.multiply(b);
    			if (n.intValue() < 10){
    				ret += n.intValue();
    			}else{
    				ret += (char)('A' + n.intValue() - 10);
    			}
    			n = n.remainder(BigDecimal.ONE);
    		}
    	}
    	if (neg) ret = "-" + ret;
    	return ret;
    }
    
    /* This section is for overridden system methods */

    protected void onSaveInstanceState(Bundle outState){
    	outState.putString("num", num.toPlainString());
    	outState.putBoolean("newnum", newnum);
    	outState.putBoolean("append", append);
    	outState.putBoolean("flt", flt);
    	outState.putInt("clicks", clicks);
    	outState.putChar("mode", mode);
    	outState.putInt("memsize", memsize);
    	outState.putString("prevnum", prevnum.toPlainString());
    	outState.putBoolean("rpn", rpn);
    	outState.putInt("base", base);
    	outState.putInt("negexp", negexp);

    	String[] memArray = new String[memsize];
    	for (int i=memsize - 1;i>=0;i--){
    		memArray[i] = memory.pop().toPlainString();
    	}
    	outState.putStringArray("memory", memArray);
        
    	super.onSaveInstanceState(outState);
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.calc_menu, menu);
    	if (rpn == true){
    		menu.findItem(R.id.rpn).setChecked(true);
    	}
    	switch (base){
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
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()){
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
}