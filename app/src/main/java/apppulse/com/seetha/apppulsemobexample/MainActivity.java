package apppulse.com.seetha.apppulsemobexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {
    private static TextView textView;
    private static String DUBUG_TAG = "EXAMPLE_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void httpGet(View view) {
        Intent intent = new Intent(this, HTTPGetShowResponse.class);
        startActivity(intent);

    }

    public void getImages(View view) {
        Intent intent = new Intent(this, FilterImages.class);
        startActivity(intent);

    }


    public static Bitmap loadImageFromUrl(String url) {
        InputStream inputStream;Bitmap b;
        try {
            inputStream = (InputStream) new URL(url).getContent();
            BitmapFactory.Options bpo=  new BitmapFactory.Options();
            if(bpo.outWidth>500)
            {
                bpo.inSampleSize=8;
                b=BitmapFactory.decodeStream(inputStream, null,bpo );
            }
            else
            {
                bpo.inSampleSize=2;
                b=BitmapFactory.decodeStream(inputStream, null,bpo );
            }
            return  b;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
