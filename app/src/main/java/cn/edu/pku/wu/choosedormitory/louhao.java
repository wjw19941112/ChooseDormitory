package cn.edu.pku.wu.choosedormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;

/**
 * Created by wu on 2017/12/22.
 */

public class louhao extends Activity implements View.OnClickListener{

    private ImageView mBtn;
    private Button number5,number8,number9,number13,number14,mSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.louhao);

        mBtn = (ImageView)findViewById(R.id.title_back);
        mBtn.setOnClickListener(this);

        number5 = (Button)findViewById(R.id.number5);
        number5.setOnClickListener(this);
        number8 = (Button)findViewById(R.id.number8);
        number8.setOnClickListener(this);
        number9 = (Button)findViewById(R.id.number9);
        number9.setOnClickListener(this);
        number13 = (Button)findViewById(R.id.number13);
        number13.setOnClickListener(this);
        number14 = (Button)findViewById(R.id.number14);
        number14.setOnClickListener(this);
        mSearch = (Button)findViewById(R.id.search);
        mSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_back){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",0);
            editor.commit();
            //跳转到页
            Intent intent = new Intent(louhao.this, message.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
        if (v.getId()==R.id.number5){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",5);
            Log.d("mymessage","选择楼号"+5);
            editor.commit();
            Intent intent = new Intent(louhao.this, studentnumber.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.number8){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",8);
            Log.d("mymessage","选择楼号"+8);
            editor.commit();
            Intent intent = new Intent(louhao.this, studentnumber.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.number9){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",9);
            Log.d("mymessage","选择楼号"+9);
            editor.commit();
            Intent intent = new Intent(louhao.this, studentnumber.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.number13){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",13);
            Log.d("mymessage","选择楼号"+13);
            editor.commit();
            Intent intent = new Intent(louhao.this, studentnumber.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.number14){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",14);
            Log.d("mymessage","选择楼号"+14);
            editor.commit();
            Intent intent = new Intent(louhao.this, studentnumber.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.search){
            Intent intent = new Intent(louhao.this, kongchuangwei.class);
            startActivity(intent);
        }


    }
}
