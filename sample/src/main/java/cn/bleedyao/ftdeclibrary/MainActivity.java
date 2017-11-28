package cn.bleedyao.ftdeclibrary;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import cn.bleedyao.ftdevlibrary.core.FtDev;
import cn.bleedyao.ftdevlibrary.lamp.LampConfig;
import cn.bleedyao.ftdevlibrary.listen.CharConvertModel;
import cn.bleedyao.ftdevlibrary.listen.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mButton;
    private TextView mTextView;

    private CountDownTimer timer = new CountDownTimer(60 * 60 * 1000, 3000) {
        @Override
        public void onTick(final long millisUntilFinished) {
            LampConfig.getIntance().sendMessage("5A0000810178");

            LampConfig.getIntance().setUpdateListenr(new UpdateListener<String, Map<String,
                    Integer>>() {

                @Override
                public void lastest(String message) {
                    Log.d(TAG, "lastest: " + message);
                }

                @Override
                public void history(Map<String, Integer> history) {
                    float correct = 0;
                    for (Map.Entry<String, Integer> entry : history.entrySet()) {
                        Log.d(TAG, "history: " + entry);
                        if (entry.getKey().length() == 52) {
                            correct += 1;
                        }
                    }
                    Log.d(TAG, " =====" + millisUntilFinished +
                            "==========准确率：" + (correct / history.size()) * 100 +
                            "%============================== ");
                }

            });
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "onFinish: ");
        }
    };
    ;

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
                .setConvertModel(new CharConvertModel())
                .setCharsetName("ISO-8859-1");
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);

        timer.start();


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LampConfig.getIntance().sendMessage("5A0000810178");

//                LampConfig.getIntance().setUpdateListenr(new UpdateListener<String, Map<String,
//                        Integer>>() {
//
//                    @Override
//                    public void lastest(String message) {
//                        Log.d(TAG, "lastest: " + message);
//                    }
//
//                    @Override
//                    public void history(Map<String, Integer> history) {
//                        for (Map.Entry<String, Integer> entry : history.entrySet()) {
//                            Log.d(TAG, "history: " + entry);
//                        }
//                        Log.d(TAG, " ========================================== ");
//                    }
//
//                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        FtDev.getInstance().close();
    }
}
