package masterigis.com.jukebox2_0.ModeConnecte.FragmentsCo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Handler;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import is.arontibo.library.ElasticDownloadView;
import m1geii.com.jukebox2_0.R;
import masterigis.com.jukebox2_0.CountingFileRequestBody;
import masterigis.com.jukebox2_0.Mode_Participant;

import static android.os.Looper.getMainLooper;

public class Fragment_Upload extends Fragment {

    /**
     * Le lien du script, qu'on appellera pour uploader notre fichier
     */
    private static final String URL_UPLOAD = "http://jukeboxv20.olympe.in/serveur/upload.php";

    private final OkHttpClient client = new OkHttpClient();

    private static final String CONTENT_TYPE = "application/octet-stream";

    private static final int FILE_CODE = 9999;


    private static final String FORM_NAME = "file";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private ElasticDownloadView mElasticDownloadView;
    private Button button;

    private File fileToUpload;

    /**
     * la taile de notre fichier
     */
    private long totalSize = 0;

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_upload, container, false);
        // setContentView(R.layout.fragment_upload);
        handler = new Handler(getMainLooper());

        mElasticDownloadView = (ElasticDownloadView)v.findViewById(R.id.elastic_download_view);
        button = (Button)v.findViewById(R.id.btn_choose_file);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               Intent i = new Intent(getContext(), FilePickerActivity.class);

                // On ne permet que la sélection de UN seul fichier
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);

              /*  ///Test
                Intent intent=new Intent(getContext(),Mode_Participant.class);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivity(intent);*/
            }
        });


        return v;
    }
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();

            fileToUpload = new File(uri.getPath());

            try {
                upload(fileToUpload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void upload(final File file) throws Exception {

        new AsyncTask<Void, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mElasticDownloadView.startIntro();
            }

            @Override
            protected String doInBackground(Void... voids) {

                totalSize = file.length();

                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart(FORM_NAME, file.getName(),
                                new CountingFileRequestBody(file, CONTENT_TYPE, new CountingFileRequestBody.ProgressListener() {
                                    @Override
                                    public void transferred(long num) {

                                        final float progress = (num / (float) totalSize) * 100;

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onProgressUpdate((int) progress);
                                            }
                                        });
                                    }
                                }))
                        .build();

                Request request = new Request.Builder()
                        .url(URL_UPLOAD)
                        .post(requestBody)
                        .build();

                Response response = null;

                try {
                    // On exécute la requête
                    response = client.newCall(request).execute();

                    String responseStr = response.body().string();

                    return responseStr;


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                // On affiche le pourcentage d'ulpoad
                mElasticDownloadView.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    int success = Integer.valueOf(jsonObject.getString(TAG_SUCCESS));
                    String message = jsonObject.getString(TAG_MESSAGE);

                    // Si c'est 1 donc l'upload s'est bien faite
                    if (success == 1)
                        mElasticDownloadView.success();
                    else
                        mElasticDownloadView.fail();

                    // On affiche le message à l'utilisateur
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }.execute();

    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach () {
        super.onDetach();
    }
}
