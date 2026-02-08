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
import net.felipealafy.studentplanner.tablemodels.ClassTable;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ClassDao_Impl implements ClassDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ClassTable> __insertionAdapterOfClassTable;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ClassTable> __deletionAdapterOfClassTable;

  private final EntityDeletionOrUpdateAdapter<ClassTable> __updateAdapterOfClassTable;

  public ClassDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClassTable = new EntityInsertionAdapter<ClassTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `class` (`id`,`subjectId`,`title`,`start`,`end`,`noteTakingLink`,`observation`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSubjectId());
        statement.bindString(3, entity.getTitle());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        statement.bindString(6, entity.getNoteTakingLink());
        statement.bindString(7, entity.getObservation());
      }
    };
    this.__deletionAdapterOfClassTable = new EntityDeletionOrUpdateAdapter<ClassTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `class` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassTable entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfClassTable = new EntityDeletionOrUpdateAdapter<ClassTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `class` SET `id` = ?,`subjectId` = ?,`title` = ?,`start` = ?,`end` = ?,`noteTakingLink` = ?,`observation` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSubjectId());
        statement.bindString(3, entity.getTitle());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        statement.bindString(6, entity.getNoteTakingLink());
        statement.bindString(7, entity.getObservation());
        statement.bindString(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final ClassTable data, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfClassTable.insert(data);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ClassTable data, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfClassTable.handle(data);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ClassTable data, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfClassTable.handle(data);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ClassTable>> getAllClasses() {
    final String _sql = "SELECT * FROM class ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"class"}, new Callable<List<ClassTable>>() {
      @Override
      @NonNull
      public List<ClassTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfNoteTakingLink = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTakingLink");
          final int _cursorIndexOfObservation = CursorUtil.getColumnIndexOrThrow(_cursor, "observation");
          final List<ClassTable> _result = new ArrayList<ClassTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final String _tmpNoteTakingLink;
            _tmpNoteTakingLink = _cursor.getString(_cursorIndexOfNoteTakingLink);
            final String _tmpObservation;
            _tmpObservation = _cursor.getString(_cursorIndexOfObservation);
            _item = new ClassTable(_tmpId,_tmpSubjectId,_tmpTitle,_tmpStart,_tmpEnd,_tmpNoteTakingLink,_tmpObservation);
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
  public Flow<List<ClassTable>> getClassesByDateTime(final LocalDateTime start,
      final LocalDateTime end) {
    final String _sql = "SELECT * FROM class WHERE start < ? AND \"end\" > ? ORDER BY start ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.dateToTimestamp(end);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.dateToTimestamp(start);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"class"}, new Callable<List<ClassTable>>() {
      @Override
      @NonNull
      public List<ClassTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfNoteTakingLink = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTakingLink");
          final int _cursorIndexOfObservation = CursorUtil.getColumnIndexOrThrow(_cursor, "observation");
          final List<ClassTable> _result = new ArrayList<ClassTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final LocalDateTime _tmpStart;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStart);
            }
            final LocalDateTime _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpStart = _tmp_3;
            }
            final LocalDateTime _tmpEnd;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEnd);
            }
            final LocalDateTime _tmp_5 = __converters.fromTimestamp(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpEnd = _tmp_5;
            }
            final String _tmpNoteTakingLink;
            _tmpNoteTakingLink = _cursor.getString(_cursorIndexOfNoteTakingLink);
            final String _tmpObservation;
            _tmpObservation = _cursor.getString(_cursorIndexOfObservation);
            _item = new ClassTable(_tmpId,_tmpSubjectId,_tmpTitle,_tmpStart,_tmpEnd,_tmpNoteTakingLink,_tmpObservation);
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
  public Flow<List<ClassTable>> getClassesBySubjectId(final String subjectId) {
    final String _sql = "SELECT * FROM class WHERE subjectId = ? ORDER BY start ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"class"}, new Callable<List<ClassTable>>() {
      @Override
      @NonNull
      public List<ClassTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfNoteTakingLink = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTakingLink");
          final int _cursorIndexOfObservation = CursorUtil.getColumnIndexOrThrow(_cursor, "observation");
          final List<ClassTable> _result = new ArrayList<ClassTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final String _tmpNoteTakingLink;
            _tmpNoteTakingLink = _cursor.getString(_cursorIndexOfNoteTakingLink);
            final String _tmpObservation;
            _tmpObservation = _cursor.getString(_cursorIndexOfObservation);
            _item = new ClassTable(_tmpId,_tmpSubjectId,_tmpTitle,_tmpStart,_tmpEnd,_tmpNoteTakingLink,_tmpObservation);
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
  public Flow<List<ClassTable>> getClassById(final String id) {
    final String _sql = "SELECT * FROM class WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"class"}, new Callable<List<ClassTable>>() {
      @Override
      @NonNull
      public List<ClassTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfNoteTakingLink = CursorUtil.getColumnIndexOrThrow(_cursor, "noteTakingLink");
          final int _cursorIndexOfObservation = CursorUtil.getColumnIndexOrThrow(_cursor, "observation");
          final List<ClassTable> _result = new ArrayList<ClassTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
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
            final String _tmpNoteTakingLink;
            _tmpNoteTakingLink = _cursor.getString(_cursorIndexOfNoteTakingLink);
            final String _tmpObservation;
            _tmpObservation = _cursor.getString(_cursorIndexOfObservation);
            _item = new ClassTable(_tmpId,_tmpSubjectId,_tmpTitle,_tmpStart,_tmpEnd,_tmpNoteTakingLink,_tmpObservation);
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
