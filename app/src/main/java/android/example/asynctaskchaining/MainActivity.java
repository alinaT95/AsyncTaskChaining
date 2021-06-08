package android.example.asynctaskchaining;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.riversun.promise.Promise;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Promise.resolve("start")
                            .then(new Promise((action, data) -> {
                                runOnUiThread(() -> {
                                    new TestTask(new Callback(action::resolve, action::reject)).execute();
                                });
                            }))
                            .then(new Promise((action, data) -> {
                                runOnUiThread(() -> {
                                    System.out.println("Result from AsyncTask #1 = " + data);
                                    new TestTask(new Callback(action::resolve, action::reject)).execute();
                                });
                            }))
                            .always((action, data) -> {
                                runOnUiThread(() -> {
                                    System.out.println("Result from AsyncTask #2 = " + data);
                                });
                            })
                            .start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Error happened : " + e.getMessage());
                }
            }
        });
    }

    private class TestTask extends AsyncTask<Void, Void, String> {

        private AlertDialog alert = null;
        private Callback callback;

        public TestTask(Callback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Start test task...")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            alert = builder.create();
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            System.out.println("Start onPreExecute");
            if (alert != null) {
                alert.show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                System.out.println("Start doInBackground");
                Thread.sleep(10000);
                result = randomHexString(10);
            }
            catch (InterruptedException e){
                e.printStackTrace();
                callback.getReject().reject(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Start onPostExecute");
            callback.getResolve().resolve(result);
            if (alert != null) alert.dismiss();
            Toast.makeText(MainActivity.this, "Task finished", Toast.LENGTH_SHORT).show();
        }
    }



    public String randomHexString(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(HEX_DIGITS[random.nextInt(HEX_DIGITS.length)]);
        }
        return sb.toString();
    }
}