package com.example.note.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteData implements Parcelable {
    private final String name;
    private final String description;
    private final Date createDate;

    public NoteData(String name, String description, Date createDate) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
    }

    protected NoteData(Parcel in) {
        name = in.readString();
        description = in.readString();
        createDate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(createDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }

}
