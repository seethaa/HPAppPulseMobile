package apppulse.com.seetha.apppulsemobexample;

/*
 * FilterImages.java
 * Activity to handle Post-processing of images.
 * Separates good photos from blurred
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilterImages extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            System.out.println("LOG: Test ");
        }

        setContentView(R.layout.activity_filter_images);

        new CheckBlurTask().execute();

        // Call this function to delete all the files from raw folder
        // deleteFiles(fileNames);
    }

    /**
     * Async task to handle background processing of images in separate thread
     */
    private class CheckBlurTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String dir = Environment.getExternalStorageDirectory()
                        + File.separator + "AppPulseTutorial/";
                System.out.println("full path = "+dir);
                String dir1 = Environment.getExternalStorageDirectory()
                        + File.separator + "AppPulseTutorial/good/";
                System.out.println(dir);
                File folder = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "AppPulseTutorial");
                File[] listOfFiles = folder.listFiles();
                String files = new String();
                List<String> fileNames = new ArrayList<String>();
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        files = listOfFiles[i].getName();
                        System.out.println("----------" + dir + files);
                        boolean blurred = isBlurred(dir + files);
                        if (blurred == false) {

                            moveToFinal(files, dir, dir1);
                        } else {
                            fileNames.add(dir + files);
                        }
                    }
                }
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            Intent intent = new Intent(FilterImages.this, ViewPictures.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG)
                    .show();

            finish();


        }

        /**
         * Function to move good images to good folder
         *
         * @param imageName
         * @param sourceDirectory
         * @param destinationDirectory
         */
        private void moveToFinal(String imageName, String sourceDirectory,
                                 String destinationDirectory) {
            // TODO Auto-generated method stub
            try {

                File afile = new File(sourceDirectory + imageName);
                File bfile = new File(destinationDirectory + imageName);
                if (afile.renameTo(bfile)) {
                    System.out.println("File is moved successful!");
                } else {
                    System.out.println("File is failed to move!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * Function to check if the current image is blurred or not
         *
         * @param imageLocation
         * @return
         */
        boolean isBlurred(String imageLocation) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inDither = true;
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            File f = new File(imageLocation);
            File f1 = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "AppPulseTutorial/");

            Bitmap image = BitmapFactory.decodeFile(f.getAbsolutePath());
            Bitmap image1 = BitmapFactory.decodeFile(f.getAbsolutePath());
            int l = CvType.CV_8UC1; // 8-bit grey scale image
            Mat matImage = new Mat();
            Utils.bitmapToMat(image, matImage);
            Mat matImageGrey = new Mat();
            Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);

            Bitmap destImage;
            destImage = Bitmap.createBitmap(image);
            Mat dst2 = new Mat();
            Utils.bitmapToMat(destImage, dst2);
            Mat laplacianImage = new Mat();
            dst2.convertTo(laplacianImage, l);
            Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
            Mat laplacianImage8bit = new Mat();
            laplacianImage.convertTo(laplacianImage8bit, l);

            Bitmap bmp = Bitmap.createBitmap(laplacianImage8bit.cols(),
                    laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(laplacianImage8bit, bmp);
            int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
            bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(),
                    bmp.getHeight());

            int maxLap = -16777216;

            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] > maxLap)
                    maxLap = pixels[i];
            }

            int soglia = -400671;// -6118750; -93492394; //
            // blurred: -12500671
            // nonblurred: -8487298
            // medium blurred: -8947849
            if (maxLap < soglia || maxLap == soglia) {
                String result = "Blurred";
                System.out.println("img is: " + maxLap + " blurred");
                Log.d("DEBUG", "" + maxLap);
                return true;
            } else {
                String result = "Not blurred";
                Log.d("DEBUG", "" + maxLap);
                System.out.println("img is: " + maxLap + " not blurred");
                return false;
            }
        }
    }

    /**
     * Function to delete files provided to the function in a form of list of
     * all file names
     *
     * @param fileNames
     */
    public void deleteFiles(List<String> fileNames) {
        File fileToDelete;

        for (int i = 0; i < fileNames.size(); i++) {
            fileToDelete = new File(fileNames.get(i));
            if (fileToDelete.isFile()) {
                fileToDelete.delete();
            }
        }
    }
}
