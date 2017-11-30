package cn.bleedyao.ftdeclibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import cn.bleedyao.ftdevlibrary.core.FtDev;
import cn.bleedyao.ftdevlibrary.lamp.LampConfig;
import cn.bleedyao.ftdevlibrary.listen.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LampConfig.init(this);
        LampConfig.getIntance().addObserver();
        FtDev.getInstance()
                .setBaudRate(115200)
                .setDataBit((byte) 8)
                .setFlowControl((byte) 0)
                .setParity((byte) 0)
                .setFlowControl((byte) 0)
                .setStopBit((byte) 1)
                .setCharsetName("ISO-8859-1");
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LampConfig.getIntance().sendMessage("AT+UID=?");

                LampConfig.getIntance().setUpdateListenr(new UpdateListener<String, Map<String,
                        Integer>>() {

                    @Override
                    public void lastest(String message) {
                        Log.d(TAG, "lastest: " + message);
                    }

                    @Override
                    public void history(Map<String, Integer> history) {
                        for (Map.Entry<String, Integer> entry : history.entrySet()) {
                            Log.d(TAG, "history: " + entry);
                        }
                        Log.d(TAG, " ========================================== ");
                    }

                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FtDev.getInstance().close();
    }
}
