package cn.edu.pku.wu.choosedormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wu on 2017/12/20.
 */

public class message extends Activity implements ViewStub.OnClickListener{

    private TextView xueHao,xingMing,xingBie,yanZhengma,xiaoQu,nianJi,xuanLouhao,xuanSushehao;
    private HttpURLConnection conn;
    private static final int RESEARCH =1;
    private int errcode=1;
    private String data;
    private ImageView mBtn;
    private ImageView nextBtu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        //初始化
        initView();
        //退出按钮
        mBtn=(ImageView) findViewById(R.id.title_back);
        mBtn.setOnClickListener(this);
//        //开始办理按钮
//        nextBtu=findViewById(R.id.start);
//        nextBtu.setOnClickListener(this);
    }

    private void initView(){


        //获取登陆人学号
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String usercode=sharedPreferences.getString("usercode","");
        Log.d("学号","学号"+usercode);
        Log.d("myapp","登陆标志"+sharedPreferences.getInt("logInFlag", 1));

        final String ip1 ="https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+usercode;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(ip1);
                            if("https".equalsIgnoreCase(url.getProtocol())){
                                SslUtil.ignoreSsl();
                            }
                            conn=(HttpURLConnection)url.openConnection();
                            conn.setRequestMethod("GET");
                            InputStream in = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String str;
                            while((str=reader.readLine()) != null){
                                response.append(str);
                                Log.d("查询", str);
                            }
                            String responseStr=response.toString();
                            Log.d("查询", "查询成功"+responseStr);

                            //将结果传给主线程
                            Message msg = new Message();
                            msg.what=RESEARCH;
                            msg.obj=responseStr;
                            mHandler.sendMessage(msg);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();
    }

    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case RESEARCH:
                    String responseStr=(String)msg.obj;
                    Log.d("myapp","查询结果"+responseStr);
                    try {
                        //          创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象)
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        data = obj.getString("data");

                        //第二层解析
                        JSONObject obj1 = new JSONObject(data);

                        //更新查询信息
                        xueHao=(TextView)findViewById(R.id.xuehao);
                        xingMing=(TextView)findViewById(R.id.name);
                        xingBie=(TextView)findViewById(R.id.sex);
                        yanZhengma=(TextView)findViewById(R.id.number);
                        nianJi=(TextView)findViewById(R.id.nianji);
                        xuanLouhao=(TextView)findViewById(R.id.louhao);
                        xuanSushehao=(TextView)findViewById(R.id.sushehao);

                        Log.d("myapp","解析结果"+obj1.getString("studentid"));

                        xueHao.setText("学号："+obj1.getString("studentid"));
                        xingMing.setText("姓名："+obj1.getString("name"));
                        xingBie.setText("性别："+obj1.getString("gender"));
                        yanZhengma.setText("验证码："+obj1.getString("vcode"));
                        nianJi.setText("年级："+obj1.getString("grade"));
                        int i = Integer.parseInt(obj1.getString("studentid"));
                        if(i%2==0){
                            xuanLouhao.setText("已选宿舍楼号："+obj1.getString("building"));
                            xuanSushehao.setText("宿舍号："+obj1.getString("room"));
                        }else{
                            xuanLouhao.setText("已选宿舍楼号：未选");
                            xuanSushehao.setText("宿舍号：未选");
                        }

                        // 存储解析结果
                        SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                        editor.putString("xingMing", obj1.getString("name"));
                        editor.putString("xingBie", obj1.getString("gender"));
                        editor.putString("yanZhengma", obj1.getString("vcode"));
                        editor.putString("xiaoQu", obj1.getString("location"));
                        editor.putString("nianJi", obj1.getString("grade"));
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.title_back){
            // 清除存储信息
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("logInFlag", 1);
            editor.putString("usercode", "");
            editor.putString("password",  "");
            editor.putString("xingMing","");
            editor.putString("xingBie", "");
            editor.putString("yanZhengma", "");
            editor.putString("xiaoQu", "");
            editor.putString("nianJi","");
            editor.commit();

            //跳转到登陆页
            Intent intent = new Intent(message.this, LoginActivity.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
        if(v.getId()==R.id.start){
            //跳转到页
            Intent intent = new Intent(message.this,LoginActivity.class);
            startActivity(intent);
        }

    }
}
