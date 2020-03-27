package com.prembros.symptomator.db;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.prembros.symptomator.MuteJob;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class JobsDao_Impl implements JobsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMuteJob;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMuteJob;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfMuteJob;

  public JobsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMuteJob = new EntityInsertionAdapter<MuteJob>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `job`(`id`,`startTime`,`endTime`,`isFirstTimeStart`,`isFirstTimeEnd`,`repeatMode`,`repeatDays`,`eventLocation`,`eventTitle`,`isBusiness`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MuteJob value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        stmt.bindLong(2, value.getStartTime());
        stmt.bindLong(3, value.getEndTime());
        stmt.bindLong(4, value.getIsFirstTimeStart());
        stmt.bindLong(5, value.getIsFirstTimeEnd());
        stmt.bindLong(6, value.getRepeatMode());
        final String _tmp;
        _tmp = Converters.fromArray(value.getRepeatDays());
        if (_tmp == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp);
        }
        if (value.getEventLocation() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getEventLocation());
        }
        if (value.getEventTitle() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getEventTitle());
        }
        final int _tmp_1;
        _tmp_1 = value.isBusiness() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
      }
    };
    this.__deletionAdapterOfMuteJob = new EntityDeletionOrUpdateAdapter<MuteJob>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `job` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MuteJob value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfMuteJob = new EntityDeletionOrUpdateAdapter<MuteJob>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `job` SET `id` = ?,`startTime` = ?,`endTime` = ?,`isFirstTimeStart` = ?,`isFirstTimeEnd` = ?,`repeatMode` = ?,`repeatDays` = ?,`eventLocation` = ?,`eventTitle` = ?,`isBusiness` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MuteJob value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        stmt.bindLong(2, value.getStartTime());
        stmt.bindLong(3, value.getEndTime());
        stmt.bindLong(4, value.getIsFirstTimeStart());
        stmt.bindLong(5, value.getIsFirstTimeEnd());
        stmt.bindLong(6, value.getRepeatMode());
        final String _tmp;
        _tmp = Converters.fromArray(value.getRepeatDays());
        if (_tmp == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp);
        }
        if (value.getEventLocation() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getEventLocation());
        }
        if (value.getEventTitle() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getEventTitle());
        }
        final int _tmp_1;
        _tmp_1 = value.isBusiness() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getId());
        }
      }
    };
  }

  @Override
  public void insert(MuteJob muteJob) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMuteJob.insert(muteJob);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(MuteJob myVideo) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfMuteJob.handle(myVideo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(MuteJob myVideo) {
    __db.beginTransaction();
    try {
      __updateAdapterOfMuteJob.handle(myVideo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<MuteJob>> getAllJobs() {
    final String _sql = "SELECT * from job ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<MuteJob>>() {
      private Observer _observer;

      @Override
      protected List<MuteJob> compute() {
        if (_observer == null) {
          _observer = new Observer("job") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfStartTime = _cursor.getColumnIndexOrThrow("startTime");
          final int _cursorIndexOfEndTime = _cursor.getColumnIndexOrThrow("endTime");
          final int _cursorIndexOfIsFirstTimeStart = _cursor.getColumnIndexOrThrow("isFirstTimeStart");
          final int _cursorIndexOfIsFirstTimeEnd = _cursor.getColumnIndexOrThrow("isFirstTimeEnd");
          final int _cursorIndexOfRepeatMode = _cursor.getColumnIndexOrThrow("repeatMode");
          final int _cursorIndexOfRepeatDays = _cursor.getColumnIndexOrThrow("repeatDays");
          final int _cursorIndexOfEventLocation = _cursor.getColumnIndexOrThrow("eventLocation");
          final int _cursorIndexOfEventTitle = _cursor.getColumnIndexOrThrow("eventTitle");
          final int _cursorIndexOfIsBusiness = _cursor.getColumnIndexOrThrow("isBusiness");
          final List<MuteJob> _result = new ArrayList<MuteJob>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MuteJob _item;
            _item = new MuteJob();
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            _item.setStartTime(_tmpStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            _item.setEndTime(_tmpEndTime);
            final int _tmpIsFirstTimeStart;
            _tmpIsFirstTimeStart = _cursor.getInt(_cursorIndexOfIsFirstTimeStart);
            _item.setIsFirstTimeStart(_tmpIsFirstTimeStart);
            final int _tmpIsFirstTimeEnd;
            _tmpIsFirstTimeEnd = _cursor.getInt(_cursorIndexOfIsFirstTimeEnd);
            _item.setIsFirstTimeEnd(_tmpIsFirstTimeEnd);
            final int _tmpRepeatMode;
            _tmpRepeatMode = _cursor.getInt(_cursorIndexOfRepeatMode);
            _item.setRepeatMode(_tmpRepeatMode);
            final boolean[] _tmpRepeatDays;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRepeatDays);
            _tmpRepeatDays = Converters.fromString(_tmp);
            _item.setRepeatDays(_tmpRepeatDays);
            final String _tmpEventLocation;
            _tmpEventLocation = _cursor.getString(_cursorIndexOfEventLocation);
            _item.setEventLocation(_tmpEventLocation);
            final String _tmpEventTitle;
            _tmpEventTitle = _cursor.getString(_cursorIndexOfEventTitle);
            _item.setEventTitle(_tmpEventTitle);
            final boolean _tmpIsBusiness;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBusiness);
            _tmpIsBusiness = _tmp_1 != 0;
            _item.setBusiness(_tmpIsBusiness);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }

  @Override
  public MuteJob getJobById(String id) {
    final String _sql = "SELECT * from job WHERE id =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfStartTime = _cursor.getColumnIndexOrThrow("startTime");
      final int _cursorIndexOfEndTime = _cursor.getColumnIndexOrThrow("endTime");
      final int _cursorIndexOfIsFirstTimeStart = _cursor.getColumnIndexOrThrow("isFirstTimeStart");
      final int _cursorIndexOfIsFirstTimeEnd = _cursor.getColumnIndexOrThrow("isFirstTimeEnd");
      final int _cursorIndexOfRepeatMode = _cursor.getColumnIndexOrThrow("repeatMode");
      final int _cursorIndexOfRepeatDays = _cursor.getColumnIndexOrThrow("repeatDays");
      final int _cursorIndexOfEventLocation = _cursor.getColumnIndexOrThrow("eventLocation");
      final int _cursorIndexOfEventTitle = _cursor.getColumnIndexOrThrow("eventTitle");
      final int _cursorIndexOfIsBusiness = _cursor.getColumnIndexOrThrow("isBusiness");
      final MuteJob _result;
      if(_cursor.moveToFirst()) {
        _result = new MuteJob();
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        _result.setStartTime(_tmpStartTime);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _result.setEndTime(_tmpEndTime);
        final int _tmpIsFirstTimeStart;
        _tmpIsFirstTimeStart = _cursor.getInt(_cursorIndexOfIsFirstTimeStart);
        _result.setIsFirstTimeStart(_tmpIsFirstTimeStart);
        final int _tmpIsFirstTimeEnd;
        _tmpIsFirstTimeEnd = _cursor.getInt(_cursorIndexOfIsFirstTimeEnd);
        _result.setIsFirstTimeEnd(_tmpIsFirstTimeEnd);
        final int _tmpRepeatMode;
        _tmpRepeatMode = _cursor.getInt(_cursorIndexOfRepeatMode);
        _result.setRepeatMode(_tmpRepeatMode);
        final boolean[] _tmpRepeatDays;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfRepeatDays);
        _tmpRepeatDays = Converters.fromString(_tmp);
        _result.setRepeatDays(_tmpRepeatDays);
        final String _tmpEventLocation;
        _tmpEventLocation = _cursor.getString(_cursorIndexOfEventLocation);
        _result.setEventLocation(_tmpEventLocation);
        final String _tmpEventTitle;
        _tmpEventTitle = _cursor.getString(_cursorIndexOfEventTitle);
        _result.setEventTitle(_tmpEventTitle);
        final boolean _tmpIsBusiness;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsBusiness);
        _tmpIsBusiness = _tmp_1 != 0;
        _result.setBusiness(_tmpIsBusiness);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<MuteJob> getJobs() {
    final String _sql = "SELECT * from job";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfStartTime = _cursor.getColumnIndexOrThrow("startTime");
      final int _cursorIndexOfEndTime = _cursor.getColumnIndexOrThrow("endTime");
      final int _cursorIndexOfIsFirstTimeStart = _cursor.getColumnIndexOrThrow("isFirstTimeStart");
      final int _cursorIndexOfIsFirstTimeEnd = _cursor.getColumnIndexOrThrow("isFirstTimeEnd");
      final int _cursorIndexOfRepeatMode = _cursor.getColumnIndexOrThrow("repeatMode");
      final int _cursorIndexOfRepeatDays = _cursor.getColumnIndexOrThrow("repeatDays");
      final int _cursorIndexOfEventLocation = _cursor.getColumnIndexOrThrow("eventLocation");
      final int _cursorIndexOfEventTitle = _cursor.getColumnIndexOrThrow("eventTitle");
      final int _cursorIndexOfIsBusiness = _cursor.getColumnIndexOrThrow("isBusiness");
      final List<MuteJob> _result = new ArrayList<MuteJob>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MuteJob _item;
        _item = new MuteJob();
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        _item.setStartTime(_tmpStartTime);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final int _tmpIsFirstTimeStart;
        _tmpIsFirstTimeStart = _cursor.getInt(_cursorIndexOfIsFirstTimeStart);
        _item.setIsFirstTimeStart(_tmpIsFirstTimeStart);
        final int _tmpIsFirstTimeEnd;
        _tmpIsFirstTimeEnd = _cursor.getInt(_cursorIndexOfIsFirstTimeEnd);
        _item.setIsFirstTimeEnd(_tmpIsFirstTimeEnd);
        final int _tmpRepeatMode;
        _tmpRepeatMode = _cursor.getInt(_cursorIndexOfRepeatMode);
        _item.setRepeatMode(_tmpRepeatMode);
        final boolean[] _tmpRepeatDays;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfRepeatDays);
        _tmpRepeatDays = Converters.fromString(_tmp);
        _item.setRepeatDays(_tmpRepeatDays);
        final String _tmpEventLocation;
        _tmpEventLocation = _cursor.getString(_cursorIndexOfEventLocation);
        _item.setEventLocation(_tmpEventLocation);
        final String _tmpEventTitle;
        _tmpEventTitle = _cursor.getString(_cursorIndexOfEventTitle);
        _item.setEventTitle(_tmpEventTitle);
        final boolean _tmpIsBusiness;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsBusiness);
        _tmpIsBusiness = _tmp_1 != 0;
        _item.setBusiness(_tmpIsBusiness);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
