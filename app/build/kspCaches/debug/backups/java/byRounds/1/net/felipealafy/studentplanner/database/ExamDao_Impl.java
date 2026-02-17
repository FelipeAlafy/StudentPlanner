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
import net.felipealafy.studentplanner.tablemodels.ExamTable;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExamDao_Impl implements ExamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExamTable> __insertionAdapterOfExamTable;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ExamTable> __deletionAdapterOfExamTable;

  private final EntityDeletionOrUpdateAdapter<ExamTable> __updateAdapterOfExamTable;

  public ExamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExamTable = new EntityInsertionAdapter<ExamTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exam` (`id`,`subjectId`,`name`,`grade`,`gradeWeight`,`start`,`end`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSubjectId());
        statement.bindString(3, entity.getName());
        statement.bindDouble(4, entity.getGrade());
        statement.bindDouble(5, entity.getGradeWeight());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfExamTable = new EntityDeletionOrUpdateAdapter<ExamTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exam` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamTable entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfExamTable = new EntityDeletionOrUpdateAdapter<ExamTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exam` SET `id` = ?,`subjectId` = ?,`name` = ?,`grade` = ?,`gradeWeight` = ?,`start` = ?,`end` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSubjectId());
        statement.bindString(3, entity.getName());
        statement.bindDouble(4, entity.getGrade());
        statement.bindDouble(5, entity.getGradeWeight());
        final String _tmp = __converters.dateToTimestamp(entity.getStart());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        final String _tmp_1 = __converters.dateToTimestamp(entity.getEnd());
        if (_tmp_1 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_1);
        }
        statement.bindString(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final ExamTable examTable, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExamTable.insert(examTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ExamTable examTable, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExamTable.handle(examTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ExamTable examTable, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExamTable.handle(examTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExamTable>> getExams(final String subjectId) {
    final String _sql = "SELECT * FROM exam WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam"}, new Callable<List<ExamTable>>() {
      @Override
      @NonNull
      public List<ExamTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfGradeWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeWeight");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<ExamTable> _result = new ArrayList<ExamTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final float _tmpGrade;
            _tmpGrade = _cursor.getFloat(_cursorIndexOfGrade);
            final float _tmpGradeWeight;
            _tmpGradeWeight = _cursor.getFloat(_cursorIndexOfGradeWeight);
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
            _item = new ExamTable(_tmpId,_tmpSubjectId,_tmpName,_tmpGrade,_tmpGradeWeight,_tmpStart,_tmpEnd);
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
  public Flow<List<ExamTable>> getExamsByPlannerId(final String plannerId) {
    final String _sql = "SELECT * FROM exam WHERE subjectId IN (SELECT id FROM subject WHERE plannerId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, plannerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam",
        "subject"}, new Callable<List<ExamTable>>() {
      @Override
      @NonNull
      public List<ExamTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfGradeWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeWeight");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<ExamTable> _result = new ArrayList<ExamTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final float _tmpGrade;
            _tmpGrade = _cursor.getFloat(_cursorIndexOfGrade);
            final float _tmpGradeWeight;
            _tmpGradeWeight = _cursor.getFloat(_cursorIndexOfGradeWeight);
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
            _item = new ExamTable(_tmpId,_tmpSubjectId,_tmpName,_tmpGrade,_tmpGradeWeight,_tmpStart,_tmpEnd);
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
  public Flow<List<ExamTable>> getExamsByDateTime(final LocalDateTime todayStart,
      final LocalDateTime todayEnd) {
    final String _sql = "SELECT * FROM exam WHERE start BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.dateToTimestamp(todayStart);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.dateToTimestamp(todayEnd);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam"}, new Callable<List<ExamTable>>() {
      @Override
      @NonNull
      public List<ExamTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfGradeWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeWeight");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<ExamTable> _result = new ArrayList<ExamTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final float _tmpGrade;
            _tmpGrade = _cursor.getFloat(_cursorIndexOfGrade);
            final float _tmpGradeWeight;
            _tmpGradeWeight = _cursor.getFloat(_cursorIndexOfGradeWeight);
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
            _item = new ExamTable(_tmpId,_tmpSubjectId,_tmpName,_tmpGrade,_tmpGradeWeight,_tmpStart,_tmpEnd);
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
  public Flow<List<ExamTable>> getExamById(final String id) {
    final String _sql = "SELECT * FROM exam WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam"}, new Callable<List<ExamTable>>() {
      @Override
      @NonNull
      public List<ExamTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final int _cursorIndexOfGradeWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeWeight");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final List<ExamTable> _result = new ArrayList<ExamTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final float _tmpGrade;
            _tmpGrade = _cursor.getFloat(_cursorIndexOfGrade);
            final float _tmpGradeWeight;
            _tmpGradeWeight = _cursor.getFloat(_cursorIndexOfGradeWeight);
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
            _item = new ExamTable(_tmpId,_tmpSubjectId,_tmpName,_tmpGrade,_tmpGradeWeight,_tmpStart,_tmpEnd);
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
