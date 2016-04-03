package vid.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by SSubra27 on 12/8/15.
 */
public class PhotoDialogPickerFragment extends DialogFragment {
    private static final String PHOTO_ARG = "photo";


    public static PhotoDialogPickerFragment newInstance(File mPhotoFile)
    {
        Bundle args = new Bundle();
        args.putSerializable(PHOTO_ARG, mPhotoFile);
        PhotoDialogPickerFragment fragment = new PhotoDialogPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        File photoFile =(File)getArguments().getSerializable(PHOTO_ARG);
        Bitmap image = PictureUtils.getScaledBitmap(photoFile.getPath(),getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v =layoutInflater.inflate(R.layout.camera_photo_view, null);
        ImageView imageView =(ImageView)v.findViewById(R.id.suspect_image);
        imageView.setImageBitmap(image);
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Photo View").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
    }
}
