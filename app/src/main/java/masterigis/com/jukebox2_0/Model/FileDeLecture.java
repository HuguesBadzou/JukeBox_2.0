package masterigis.com.jukebox2_0.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class FileDeLecture implements Parcelable {

    ArrayList<Chanson> laFileDeLecture;

    protected FileDeLecture(Parcel in) {
    }

    public static final Creator<FileDeLecture> CREATOR = new Creator<FileDeLecture>() {
        @Override
        public FileDeLecture createFromParcel(Parcel in) {
            return new FileDeLecture(in);
        }

        @Override
        public FileDeLecture[] newArray(int size) {
            return new FileDeLecture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
