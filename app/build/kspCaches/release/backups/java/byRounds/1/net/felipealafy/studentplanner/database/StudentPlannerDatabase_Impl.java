package net.felipealafy.studentplanner.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudentPlannerDatabase_Impl extends StudentPlannerDatabase {
  private volatile PlannerDao _plannerDao;

  private volatile SubjectDao _subjectDao;

  private volatile ClassDao _classDao;

  private volatile ExamDao _examDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `planner` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, `minimumGradeToPass` REAL NOT NULL, `gradeDisplayStyle` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `subject` (`id` TEXT NOT NULL, `plannerId` TEXT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, `start` TEXT NOT NULL, `end` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`plannerId`) REFERENCES `planner`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `class` (`id` TEXT NOT NULL, `subjectId` TEXT NOT NULL, `title` TEXT NOT NULL, `start` TEXT NOT NULL, `end` TEXT NOT NULL, `noteTakingLink` TEXT NOT NULL, `observation` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`subjectId`) REFERENCES `subject`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exam` (`id` TEXT NOT NULL, `subjectId` TEXT NOT NULL, `name` TEXT NOT NULL, `grade` REAL NOT NULL, `gradeWeight` REAL NOT NULL, `start` TEXT NOT NULL, `end` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`subjectId`) REFERENCES `subject`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4d0a44af4168a3c8a57b65b10cad8ba9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `planner`");
        db.execSQL("DROP TABLE IF EXISTS `subject`");
        db.execSQL("DROP TABLE IF EXISTS `class`");
        db.execSQL("DROP TABLE IF EXISTS `exam`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlanner = new HashMap<String, TableInfo.Column>(5);
        _columnsPlanner.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanner.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanner.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanner.put("minimumGradeToPass", new TableInfo.Column("minimumGradeToPass", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanner.put("gradeDisplayStyle", new TableInfo.Column("gradeDisplayStyle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlanner = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlanner = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlanner = new TableInfo("planner", _columnsPlanner, _foreignKeysPlanner, _indicesPlanner);
        final TableInfo _existingPlanner = TableInfo.read(db, "planner");
        if (!_infoPlanner.equals(_existingPlanner)) {
          return new RoomOpenHelper.ValidationResult(false, "planner(net.felipealafy.studentplanner.tablemodels.PlannerTable).\n"
                  + " Expected:\n" + _infoPlanner + "\n"
                  + " Found:\n" + _existingPlanner);
        }
        final HashMap<String, TableInfo.Column> _columnsSubject = new HashMap<String, TableInfo.Column>(6);
        _columnsSubject.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubject.put("plannerId", new TableInfo.Column("plannerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubject.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubject.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubject.put("start", new TableInfo.Column("start", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubject.put("end", new TableInfo.Column("end", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubject = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSubject.add(new TableInfo.ForeignKey("planner", "CASCADE", "NO ACTION", Arrays.asList("plannerId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSubject = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubject = new TableInfo("subject", _columnsSubject, _foreignKeysSubject, _indicesSubject);
        final TableInfo _existingSubject = TableInfo.read(db, "subject");
        if (!_infoSubject.equals(_existingSubject)) {
          return new RoomOpenHelper.ValidationResult(false, "subject(net.felipealafy.studentplanner.tablemodels.SubjectTable).\n"
                  + " Expected:\n" + _infoSubject + "\n"
                  + " Found:\n" + _existingSubject);
        }
        final HashMap<String, TableInfo.Column> _columnsClass = new HashMap<String, TableInfo.Column>(7);
        _columnsClass.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("subjectId", new TableInfo.Column("subjectId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("start", new TableInfo.Column("start", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("end", new TableInfo.Column("end", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("noteTakingLink", new TableInfo.Column("noteTakingLink", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClass.put("observation", new TableInfo.Column("observation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClass = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysClass.add(new TableInfo.ForeignKey("subject", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesClass = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClass = new TableInfo("class", _columnsClass, _foreignKeysClass, _indicesClass);
        final TableInfo _existingClass = TableInfo.read(db, "class");
        if (!_infoClass.equals(_existingClass)) {
          return new RoomOpenHelper.ValidationResult(false, "class(net.felipealafy.studentplanner.tablemodels.ClassTable).\n"
                  + " Expected:\n" + _infoClass + "\n"
                  + " Found:\n" + _existingClass);
        }
        final HashMap<String, TableInfo.Column> _columnsExam = new HashMap<String, TableInfo.Column>(7);
        _columnsExam.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("subjectId", new TableInfo.Column("subjectId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("grade", new TableInfo.Column("grade", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("gradeWeight", new TableInfo.Column("gradeWeight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("start", new TableInfo.Column("start", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExam.put("end", new TableInfo.Column("end", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExam = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExam.add(new TableInfo.ForeignKey("subject", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExam = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExam = new TableInfo("exam", _columnsExam, _foreignKeysExam, _indicesExam);
        final TableInfo _existingExam = TableInfo.read(db, "exam");
        if (!_infoExam.equals(_existingExam)) {
          return new RoomOpenHelper.ValidationResult(false, "exam(net.felipealafy.studentplanner.tablemodels.ExamTable).\n"
                  + " Expected:\n" + _infoExam + "\n"
                  + " Found:\n" + _existingExam);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4d0a44af4168a3c8a57b65b10cad8ba9", "be4bb2b2d150795218324cb86c6b0cc1");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "planner","subject","class","exam");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `planner`");
      _db.execSQL("DELETE FROM `subject`");
      _db.execSQL("DELETE FROM `class`");
      _db.execSQL("DELETE FROM `exam`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PlannerDao.class, PlannerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubjectDao.class, SubjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ClassDao.class, ClassDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExamDao.class, ExamDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PlannerDao PlannerDao() {
    if (_plannerDao != null) {
      return _plannerDao;
    } else {
      synchronized(this) {
        if(_plannerDao == null) {
          _plannerDao = new PlannerDao_Impl(this);
        }
        return _plannerDao;
      }
    }
  }

  @Override
  public SubjectDao SubjectDao() {
    if (_subjectDao != null) {
      return _subjectDao;
    } else {
      synchronized(this) {
        if(_subjectDao == null) {
          _subjectDao = new SubjectDao_Impl(this);
        }
        return _subjectDao;
      }
    }
  }

  @Override
  public ClassDao ClassDao() {
    if (_classDao != null) {
      return _classDao;
    } else {
      synchronized(this) {
        if(_classDao == null) {
          _classDao = new ClassDao_Impl(this);
        }
        return _classDao;
      }
    }
  }

  @Override
  public ExamDao ExamDao() {
    if (_examDao != null) {
      return _examDao;
    } else {
      synchronized(this) {
        if(_examDao == null) {
          _examDao = new ExamDao_Impl(this);
        }
        return _examDao;
      }
    }
  }
}
