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
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import net.felipealafy.studentplanner.datamodels.GradeStyle;
import net.felipealafy.studentplanner.tablemodels.PlannerTable;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlannerDao_Impl implements PlannerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlannerTable> __insertionAdapterOfPlannerTable;

  private final EntityDeletionOrUpdateAdapter<PlannerTable> __deletionAdapterOfPlannerTable;

  private final EntityDeletionOrUpdateAdapter<PlannerTable> __updateAdapterOfPlannerTable;

  public PlannerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlannerTable = new EntityInsertionAdapter<PlannerTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `planner` (`id`,`name`,`color`,`minimumGradeToPass`,`gradeDisplayStyle`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannerTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getColor());
        statement.bindDouble(4, entity.getMinimumGradeToPass());
        statement.bindString(5, __GradeStyle_enumToString(entity.getGradeDisplayStyle()));
      }
    };
    this.__deletionAdapterOfPlannerTable = new EntityDeletionOrUpdateAdapter<PlannerTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `planner` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannerTable entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlannerTable = new EntityDeletionOrUpdateAdapter<PlannerTable>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `planner` SET `id` = ?,`name` = ?,`color` = ?,`minimumGradeToPass` = ?,`gradeDisplayStyle` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlannerTable entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getColor());
        statement.bindDouble(4, entity.getMinimumGradeToPass());
        statement.bindString(5, __GradeStyle_enumToString(entity.getGradeDisplayStyle()));
        statement.bindString(6, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final PlannerTable plannerTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlannerTable.insert(plannerTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PlannerTable plannerTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlannerTable.handle(plannerTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PlannerTable plannerTable,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlannerTable.handle(plannerTable);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlannerTable>> getAllPlanners() {
    final String _sql = "SELECT * FROM planner ORDER BY NAME ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"planner"}, new Callable<List<PlannerTable>>() {
      @Override
      @NonNull
      public List<PlannerTable> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfMinimumGradeToPass = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumGradeToPass");
          final int _cursorIndexOfGradeDisplayStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeDisplayStyle");
          final List<PlannerTable> _result = new ArrayList<PlannerTable>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlannerTable _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpColor;
            _tmpColor = _cursor.getLong(_cursorIndexOfColor);
            final float _tmpMinimumGradeToPass;
            _tmpMinimumGradeToPass = _cursor.getFloat(_cursorIndexOfMinimumGradeToPass);
            final GradeStyle _tmpGradeDisplayStyle;
            _tmpGradeDisplayStyle = __GradeStyle_stringToEnum(_cursor.getString(_cursorIndexOfGradeDisplayStyle));
            _item = new PlannerTable(_tmpId,_tmpName,_tmpColor,_tmpMinimumGradeToPass,_tmpGradeDisplayStyle);
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
  public PlannerTable getPlanner(final String plannerId) {
    final String _sql = "SELECT * FROM planner WHERE id = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, plannerId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfMinimumGradeToPass = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumGradeToPass");
      final int _cursorIndexOfGradeDisplayStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "gradeDisplayStyle");
      final PlannerTable _result;
      if (_cursor.moveToFirst()) {
        final String _tmpId;
        _tmpId = _cursor.getString(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final long _tmpColor;
        _tmpColor = _cursor.getLong(_cursorIndexOfColor);
        final float _tmpMinimumGradeToPass;
        _tmpMinimumGradeToPass = _cursor.getFloat(_cursorIndexOfMinimumGradeToPass);
        final GradeStyle _tmpGradeDisplayStyle;
        _tmpGradeDisplayStyle = __GradeStyle_stringToEnum(_cursor.getString(_cursorIndexOfGradeDisplayStyle));
        _result = new PlannerTable(_tmpId,_tmpName,_tmpColor,_tmpMinimumGradeToPass,_tmpGradeDisplayStyle);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __GradeStyle_enumToString(@NonNull final GradeStyle _value) {
    switch (_value) {
      case FROM_ZERO_TO_ONE_HUNDRED: return "FROM_ZERO_TO_ONE_HUNDRED";
      case FROM_ZERO_TO_TEN: return "FROM_ZERO_TO_TEN";
      case FROM_A_TO_F: return "FROM_A_TO_F";
      case FROM_A_TO_F_WITH_E: return "FROM_A_TO_F_WITH_E";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private GradeStyle __GradeStyle_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "FROM_ZERO_TO_ONE_HUNDRED": return GradeStyle.FROM_ZERO_TO_ONE_HUNDRED;
      case "FROM_ZERO_TO_TEN": return GradeStyle.FROM_ZERO_TO_TEN;
      case "FROM_A_TO_F": return GradeStyle.FROM_A_TO_F;
      case "FROM_A_TO_F_WITH_E": return GradeStyle.FROM_A_TO_F_WITH_E;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
