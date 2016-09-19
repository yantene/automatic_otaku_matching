package tempakunoshiro.automaticotakumatching;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 2016/09/19.
 */
@DatabaseTable(tableName = "tagger")
public class MyTagger implements Parcelable {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    private long tagId;
    @DatabaseField(canBeNull = false, uniqueCombo = true)
    private long userId;

    private MyTagger(){}

    protected MyTagger(Parcel in) {
        id = in.readLong();
        tagId = in.readLong();
        userId = in.readLong();
    }

    public MyTagger(long tagId, long userId) {
        this.tagId = tagId;
        this.userId = userId;
    }

    @Nullable
    public static List<String> getTagListFromId(Context context, long userId) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context.getApplicationContext());
        List<String> tagList = new ArrayList<>();
        try {
            Dao taggerDao =  dbHelper.getDao(MyTagger.class);
            QueryBuilder<MyTagger, Integer> queryBuilder = taggerDao.queryBuilder();
            queryBuilder.where().eq("userId", userId);
            List<MyTagger> taggers = queryBuilder.query();
            for(MyTagger tgg : taggers){
                Dao tagDao =  dbHelper.getDao(MyTag.class);
                QueryBuilder<MyTag, Integer> queryBuilder2 = tagDao.queryBuilder();
                queryBuilder2.where().eq("id", tgg.getTagId());
                List<MyTag> tags = queryBuilder2.query();
                for(MyTag t : tags){
                    tagList.add(t.getTag());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    public static final Creator<MyTagger> CREATOR = new Creator<MyTagger>() {
        @Override
        public MyTagger createFromParcel(Parcel in) {
            return new MyTagger(in);
        }

        @Override
        public MyTagger[] newArray(int size) {
            return new MyTagger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tagId);
        dest.writeLong(userId);
    }

    public long getId() {
        return id;
    }

    public long getTagId() {
        return tagId;
    }

    public long getUserId() {
        return userId;
    }
}
