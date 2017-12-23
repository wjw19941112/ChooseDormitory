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
import android.widget.EditText;
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

public class siren extends Activity implements View.OnClickListener{
    private ImageView mBtn;
    private Button mStart;
    private EditText xueHao1,xueHao2,xueHao3,yanZhengma1,yanZhengma2,yanZhengma3;
    private HttpURLConnection conn;
    private static final int SUCCESS =1;
    private int errcode=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siren);
        mBtn=(ImageView)findViewById(R.id.title_back);
        mBtn.setOnClickListener(this);

        mStart=(Button) findViewById(R.id.login3);
        mStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_back){
            //清空同学储存信息
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("xueHao1","");
            editor.putString("yanZhengma1","");
            editor.putString("xueHao2","");
            editor.putString("yanZhengma2","");
            editor.putString("xueHao3","");
            editor.putString("yanZhengma3","");
            editor.commit();
            //跳转到页
            Intent intent = new Intent(siren.this, studentnumber.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
        if(v.getId()==R.id.login3){
            xueHao1=(EditText)findViewById(R.id.xuehao4);
            yanZhengma1=(EditText)findViewById(R.id.number4);
            xueHao2=(EditText)findViewById(R.id.xuehao5);
            yanZhengma2=(EditText)findViewById(R.id.number5);
            xueHao3=(EditText)findViewById(R.id.xuehao6);
            yanZhengma3=(EditText)findViewById(R.id.number6);
            // 存储同学1信息
            if(xueHao1.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("xueHao1",xueHao1.getText().toString());
                Log.d("myapp","同学1的学号"+xueHao1.getText().toString());
                editor.commit();
            }
            if(yanZhengma1.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("yanZhengma1",yanZhengma1.getText().toString());
                Log.d("myapp","同学1的验证码"+yanZhengma1.getText().toString());
                editor.commit();
            }
            //存储同学2的信息
            if(xueHao2.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("xueHao2",xueHao2.getText().toString());
                Log.d("myapp","同学2的学号"+xueHao2.getText().toString());
                editor.commit();
            }
            if(yanZhengma2.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("yanZhengma2",yanZhengma2.getText().toString());
                Log.d("myapp","同学2的验证码"+yanZhengma2.getText().toString());
                editor.commit();
            }
            //存储同学3的信息
            if(xueHao3.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("xueHao3",xueHao3.getText().toString());
                Log.d("myapp","同学3的学号"+xueHao3.getText().toString());
                editor.commit();
            }
            if(yanZhengma3.getText().toString().length() != 0){
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("yanZhengma3",yanZhengma3.getText().toString());
                Log.d("myapp","同学3的验证码"+yanZhengma3.getText().toString());
                editor.commit();
            }
            //创建map类型
            Map<String, Object> map = new HashMap<String, Object>();
            Log.d("myapp","办理人总数"+4);
            map.put("num", 4);
            //获取办理人
            SharedPreferences sharedPreferences = getSharedPreferences("config",Activity.MODE_PRIVATE);
            String xueHao=sharedPreferences.getString("username","");
            Log.d("myapp","办理人id"+xueHao);
            map.put("stuid", xueHao);
            String xueHao1=sharedPreferences.getString("xueHao1","");
            map.put("stu1id", xueHao1);
            String yanZhengma1=sharedPreferences.getString("yanZhengma1","");
            map.put("v1code", yanZhengma1);
            String xueHao2=sharedPreferences.getString("xueHao2","");
            map.put("stu2id", xueHao2);
            String yanZhengma2=sharedPreferences.getString("yanZhengma2","");
            map.put("v2code", yanZhengma2);
            String xueHao3=sharedPreferences.getString("xueHao3","");
            map.put("stu3id", xueHao3);
            String yanZhengma3=sharedPreferences.getString("yanZhengma3","");
            map.put("v3code", yanZhengma3);

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

    }
    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case SUCCESS:
                    String responseStr=(String)msg.obj;
                    Log.d("myapp","查询结果"+responseStr);
                    try {
                        //  创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        if(errcode==0){
                            if(xueHao1.getText().toString().length() != 0 && yanZhengma1.getText().toString().length() != 0 && xueHao2.getText().toString().length() != 0 && yanZhengma2.getText().toString().length() != 0 && xueHao3.getText().toString().length() != 0 && yanZhengma3.getText().toString().length() != 0) {
                                Toast.makeText(siren.this, "选宿舍成功", Toast.LENGTH_LONG).show();
                                //跳转到页
                                Intent intent = new Intent(siren.this, success.class);
                                startActivity(intent);
                                finish();
                            }else {if (xueHao1.getText().toString().length() == 0){
                                Toast.makeText(siren.this, "请输入第二位同学的学号", Toast.LENGTH_LONG).show();
                            }
                                if (yanZhengma1.getText().toString().length() == 0){
                                    Toast.makeText(siren.this, "请输入第二位同学的验证码", Toast.LENGTH_LONG).show();
                                }
                                if (xueHao2.getText().toString().length() == 0){
                                    Toast.makeText(siren.this, "请输入第三位同学的学号", Toast.LENGTH_LONG).show();
                                }
                                if (yanZhengma2.getText().toString().length() == 0){
                                    Toast.makeText(siren.this, "请输入第三位同学的验证码", Toast.LENGTH_LONG).show();
                                }
                                if (xueHao3.getText().toString().length() == 0){
                                    Toast.makeText(siren.this, "请输入第四位同学的学号", Toast.LENGTH_LONG).show();
                                }
                                if (yanZhengma3.getText().toString().length() == 0){
                                    Toast.makeText(siren.this, "请输入第四位同学的验证码", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
}
