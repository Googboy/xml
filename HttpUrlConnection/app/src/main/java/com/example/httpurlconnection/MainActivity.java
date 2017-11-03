package com.example.httpurlconnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private ImageView imageView;
    private static final int LOAD_SUCCESS = 1;
    private static final int LOAD_ERROR = -1;
    //耗时操作采用异步处理：下面的是用于异步的显示图片
    private Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_SUCCESS:
                    File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");//获取图片的文件对象
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOAD_ERROR:
                    Toast.makeText(mContext,"加载失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageView);
        Button show = findViewById(R.id.btnShow);
        show.setOnClickListener(this);
    }

    private void getPicture() {
        URL url;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            url = new URL("http://192.168.12.100:8080/Test/1.jpg");//本机地址IP
            //url = new URL("http://10.0.2.2:8080/Test/1.jpg");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//打开连接
            conn.setConnectTimeout(8000);//设置8秒以后超时
            conn.setRequestMethod("GET");//设置请求方法为GET
            if (conn.getResponseCode() == 200){  //响应码为200 则访问成功
                is = conn.getInputStream();//获取连接的输入流，这个输入流就是图片的输入流
                File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");//构建一个file对象用于存储图片
                fos = new FileOutputStream(file);
                int length = 0;
                byte[] buffer = new byte[1024];
                while ((length = is.read(buffer))!= -1){//将输入流写入到我们定义好的文件中
                    fos.write(buffer,0,length);
                }
                fos.flush();//将缓冲输入文件
                handle.sendEmptyMessage(LOAD_SUCCESS);
            }
        } catch (IOException e) {
            handle.sendEmptyMessage(LOAD_ERROR);
            e.printStackTrace();
        }finally {//最后将流关闭
            try {
                if (is != null){
                    is.close();
                }
                if (fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                handle.sendEmptyMessage(LOAD_ERROR);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPicture();
            }
        }).start();
    }

}
