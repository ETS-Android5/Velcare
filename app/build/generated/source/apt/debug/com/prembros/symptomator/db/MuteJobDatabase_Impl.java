package com.prembros.symptomator.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class MuteJobDatabase_Impl extends MuteJobDatabase {
  private volatile JobsDao _jobsDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `job` (`id` TEXT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `isFirstTimeStart` INTEGER NOT NULL, `isFirstTimeEnd` INTEGER NOT NULL, `repeatMode` INTEGER NOT NULL, `repeatDays` TEXT, `eventLocation` TEXT, `eventTitle` TEXT, `isBusiness` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ee373a61b4db50bedb753e6c933d433f\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `job`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsJob = new HashMap<String, TableInfo.Column>(10);
        _columnsJob.put("id", new TableInfo.Column("id", "TEXT", true, 1));
        _columnsJob.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0));
        _columnsJob.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0));
        _columnsJob.put("isFirstTimeStart", new TableInfo.Column("isFirstTimeStart", "INTEGER", true, 0));
        _columnsJob.put("isFirstTimeEnd", new TableInfo.Column("isFirstTimeEnd", "INTEGER", true, 0));
        _columnsJob.put("repeatMode", new TableInfo.Column("repeatMode", "INTEGER", true, 0));
        _columnsJob.put("repeatDays", new TableInfo.Column("repeatDays", "TEXT", false, 0));
        _columnsJob.put("eventLocation", new TableInfo.Column("eventLocation", "TEXT", false, 0));
        _columnsJob.put("eventTitle", new TableInfo.Column("eventTitle", "TEXT", false, 0));
        _columnsJob.put("isBusiness", new TableInfo.Column("isBusiness", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJob = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesJob = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoJob = new TableInfo("job", _columnsJob, _foreignKeysJob, _indicesJob);
        final TableInfo _existingJob = TableInfo.read(_db, "job");
        if (! _infoJob.equals(_existingJob)) {
          throw new IllegalStateException("Migration didn't properly handle job(com.prembros.symptomator.MuteJob).\n"
                  + " Expected:\n" + _infoJob + "\n"
                  + " Found:\n" + _existingJob);
        }
      }
    }, "ee373a61b4db50bedb753e6c933d433f", "c1ca8987abd207350c674666c5f9e4f0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "job");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `job`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public JobsDao jobsDao() {
    if (_jobsDao != null) {
      return _jobsDao;
    } else {
      synchronized(this) {
        if(_jobsDao == null) {
          _jobsDao = new JobsDao_Impl(this);
        }
        return _jobsDao;
      }
    }
  }
}
