package Model;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class pop {

    public pop(){
        return;
    }

    public void createWindow(View v,LayoutInflater inflater, CharSequence buttontext, boolean fill_span, boolean fill_eng, ArrayList<WordsPairs> list, int orientation) {
        if(buttontext != null) {
            if(buttontext.length() > 6){
                buttontext = getFullText((Button) v, buttontext,fill_span, fill_eng, list);
            }
            int yoff = 0;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                if(location[1] >= 900){
                    yoff = yoff - 400;
                }
            }


            View popupView = inflater.inflate(R.layout.popwindow, null);



            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            ((TextView) popupWindow.getContentView().findViewById(R.id.poptext)).setText(buttontext);
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAsDropDown(v, 0, yoff);
            //popupWindow.showAtLocation(v,0,0,0);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
    }

    private CharSequence getFullText(Button v, CharSequence buttontext, boolean fill_span, boolean fill_eng, ArrayList<WordsPairs> list){

            CharSequence sixText = buttontext.subSequence(0,6);
            CharSequence shortlist;
            CharSequence fullText = buttontext;
            if(fill_eng){
                if(v.getCurrentTextColor() == Color.parseColor("#000000")){
                    for(int i = 0; i < 9; i++){
                        if(list.get(i).getSPAN().length() > 6){
                            shortlist = list.get(i).getSPAN().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(i).getSPAN();
                            }
                        }

                    }
                }
                else{
                    for(int j = 0; j < 9; j++){
                        if(list.get(j).getENG().length() > 6){
                            shortlist = list.get(j).getENG().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(j).getENG();
                            }
                        }
                    }
                }
            }
            if(fill_span){
                if(v.getCurrentTextColor() == Color.parseColor("#000000")){
                    for(int i = 0; i < 9; i++){
                        if(list.get(i).getENG().length() > 6){
                            shortlist = list.get(i).getENG().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(i).getENG();
                            }
                        }

                    }
                }
                else{
                    for(int j = 0; j < 9; j++){
                        if(list.get(j).getSPAN().length() > 6){
                            shortlist = list.get(j).getSPAN().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(j).getSPAN();
                            }
                        }
                    }
                }
            }
            return fullText;
    }
}