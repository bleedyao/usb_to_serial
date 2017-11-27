package cn.bleedyao.ftdeclibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.bleedyao.ftdevlibrary.core.FtDev;
import cn.bleedyao.ftdevlibrary.listen.CharConvertModel;
import cn.bleedyao.ftdevlibrary.listen.UpdateListener;
import cn.bleedyao.ftdevlibrary.role.MessageObserver;

public class MainActivity extends AppCompatActivity implements MessageObserver {
    private static final String TAG = "MainActivity";

    private Button mButton;
    private TextView mTextView;
    private Map<String, Integer> commandMap;
    private String lastUpdated = "";
    private UpdateListener<Map<String, Integer>> listener;
    private int maxAvailable = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commandMap = new HashMap<>();
        FtDev.init(this);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);
        FtDev.getInstance()
                .setBaudRate(115200)
                .setConvertModel(new CharConvertModel())
                .addObserver(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FtDev.getInstance().sendMessage("5A0000810178");
                listener = new UpdateListener<Map<String, Integer>>() {
                    @Override
                    public void success(Map<String, Integer> list) {
                        if (list == null) return;
                        for (Map.Entry<String, Integer> entry : list.entrySet()) {
                            Log.d(TAG, "success: " + entry);
                        }
//                        Log.d(TAG, "success: " + lastUpdated);
                    }

                    @Override
                    public void fail() {

                    }
                };

            }
        });
    }

    @Override
    public boolean filter(String message) {
        return true;
    }

    @Override
    public void receive(String message, int available) {
        available *= 2;
        if (available > maxAvailable) {
            maxAvailable = available;
        }
        if (message.length() > maxAvailable) {
            Log.d(TAG, "receive: " + maxAvailable);
            return;
        }
//        mTextView.setText(message);
//        Log.d(TAG, "receive: " + message);
        lastUpdated = message;
        if (commandMap.containsKey(message)) {
            int temp = commandMap.get(message);
            commandMap.put(message, temp + 1);
        } else {
            commandMap.put(message, 1);
        }
        listener.success(commandMap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FtDev.getInstance().close();
    }
}
