package example.thiyagu.com.cert;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

   static String currentSignature;
   static String decryptedText;
    private static Sample[] mSamples;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(validateAppSignature())
        {
            Toast.makeText(getApplicationContext(), "Securtiy Verified Success for Testing ", Toast.LENGTH_LONG).show();
            // Instantiate the list of samples.


        }
        else
        {

            Toast.makeText(getApplicationContext(), "Security Violation APK Tampered", Toast.LENGTH_LONG).show();

        }
    }

    private void setListAdapter(ArrayAdapter<Sample> sampleArrayAdapter) {
    }

    public boolean validateAppSignature()
    {
        Context context = getApplicationContext();
        try {

            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            Signature[] appSignatures = packageInfo.signatures;

            for (Signature signature : appSignatures)
            {
                byte[] signatureBytes = signature.toByteArray();

                currentSignature = calcSHA1(signatureBytes);
                String key = currentSignature.substring(0,16);
                System.out.println(key);
                InputStream iStream = context.getResources().openRawResource(R.raw.text);
                ByteArrayOutputStream byteStream = null;


                try {
                    byte[] buffer = new byte[iStream.available()];
                    iStream.read(buffer);
                    byteStream = new ByteArrayOutputStream();
                    byteStream.write(buffer);
                    byteStream.close();
                    Log.d("mytag", byteStream.toString());
                    String encrypted =  byteStream.toString();
                    AesEnc aesenc = new AesEnc();
                    String decryptedText = aesenc.decrypt(encrypted,key.getBytes());
                    System.out.println("decrypted TExt  "+decryptedText);
                    Log.d("mytag",decryptedText);
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private static String calcSHA1(byte[] signature)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(signature);
        byte[] signatureHash = digest.digest();
        return bytesToHex(signatureHash);
    }

    public static String bytesToHex(byte[] bytes)
    {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6',
                '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++)
        {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private class Sample {
        private CharSequence title;
        private Class<? extends Activity> activityClass;

        public Sample(int titleResId, Class<? extends Activity> activityClass) {
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }

}

