package net.felipealafy.studentplanner

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.database.StudentPlannerDatabase
import net.felipealafy.studentplanner.feature_exam.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubjectMemoryBenchmarkRule {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private lateinit var db: StudentPlannerDatabase
    private lateinit var subjectDao: SubjectDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StudentPlannerDatabase::class.java).build()

        subjectDao = db.SubjectDao()
        val plannerDao = db.PlannerDao() // Pegue a referência do DAO do Planner

        kotlinx.coroutines.runBlocking {
            // 1. INSIRA O PAI PRIMEIRO
            plannerDao.insert(
                net.felipealafy.studentplanner.feature_planner.data.local.PlannerTable(
                    id = "planner-id", // Este ID precisa ser idêntico ao que a matéria vai pedir
                    name = "Semestre Benchmark",
                    color = 0L,
                    minimumGradeToPass = 70f,
                    gradeDisplayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED // Ajuste os campos falsos conforme a sua PlannerTable real
                )
            )

            // 2. INSIRA O FILHO DEPOIS
            subjectDao.insert(
                net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable(
                    id = "10904168-7040-4034-a52d-6421ae0b42c0",
                    plannerId = "planner-id", // Agora a Foreign Key vai encontrar o Pai!
                    name = "Engenharia de Software e Estruturas de Dados Avançadas",
                    color = 4294967295L,
                    start = java.time.LocalDateTime.now(),
                    end = java.time.LocalDateTime.now().plusMonths(6)
                )
            )
        }
    }

    @After
    fun tearDown() {
        // Fecha o banco de dados e limpa a memória após o teste
        db.close()
    }

    @Test
    fun measureSubjectProjectionMemory() = runTest {
        benchmarkRule.measureRepeated {
            kotlinx.coroutines.runBlocking {
                val dates = subjectDao.getSubjectDates("10904168-7040-4034-a52d-6421ae0b42c0").first()
            }
        }
    }

    @Test
    fun measureSubjectProjectionMemoryUnoptimized() = runTest {
        benchmarkRule.measureRepeated {
            kotlinx.coroutines.runBlocking {
                val subjectData = subjectDao.getSubjectById("10904168-7040-4034-a52d-6421ae0b42c0").first()
            }
        }
    }
}