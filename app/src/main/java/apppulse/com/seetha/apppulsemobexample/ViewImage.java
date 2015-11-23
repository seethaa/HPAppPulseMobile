package apppulse.com.seetha.apppulsemobexample;

/**
 * Created by seethaa on 11/5/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewImage extends Activity {
    TextView text;
    ImageView imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        Intent i = getIntent();

        int position = i.getExtras().getInt("position");

        String[] filepath = i.getStringArrayExtra("filepath");
        String[] filename = i.getStringArrayExtra("filename");
        text = (TextView) findViewById(R.id.imagetext);

        text.setText(filename[position]);

        imageview = (ImageView) findViewById(R.id.full_image_view);

        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        imageview.setImageBitmap(bmp);

    }
}