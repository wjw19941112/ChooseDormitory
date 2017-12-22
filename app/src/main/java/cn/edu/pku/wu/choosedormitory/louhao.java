package cn.edu.pku.wu.choosedormitory;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;

/**
 * Created by wu on 2017/12/22.
 */

public class louhao extends Activity implements View.OnClickListener{

    private ImageView backForward2;
    private Button dorm5,dorm8,dorm9,dorm13,dorm14;
    private HttpURLConnection conn;
    private static final int SUCCESS =1;
    private int errcode=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.louhao);
    }

    @Override
    public void onClick(View view) {

    }
}
