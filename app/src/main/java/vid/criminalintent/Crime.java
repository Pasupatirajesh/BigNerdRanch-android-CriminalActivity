package vid.criminalintent;

import java.util.Date;
import java.util.UUID;
/**
 * Created by SSubra27 on 9/30/15.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Date mTime;
    private boolean mSolved;
    private String mSuspect;
    private long mCaller;


    public Crime() {
        this(UUID.randomUUID());

//        mId=UUID.randomUUID();
//        mDate=new Date();
//        mTime =new Date();

    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
        mTime = new Date();
    }


    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public long getCaller() {
        return mCaller;
    }

    public void setCaller(long caller) {
        mCaller = caller;
    }
    public String getPhotoFilename()
    {
        return "IMG_"+ getId().toString()+ ".jpg";
    }

}
