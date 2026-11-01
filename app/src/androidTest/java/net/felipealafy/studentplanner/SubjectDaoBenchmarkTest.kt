package net.felipealafy.studentplanner

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.felipealafy.studentplanner.database.StudentPlannerDatabase
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(/* value = */ AndroidJUnit4::class)
class SubjectDaoBenchmarkTest {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private lateinit var db: StudentPlannerDatabase
    private lateinit var subjectDao: SubjectDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            StudentPlannerDatabase::class.java
        ).build()
        subjectDao = db.SubjectDao()

        // Setup: Insira um dado mockado para a query encontrar
        // runBlocking { subjectDao.insert(SubjectTable(...)) }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun benchmarkGetSubjectDates() = runBlocking {
        // TUDO DENTRO DO measureRepeated É MEDIDO RIGOROSAMENTE
        benchmarkRule.measureRepeated {
            // Como estamos dentro do microbenchmark, precisamos bloquear a thread
            // para medir o tempo síncrono exato que o SQLite demora.
            runWithTimingDisabled {
                // Use runWithTimingDisabled para qualquer lógica que não deva
                // entrar na métrica de tempo, se necessário.
            }

            // O código que queremos metrificar:
            runBlocking {
                subjectDao.getSubjectDates("mock_id").first()
            }
        }
    }
}