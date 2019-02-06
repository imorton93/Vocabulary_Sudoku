package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class reinit_adialog extends Dialog implements android.view.View.OnClickListener{
//public class CustomDialog extends Dialog implements android.view.View.OnClickListener {
//public class Reinit_dialog extends Dialog implements android.view.View.OnClickListener{
//public class reinit_dialog extends Dialog    {
//public class reinit_dialog {
    public Activity activity;
   // public Dialog d;
    public Button yes, cancel;

    //public CustomDialogClass(Activity )
    public reinit_dialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warn_reinitialize);
        yes = (Button) findViewById(R.id.cont_yes);
        cancel = (Button) findViewById(R.id.cont_cancel);
        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cont_yes:
                //Activity continues
                break;
            case R.id.cont_cancel:
                dismiss();
                //Go back? Closes dialog
                break;

            default:
                break;
        }

    }
}





