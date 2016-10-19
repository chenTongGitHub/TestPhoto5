package edu.feicui.testphotocopy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    PopupWindow mPop;
    ImageView mIv;
    View mView;
    LinearLayout mLlCamera;
    LinearLayout mLlSetPhoto;
    /**
     * 权限请求码  ssss
     */
    public static final int GOTO_CAMERA=201;
    /**
     * 图库的请求码
     */
    public static final int  GOTO_PICK=203;
    public static final int REQUESTCODE_CUTTING=202;
    /**
     * 图片存储文件夹路径
     */
    public static final String DIR_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+
            File.separator+"EveryDayNews";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mView=getLayoutInflater().inflate(R.layout.photo_pop,null);
        mIv= (ImageView) findViewById(R.id.iv_main);
        mLlCamera= (LinearLayout) mView.findViewById(R.id.ll_camera_photo);
        mLlSetPhoto= (LinearLayout) mView.findViewById(R.id.ll_set_photo);
        mIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_main://更换头像点击事件
                mPop=new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPop.showAtLocation(mView, Gravity.BOTTOM,0,0);
                break;
            case R.id.ll_camera_photo://调用相机拍照  添加权限
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//6.0  api 23
                    //如果版本大于等于6.0  则需要检测手机有没有添加此权限
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                            &&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            ==PackageManager.PERMISSION_GRANTED){//有权限  直接调用相机

                    }else {//申请权限
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},GOTO_CAMERA);
                    }
                }else {
                    Intent takeIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File fileDir=new File(DIR_PATH);
                    if(!fileDir.exists()){//不存在 创建文件夹
                        fileDir.mkdirs();
                    }
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileDir));
                    startActivityForResult(takeIntent,GOTO_CAMERA);
                }
                break;
            case R.id.ll_set_photo://图库中选择

                Intent pickIntent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickIntent,GOTO_PICK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case GOTO_CAMERA:
                break;
            case GOTO_PICK:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop",true);
        //
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,REQUESTCODE_CUTTING);
	}

}
