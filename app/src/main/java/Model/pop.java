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

    public void createWindow(View v,LayoutInflater inflater, boolean fill_span, boolean fill_eng, ArrayList<WordsPairs> list, int orientation, int gridsize, Object tag) {

        Button button = (Button) v;
        CharSequence buttontext = button.getText();
        //check to see if empty
        if(buttontext != "") {
            int length;
            //adjust length based on gridsize
            if(gridsize == 9){
                // code for 9x9 grid
                length = 9;
            }
            else if(gridsize == 6){
                // code for 6x6 grid
                length = 6;
            }
            else{
                //code for 12x12 grid
                length = 12;
            }
            //if the text is longer than 6 characters, it calls getFullText to find full word to display
            if(buttontext.length() > 6){
                buttontext = getFullText((Button) v, buttontext,fill_span, fill_eng, list, length);
            }
            int yoff = 0;
            //see if the device is in landscape orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                if(location[1] >= 900){
                    yoff = yoff - 200;
                }
            }
            int[] location = new int[2];
            v.getLocationOnScreen(location);


            View popupView = inflater.inflate(R.layout.popwindow, null);


            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            ((TextView) popupWindow.getContentView().findViewById(R.id.poptext)).setText(buttontext);
            // show as drop down drops down the window from the view v
            // the y position of the window changes if the grid button is lower on the puzzle
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
    private void cell_layout_changes(View v, int orientation){
        int yoff = 0;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            if(location[1] >= 900){
                yoff = yoff - 400;
            }
        }
    }


    private CharSequence getFullText(Button v, CharSequence buttontext, boolean fill_span, boolean fill_eng, ArrayList<WordsPairs> list, int Length){

            CharSequence sixText = buttontext.subSequence(0,6);
            CharSequence shortlist;
            CharSequence fullText = buttontext;
            if(fill_eng){
                //if the grid text color is black, the grid spot is a prefilled word
                if(v.getCurrentTextColor() == Color.parseColor("#000000")){
                    for(int i = 0; i < Length); i++){
                        if(list.get(i).getSPAN().length() > 6){
                            shortlist = list.get(i).getSPAN().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(i).getSPAN();
                            }
                        }

                    }
                }
                else{
                    for(int j = 0; j < Length; j++){
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
                //if the grid text color is black, the grid spot is a prefilled word
                if(v.getCurrentTextColor() == Color.parseColor("#000000")){
                    for(int i = 0; i < Length; i++){
                        if(list.get(i).getENG().length() > 6){
                            shortlist = list.get(i).getENG().subSequence(0,6);
                            if(sixText.equals(shortlist)){
                                fullText = list.get(i).getENG();
                            }
                        }

                    }
                }
                else{
                    for(int j = 0; j < Length; j++){
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

    private void adjustTextSize(Object tag, PopupWindow popupWindow){

        if(tag.equals("720")){
            ((TextView) popupWindow.getContentView().findViewById(R.id.poptext)).setTextSize(40f);
        }
        else if(tag.equals("600")){
            ((TextView) popupWindow.getContentView().findViewById(R.id.poptext)).setTextSize(34f);
        }
        else{
            ((TextView) popupWindow.getContentView().findViewById(R.id.poptext)).setTextSize(24f);
        }

        return;
    }

}
