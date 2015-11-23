package apppulse.com.seetha.apppulsemobexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

public class ViewPictures extends Activity {

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    GridViewAdapter adapter;
    File file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_main);

        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found.", Toast.LENGTH_LONG)
                    .show();
        } else {
            // get image folder
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "AppPulseTutorial/good");
            System.out.println("file path: "+file.toString());

            file.mkdirs();
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

//          ArrayList<String> newFilePathStrings = new ArrayList<String>();
//            ArrayList<String> newFileNameStrings = new ArrayList<String>();


            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();

                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();

                System.out.println("check path: " +FilePathStrings[i]);

            }
        }
        grid = (GridView) findViewById(R.id.gridview);

        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        // set LazyAdapter to the GridView
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //extras for file information
                Intent i = new Intent(ViewPictures.this, ViewImage.class);
                i.putExtra("filepath", FilePathStrings);
                i.putExtra("filename", FileNameStrings);
                i.putExtra("position", position);
                startActivity(i);
            }

        });
    }




}