package com.gameloft.bt_timepicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.support.v7.widget.AppCompatDrawableManager.get;


public class MainActivity extends Activity {
    TextView txt_date,txt_time;
    EditText et_nd,et_cv;
    Button btn_date,btn_time,btn_cv;
    ArrayList<JobInWeak>arrJob=new ArrayList<JobInWeak>();
    ArrayAdapter<JobInWeak>adapter=null;
    ListView lv_cv;
    Calendar calendar;
    Date dateFinish;
    Date hourFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFormWidgets();
        getDefaultInfor();
        addEventFormWidgets();
    }

    public void getFormWidgets()
    {
        txt_date=(TextView) findViewById(R.id.txt_date);
        txt_time=(TextView) findViewById(R.id.txt_time);
        et_cv=(EditText) findViewById(R.id.et_cv);
        et_nd=(EditText) findViewById(R.id.et_nd);
        btn_date=(Button) findViewById(R.id.btn_date);
        btn_time=(Button) findViewById(R.id.btn_time);
        btn_cv=(Button) findViewById(R.id.btn_cv);
        lv_cv=(ListView) findViewById(R.id.lv_congviec);
        adapter=new ArrayAdapter<JobInWeak>(this,
                        android.R.layout.simple_list_item_1,
                        arrJob);
        lv_cv.setAdapter(adapter);
    }
    public void getDefaultInfor()
    {
        calendar=Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String strDate=dft.format(calendar.getTime());
        txt_date.setText(strDate);
        dft=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String strTime=dft.format(calendar.getTime());
        txt_time.setText(strTime);
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        txt_time.setTag(dft.format(calendar.getTime()));

        et_cv.requestFocus();
        dateFinish=calendar.getTime();
        hourFinish=calendar.getTime();
    }

    public void addEventFormWidgets() {
        btn_date.setOnClickListener(new MyButtonEvent());
        btn_time.setOnClickListener(new MyButtonEvent());
        btn_cv.setOnClickListener(new MyButtonEvent());
        lv_cv.setOnItemClickListener(new MyListViewEvent());
        lv_cv.setOnItemLongClickListener(new MyListViewEvent());
//        lv_cv.setOnItemLongClickListener(new ItemLongClickRemove());
    }
//
//    private class ItemLongClickRemove implements AdapterView.OnItemLongClickListener {
//        @Override
//        public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//            alertDialogBuilder.setMessage("Bán có muốn xóa công việc này!");
//            alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                        lv_cv.get().getProducts().remove(position);
//                    adapter.notifyDataSetChanged();
//
//                }
//            });
//            alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            alertDialogBuilder.show();
//            return true;
//        }
//    }
    private class MyButtonEvent implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_date:
                    showDatePickerDialog();
                    break;
                case R.id.btn_time:
                    showTimePickerDialog();
                    break;
                case R.id.btn_cv:
                    processAddJob();
                    break;
            }
        }

    }


    private class MyListViewEvent implements
            OnItemClickListener,
            OnItemLongClickListener
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            arrJob.remove(arg2);
            adapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Toast.makeText(MainActivity.this,
                    arrJob.get(arg2).getDesciption(),
                    Toast.LENGTH_LONG).show();
        }

    }



    public void showDatePickerDialog()
    {
        OnDateSetListener callback=new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                txt_date.setText(
                        (dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
                calendar.set(year, monthOfYear, dayOfMonth);
                dateFinish=calendar.getTime();
            }
        };
        String s=txt_date.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                MainActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chose Date");
        pic.show();
    }
    public void showTimePickerDialog()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                txt_time.setText
                        (hourTam +":"+minute +(hourOfDay>12?" PM":" AM"));
                txt_time.setTag(s);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                hourFinish=calendar.getTime();
            }
        };

        String s=txt_time.getTag()+"";
        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                MainActivity.this,
                callback, gio, phut, true);
        time.setTitle("Done Chose Time");
        time.show();
    }
public void processAddJob()
{
    String title=et_cv.getText()+"";
    String description=et_nd.getText()+"";
    JobInWeak job=new JobInWeak(title, description, dateFinish, hourFinish);
    arrJob.add(job);
    adapter.notifyDataSetChanged();
    et_cv.setText("");
    et_nd.setText("");
    et_cv.requestFocus();
}

}


