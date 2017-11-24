package cn.bleedyao.ftdeclibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bleedyao.ftdevlibrary.exchange.FtDev;
import cn.bleedyao.ftdevlibrary.interfaces.role.MessageObserver;

public class MainActivity extends AppCompatActivity implements MessageObserver {

    private Button mButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FtDev.init(this);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);
        FtDev.getInstance().addObserver(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FtDev.getInstance().sendMessage("9876543210");
            }
        });

    }

    @Override
    public boolean filter(String message) {
        return true;
    }

    @Override
    public void receive(String message) {
        mTextView.setText(message);
    }
}
