package net.felipealafy.studentplanner.feature_class.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_class.domain.use_case.DeleteClassUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class DeleteClassUseCaseTest {
    private lateinit var deleteClassUseCase: DeleteClassUseCase
    private lateinit var repository: ClassRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        deleteClassUseCase = DeleteClassUseCase(repository)
    }

    @Test
    fun shouldEmitAnErrorWhenClassNotFounded() = runTest {
        coEvery {
            repository.delete("example")
        } returns 0

        assertFailsWith<InvalidClassExceptions.ClassDeletionError> {
            deleteClassUseCase("example")
        }
    }

    @Test
    fun shouldEmitAnErrorWhenDatabaseIOException() = runTest {
        coEvery {
            repository.delete("example")
        } throws Exception("SQLite Disk I/O Error")

        assertFailsWith<InvalidClassExceptions.ClassDeletionIODBError> {
            deleteClassUseCase("example")
        }
    }

    @Test
    fun shouldDeleteClassSuccessfully() = runTest {
        coEvery {
            repository.delete("example")
        } returns 1

        deleteClassUseCase("example")

        coVerify(exactly = 1) {
            repository.delete("example")
        }
    }

}