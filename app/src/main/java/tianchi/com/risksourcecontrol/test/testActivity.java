package tianchi.com.risksourcecontrol.test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.util.CameraUtils;

public class testActivity extends BaseActivity {
//    TextView tvJD, tvWD;
//    public static final String URL = "http://192.168.0.21:8080/risk/riskmanager/JsonObjecTrans.json";
//    public static final int PORT = 8080;
//    public static String USERINFOURL = "/risk/login/getInfo.json";//登陆信息是否一致
//    public static String GetRiskInfo = "/risk/riskmanager/getRiskInfo.json"; //获取风险源信息
//
//    public static String User_Test = "/risk/login/getInfoTest.json";
//    public static String Check_Login = "/risk/login/getInfo.json";
//
//    public static String CheckRiskInfoByVague = "/risk/riskmanager/getCheckInfo.json"; //模糊查询
//    public String jsonString = "";
//    private TextView tvResult;
//    OkHttpClient m_client;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        img = (ImageView) findViewById(R.id.img);
        //        tvJD = (TextView) findViewById(R.id.tvJD);
        //        tvWD = (TextView) findViewById(R.id.tvWD);
        //        Intent _intent = getIntent();
        //        tvJD.setText(_intent.getStringExtra("JD"));
        //        tvWD.setText(_intent.getStringExtra("WD"));
//        tvResult = (TextView) findViewById(R.id.tvResult);
//        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvResult.setText("");
//            }
//        });
//        m_client = new OkHttpClient();
//        FormBody.Builder _builder = new FormBody.Builder();
        //        _builder.add("id", "test");
        //        _builder.add("pwd", "admin");

//        RequestBody _requestBody = _builder.build();
//        FormBody _body = _builder.build();
//        Request _request = new Request.Builder()
//                .url(URL)
//                .post(_body)
//                .build();
//        HttpUrl _httpUrl = _request.url().newBuilder()
//                .addQueryParameter("id","test")
//                .addQueryParameter("pwd","admin")
//                .build();
//        Request _request1 = _request.newBuilder()
//                .method(_request.method(), _request.body())
//                .url(_httpUrl)
//                .build();
        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                _intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraUtils.fileUri);
                startActivityForResult(_intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
            }
        });


//        TestBean _testBean = new TestBean();
//        _testBean.setId("test");
//        _testBean.setPwd("admin");

//        JSONObject _jsonObject = new JSONObject();
//        byte[] img = new byte[]{1,2,3,3};
//        try {
//            _jsonObject.put("date", new Date(System.currentTimeMillis()));
//            _jsonObject.put("picture", img);
//            TestBean t = GsonUtils.parseJson2Bean(_jsonObject, TestBean.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap _bitmap = CameraUtils.getBitmapFromCG(this, requestCode, resultCode, data,999,998);
//        img.setImageBitmap(_bitmap);
    }
}
