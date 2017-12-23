package cn.edu.pku.wu.choosedormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 2017/12/23.
 */

public class studentnumber extends Activity implements View.OnClickListener{
    private ImageView mBtn;
    private Button dan,shuang,san,si;
    private  HttpURLConnection conn;
    private static final int SUCCESS =1;
    private int errcode=1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentnumber);

        mBtn = (ImageView)findViewById(R.id.title_back);
        mBtn.setOnClickListener(this);

        dan = (Button)findViewById(R.id.danren);
        dan.setOnClickListener(this);
        shuang = (Button)findViewById(R.id.shuangren);
        shuang.setOnClickListener(this);
        san = (Button)findViewById(R.id.sanren);
        san.setOnClickListener(this);
        si = (Button)findViewById(R.id.siren);
        si.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.title_back){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("renshu",0);
            editor.commit();
            //跳转到页
            Intent intent = new Intent(studentnumber.this, louhao.class);
            startActivity(intent);
            //关闭当前界面
            finish();

        }
        if (v.getId()==R.id.danren){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("renshu",1);
            editor.commit();
            //创建map类型
            Map<String, Object> map = new HashMap<String, Object>();
            Log.d("myapp","办理人总数"+1);
            map.put("num", 1);
            //获取办理人
            SharedPreferences sharedPreferences = getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);
            String xuehao=sharedPreferences.getString("username","");
            Log.d("myapp","办理人id"+xuehao);
            map.put("stuid", xuehao);

            int DormitoryNum=sharedPreferences.getInt("Dormitory",0);
            map.put("buildingNo",DormitoryNum );
            Log.d("myapp","map里的值"+map);

            //连接网络
            StringBuffer sbRequest =new StringBuffer();
            if(map!=null&&map.size()>0){
                for (String key:map.keySet()){
                    sbRequest.append(key+"="+map.get(key)+"&");
                }
            }
            final String request = sbRequest.substring(0,sbRequest.length()-1);
            final String ip3="https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(ip3);
                                if("https".equalsIgnoreCase(url.getProtocol())){
                                    SslUtil.ignoreSsl();
                                }
                                conn=(HttpURLConnection)url.openConnection();
                                conn.setRequestMethod("POST");
                                //通过正文发送数据
                                OutputStream os =conn.getOutputStream();
                                os.write(request.getBytes());
                                os.flush();

                                InputStream in = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String str;
                                while((str=reader.readLine()) != null){
                                    response.append(str);
                                    Log.d("myapp", str);
                                }
                                String responseStr=response.toString();
                                Log.d("myapp", "选宿舍结果"+responseStr);

                                //将结果传给主线程
                                Message msg = new Message();
                                msg.what=SUCCESS;
                                msg.obj=responseStr;
                                mHandler.sendMessage(msg);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
            ).start();

        }


        if (v.getId()==R.id.shuangren){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("renshu",2);
            editor.commit();
            Intent intent = new Intent(studentnumber.this, shuangren.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.sanren){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("renshu",3);
            editor.commit();
            Intent intent = new Intent(studentnumber.this, sanren.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.siren){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("renshu",4);
            editor.commit();
            Intent intent = new Intent(studentnumber.this, siren.class);
            startActivity(intent);

        }

    }
    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case SUCCESS:
                    String responseStr=(String)msg.obj;
                    Log.d("myapp","查询结果"+responseStr);
                    try {
                        //          创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象)
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        if(errcode==0){
                            Toast.makeText(studentnumber.this, "选宿舍成功", Toast.LENGTH_LONG).show();
                            //跳转到页
                            Intent intent = new Intent(studentnumber.this, success.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(studentnumber.this, "选宿舍失败，请重新选宿舍", Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
}
