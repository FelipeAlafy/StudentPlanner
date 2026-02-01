package net.felipealafy.studentplanner.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import net.felipealafy.studentplanner.tablemodels.SubjectTable;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubjectDao_Impl implements SubjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubjectTable> __insertionAdapterOfSubjectTable;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<SubjectTable> __deletionAdapterOfSubjectTable;

  private final EntityDeletionOrUpdateAdapter<SubjectTable> __updateAdapterOfSubjectTable;

  public SubjectDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubjectTable = new EntityInsertionAdapter<SubjectTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `subject` (`id`,`plannerId`,`name`,`color`,`start`,`end`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPlannerId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getColor());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfSubjectTable = new EntityDeletionOrUpdateAdapter<SubjectTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `subject` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectTable entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfSubjectTable = new EntityDeletionOrUpdateAdapter<SubjectTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subject` SET `id` = ?,`plannerId` = ?,`name` = ?,`color` = ?,`start` = ?,`end` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPlannerId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getColor());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        statement.bindString(7, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final SubjectTable subjectTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubjectTable.insert(subjectTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final SubjectTable subjectTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSubjectTable.handle(subjectTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SubjectTable subjectTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubjectTable.handle(subjectTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubjectTable>> getAllSubjects() {
    final String _sql = "SELECT * FROM subject ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subject"}, new Callable<List<SubjectTable>>() {
      @Override
      @NonNull
      public List<SubjectTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannerId = CursorUtil.getColumnIndexOrThrow(_cursor, "plannerId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<SubjectTable> _result = new ArrayList<SubjectTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlannerId;
            _tmpPlannerId = _cursor.getString(_cursorIndexOfPlannerId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpColor;
            _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            final LocalDateTime _tmpStart;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStart);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStart = _tmp_1;
            }
            final LocalDateTime _tmpEnd;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEnd);
            }
            final LocalDateTime _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEnd = _tmp_3;
            }
            _item = new SubjectTable(_tmpId,_tmpPlannerId,_tmpName,_tmpColor,_tmpStart,_tmpEnd);
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
    });
  }

  @Override
  public Flow<List<SubjectTable>> getSubjectsOfAPlanner(final String plannerId) {
    final String _sql = "SELECT * FROM subject WHERE plannerId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, plannerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subject"}, new Callable<List<SubjectTable>>() {
      @Override
      @NonNull
      public List<SubjectTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannerId = CursorUtil.getColumnIndexOrThrow(_cursor, "plannerId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<SubjectTable> _result = new ArrayList<SubjectTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlannerId;
            _tmpPlannerId = _cursor.getString(_cursorIndexOfPlannerId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpColor;
            _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            final LocalDateTime _tmpStart;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStart);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStart = _tmp_1;
            }
            final LocalDateTime _tmpEnd;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEnd);
            }
            final LocalDateTime _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEnd = _tmp_3;
            }
            _item = new SubjectTable(_tmpId,_tmpPlannerId,_tmpName,_tmpColor,_tmpStart,_tmpEnd);
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
    });
  }

  @Override
  public Flow<List<SubjectTable>> getSubjectById(final String subjectId) {
    final String _sql = "SELECT * FROM subject WHERE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subject"}, new Callable<List<SubjectTable>>() {
      @Override
      @NonNull
      public List<SubjectTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannerId = CursorUtil.getColumnIndexOrThrow(_cursor, "plannerId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<SubjectTable> _result = new ArrayList<SubjectTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlannerId;
            _tmpPlannerId = _cursor.getString(_cursorIndexOfPlannerId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpColor;
            _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            final LocalDateTime _tmpStart;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStart);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStart = _tmp_1;
            }
            final LocalDateTime _tmpEnd;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEnd);
            }
            final LocalDateTime _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEnd = _tmp_3;
            }
            _item = new SubjectTable(_tmpId,_tmpPlannerId,_tmpName,_tmpColor,_tmpStart,_tmpEnd);
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
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
