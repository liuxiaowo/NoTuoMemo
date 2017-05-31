package com.example.memo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class InstructionActivity extends Activity {
TextView instruction_word2;
TextView instruction_word3;
TextView instruction_word4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_instruction);
        instruction_word2 = (TextView)findViewById(R.id.instruction_word2);
        instruction_word3 = (TextView)findViewById(R.id.instruction_word3);
        instruction_word4 = (TextView)findViewById(R.id.instruction_word4);
        int fstart2= getString(R.string.instruction_word2).indexOf("仅可以拨打电话");
        int fend2=fstart2+"仅可以拨打电话".length();
        int fstart3= getString(R.string.instruction_word3).indexOf("只有一次");
        int fend3=fstart3+"只有一次".length();
        int fstart4= getString(R.string.instruction_word4).indexOf("不能再退出");
        int fend4=fstart4+("不能再退出").length();
        SpannableStringBuilder style2=new SpannableStringBuilder(getString(R.string.instruction_word2)); 
        style2.setSpan(new ForegroundColorSpan(Color.RED),fstart2,fend2,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        SpannableStringBuilder style3=new SpannableStringBuilder(getString(R.string.instruction_word3)); 
        style3.setSpan(new ForegroundColorSpan(Color.RED),fstart3,fend3,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        SpannableStringBuilder style4=new SpannableStringBuilder(getString(R.string.instruction_word4)); 
        style4.setSpan(new ForegroundColorSpan(Color.RED),fstart4,fend4,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        instruction_word2.setText(style2);
        instruction_word3.setText(style3);
        instruction_word4.setText(style4);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.instruction, menu);
        return true;
    }
    
}
