package com.example.thefivetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         *      第一个实验
         *      对话框登录
         *
         *
         */
        Button tip = findViewById(R.id.tip);
        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "请登录", Toast.LENGTH_SHORT).show();
            }
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View view1 = layoutInflater.inflate(R.layout.dialogue_layout, null);
                builder.setView(view1);
                builder.setTitle("LOGIN");
                builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText2 = view1.findViewById(R.id.user_pwd);
                        String id, pwd;
                        EditText editText1 = view1.findViewById(R.id.user_id);
                        if (editText1 != null && editText2 != null) {
                            id = editText1.getText().toString();
                            pwd = editText2.getText().toString();
                            if (id.equals("123") && pwd.equals("abc")) {
                                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(MainActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.show();
            }
        });
        /*
         *      实验二
         *  在ativity之间的转跳，并拿到数据
         *
         *
         */
        Button turnTo = findViewById(R.id.turnTo);
        turnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, secondActivity.class);
                intent.putExtra("name", "张三");
                intent.putExtra("age", 20);
                startActivityForResult(intent, 1001);
            }
        });

        /*
         *   实验三
         * 实现intent的隐式启动
         * 转跳到acticity3
         *
         *
         * */
        Button turnTo3 = findViewById(R.id.turnTo3);
        turnTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("startAnotherActivity");
//                intent.addCategory("my_category");
                startActivity(intent);
            }
        });
        /*
         *   实验四
         * 实现向内部储存中写入和读取数据
         *
         * */

        Button writeTo = findViewById(R.id.writeToSD);
        writeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutputStream out;
                try {
                    FileOutputStream fos = openFileOutput("info.txt", MODE_PRIVATE);
                    out = new BufferedOutputStream(fos);
                    try {
                        out.write("zhangsan,IdNumber: 12345678".getBytes());
                    } finally {
                        out.close();
                    }
//                    fos.write("张三，学号：12345678".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        Button readBtn = findViewById(R.id.readToSD);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream in;
                try {
                    FileInputStream fileInputStream = openFileInput("info.txt");
                    in = new BufferedInputStream(fileInputStream);
                    int c;
                    StringBuilder stringBuilder = new StringBuilder("");
                    try {
                        while ((c = in.read()) != -1) {
                            stringBuilder.append((char) c);
                        }
                        Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                    } finally {
                        in.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*
         *
         *   实验五
         *使用handler处理消息
         *
         * */
        initData();

    }

    private static final String TAG = "TestHandlerActivity";
    //主线程中的handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获得刚才发送的Message对象，然后在这里进行UI操作
            Log.e(TAG, "------------> msg.what = " + msg.what);
        }
    };
    //子线程中的handler
    private Handler mHandlerThread = null;

    private void initData() {

        //开启一个线程模拟处理耗时的操作
        new Thread(new Runnable() {
            @Override
            public void run() {

                SystemClock.sleep(4*1000);
                //通过Handler发送一个消息切换回主线程（mHandler所在的线程）
                mHandler.sendEmptyMessage(0);

                //调用Looper.prepare（）方法
                Looper.prepare();

                //在子线程中创建Handler
                mHandlerThread = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("sub thread", "---------> msg.what = " + msg.what);
                    }
                };
                mHandlerThread.sendEmptyMessage(1);

                //调用Looper.loop（）方法
                Looper.loop();
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1001:
                if (resultCode == 1001) {
                    String con = data.getStringExtra("ok");
                    Toast.makeText(MainActivity.this, con, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
