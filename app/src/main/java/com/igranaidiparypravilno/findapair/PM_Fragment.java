package com.igranaidiparypravilno.findapair;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class PM_Fragment extends Fragment implements AdapterView.OnItemSelectedListener{

    View rootView;
    GridView gridView;
    LinearLayout linerglav;
    TextView resetButton ,button2, time, record;
    int counter, imageSelected, volt, gan;
    public Integer[] mThumbIds;

    int[] num;
    int[] cot;
    int[] aux = new int[2];
    boolean[] est;
    ImageView imgView0, imgView1;
    int totalGridColumn = GameActivity.x;
    int[] numGridColumn     = new int[] { 0,0,2,0,4,0,6,0,8};
    int[] couplesGridColumn = new int[] { 0,0,2,0,8,0,18,0,32 };
    int[] cellGridColumn    = new int[] { 0,0,4,0,16,0,36,0,64 };

    private Chronometer mChronometer;
    SharedPreferences sPref;
    String Saned_Text4 = "Saned_Text4";
    String Saned_Text6 = "Saned_Text6";
    String Saned_Text8 = "Saned_Text8";
    String Saned_Int4 = "Saned_Int4";
    String Saned_Int6 = "Saned_Int6";
    String Saned_Int8 = "Saned_Int8";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initActivity();

        gridView.setAdapter(new ImageAdapter(getActivity(), cellGridColumn[totalGridColumn]));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View imgView, int position, long id) {
                if( imageSelected != position && !est[position] && aux[0] != position){
                    ImageView imageView = (ImageView) imgView;
                    imageView.setImageResource(mThumbIds[position]);

                    if(volt == 0){
                        imgView0 = (ImageView) imgView;
                    }else if(volt == 1){
                        imgView1 = (ImageView) imgView;
                        checkFinishGame();
                    }else
                    if (volt==2) {
                        checkImages();
                        imgView0 = (ImageView) imgView;
                    }
                    volt++;
                    aux[volt-1]=position;

                    imageSelected = position;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pm_fragment, container, false);

        record = rootView.findViewById(R.id.record);
        time = rootView.findViewById(R.id.time);
        mChronometer = rootView.findViewById(R.id.chronometer);
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loadText();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();
            }
        });
        onStartClick();

        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        linerglav = (LinearLayout) rootView.findViewById(R.id.linerglav);
        resetButton = (TextView) rootView.findViewById(R.id.button1);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGridView();
                button2.setVisibility(View.GONE);
                linerglav.setVisibility(View.GONE);
                onResetClick();
                loadText();
                onStartClick();
            }
        });
        button2 = (TextView) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        totalGridColumn = Integer.parseInt(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initActivity(){
        counter = 0;
        imageSelected = -1;
        gan = 0;
        volt = 0;
        aux[0] = -1;
        aux[0] = -1;
        setGridRandomImages(totalGridColumn);
        gridView.setNumColumns(totalGridColumn);

        gridView.setAdapter(new ImageAdapter(getActivity(), cellGridColumn[totalGridColumn]));
    }

    private void setGridRandomImages(int numGridColumn){
        int total = cellGridColumn[totalGridColumn];
        num = new int[couplesGridColumn[totalGridColumn]];
        cot = new int[total];
        est = new boolean[total];
        int img;
        mThumbIds = new Integer[total];
        Random r = new Random();
        for(int cant=0;cant<total;cant++){
            img = r.nextInt(couplesGridColumn[totalGridColumn]);
            if (num[img]<2){
                num[img]++;
                cot[cant]=img+1;
                mThumbIds[cant] = rootView.getContext().getResources().getIdentifier("pic_"+img, "drawable", "com.igranaidiparypravilno.findapair");
            }else{
                cant--;
            }
        }
    }

    private void checkFinishGame(){
        if (gan == num.length - 1){
            button2.setVisibility(View.VISIBLE);
            linerglav.setVisibility(View.VISIBLE);
            onStopClick();
            time.setText("Ваше время: " + mChronometer.getText().toString());
            saveText(mChronometer.getText().toString());
        }
    }

    private void checkImages(){
        if(cot[aux[0]]==cot[aux[1]]){
            est[aux[0]]=true;
            est[aux[1]]=true;
            volt=0;
            gan++;
        }else{
            volt=0;
            int resId = rootView.getContext().getResources().getIdentifier("iconcard", "drawable", "com.igranaidiparypravilno.findapair");

            ImageView imageView0 = (ImageView) imgView0;
            imageView0.setImageResource(resId);

            ImageView imageView1 = (ImageView) imgView1;
            imageView1.setImageResource(resId);
        }
    }

    private void resetGridView(){
        initActivity();
        gridView.invalidateViews();
    }

    public void onStartClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void onStopClick() {
        mChronometer.stop();
    }
    public void onResetClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void saveText(String ch_text){
        SharedPreferences.Editor ed = sPref.edit();
        String t;
        t = ch_text.replaceAll("[^0-9]", "");
        int i = 0;
        try
        {
            i = Integer.parseInt(t.trim());
        }
        catch (NumberFormatException nfe) { }
        int i2 = loadTextInt();
        if (i2==0)i2=30000;
        if (totalGridColumn == 4 && i<i2){
            ed.putString(Saned_Int4, Integer.toString(i));
            ed.putString(Saned_Text4,ch_text);
        }else if (totalGridColumn == 6 && i<i2){
            ed.putString(Saned_Int6, Integer.toString(i));
            ed.putString(Saned_Text6,ch_text);
        }else if (totalGridColumn == 8 && i<i2){
            ed.putString(Saned_Int8, Integer.toString(i));
            ed.putString(Saned_Text8,ch_text);
        }
        ed.commit();
    }

    private void loadText(){
        String savedText="";
        int i=0;

        if (totalGridColumn == 4){
            i = Integer.parseInt(sPref.getString(Saned_Int4,"0"));
            if(i!=0){
                savedText = sPref.getString(Saned_Text4,"");
            }

        }else if (totalGridColumn == 6){
            i = Integer.parseInt(sPref.getString(Saned_Int6,"0"));
            if(i!=0){
                savedText = sPref.getString(Saned_Text6,"");
            }
        }else if (totalGridColumn == 8){
            i = Integer.parseInt(sPref.getString(Saned_Int8,"0"));
            if(i!=0){
                savedText = sPref.getString(Saned_Text8,"");
            }
        }
        if(!savedText.equals("")){
            record.setText("Рекорд " + savedText);
        }

    }

    private int loadTextInt(){
        int i=0;
        if (totalGridColumn == 4){
            i = Integer.parseInt(sPref.getString(Saned_Int4,"0"));
        }else if (totalGridColumn == 6){
            i = Integer.parseInt(sPref.getString(Saned_Int6,"0"));
        }else if (totalGridColumn == 8){
            i = Integer.parseInt(sPref.getString(Saned_Int8,"0"));
        }
        return i;
    }
}
