package cn.edu.pku.wu.choosedormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by wu on 2017/12/23.
 */

public class success extends Activity implements View.OnClickListener{
    private Button mBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        mBtn = (Button)findViewById(R.id.bt_go_home);
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_go_home){
            SharedPreferences sharedPreferences = getSharedPreferences("config",Activity.MODE_PRIVATE);
            String xueHao=sharedPreferences.getString("username","");
            Intent intent = new Intent(success.this, message.class);
            intent.putExtra("username",xueHao);
            setResult(RESULT_OK,intent);
            startActivity(intent);
            //关闭当前界面
            finish();
        }

    }
}
