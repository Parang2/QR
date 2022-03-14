package kr.example.qrcode_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    TextView tv1;
    Button btn1;

    /**
     * 스캔
     * - 앱을 실행 시 사용자에게 촬영권한을 요청하고 바로 띄워지는 스캔화면이며 QR코드를 스캔할 수 있습니다.
     *
     * 결과
     * - 스캔에 성공했을 경우 스캔 결과가 http가 포함되어 있다면 주소로 판단하여 해당 주소로 이동합니다.
     *   취소할 경우 Toast 메세지로 취소되었음을 사용자에게 알립니다.
     *
     * 스캔하기버튼
     * - 버튼 클릭 시 다시 스캔 준비를 합니다.
     *
     * @param savedInstanceState
     *
     * 클래스 : MainActivity
     * 시작라인 : 62
     * 끝라인 : 92
     *
     *
     */

    // 참고 : https://m.blog.naver.com/cosmosjs/220937443111
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.bt1);

        // 스캔하기 버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 재시작
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        qrScan = new IntentIntegrator(this);    // 객체 생성
        qrScan.setOrientationLocked(true);             // 화면 고정
        qrScan.initiateScan();                         // scanner 실행
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        tv1 = (TextView) findViewById(R.id.tv1);
        if(result != null) {
            // scan을 하지 않았다면
            if(result.getContents() == null) {
                // cancel을 띄워줌
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            // scan을 했다면
            else {
                // scan한 값을 토스트 메시지로 띄워줌
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // 만약 반환값이 url일 경우
                try {
                    if(result.getContents().indexOf("http") != -1){
                        // url로 연결한다
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                tv1.setText(result.getContents());  // 스캔 결과를 텍스트뷰에 띄워준다.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}